package com.example.myfirsttodo.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity
public class Task {




    @PrimaryKey
    @NonNull
    String id;
    String textTask;
    int status;
    String date;
    String time;






    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTextTask() {
        return textTask;
    }

    public void setTextTask(String textTask) {
        this.textTask = textTask;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Task(String textTask, int status, String date, String time) {
        this.id = UUID.randomUUID().toString();
        this.textTask = textTask;
        this.status = status;
        this.date = date;
        this.time = time;

    }
}
