package ptit.edu.vn.service.json;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonBean {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper =  new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
