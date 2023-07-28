package com.db.dataplatform.techtest.component;

import com.db.dataplatform.techtest.TestDataHelper;

import com.db.dataplatform.techtest.client.RestTemplateConfiguration;
import com.db.dataplatform.techtest.client.component.Client;
import com.db.dataplatform.techtest.client.component.impl.ClientImpl;
import com.db.dataplatform.techtest.server.api.controller.ServerController;
import com.db.dataplatform.techtest.client.api.model.DataBody;
import com.db.dataplatform.techtest.client.api.model.DataEnvelope;
import com.db.dataplatform.techtest.client.api.model.DataHeader;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.exception.HadoopClientException;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.db.dataplatform.techtest.server.service.DataBodyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.query.criteria.internal.expression.SimpleCaseExpression;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.db.dataplatform.techtest.TestDataHelper.DUMMY_DATA;
import static com.db.dataplatform.techtest.TestDataHelper.TEST_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class ClientImplComponentTest {

	public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
	public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
	public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");

	@Mock
	private Client client;

	private DataEnvelope testDataEnvelope;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;
	@InjectMocks
	private ClientImpl clientImpl;

	@Mock
	private DataBodyService dataBodyServiceImpl;

	@Mock
	RestTemplate restTemplate;

	@Mock
	private RestTemplateConfiguration restTemplateConfiguration;

	@Before
	public void setUp() throws HadoopClientException, NoSuchAlgorithmException, IOException {
	//	clientImpl = new ClientImpl(restTemplateConfiguration);
		objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.build();
	//	RestTemplate restTemplate = new RestTemplate();
		testDataEnvelope = createTestDataEnvelopeApiObject();
		when(restTemplateConfiguration.createRestTemplate(any(),any())).thenReturn(restTemplate);
		when( restTemplate.postForEntity(URI_PUSHDATA,testDataEnvelope, Boolean.class)).thenReturn(ResponseEntity.ok(true));
	//	when(dataBodyServiceImpl.saveDataBody(any())).
	//	when(restTemplate.postForEntity("http://localhost:8090/dataserver/pushdata",any(DataBodyEntity.class), Boolean.class)).thenReturn(ResponseEntity.ok(true));
	}

	public static DataEnvelope createTestDataEnvelopeApiObject() {
		DataBody dataBody = new DataBody(DUMMY_DATA);
		DataHeader dataHeader = new DataHeader(TEST_NAME, BlockTypeEnum.BLOCKTYPEA);

		DataEnvelope dataEnvelope = new DataEnvelope(dataHeader, dataBody,"cecfd3953783df706878aaec2c22aa70");
		return dataEnvelope;
	}


	@Test
	public void TestPushData() throws JsonProcessingException {
		clientImpl.pushData(testDataEnvelope);
	}
}
