package com.example.taskappkb.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskappkb.model.TaskModel;

import java.util.List;


@Dao
public interface TaskDao {
    @Query("SELECT * FROM taskmodel")
    LiveData<List<TaskModel>> getAllLive();
//    @Query("SELECT * FROM taskmodel")
//    List<TaskModel> getAllLive();

    @Insert
    void insert(TaskModel taskModel);

    @Update
    void update(TaskModel taskModel);

    @Delete
    void delete(TaskModel taskModel);




}
