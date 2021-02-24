package com.example.myfirsttodo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirsttodo.R;
import com.example.myfirsttodo.database.TaskDatabase;
import com.example.myfirsttodo.database.TaskDatabase_Impl;
import com.example.myfirsttodo.model.Task;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class mainAdapter extends RecyclerView.Adapter<mainAdapter.MainViewHolder> {
    List<Task> taskList;
    Context context;
    CheckBox checkBox;
    TaskDatabase taskDatabase;
    Executor executor;

    public mainAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
        taskDatabase=TaskDatabase.getInstance(context);
        executor = Executors.newSingleThreadExecutor();
    }


    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_todo, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewTask.setText(task.getTextTask());
        if (task.getStatus() == 1) {
            holder.imageViewCheck.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.imageViewCheck.setImageResource(R.drawable.ic_baseline_check_24);
            holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    //ViewHolder
    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewDelete;
        TextView textViewTask;
        ImageView imageViewCheck;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCheck = itemView.findViewById(R.id.imageViewCheckBox);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);


            imageViewCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Task task = taskList.get(getAdapterPosition());
                    if (task.getStatus() == 0) {
                        task.setStatus(1);
                    } else {
                        task.setStatus(0);
                    }
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            taskDatabase.taskDao().updateTask(task);
                            taskList = taskDatabase.taskDao().getAllTasks();
                        }
                    });
                    notifyItemChanged(getAdapterPosition());
                }
            });

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTask(taskList.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    //Method of delete a task
    public void deleteTask(Task task) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                taskDatabase = TaskDatabase.getInstance(context);
                taskDatabase.taskDao().deleteTask(task);
            }
        });
        taskList.remove(task);
        updateAdapter(taskList);

    }

    //update our taskList
    public void updateAdapter(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }



}
