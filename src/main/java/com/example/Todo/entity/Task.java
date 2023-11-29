package com.example.Todo.entity;

import com.example.Todo.validation.constraints.TaskContentNotEmpty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tasks")
@TaskContentNotEmpty(message = "Task content must not be empty")
public class Task extends AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long taskId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 1000)
	private String content;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(nullable = false)
	private boolean allDay;

	@Column(nullable = false)
	private String status;

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