package com.example.repositiry;

import com.example.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Page<Task> findAll(Pageable pageable);

    Page<Task> findByAuthorId(Integer authorId, Pageable pageable);

    Page<Task> findByAssigneeId(Integer assigneeId, Pageable pageable);
}
