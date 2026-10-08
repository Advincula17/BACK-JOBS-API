package com.uap.proiv.jobs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uap.proiv.jobs.JobsApplication;
import com.uap.proiv.jobs.client.UserApiRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.http.HttpClient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = JobsApplication.class)
@AutoConfigureMockMvc
@Import(AssignControllerIntegrationTest.TestConfig.class)
class AssignControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void assign_success() throws Exception {

        String usersJson = """
                {
                    "page": 1,
                    "per_page": 10,
                    "total": 10,
                    "total_pages": 1,
                    "data": [
                        {
                            "id": 1,
                            "email": "user1@gmail.com",
                            "first_name": "Juan",
                            "last_name": "Perez",
                            "avatar": "https://reqres.in/img/faces/1.jpg"
                        },
                        {
                            "id": 2,
                            "email": "user2@gmail.com",
                            "first_name": "Carlos",
                            "last_name": "Gomez",
                            "avatar": "https://reqres.in/img/faces/2.jpg"
                        },
                        {
                            "id": 3,
                            "email": "user3@gmail.com",
                            "first_name": "Pedro",
                            "last_name": "Lopez",
                            "avatar": "https://reqres.in/img/faces/3.jpg"
                        },
                        {
                            "id": 4,
                            "email": "user4@gmail.com",
                            "first_name": "Martin",
                            "last_name": "Diaz",
                            "avatar": "https://reqres.in/img/faces/4.jpg"
                        },
                        {
                            "id": 5,
                            "email": "user5@gmail.com",
                            "first_name": "Lucas",
                            "last_name": "Sanchez",
                            "avatar": "https://reqres.in/img/faces/5.jpg"
                        },
                        {
                            "id": 6,
                            "email": "user6@gmail.com",
                            "first_name": "Nicolas",
                            "last_name": "Rodriguez",
                            "avatar": "https://reqres.in/img/faces/6.jpg"
                        },
                        {
                            "id": 7,
                            "email": "user7@gmail.com",
                            "first_name": "Matias",
                            "last_name": "Fernandez",
                            "avatar": "https://reqres.in/img/faces/7.jpg"
                        },
                        {
                            "id": 8,
                            "email": "user8@gmail.com",
                            "first_name": "Santiago",
                            "last_name": "Martinez",
                            "avatar": "https://reqres.in/img/faces/8.jpg"
                        },
                        {
                            "id": 9,
                            "email": "user9@gmail.com",
                            "first_name": "Diego",
                            "last_name": "Gonzalez",
                            "avatar": "https://reqres.in/img/faces/9.jpg"
                        },
                        {
                            "id": 10,
                            "email": "user10@gmail.com",
                            "first_name": "Federico",
                            "last_name": "Torres",
                            "avatar": "https://reqres.in/img/faces/10.jpg"
                        }
                    ]
                }
                """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(usersJson)
        );

        String usersPage2Json = """
                {
                    "page": 2,
                    "per_page": 10,
                    "total": 10,
                    "total_pages": 1,
                    "data": []
                }
                """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(usersPage2Json)
        );

        String requestJson = """
                {
                    "requestNumber": 123,
                    "clientName": "Cliente de prueba"
                }
                """;

        mockMvc.perform(post("/api/assign")
                .contentType("application/json")
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Client").value("Cliente de prueba"))
                .andExpect(jsonPath("$.Request_Number").value(123))
                .andExpect(jsonPath("$.Assign").isArray())
                .andExpect(jsonPath("$.Assign").isNotEmpty());
    }

    @Configuration
    static class TestConfig {

        @Bean
        UserApiRepository userApiRepository() {

            String baseUrl = mockWebServer.url("/api/users").toString();

            return new UserApiRepository(
                    HttpClient.newHttpClient(),
                    new ObjectMapper(),
                    baseUrl,
                    "test-api-key"
            );
        }
    }
}