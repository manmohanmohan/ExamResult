package com.db.dataplatform.techtest.server.api.controller;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeader;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.exception.DataIntegrityException;
import com.db.dataplatform.techtest.server.exception.EntityNotFoundException;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/dataserver")
@RequiredArgsConstructor
@Validated
public class ServerController {

    private final Server server;

    @PostMapping(value = "/pushdata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushData(@Valid @RequestBody DataEnvelope dataEnvelope) throws IOException, NoSuchAlgorithmException, DataIntegrityException {

        log.info("Data envelope received: {}", dataEnvelope.getDataHeader().getName());
        boolean checksumPass = server.saveDataEnvelope(dataEnvelope);

        log.info("Data envelope persisted. Attribute name: {}", dataEnvelope.getDataHeader().getName());
        return ResponseEntity.ok(checksumPass);
    }

    @GetMapping(value = "/data/{blockType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DataEnvelope>> getData(@Valid @PathVariable(value = "blockType") BlockTypeEnum blockType) throws EntityNotFoundException {
        List<DataEnvelope> dataEnvelopes = server.getDataBodyEntities(blockType);
        return ResponseEntity.ok(dataEnvelopes);
    }

    @PostMapping(value = "/pushalldata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pushAllData(@Valid @RequestBody List<DataEnvelope> dataEnvelope) throws IOException, NoSuchAlgorithmException {
        log.info("Data envelopes received: ");
        boolean checksumPass = server.saveDataEnvelope(dataEnvelope);
        log.info("Data envelopes persisted");
        return ResponseEntity.ok(checksumPass);
    }


    @PatchMapping(value = "/update/{name}/{newBlockType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateData(@PathVariable(value = "name") String name,
                                             @PathVariable(value = "newBlockType") String blockType) throws IOException, NoSuchAlgorithmException, EntityNotFoundException {
        log.info("Data envelope received: {}", name);
        try {
            BlockTypeEnum block = BlockTypeEnum.valueOf(blockType);
            server.updateBlockType(name,block);
            log.info("Data header updated", name);
            return ResponseEntity.ok(true);
        }catch (Exception e){
            throw new EntityNotFoundException("Requested Block does not found");
        }

    }
}
