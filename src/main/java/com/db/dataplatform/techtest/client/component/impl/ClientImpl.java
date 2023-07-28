package com.db.dataplatform.techtest.client.component.impl;

import com.db.dataplatform.techtest.client.RestTemplateConfiguration;
import com.db.dataplatform.techtest.client.api.model.DataEnvelope;
import com.db.dataplatform.techtest.client.component.Client;
import com.db.dataplatform.techtest.server.persistence.model.DataBodyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Client code does not require any test coverage
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientImpl implements Client {

    public static final String URI_PUSHDATA = "http://localhost:8090/dataserver/pushdata";
    public static final UriTemplate URI_GETDATA = new UriTemplate("http://localhost:8090/dataserver/data/{blockType}");
    public static final UriTemplate URI_PATCHDATA = new UriTemplate("http://localhost:8090/dataserver/update/{name}/{newBlockType}");

    private final RestTemplateConfiguration restTemplateConfiguration;

    @Override
    public void pushData(DataEnvelope dataEnvelope) {
        RestTemplate restTemplate = getRestTemplate();
        restTemplate.postForEntity(URI_PUSHDATA,dataEnvelope, Boolean.class);
        log.info("Pushing data {} to {}", dataEnvelope.getDataHeader().getName(), URI_PUSHDATA);

    }

    private RestTemplate getRestTemplate() {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringConverter.setSupportedMediaTypes(
                Arrays.asList(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, MediaType.APPLICATION_JSON));
        RestTemplate restTemplate= restTemplateConfiguration.createRestTemplate(new MappingJackson2HttpMessageConverter(),stringConverter);
        return restTemplate;
    }

    @Override
    public List<DataEnvelope> getData(String blockType) {
        log.info("Query for data with header block type {}", blockType);
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<List<DataEnvelope>> responseEntity;
     //           restTemplate.getForObject(URI_GETDATA,DataEnvelope[].class);
        return null;
    }

    @Override
    public boolean updateData(String blockName, String newBlockType) {
        log.info("Updating blocktype to {} for block with name {}", newBlockType, blockName);
        RestTemplate restTemplate = getRestTemplate();
//        restTemplate.patchForObject(URI_PATCHDATA, Boolean.class,blockName,newBlockType);
        return true;
    }


}
