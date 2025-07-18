package com.tasks.mappers;

import com.tasks.domain.dto.TaskListDto;
import com.tasks.domain.entities.TaskList;

public interface TaskListMapper {

  TaskList fromDto(TaskListDto taskListDto);

  TaskListDto toDto(TaskList taskList);

}
