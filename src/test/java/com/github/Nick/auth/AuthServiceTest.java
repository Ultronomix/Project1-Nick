package com.github.Nick.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.Nick.common.exceptions.AuthenticationException;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.users.User;
import com.github.Nick.users.UserDAO;
import com.github.Nick.users.UserResponse;

public class AuthServiceTest {
    AuthService sut; //* System Under Test
    UserDAO mockUserDAO;

    @BeforeEach
    public void setUp() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new AuthService(mockUserDAO);
    }

    @AfterEach
    public void cleanUp () {
        Mockito.reset(mockUserDAO);
    }

    @Test
    public void test_authenticationReturnsSuccessfully_givenValidAndKnownCredentials () {

        //* Arrange
        Credentials credentialsStub = new Credentials("valid", "credentials");
        User userStub = new User("some-id", "val", "valid123@gmail.com", "credentials", "Valid", "test", true);
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(userStub));
        UserResponse expectedResult = new UserResponse(userStub);
        //* Act
        UserResponse actualResult = sut.authenticate(credentialsStub);
        //* Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult); //* Objects being compared needs to have a .equals method
    }

    @Test
    public void test_authentication_throwsInvalidRequestException_givenInvalidCredentials() {

        //* Arrange
        Credentials credentialsStub = new Credentials("invalid", "cred");
        //* Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void test_authentication_throwsAuthenticationException_givenInvalidCredentials() {
        
        //* Arrange
        Credentials credentialsStub = new Credentials("invalid", "credential");
        //* Act & Assert
        assertThrows(AuthenticationException.class, () ->{
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(1)).findUserByUsernameAndPassword(anyString(), anyString());
    }
}
