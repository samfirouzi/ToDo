package com.example.myfirsttodo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myfirsttodo.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("select * from task")
    List<Task> getAllTasks();
    @Insert
    void addTask(Task task);
    @Delete
    void deleteTask(Task task);
    @Update
    void updateTask(Task task);
    @Query("select * from task where id=:id")
    Task getTaskById(String id);
}
