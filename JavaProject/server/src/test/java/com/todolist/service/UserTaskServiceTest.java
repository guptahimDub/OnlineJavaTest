package com.todolist.service;

import com.todolist.module.model.TaskModel;
import com.todolist.repository.UserTaskRepository;
import com.todolist.repository.model.UserInfo;
import com.todolist.repository.model.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserTaskServiceTest {

    @Mock
    private UserTaskRepository userTaskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserTaskService userTaskService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void userCanCreateNewTask() throws Exception {
        final long userId = 1L;
        final String username = "test";
        final String password = "pwd123";
        final UserInfo user = UserInfo.builder().id(userId).username(username).password(password).build();

        final String taskDescription = "Test Task";
        final Date currentDate = new Date();
        final boolean checked = false;
        final TaskInfo userTask = TaskInfo.builder().id(1).user(user).checked(checked).lastUpdate(currentDate).description(taskDescription).build();

        when(userTaskRepository.save(any(TaskInfo.class))).thenReturn(userTask);

        final TaskModel task = userTaskService.createTask(userId, taskDescription, checked);
        assertNotNull(task);
        assertThat(task.getId(), is(userTask.getId()));
        assertThat(task.getDescription(), is(userTask.getDescription()));
        assertThat(task.getLastUpdate(), is(notNullValue()));

    }


    @Test
    public void userCanChangeStatusOfATask() throws Exception {
        final long userId = 1L;
        final String username = "test";
        final String password = "pwd123";
        final UserInfo user = UserInfo.builder().id(userId).username(username).password(password).build();

        final String taskDescription = "Test Task";
        final Date currentDate = new Date();
        final boolean checked = false;
        final int taskId = 1;
        final TaskInfo userTask = TaskInfo.builder().id(taskId).user(user).checked(checked).lastUpdate(currentDate).description(taskDescription).build();

        when(userTaskRepository.findById(taskId)).thenReturn(Optional.of(userTask));
        when(userTaskRepository.save(any(TaskInfo.class))).thenReturn(userTask);

        final TaskModel task = userTaskService.updateTask(userId, taskId, "NEW DESCRIPTION", true);
        assertNotNull(task);

        assertThat(task.isChecked(), is(true));
        assertThat(task.getDescription(), is("NEW DESCRIPTION"));

    }

    @Test
    public void userCanDeleteATask() throws Exception {
        final long userId = 1L;
        final String username = "test";
        final String password = "pwd123";
        final UserInfo user = UserInfo.builder().id(userId).username(username).password(password).build();

        final String taskDescription = "Test Task";
        final Date currentDate = new Date();
        final boolean checked = false;
        final int taskId = 1;
        final TaskInfo userTask = TaskInfo.builder().id(taskId).user(user).checked(checked).lastUpdate(currentDate).description(taskDescription).build();

        when(userTaskRepository.findById(taskId)).thenReturn(Optional.of(userTask));
        doNothing().when(userTaskRepository).delete(any(TaskInfo.class));

        userTaskService.deleteTask(userId, taskId);

    }

}