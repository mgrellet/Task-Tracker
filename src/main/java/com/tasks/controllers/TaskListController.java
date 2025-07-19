package com.tasks.controllers;

import com.tasks.domain.dto.TaskListDto;
import com.tasks.domain.entities.TaskList;
import com.tasks.mappers.TaskListMapper;
import com.tasks.services.TaskListService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task-lists")
public class TaskListController {

  private final TaskListService taskListService;

  private final TaskListMapper taskListMapper;

  public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
    this.taskListService = taskListService;
    this.taskListMapper = taskListMapper;
  }


  @GetMapping
  public List<TaskListDto> listTaskLists() {
    return taskListService.listTaskLists()
        .stream()
        .map(taskListMapper::toDto)
        .toList();
  }

  @PostMapping
  public TaskListDto createTaskList(@RequestBody TaskListDto taskListDto) {
    TaskList taskList = taskListService.createTaskList(taskListMapper.fromDto(taskListDto));
    return taskListMapper.toDto(taskList);
  }

  @GetMapping("/{task_list_id}")
  public Optional<TaskListDto> getTaskListById(@PathVariable("task_list_id") UUID id) {
    return taskListService.getTaskList(id).map(taskListMapper::toDto);
  }

  @PutMapping("/{task_list_id}")
  public TaskListDto updateTaskList(@PathVariable("task_list_id") UUID id,
      @RequestBody TaskListDto taskListDto) {
    TaskList updeatedTaskList = taskListService.updateTaskList(id,
        taskListMapper.fromDto(taskListDto));

    return taskListMapper.toDto(updeatedTaskList);
  }

  @DeleteMapping("/{task_list_id}")
  public void deleteTaskListById(@PathVariable("task_list_id") UUID id) {
    taskListService.deleteTaskList(id);
  }


}
