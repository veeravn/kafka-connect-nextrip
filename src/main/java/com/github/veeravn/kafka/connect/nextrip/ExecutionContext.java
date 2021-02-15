package com.github.veeravn.kafka.connect.nextrip;

public class ExecutionContext {
    private String taskName;


    public String getTaskName() {
        return this.taskName;
    }

    public static ExecutionContext create(String taskName) {
        ExecutionContext context = new ExecutionContext();
        context.taskName = taskName;
        return context;
    }
}
