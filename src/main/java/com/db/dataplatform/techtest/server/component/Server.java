package com.db.dataplatform.techtest.server.component;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.exception.DataIntegrityException;
import com.db.dataplatform.techtest.server.exception.EntityNotFoundException;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface Server {
    boolean saveDataEnvelope(DataEnvelope envelope) throws IOException, NoSuchAlgorithmException, DataIntegrityException;

    boolean updateBlockType(String blockName,BlockTypeEnum blockType) throws IOException, NoSuchAlgorithmException, EntityNotFoundException;

    boolean saveDataEnvelope(List<DataEnvelope> envelope) throws IOException, NoSuchAlgorithmException;

    List<DataEnvelope> getDataBodyEntities(BlockTypeEnum blockType) throws EntityNotFoundException;

}
