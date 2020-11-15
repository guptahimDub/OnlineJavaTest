package com.todolist.service;

import com.todolist.module.model.TaskModel;
import com.todolist.module.model.TasklistModel;
import com.todolist.repository.UserTaskRepository;
import com.todolist.repository.model.UserInfo;
import com.todolist.repository.model.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.todolist.repository.model.TaskInfo.*;

@Service
@Transactional
public class UserTaskService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserTaskRepository userTaskRepository;

    public TasklistModel getTasklist(int userId) {
        final List<TaskInfo> tasks = userTaskRepository.findByUserId(userId);
        List<TaskModel> tasklist = new ArrayList<>();
        tasks.forEach(task -> tasklist.add(toPresentation(task)));
        return TasklistModel.builder().taskList(tasklist).build();
    }

    public TaskModel createTask(long userId, String description, boolean checked) {
        final UserInfo user = userService.findById(userId);

        final TaskInfo userTask = builder()
                .user(user)
                .description(description)
                .checked(checked)
                .lastUpdate(new Date())
                .build();

        final TaskInfo storedUserTask = userTaskRepository.save(userTask);

        return toPresentation(storedUserTask);
    }

    public TaskModel updateTask(long userId, int taskId, String description, boolean checked) {
        userService.findById(userId);
        final Optional<TaskInfo> userTaskEntity = userTaskRepository.findById(taskId);

        if (!userTaskEntity.isPresent()){
            throw new RuntimeException("Task list not found");
        }

        final TaskInfo userTask = userTaskEntity.get();
        userTask.setChecked(checked);
        userTask.setDescription(description);
        userTask.setLastUpdate(new Date());

        final TaskInfo updatedUserTask = userTaskRepository.save(userTask);

        return toPresentation(updatedUserTask);
    }

    public void deleteTask(long userId, int taskId) {
        userService.findById(userId);
        final Optional<TaskInfo> userTaskEntity = userTaskRepository.findById(taskId);

        if (!userTaskEntity.isPresent()){
            throw new RuntimeException("Task list not found");
        }
        userTaskRepository.deleteById(taskId);
    }

    private TaskModel toPresentation(TaskInfo updatedUserTask) {
        return TaskModel.builder()
                .id(updatedUserTask.getId())
                .description(updatedUserTask.getDescription())
                .checked(updatedUserTask.isChecked())
                .lastUpdate(updatedUserTask.getLastUpdate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .build();
    }

}
