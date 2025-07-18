package com.tasks.repositories;

import com.tasks.domain.entities.Task;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findByTaskListId(UUID taskListId);

  Optional<Task> findByTaskListIdAndId(UUID taskListId, UUID id);

}
