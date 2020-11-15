package com.todolist.service;

import com.todolist.repository.UserRepository;
import com.todolist.repository.model.UserInfo;
import com.todolist.service.exception.AuthenticationError;
import com.todolist.service.exception.DuplicateError;
import com.todolist.service.exception.MissingError;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncodingService encodingService;

    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void userCanRegister() throws Exception {
        final String username = "test";
        final String password = "pwd123";

        // Stub encoding service and repository
        UserInfo user = UserInfo.builder().id(1L).username(username).password(password).build();
        when(encodingService.hash(username)).thenReturn(username);
        when(encodingService.hash(password)).thenReturn(password);
        when(userRepository.save(any())).thenReturn(user);

        userService.register("test", "pwd123");
    }

    @Test(expected = DuplicateError.class)
    public void cannotRegisterDuplicatedUser() throws Exception {
        final String username = "test";
        final String password = "pwd123";

        // Stub encoding service and repository
        UserInfo user = UserInfo.builder().id(1L).username(username).password(password).build();
        when(encodingService.hash(username)).thenReturn(username);
        when(encodingService.hash(password)).thenReturn(password);
        when(userRepository.findByUsername(username)).thenReturn(user);

        userService.register(username, password);
    }

    @Test
    public void userCanLogin() throws Exception {
        final String username = "test";
        final String password = "pwd123";

        // Stub encoding service and repository
        UserInfo user = UserInfo.builder().id(1L).username(username).password(password).build();
        when(encodingService.hash(username)).thenReturn(username);
        when(encodingService.hash(password)).thenReturn(password);
        when(userRepository.findByUsername(username)).thenReturn(user);

        userService.login(username, password);

    }

    @Test(expected = MissingError.class)
    public void missingUser() throws Exception {
        final String username = "test";
        final String password = "pwd123";

        // Stub encoding service and repository
        when(encodingService.hash(username)).thenReturn(username);
        when(encodingService.hash(password)).thenReturn(password);
        when(userRepository.findByUsername(username)).thenReturn(null);

        userService.login(username, password);

    }


    @Test(expected = AuthenticationError.class)
    public void passwordMismatch() throws Exception {
        final String username = "test";
        final String storedPassword = "pwd123";
        final String inputPassword = "pwd123456";

        // Stub encoding service and repository
        UserInfo user = UserInfo.builder().username(username).password(storedPassword).build();
        when(encodingService.hash(username)).thenReturn(username);
        when(encodingService.hash(inputPassword)).thenReturn(inputPassword);
        when(userRepository.findByUsername(username)).thenReturn(user);

        userService.login(username, inputPassword);

    }
}