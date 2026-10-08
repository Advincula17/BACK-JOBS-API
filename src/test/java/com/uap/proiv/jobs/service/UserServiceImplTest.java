package com.uap.proiv.jobs.service;

import com.uap.proiv.jobs.client.UserApiRepository;
import com.uap.proiv.jobs.dto.User;
import com.uap.proiv.jobs.dto.UserApiResponse;
import com.uap.proiv.jobs.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserApiRepository userApiRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userApiRepository);
    }

    @Test
    void search_success() {
        // Arrange
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("Juan");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("Carlos");

        UserApiResponse response = new UserApiResponse();
        response.setData(List.of(user1, user2));

        when(userApiRepository.getUsers(1)).thenReturn(response);

        // Act
        UserApiResponse result = userService.search(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getData().size());

        assertEquals(1, result.getData().get(0).getJobId());
        assertEquals(2, result.getData().get(1).getJobId());

        verify(userApiRepository).getUsers(1);
    }

    @Test
    void searchById_success() {
        // Arrange
        User user = new User();
        user.setId(2);
        user.setFirstName("Juan");
        user.setLastName("Perez");

        when(userApiRepository.getUserById(2)).thenReturn(user);

        // Act
        User result = userService.searchById(2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals("Juan", result.getFirstName());
        assertEquals("Perez", result.getLastName());
        assertEquals(1, result.getJobId());

        verify(userApiRepository).getUserById(2);
    }

    @Test
    void update_success() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setFirstName("Carlos");
        user.setLastName("Perez");

        doNothing().when(userApiRepository).updateUser(user);

        // Act
        assertDoesNotThrow(() -> userService.update(user));

        // Assert
        verify(userApiRepository).updateUser(user);
    }

    @Test
    void update_error() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setFirstName("Carlos");
        user.setLastName("Perez");

        doThrow(new RuntimeException("Error de conexión"))
                .when(userApiRepository)
                .updateUser(user);

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.update(user)
        );

        assertEquals(
                "Error al crear el usuario: Error de conexión",
                exception.getMessage()
        );

        verify(userApiRepository).updateUser(user);
    }
}