package com.db.dataplatform.techtest.service;

import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.exception.DataIntegrityException;
import com.db.dataplatform.techtest.server.mapper.ServerMapperConfiguration;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.component.impl.ServerImpl;
import com.db.dataplatform.techtest.server.service.DataHeaderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.db.dataplatform.techtest.TestDataHelper.createTestDataEnvelopeApiObject;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ServerServiceTests {

    @Mock
    private DataBodyService dataBodyServiceImplMock;

    @Mock
    private DataHeaderService dataHeaderServiceImplMock;

    private ModelMapper modelMapper;

    private DataBodyEntity expectedDataBodyEntity;
    private DataEnvelope testDataEnvelope;

    private Server server;

    @Before
    public void setup() {
        ServerMapperConfiguration serverMapperConfiguration = new ServerMapperConfiguration();
        modelMapper = serverMapperConfiguration.createModelMapperBean();

        testDataEnvelope = createTestDataEnvelopeApiObject();
        expectedDataBodyEntity = modelMapper.map(testDataEnvelope.getDataBody(), DataBodyEntity.class);
        expectedDataBodyEntity.setDataHeaderEntity(modelMapper.map(testDataEnvelope.getDataHeader(), DataHeaderEntity.class));

        server = new ServerImpl(dataBodyServiceImplMock,dataHeaderServiceImplMock, modelMapper);
    }

    @Test
    public void shouldSaveDataEnvelopeAsExpected() throws NoSuchAlgorithmException, IOException, DataIntegrityException {
        boolean success = server.saveDataEnvelope(testDataEnvelope);

        assertThat(success).isTrue();
        //verify(dataBodyServiceImplMock, times(1)).saveDataBody(eq(expectedDataBodyEntity));
    }
}
