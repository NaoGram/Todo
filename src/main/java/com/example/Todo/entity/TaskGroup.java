package com.example.Todo.entity;

import javax.persistence.*;
import java.util.List;

import lombok.Data;

@Entity
@Table(name = "task_groups")
@Data
public class TaskGroup extends AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private String group_name;

	@Column
	private String color;

	@OneToMany(mappedBy = "taskGroup")
	private List<Task> tasks;
	

    // コンストラクタ、ゲッター、セッター、その他のメソッド

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
