package com.example.Todo.entity;

import com.example.Todo.validation.constraints.TaskContentNotEmpty;
import com.example.Todo.entity.User;
import com.example.Todo.entity.Task;
import com.example.Todo.entity.TaskGroup;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;

import lombok.Data;

@Entity
@Table(name = "tasks")
@TaskContentNotEmpty(message = "Task content must not be empty")
@Data
public class Task extends AbstractEntity {
	
	@Id
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int taskId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 1000)
	private String content;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(nullable = true)
	private boolean allDay;

	@Column(nullable = true)
	private String status;
	
//	@Column(nullable = false)
//	private Long taskGroupId;
	
//	@Column(name = "created_at", nullable = false, updatable = false)
//    private Date createdAt;
//
//    @Column(name = "updated_at")
//    private Date updatedAt;
	
//	@Column(nullable = false)
//	private Long userId;
	
//	@Column(nullable = false)
//	private Long taskGroupId;


	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "task_group_id")
	private TaskGroup taskGroup;

	public String getContent() {
		return content;
	}

	public void setUser(User user) {
		this.user = user;
	}
}