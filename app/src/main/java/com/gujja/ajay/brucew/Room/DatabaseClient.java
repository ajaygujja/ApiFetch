package com.gujja.ajay.brucew.Room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class DatabaseClient {
    private static DatabaseClient databaseClient;

    private final AppDatabase appDatabase;

    private DatabaseClient(Context mContext){

        appDatabase = Room.databaseBuilder(mContext,AppDatabase.class,"alldata").build();
    }

    public static synchronized DatabaseClient getInstance(Context mContext){
        if (databaseClient == null){
            databaseClient = new DatabaseClient(mContext);
        }
        return databaseClient;
    }

    public AppDatabase getAppDatabase(){
        return appDatabase;
    }
}
