package com.example.Todo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "task_groups")
public class TaskGroup extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskGroupId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToMany(mappedBy = "taskGroup")
    private List<Task> tasks;

    // Constructors, getters, setters, and other methods
}