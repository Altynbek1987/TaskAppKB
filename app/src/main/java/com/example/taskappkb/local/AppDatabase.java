package com.example.taskappkb.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.taskappkb.model.TaskModel;

@Database(entities = {TaskModel.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
