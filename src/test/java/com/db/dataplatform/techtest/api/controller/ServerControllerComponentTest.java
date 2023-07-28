package com.db.dataplatform.techtest.api.controller;

import com.db.dataplatform.techtest.TestDataHelper;
import com.db.dataplatform.techtest.server.api.controller.ServerController;
import com.db.dataplatform.techtest.server.api.model.DataEnvelope;
import com.db.dataplatform.techtest.server.exception.DataIntegrityException;
import com.db.dataplatform.techtest.server.exception.EntityNotFoundException;
import com.db.dataplatform.techtest.server.exception.HadoopClientException;
import com.db.dataplatform.techtest.server.component.Server;
import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class ServerControllerComponentTest {

	public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
	public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
	public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");

	@Mock
	private Server serverMock;

	private DataEnvelope testDataEnvelope;
	private ObjectMapper objectMapper;
	private MockMvc mockMvc;
	private ServerController serverController;

	@Before
	public void setUp() throws HadoopClientException, NoSuchAlgorithmException, IOException, EntityNotFoundException, DataIntegrityException {
		serverController = new ServerController(serverMock);
		mockMvc = standaloneSetup(serverController).build();
		objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.build();

		testDataEnvelope = TestDataHelper.createTestDataEnvelopeApiObject();

		when(serverMock.saveDataEnvelope(any(DataEnvelope.class))).thenReturn(true);
		List<DataEnvelope> dataEnvelopes= new ArrayList<>();
		dataEnvelopes.add(testDataEnvelope);
		//when(serverMock.getDataBodyEntities("invalidBockType")).thenThrow(new EntityNotFoundException("Entity Not Found"));
		when(serverMock.getDataBodyEntities(BlockTypeEnum.BLOCKTYPEA)).thenReturn(dataEnvelopes);

	}

	@Test
	public void testPushDataPostCallWorksAsExpected() throws Exception {

		String testDataEnvelopeJson = objectMapper.writeValueAsString(testDataEnvelope);

		MvcResult mvcResult = mockMvc.perform(post(URI_PUSHDATA)
						.content(testDataEnvelopeJson)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn();

		boolean checksumPass = Boolean.parseBoolean(mvcResult.getResponse().getContentAsString());
		assertThat(checksumPass).isTrue();
	}

	@Test
	public void testGetDataGetCallWorksAsExpected() throws Exception {

		MvcResult mvcResult = mockMvc.perform(get(String.valueOf(URI_GETDATA),"BLOCKTYPEA")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn();

		List<DataEnvelope> dataEnvelopes= Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), DataEnvelope[].class));
		Assertions.assertEquals(1,dataEnvelopes.size());

	}

	@Test
	public void testGetDataGetCallThrowsBadRequest() throws Exception {

		MvcResult mvcResult = mockMvc.perform(get(String.valueOf(URI_GETDATA),"invalidBockType")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andReturn();

	}
}
