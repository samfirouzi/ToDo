package com.example.myfirsttodo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirsttodo.database.TaskDatabase;
import com.example.myfirsttodo.model.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddTask extends AppCompatActivity {
    EditText editTextTask;
    FloatingActionButton buttonAddTAsk;
    TaskDatabase taskDatabase;
    String taskDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Executor executor = Executors.newSingleThreadExecutor();
        taskDatabase = TaskDatabase.getInstance(this);
        editTextTask = findViewById(R.id.editTextTextWriteTask);
        buttonAddTAsk = findViewById(R.id.buttonAddTask);
        ImageView buttonSetDate = findViewById(R.id.buttonSetdate);
        TextView textViewAddDate = findViewById(R.id.textViewAddDate);
        final String taskId = getIntent().getStringExtra("taskId");

        if (taskId != null && !taskId.equals("")) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Task task = taskDatabase.taskDao().getTaskById(taskId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTextTask.setText(task.getTextTask());
                            textViewAddDate.setText(task.getDate());
                        }
                    });
                }
            });

        }

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        MaterialDatePicker materialDatePicker = builder.build();

        buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                textViewAddDate.setText(materialDatePicker.getHeaderText());
                taskDate = materialDatePicker.getHeaderText();
            }
        });

        buttonAddTAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTask.getText().toString().equals("")) {
                    Toast.makeText(AddTask.this, "Please enter a note", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String content = editTextTask.getText().toString();
                if (taskId != null && !taskId.equals("")) {
                    final String strDate = textViewAddDate.getText().toString();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            final Task task = taskDatabase.taskDao().getTaskById(taskId);
                            task.setTextTask(content);
                            task.setDate(strDate);
                            task.setStatus(0);
                            taskDatabase.taskDao().updateTask(task);
                        }
                    });
                    finish();
                    return;
                }
                executor.execute(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        taskDatabase.taskDao().addTask(new Task(content, 0, taskDate));
                    }
                });

            }

        });
    }
}