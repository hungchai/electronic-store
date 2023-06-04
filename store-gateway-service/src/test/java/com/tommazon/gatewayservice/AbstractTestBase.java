package com.tommazon.gatewayservice;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {GatewayServiceApplication.class, TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(value = {"test"})
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Data
public abstract class AbstractTestBase {

    String refId = String.valueOf(UUID.randomUUID());

    @MockBean
    protected DateTimeProvider dateTimeProvider;

    @SpyBean
    protected AuditingHandler handler;

    @LocalServerPort
    protected int serverPort;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected LocalDateTime createdDateTime;
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        handler.setDateTimeProvider(dateTimeProvider);
        createdDateTime = createLocalDateTime("2023-06-01 19:00:01.123");
        when(dateTimeProvider.getNow()).thenReturn(Optional.of(createdDateTime));
        refId = String.valueOf(UUID.randomUUID());
    }

    protected HttpHeaders getHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        return headers;
    }

    protected <T> HttpEntity<T> getEntity(T body) {
        return new HttpEntity<>(body, getHeader());
    }



    protected LocalDateTime createLocalDateTime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
