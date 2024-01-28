package com.example.Todo.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class TaskForm {
	
	public void printout(){
		System.out.println(taskId);
		System.out.println(title);
		System.out.println(content);
		System.out.println(startTime);
		System.out.println(userId);
		System.out.println(taskGroupId);
		System.out.println();
		System.out.println();
		}
	
    private int taskId;

    @NotEmpty(message = "タイトルは必須です")
    private String title;

    @NotEmpty(message = "内容は必須です")
    @Size(max = 1000, message = "内容は1000文字以下で入力してください")
    private String content;

    private Date startTime;

    private Date endTime;

    private boolean allDay;

//    @NotEmpty(message = "ステータスは必須です")
    private String status;

    private int userId;

    private int taskGroupId;

//    private Date createdAt;
//
//    private Date updatedAt;

    // コンストラクタ、ゲッター、セッターなど
}