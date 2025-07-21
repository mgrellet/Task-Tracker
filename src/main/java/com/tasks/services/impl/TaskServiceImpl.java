package com.tasks.services.impl;

import com.tasks.domain.entities.Task;
import com.tasks.domain.entities.TaskList;
import com.tasks.domain.entities.TaskPriority;
import com.tasks.domain.entities.TaskStatus;
import com.tasks.repositories.TaskListRepository;
import com.tasks.repositories.TaskRepository;
import com.tasks.services.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  private final TaskListRepository taskListRepository;

  public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
    this.taskRepository = taskRepository;
    this.taskListRepository = taskListRepository;
  }


  @Override
  public List<Task> listTasks(UUID taskListId) {
    return taskRepository.findByTaskListId(taskListId);
  }

  @Transactional
  @Override
  public Task createTask(UUID taskListId, Task task) {
    if (null != task.getId()) {
      throw new IllegalArgumentException("Task id is already set");
    }

    if (null == task.getTitle() || task.getTitle().isBlank()) {
      throw new IllegalArgumentException("Task title is empty");
    }

    TaskPriority taskPriority = Optional.ofNullable(task.getPriority()).orElse(TaskPriority.MEDIUM);

    TaskStatus taskStatus = TaskStatus.OPEN;

    TaskList taskList = taskListRepository.findById(taskListId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid id provided"));

    LocalDateTime now = LocalDateTime.now();
    Task taskToSave = new Task(
        null,
        task.getTitle(),
        task.getDescription(),
        task.getDueDate(),
        taskStatus,
        taskPriority,
        taskList,
        now,
        now
    );

    return taskRepository.save(taskToSave);
  }

  @Override
  public Optional<Task> getTask(UUID taskListId, UUID taskId) {
    return taskRepository.findByTaskListIdAndId(taskListId, taskId);
  }

  @Transactional
  @Override
  public Task updateTask(UUID taskListId, UUID taskId, Task task) {
    if (null == task.getId()) {
      throw new IllegalArgumentException("Task must have an ID");
    }

    if (!Objects.equals(taskId, task.getId())) {
      throw new IllegalArgumentException("Task id does not match");
    }

    if (null == task.getPriority()) {
      throw new IllegalArgumentException("Task must have a valid priority");
    }

    if (null == task.getStatus()) {
      throw new IllegalArgumentException("Task must have a valid status");
    }

    Task existingTask = taskRepository.findByTaskListIdAndId(taskListId, taskId)
        .orElseThrow(() -> new IllegalArgumentException("Task not found"));

    existingTask.setTitle(task.getTitle());
    existingTask.setDescription(task.getDescription());
    existingTask.setDueDate(task.getDueDate());
    existingTask.setPriority(task.getPriority());
    existingTask.setStatus(task.getStatus());

    return taskRepository.save(existingTask);
  }

  @Transactional
  @Override
  public void deleteTask(UUID taskListId, UUID taskId) {
    taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
  }
}
