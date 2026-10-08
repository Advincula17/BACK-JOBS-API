package com.uap.proiv.jobs.controller;

import com.uap.proiv.jobs.dto.UserJobAssigned;
import com.uap.proiv.jobs.service.UserJobAssignedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AssignControllerTest {

    @Mock
    private UserJobAssignedService userJobAssignedService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AssignController assignController =
                new AssignController(userJobAssignedService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(assignController)
                .build();
    }

    @Test
    void assign_success() throws Exception {

        // Arrange
        when(userJobAssignedService.assign())
                .thenReturn(List.of());

        String requestJson = """
                {
                    "requestNumber": 123,
                    "clientName": "Cliente de prueba"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Client")
                        .value("Cliente de prueba"))
                .andExpect(jsonPath("$.Request_Number")
                        .value(123))
                .andExpect(jsonPath("$.Assign")
                        .isArray());

        verify(userJobAssignedService).assign();
    }

    @Test
    void assign_error() throws Exception {

        // Arrange
        when(userJobAssignedService.assign())
                .thenThrow(new RuntimeException("Error al realizar la asignación"));

        String requestJson = """
                {
                    "requestNumber": 123,
                    "clientName": "Cliente de prueba"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content()
                        .string("Error al realizar la asignación"));

        verify(userJobAssignedService).assign();
    }
}