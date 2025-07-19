package com.tasks.mappers.impl;

import com.tasks.domain.dto.TaskDto;
import com.tasks.domain.dto.TaskListDto;
import com.tasks.domain.entities.Task;
import com.tasks.domain.entities.TaskList;
import com.tasks.domain.entities.TaskStatus;
import com.tasks.mappers.TaskListMapper;
import com.tasks.mappers.TaskMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TaskListMapperImpl implements TaskListMapper {

  private final TaskMapper taskMapper;

  public TaskListMapperImpl(TaskMapper taskMapper) {
    this.taskMapper = taskMapper;
  }

  @Override
  public TaskList fromDto(TaskListDto taskListDto) {

    List<Task> taskList = Optional.ofNullable(taskListDto.tasks())
        .map(tasks -> tasks.stream()
            .map(taskMapper::fromDto)
            .toList())
        .orElse(null);

    return new TaskList(
        taskListDto.id(),
        taskListDto.title(),
        taskListDto.description(),
        taskList,
        null,
        null

    );
  }

  @Override
  public TaskListDto toDto(TaskList taskList) {

    int count = Optional.ofNullable(taskList.getTasks()).map(List::size).orElse(0);

    Double progress = calculateTaskListProgress(taskList.getTasks());

    List<TaskDto> taskListDto =
        Optional.ofNullable(taskList.getTasks())
            .map(tasks -> tasks.stream()
                .map(taskMapper::toDto)
                .toList())
            .orElse(null);

    return new TaskListDto(
        taskList.getId(),
        taskList.getTitle(),
        taskList.getDescription(),
        count,
        progress,
        taskListDto
        );
  }

  private Double calculateTaskListProgress(List<Task> tasks) {
    if (null == tasks || tasks.isEmpty()) {
      return null;
    }

    long closedTaskCount = tasks.stream().filter(task -> TaskStatus.CLOSE == task.getStatus())
        .count();

    return (double) (closedTaskCount / tasks.size());

  }
}
