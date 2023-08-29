package io.ram.config;

import com.dtflys.forest.converter.json.ForestJacksonConverter;
import com.dtflys.forest.converter.json.ForestJsonConverter;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ForestConfig {
    @Bean
    public ForestJsonConverter forestJacksonConverter() {
        ForestJacksonConverter converter = new ForestJacksonConverter();
        // 获取 Jackson 的 ObjectMapper 对象
        ObjectMapper mapper = converter.getMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true);
        return converter;
    }
}
