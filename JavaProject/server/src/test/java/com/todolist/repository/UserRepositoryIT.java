package com.todolist.repository;

import com.todolist.configuration.JpaConfiguration;
import com.todolist.repository.model.UserInfo;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static com.todolist.repository.model.UserInfo.builder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = { JpaConfiguration.class },
        loader = AnnotationConfigContextLoader.class
)
@Transactional
public class UserRepositoryIT {

    @Autowired
    private UserRepository repository;

    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void givenUser_whenSave_thenGetOk() {
        final UserInfo user = builder().username("test").password("psw123").build();

        repository.save(user);

        final UserInfo createdUser = repository.findByUsername("test");
        assertNotNull(createdUser);
        assertThat(createdUser.getUsername(), is(user.getUsername()));
        assertThat(createdUser.getPassword(), is(user.getPassword()));
    }

    @Test
    public void givenUser_whenGetMissingUser_thenGetIsEmpty() {
        final UserInfo user = builder().username("test").password("psw123").build();

        repository.save(user);

        final UserInfo searchUser = repository.findByUsername("unknown");
        assertNull(searchUser);
    }


}