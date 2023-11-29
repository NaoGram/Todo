package com.example.Todo.validation.constraints;

import com.example.Todo.entity.Task;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaskContentNotEmptyValidator implements ConstraintValidator<TaskContentNotEmpty, Task> {

    @Override
    public void initialize(TaskContentNotEmpty constraintAnnotation) {
    }

    @Override
    public boolean isValid(Task task, ConstraintValidatorContext context) {
        if (task == null) {
            return true; // nullの場合は検証をスキップ
        }

        // contentフィールドが空でないことを検証
        return task.getContent() != null && !task.getContent().trim().isEmpty();
    }
}
