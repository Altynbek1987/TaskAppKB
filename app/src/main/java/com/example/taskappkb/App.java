package com.example.taskappkb;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.taskappkb.local.AppDatabase;

public class App extends Application {
    //public static App instance;
    private static AppDatabase instance = null;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static AppDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.
                    databaseBuilder(context, AppDatabase.class, "mydatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

//    public AppDatabase getDatabase() {
//
//        return database;
//    }
}
