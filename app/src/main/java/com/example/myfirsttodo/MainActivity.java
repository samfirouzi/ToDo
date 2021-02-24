package com.example.myfirsttodo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirsttodo.adapter.mainAdapter;
import com.example.myfirsttodo.database.TaskDatabase;
import com.example.myfirsttodo.model.Task;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Context context;
    TaskDatabase taskDatabase;
    mainAdapter mainAdapter;
    List<Task> taskList;
    RecyclerView recyclerViewMain;
    EditText editTextTask;
    Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonAddTAsk = findViewById(R.id.buttonAddTask);
        editTextTask = findViewById(R.id.editTextTextWriteTask);
        recyclerViewMain = findViewById(R.id.recyclerViewTask);
        taskDatabase = TaskDatabase.getInstance(MainActivity.this);
        executor = Executors.newSingleThreadExecutor();
        buttonAddTAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTask.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter a note", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = editTextTask.getText().toString();
                executor.execute(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        taskDatabase.taskDao().addTask(new Task(content, 0));
                        updateList();
                    }
                });

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