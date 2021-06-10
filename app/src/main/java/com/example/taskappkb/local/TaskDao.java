package com.example.taskappkb.local;

import android.app.DownloadManager;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.taskappkb.model.TaskModel;

import java.util.List;
@Dao
public interface TaskDao {
@Query("SELECT * FROM taskmodel")
    LiveData<List<TaskModel>> getAllLive();

@Insert
    void insert(TaskModel taskModel);
}
