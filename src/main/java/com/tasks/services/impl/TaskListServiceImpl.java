package com.tasks.services.impl;

import com.tasks.domain.entities.TaskList;
import com.tasks.repositories.TaskListRepository;
import com.tasks.services.TaskListService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskListServiceImpl implements TaskListService {

  private final TaskListRepository taskListRepository;

  public TaskListServiceImpl(TaskListRepository taskListRepository) {
    this.taskListRepository = taskListRepository;
  }


  @Override
  public List<TaskList> listTaskLists() {
    return taskListRepository.findAll();
  }

  @Override
  public TaskList createTaskList(TaskList taskList) {
    if (null != taskList.getId()) {
      throw new IllegalArgumentException("Task list already has an ID");
    }

    if (null == taskList.getTitle() || taskList.getTitle().isBlank()) {
      throw new IllegalArgumentException("Task list title cannot be empty");
    }

    LocalDateTime now = LocalDateTime.now();
    return taskListRepository.save(new TaskList(
        null,
        taskList.getTitle(),
        taskList.getDescription(),
        null,
        now,
        now
    ));
  }

  @Override
  public Optional<TaskList> getTaskList(UUID id) {
    return taskListRepository.findById(id);
  }

  @Transactional
  @Override
  public TaskList updateTaskList(UUID id, TaskList taskList) {
    if (null == taskList.getId()) {
      throw new IllegalArgumentException("Task list must have an ID");
    }

    if (!Objects.equals(taskList.getId(), id)) {
      throw new IllegalArgumentException(
          "Attempting to change task list ID, this is not permitted");
    }

    TaskList existingTaskList = taskListRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Task list does not exist"));

    existingTaskList.setTitle(taskList.getTitle());
    existingTaskList.setDescription(taskList.getDescription());
    existingTaskList.setUpdated(LocalDateTime.now());
    return taskListRepository.save(existingTaskList);

  }

  @Override
  public void deleteTaskList(UUID id) {
    taskListRepository.deleteById(id);
  }
}
