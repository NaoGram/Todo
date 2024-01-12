package com.example.Todo.repository;

import com.example.Todo.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.Todo.entity.User;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Integer> {
	List<TaskGroup> findByUser(User user);
}
