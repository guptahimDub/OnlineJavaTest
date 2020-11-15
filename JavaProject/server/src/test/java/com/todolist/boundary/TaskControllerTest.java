package com.todolist.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.todolist.module.handler.TaskHandler;
import com.todolist.module.model.TaskModel;
import com.todolist.module.model.TasklistModel;
import com.todolist.service.UserTaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest {
    private MockMvc mvc;

    private ObjectWriter jsonWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Mock
    private UserTaskService userTaskService;

    @InjectMocks
    private TaskController userTaskController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .standaloneSetup(userTaskController)
                .setControllerAdvice(new TaskHandler())
                .build();
    }

    @Test
    public void givenUser_whenUserGetTaskList_thenApiReturnUpdatedTaskList() throws Exception {
        final int userId = 1;

        final LocalDateTime now = LocalDateTime.now();

        final TaskModel task = TaskModel.builder()
                .id(1)
                .description("Test Task")
                .lastUpdate(now)
                .checked(false)
                .build();

        final TasklistModel tasklistPresentation = TasklistModel.builder()
                .taskList(asList(task))
                .build();

        given(userTaskService.getTasklist(userId)).willReturn(tasklistPresentation);


        mvc.perform(get(String.format("/users/%d/tasks", userId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskList.length()", is(1)))
                .andExpect(jsonPath("$.taskList[0].id", is(task.getId())))
                .andExpect(jsonPath("$.taskList[0].description", is(task.getDescription())))
                .andExpect(jsonPath("$.taskList[0].checked", is(task.isChecked())))
                .andExpect(jsonPath("$.taskList[0].lastUpdate", is(notNullValue())));
    }


    @Test
    public void givenUser_whenUserGetMultipleTasksInList_thenApiReturnUpdatedTaskList() throws Exception {
        final int userId = 1;

        final LocalDateTime now = LocalDateTime.now();

        final TaskModel task1 = TaskModel.builder()
                .id(1)
                .description("Test Task")
                .lastUpdate(now)
                .checked(false)
                .build();
        final TaskModel task2 = TaskModel.builder()
                .id(2)
                .description("Test Task 2")
                .lastUpdate(now.minusDays(2))
                .checked(true)
                .build();

        final TasklistModel tasklistPresentation = TasklistModel.builder()
                .taskList(asList(task1,task2))
                .build();

        given(userTaskService.getTasklist(userId)).willReturn(tasklistPresentation);


        mvc.perform(get(String.format("/users/%d/tasks", userId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskList.length()", is(2)))
                .andExpect(jsonPath("$.taskList[0].id", is(task1.getId())))
                .andExpect(jsonPath("$.taskList[0].description", is(task1.getDescription())))
                .andExpect(jsonPath("$.taskList[0].checked", is(task1.isChecked())))
                .andExpect(jsonPath("$.taskList[0].lastUpdate", is(notNullValue())))
                .andExpect(jsonPath("$.taskList[1].id", is(task2.getId())))
                .andExpect(jsonPath("$.taskList[1].description", is(task2.getDescription())))
                .andExpect(jsonPath("$.taskList[1].checked", is(task2.isChecked())))
                .andExpect(jsonPath("$.taskList[1].lastUpdate", is(notNullValue())));
    }


    @Test
    public void givenUser_whenUserCreateTask_thenApiReturnCreatedTask() throws Exception{
        final int userId = 1;

        final LocalDateTime now = LocalDateTime.now();

        final String description = "Test Task";
        final boolean checked = false;
        final TaskModel task = TaskModel.builder()
                .id(1)
                .description(description)
                .lastUpdate(now)
                .checked(checked)
                .build();

        given(userTaskService.createTask(userId, description, checked)).willReturn(task);


        mvc.perform(post(String.format("/users/%d/tasks", userId))
                .content(jsonWriter.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(task.getId())))
                .andExpect(jsonPath("$.description", is(task.getDescription())))
                .andExpect(jsonPath("$.checked", is(task.isChecked())))
                .andExpect(jsonPath("$.lastUpdate", is(notNullValue())));
    }


    @Test
    public void givenUser_whenUserUpdateTask_thenApiReturnUpdatedTask() throws Exception{
        final int userId = 1;
        final int taskId = 1;

        final LocalDateTime now = LocalDateTime.now();

        final String description = "Test Task";
        final boolean checked = false;
        final TaskModel task = TaskModel.builder()
                .id(1)
                .description(description)
                .lastUpdate(now)
                .checked(checked)
                .build();

        given(userTaskService.updateTask(userId, taskId, description, checked)).willReturn(task);


        mvc.perform(put(String.format("/users/%d/tasks/%d", userId, taskId))
                .content(jsonWriter.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(task.getId())))
                .andExpect(jsonPath("$.description", is(task.getDescription())))
                .andExpect(jsonPath("$.checked", is(task.isChecked())))
                .andExpect(jsonPath("$.lastUpdate", is(notNullValue())));
    }


    @Test
    public void givenUser_whenUserDeleteTask_thenApiReturnNoContentResponse() throws Exception{
        final int userId = 1;
        final int taskId = 1;

        doNothing().when(userTaskService).deleteTask(userId, taskId);


        mvc.perform(delete(String.format("/users/%d/tasks/%d", userId, taskId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}