package com.tasks.controllers;

import com.tasks.domain.dto.TaskDto;
import com.tasks.domain.entities.Task;
import com.tasks.mappers.TaskMapper;
import com.tasks.services.TaskService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task-lists/{task_list_id}/tasks")
public class TasksController {

  private final TaskService taskService;
  private final TaskMapper taskMapper;

  public TasksController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @GetMapping
  public List<TaskDto> getTasks(@PathVariable("task_list_id") UUID taskListId) {
    return taskService.listTasks(taskListId).stream().map(taskMapper::toDto).toList();
  }

  @PostMapping
  public TaskDto createTask(@PathVariable("task_list_id") UUID taskListId,
      @RequestBody TaskDto taskDto) {

    Task taskCreated = taskService.createTask(taskListId, taskMapper.fromDto(taskDto));
    return taskMapper.toDto(taskCreated);
  }

  @GetMapping("/{task_id}")
  public Optional<TaskDto> getTask(@PathVariable("task_list_id") UUID taskListId,
      @PathVariable("task_id") UUID taskId) {

    return taskService.getTask(taskListId, taskId).map(taskMapper::toDto);
  }

  @PutMapping("/{task_id}")
  public TaskDto updateTask(@PathVariable("task_list_id") UUID taskListId,
      @PathVariable("task_id") UUID taskId, @RequestBody TaskDto taskDto) {

    Task updatedTask = taskService.updateTask(taskListId, taskId, taskMapper.fromDto(taskDto));
    return taskMapper.toDto(updatedTask);
  }

  @DeleteMapping("/{task_id}")
  public void deleteTask(@PathVariable("task_list_id") UUID taskListId,
      @PathVariable("task_id") UUID taskId) {

    taskService.deleteTask(taskListId, taskId);
  }
}
