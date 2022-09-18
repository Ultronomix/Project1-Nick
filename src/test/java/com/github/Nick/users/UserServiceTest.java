package com.github.Nick.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.Nick.common.ResourceCreationResponse;
import com.github.Nick.common.exceptions.InvalidRequestException;
import com.github.Nick.common.exceptions.ResourcePersistenceException;

public class UserServiceTest {

    UserService sut;
    UserDAO mockUserDAO;

    @BeforeEach
    public void setUp() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new UserService(mockUserDAO);
    }

    @AfterEach
    public void cleanUp () {
        Mockito.reset(mockUserDAO);
    }

    @Test
    public void testGetAllUsers() {
        User userStub = new User("some-id", "valid", "valid123@gmail.com", "credentials", "Valid", "test", true);
        User userStub1 = new User("some-id", "valid", "valid123@gmail.com", "credentials", "Valid", "test", true);
        List<UserResponse>results = new ArrayList<>();
        List<User> users = new ArrayList<>();
        users.add(userStub);
        users.add(userStub1);
        results.add(new UserResponse(userStub));
        results.add(new UserResponse(userStub1));
        when(mockUserDAO.getAllUsers()).thenReturn(users);

        List<UserResponse> expected = results;

        List<UserResponse> actual = sut.getAllUsers();
        
        assertNotNull(results);
        assertEquals(expected, actual);
        
    }
    
    @Test
    public void testGetUserbyID_InvalidRequestException_EmptyString() {
        
        String id = "";

        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserbyID(id);
        });

        verify(mockUserDAO, times(0)).findUserById(id);
    }

    @Test
    public void testGetUserbyID_InvalidRequestException_Null() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserbyID(null);
        });

        verify(mockUserDAO, times(0)).findUserById(anyString());
    }

    @Test
    public void testGetUserById() {
        
        User userStub = new User("some-id", "valid", "valid123@gmail.com", "credentials", "Valid", "test", true);
        when(mockUserDAO.findUserById(anyString())).thenReturn(Optional.of(userStub));

        UserResponse actual = sut.getUserbyID("some-id");
        UserResponse expected = new UserResponse(userStub);

        assertEquals(expected, actual);
    }


    @Test
    public void testGetUserbyUsername_InvalidRequestException_EmptyString() {

        String id = "";
        
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserbyUsername(id);
        });

        verify(mockUserDAO, times(0)).findUserByUsername(anyString());
    }

    @Test
    public void testGetUserbyUsername_InvalidRequestException_Null() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserbyUsername(null);
        });

        verify(mockUserDAO, times(0)).findUserByUsername(anyString());
    }

    @Test
    public void testGetUserbyUsername() {
        
        User userStub = new User("some-id", "valid", "valid123@gmail.com", "credentials", "Valid", "test", true);
        when(mockUserDAO.findUserByUsername(anyString())).thenReturn(Optional.of(userStub));

        UserResponse actual = sut.getUserbyUsername("some-id");
        UserResponse expected = new UserResponse(userStub);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUserbyRole_InvalidRequestException_EmptyString() {
        
        String role = "";

        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByRole(role);
        });

        verify(mockUserDAO, times(0)).findUserByRole(role);
    }

    @Test
    public void testGetUserbyRole_InvalidRequestException_Null() {

        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByRole(null);
        });

        verify(mockUserDAO, times(0)).findUserByRole(null);
    }

    @Test
    public void testGetUserbyRole() {
        
        User userStub = new User("some-id", "valid", "valid123@gmail.com", "credentials", "Valid", "test", true);
        when(mockUserDAO.findUserByRole(anyString())).thenReturn(Optional.of(userStub));

        UserResponse actual = sut.getUserByRole("Admin");
        UserResponse expected = new UserResponse(userStub);

        assertEquals(expected, actual);
    }

    @Test
    void testRegister_InvalidRequestException_Null() {

        NewUserRequest newUser = new NewUserRequest();

        User user = newUser.extractEntity();
        
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(null);
        });

        verify(mockUserDAO, times(0)).save(user);
    }
    
    @Test
    void testRegister_InvalidRequestException_GivenName_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setGiven_name("");        

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(null);
    }

    @Test
    void testRegister_InvalidRequestException_GivenName_Null() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name(null);
        newUser.setSurname("Test");
        newUser.setEmail("test@gmail.com");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }

    @Test
    public void testRegister_InvalidRequestException_All () {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("");
        newUser.setSurname("");
        newUser.setEmail("test@gmail.com");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }

    @Test
    void testRegister_InvalidRequestException_Surname_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("");
        newUser.setEmail("test@gmail.com");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }

    
    //@Test
    void testRegister_NullPointerException_Surname_Null() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname(null);
        newUser.setEmail("test@gmail.com");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(NullPointerException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }
    
    @Test
    void testRegister_InvalidRequestException_Email_Null() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("Test");
        newUser.setEmail(null);
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }
    
    @Test
    void testRegister_InvalidRequestException_Email_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }

    @Test
    void testRegister_InvalidRequestException_Username_Null() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername(null);

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }

    @Test
    void testRegister_InvalidRequestException_Username_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }

    @Test
    void testRegister_InvalidRequestException_Password_EmptyString() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setIs_active(true);
        newUser.setPassword("");
        newUser.setUser_id("6");
        newUser.setUsername("Tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }

    @Test
    void testRegister_InvalidRequestException_Password_Null() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setIs_active(true);
        newUser.setPassword(null);
        newUser.setUser_id("6");
        newUser.setUsername("Tester");

        User user = newUser.extractEntity();

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(0)).save(user);
    }

    @Test
    void testRegister_ResourcePersistenceException_IsEmailTaken_true() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("Tester");
        
        when(mockUserDAO.isEmailTaken(newUser.getEmail())).thenReturn(true);

        assertThrows(ResourcePersistenceException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(1)).isEmailTaken(newUser.getEmail());
    }

    @Test
    void testRegister_ResourcePersistenceException_IsUsernameTaken_true() {

        NewUserRequest newUser = new NewUserRequest();   
        newUser.setGiven_name("Tester");
        newUser.setSurname("Test");
        newUser.setEmail("test@test.com");
        newUser.setIs_active(true);
        newUser.setPassword("password");
        newUser.setUser_id("6");
        newUser.setUsername("Tester");
        
        when(mockUserDAO.isUsernameTaken(newUser.getUsername())).thenReturn(true);

        assertThrows(ResourcePersistenceException.class, () -> {
            sut.register(newUser);
        });

        verify(mockUserDAO, times(1)).isUsernameTaken(newUser.getUsername());
    }

    // @Test
    // public void testRegister_Extract() {
        
    //     NewUserRequest newUser = new NewUserRequest();   
    //     newUser.setGiven_name("Tester");
    //     newUser.setSurname("Test");
    //     newUser.setEmail("test@test.com");
    //     newUser.setIs_active(true);
    //     newUser.setPassword("password");
    //     newUser.setUser_id("6");
    //     newUser.setUsername("Tester");
    //     User user = newUser.extractEntity();

    //     when(mockUserDAO.save(user)).thenReturn(user.getUser_id());

    //     ResourceCreationResponse expected = new ResourceCreationResponse(user.getUser_id());

    //     String id = mockUserDAO.save(user);

    //     ResourceCreationResponse actual = new ResourceCreationResponse(id);

    //     assertNotNull(actual);
    //     assertEquals(expected, actual);
    // }

    //@Test
    // public void testUpdateUser_InvalidRequestException_Null() {

    //     assertThrows(InvalidRequestException.class, () -> {
    //         sut.updateUser(null, "1");
    //     });

    //     verify(mockUserDAO, times(0)).updateUser(null);
    // }

    @Test
    public void testUpdateUser_InvalidRequestException_Email_NotNull() {

        UpdateUserRequest updateUser = new UpdateUserRequest();
        updateUser.setGiven_name("Test");
        updateUser.setIs_active(true);
        updateUser.setSurname("tester");
        updateUser.setPassword("password");
        updateUser.setEmail("test@test.com");

        String id = "2";
        when(mockUserDAO.updateUserEmail(updateUser.getEmail(),id )).thenReturn("Email Updated");

        String actual = mockUserDAO.updateUserEmail(updateUser.getEmail(), id);
        String expected = "Email Updated";

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateUser_Email_Updated() {

        UpdateUserRequest updateUser = new UpdateUserRequest();
        updateUser.setGiven_name("Test");
        updateUser.setIs_active(true);
        updateUser.setSurname("tester");
        updateUser.setPassword("password");
        updateUser.setEmail("test@test.com");

        when(mockUserDAO.updateUserEmail(anyString(), anyString())).thenReturn("Email Updated");
        ResourceCreationResponse expected = new ResourceCreationResponse("Email Updated");
    
        String user_id = "2";
        String email = updateUser.extractEntity().getEmail();
        String update = mockUserDAO.updateUserEmail(email, user_id);

        ResourceCreationResponse actual = new ResourceCreationResponse(update);
        
        assertEquals(expected, actual);
    }
}
