package com.example.myfirsttodo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirsttodo.adapter.mainAdapter;
import com.example.myfirsttodo.database.TaskDatabase;
import com.example.myfirsttodo.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Context context;
    TaskDatabase taskDatabase;
    mainAdapter mainAdapter;
    List<Task> taskList;
    RecyclerView recyclerViewMain;
    Executor executor;
    FloatingActionButton buttonGoAddTAsk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         buttonGoAddTAsk = findViewById(R.id.floatingActionButton);
        recyclerViewMain = findViewById(R.id.recyclerViewTask);
        taskDatabase = TaskDatabase.getInstance(MainActivity.this);
        executor = Executors.newSingleThreadExecutor();

        buttonGoAddTAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddTask.class);
                startActivity(intent);
            }
        });

        //getAllData
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Task> taskList = taskDatabase.taskDao().getAllTasks();
                mainAdapter = new mainAdapter(taskList, MainActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewMain.setAdapter(mainAdapter);
                        recyclerViewMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    }
                });

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    public void updateList() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Task> taskList = taskDatabase.taskDao().getAllTasks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainAdapter.updateAdapter(taskList);
                    }
                });
            }
        });


    }
}