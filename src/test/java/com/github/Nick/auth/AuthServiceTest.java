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
import com.github.Nick.common.exceptions.ResourceNotFoundException;
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
        User userStub = new User("some-id", "valid", "valid123@gmail.com", "credentials", "Valid", "test", true);
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(userStub));
        when(mockUserDAO.isActive(anyString(), anyString())).thenReturn(true);
        UserResponse expectedResult = new UserResponse(userStub);

        //* Act
        UserResponse actualResult = sut.authenticate(credentialsStub);

        //* Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult); //* Objects being compared needs to have a .equals method
    }

    @Test
    public void test_authentication_throwsInvalidRequestException_givenTooShortPassword() {

        //* Arrange
        Credentials credentialsStub = new Credentials("invalid", "cred");
        
        //* Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void test_authentication_throwsAuthenticationException_givenTooShortUsername() {
        
        //* Arrange
        Credentials credentialsStub = new Credentials("x", "credential");
        //* Act & Assert
        assertThrows(InvalidRequestException.class, () ->{
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void test_authentication_throwsInvalidRequestException_givenNullCredential () {
        // Arrange
        Credentials credentialsStub = null;

        //act
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        // Assert
        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void test_authenticate_throwsAuthenticationException_givenValidUnknownCredentials() {

        // Arrange
        Credentials credentialsStub = new Credentials("unknown", "credentials");
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        // Act
        assertThrows(AuthenticationException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        // Assert
        verify(mockUserDAO, times(1)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authentication_throwsInvalidRequestException_givenInActiveUser () {

        Credentials credentialsStub = new Credentials("valid", "credentials");
        User userStub = new User("some-id", "valid", "valid123@gmail.com", "credentials", "Valid", "test", false);
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(userStub));
        when(mockUserDAO.isActive(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidRequestException.class, () -> { 
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(1)).isActive("valid", "credentials");
    }

    @Test
    public void test_authenticate_throwsAuthenticationException_givenValidUnknownCredentialsToIsActive () {

        Credentials credentialsStub = new Credentials("unknown", "credential");
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenThrow(ResourceNotFoundException.class);
        
        assertThrows(AuthenticationException.class, () -> { 
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(1)).isActive(anyString(), anyString());
    }
}
