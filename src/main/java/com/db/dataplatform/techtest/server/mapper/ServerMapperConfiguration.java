package com.db.dataplatform.techtest.server.mapper;

import org.modelmapper.Converter;
import org.modelmapper.Converters;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Converts;

@Configuration
public class ServerMapperConfiguration {

    @Bean
    public ModelMapper createModelMapperBean() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }

}
