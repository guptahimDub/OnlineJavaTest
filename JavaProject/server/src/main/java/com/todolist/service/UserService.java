package com.todolist.service;

import com.todolist.module.model.UserModel;
import com.todolist.repository.UserRepository;
import com.todolist.repository.model.UserInfo;
import com.todolist.service.exception.AuthenticationError;
import com.todolist.service.exception.DuplicateError;
import com.todolist.service.exception.MissingError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static java.lang.String.format;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EncodingService encodingService;

    public UserModel register(final String username, final String password) {
        final String usernameHash = encodingService.hash(username);
        final String passwordHash = encodingService.hash(password);

        assertNotDuplicatedUser(usernameHash);

        UserInfo userToRegister = UserInfo.builder()
                .username(usernameHash)
                .password(passwordHash)
                .build();

        final UserInfo newUser = repository.save(userToRegister);
        return UserModel.builder().id(newUser.getId()).username(username).build();

    }

    public UserModel login(final String username, final String password) {
        final String usernameHash = encodingService.hash(username);
        final String passwordHash = encodingService.hash(password);

        final UserInfo user = tryToGetUser(usernameHash);
        if (!user.getPassword().equals(passwordHash)){
            throw new AuthenticationError("Mismatch in Password");
        }
        return UserModel.builder().id(user.getId()).username(username).build();
    }

    public UserInfo findById(final long userId){
        final Optional<UserInfo> user = repository.findById(userId);
        if (!user.isPresent()){
            throw new MissingError("User Name is Missing");
        }
        return user.get();
    }

    private void assertNotDuplicatedUser(String username) {
        final UserInfo user = repository.findByUsername(username);
        if (user != null){
            throw new DuplicateError(format("User %s already exists in database", username));
        }
    }


    private UserInfo tryToGetUser(final String usernameHash) {
        final UserInfo user = repository.findByUsername(usernameHash);
        if (user == null){
            throw new MissingError("User Name is Missing");
        }
        return user;
    }

}
