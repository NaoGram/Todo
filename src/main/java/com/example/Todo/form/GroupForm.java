package com.example.Todo.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class GroupForm {

    private int groupId;

    @NotEmpty(message = "グループ名は必須です")
    @Size(max = 255, message = "グループ名は255文字以下で入力してください")
    private String groupName;

    private Date createdAt;

    private Date updatedAt;

    // コンストラクタ、ゲッター、セッターなど

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}