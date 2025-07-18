package com.tasks.mappers;

import com.tasks.domain.dto.TaskDto;
import com.tasks.domain.entities.Task;

public interface TaskMapper {
  Task fromDto(TaskDto taskDto);

  TaskDto toDto(Task task);
}
