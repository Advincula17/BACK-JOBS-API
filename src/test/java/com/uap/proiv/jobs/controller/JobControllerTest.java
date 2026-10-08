package com.uap.proiv.jobs.controller;

import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {

    @Mock
    private JobService jobService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        JobController jobController = new JobController(jobService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(jobController)
                .build();
    }

    @Test
    void getAllJobs_success() throws Exception {

        // Arrange
        when(jobService.getAllJobs())
                .thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/job/all"))
                .andExpect(status().isOk());

        verify(jobService).getAllJobs();
    }

    @Test
    void getAllJobs_error() throws Exception {

        // Arrange
        when(jobService.getAllJobs())
                .thenThrow(new RuntimeException("Error al obtener los trabajos"));

        // Act & Assert
        mockMvc.perform(get("/api/job/all"))
                .andExpect(status().isInternalServerError());

        verify(jobService).getAllJobs();
    }

    @Test
    void getJobById_success() throws Exception {

        // Arrange
        Job job = mock(Job.class);

        when(jobService.getJobById(1))
                .thenReturn(job);

        // Act & Assert
        mockMvc.perform(get("/api/job/1"))
                .andExpect(status().isOk());

        verify(jobService).getJobById(1);
    }

    @Test
    void getJobById_error() throws Exception {

        // Arrange
        when(jobService.getJobById(1))
                .thenThrow(new RuntimeException("Trabajo no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/job/1"))
                .andExpect(status().isInternalServerError());

        verify(jobService).getJobById(1);
    }
}