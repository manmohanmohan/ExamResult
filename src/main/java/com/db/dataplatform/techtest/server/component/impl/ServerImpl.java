package com.db.dataplatform.techtest.server.component.impl;

import com.db.dataplatform.techtest.server.api.model.DataBody;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.api.model.DataHeader;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.exception.DataIntegrityException;
import com.db.dataplatform.techtest.server.exception.EntityNotFoundException;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.service.DataHeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerImpl implements Server {

    private final DataBodyService dataBodyServiceImpl;

    private final DataHeaderService dataHeaderServiceImpl;
    private final ModelMapper modelMapper;


    /**
     * @param envelope
     * @return true if there is a match with the client provided checksum.
     */
    @Override
    public boolean saveDataEnvelope(DataEnvelope envelope) throws DataIntegrityException {

        // Save to persistence.
        String md5CheckSum = calculateMD5(envelope.getDataBody().getDataBody());
        if (md5CheckSum.equals(envelope.getMd5CheckSum())) {
            persist(envelope);
            log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());
            return true;
        } else {
            return false;
        }
    }


    public static String calculateMD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] dataBytes = data.getBytes();
            byte[] digest = md.digest(dataBytes);
            return convertByteArrayToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertByteArrayToHexString(byte[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : array) {
            stringBuilder.append(String.format("%02x", b & 0xff));
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean updateBlockType(String name, BlockTypeEnum blockType) throws EntityNotFoundException {

        // Save to persistence.
        //   persist(envelope);

        Optional<DataHeaderEntity> dataHeaderEntities = dataBodyServiceImpl.getDataHeaderByBlockName(name);

        if (dataHeaderEntities.isPresent()) {
            DataHeaderEntity dataHeaderEntity = dataHeaderEntities.get();
            dataHeaderEntity.setBlocktype(blockType);
            dataHeaderServiceImpl.saveHeader(dataHeaderEntity);
        } else {
            throw new EntityNotFoundException("Entity Not Found");
        }


        //    log.info("Data persisted successfully, data name: {}", envelope.getDataHeader().getName());
        return true;
    }


    @Override
    public boolean saveDataEnvelope(List<DataEnvelope> envelope) {

        // Save to persistence.
        persistAll(envelope);
        log.info("Data persisted successfully");
        return true;
    }

    @Override
    public List<DataEnvelope> getDataBodyEntities(BlockTypeEnum blockType) throws EntityNotFoundException {

        List<DataBodyEntity> dataBodyEntities = dataBodyServiceImpl.getDataByBlockType(blockType);
        if (CollectionUtils.isEmpty(dataBodyEntities)) {
            throw new EntityNotFoundException("Entity Not Found");
        } else {
            List<DataEnvelope> dataEnvelopes = new ArrayList<>();
            for (DataBodyEntity dataBodyEntity : dataBodyEntities) {
                DataHeader dataHeader = new DataHeader(dataBodyEntity.getDataHeaderEntity().getName(), dataBodyEntity.getDataHeaderEntity().getBlocktype());
                DataBody dataBody = new DataBody(dataBodyEntity.getDataBody());
                String md5CheckSum = calculateMD5(dataBodyEntity.getDataBody());
                DataEnvelope dataEnvelope = new DataEnvelope();
                dataEnvelope.setDataHeader(dataHeader);
                dataEnvelope.setDataBody(dataBody);
                dataEnvelope.setMd5CheckSum(md5CheckSum);
                dataEnvelopes.add(dataEnvelope);
            }
            return dataEnvelopes;
        }
    }

    private void persist(DataEnvelope envelope) throws DataIntegrityException {
        log.info("Persisting data with attribute name: {}", envelope.getDataHeader().getName());
        DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);
        DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
        dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);
        saveData(dataBodyEntity);
    }

    private void persistAll(List<DataEnvelope> envelopes) {
        List<DataBodyEntity> dataBodyEntities = new ArrayList<>();
        envelopes.forEach(envelope -> {
            DataHeaderEntity dataHeaderEntity = modelMapper.map(envelope.getDataHeader(), DataHeaderEntity.class);

            DataBodyEntity dataBodyEntity = modelMapper.map(envelope.getDataBody(), DataBodyEntity.class);
            dataBodyEntity.setDataHeaderEntity(dataHeaderEntity);
            dataBodyEntities.add(dataBodyEntity);
        });
        dataBodyServiceImpl.saveAllDataBody(dataBodyEntities);

    }

    private void saveData(DataBodyEntity dataBodyEntity) throws DataIntegrityException {
        try {
            dataBodyServiceImpl.saveDataBody(dataBodyEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Resource Exist");
        }
    }
}
