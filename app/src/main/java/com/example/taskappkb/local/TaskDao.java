package com.example.taskappkb.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.taskappkb.model.TaskModel;

import java.util.List;


@Dao
public interface TaskDao {
    @Query("SELECT * FROM taskmodel")
    List<TaskModel> getAllLive();

    @Insert
    void insert(TaskModel taskModel);

    @Delete
    void delete(TaskModel taskModel);




}
