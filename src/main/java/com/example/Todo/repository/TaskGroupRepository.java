package com.example.Todo.repository;

import com.example.Todo.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, String> {
    // 追加のクエリメソッドがあればここに追加
}