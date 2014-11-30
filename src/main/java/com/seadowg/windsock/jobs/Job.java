package com.seadowg.windsock.jobs;

public class Job {

    private final String name;
    private final String status;

    public Job(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
