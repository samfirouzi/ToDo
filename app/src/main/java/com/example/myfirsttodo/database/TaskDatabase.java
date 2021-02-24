package com.example.myfirsttodo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myfirsttodo.model.Task;

@Database(entities = Task.class,exportSchema = false,version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public static  final String DB_NAME="database";
    static TaskDatabase instance;

    public static synchronized TaskDatabase getInstance(Context context){
       if (instance==null){
           instance= Room.databaseBuilder(context,TaskDatabase.class,DB_NAME)
                   .fallbackToDestructiveMigration().build();
       }
       return instance;
    }
   public abstract TaskDao taskDao();
}
