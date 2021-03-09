package com.gujja.ajay.brucew.Room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class DatabaseClient {
    private static DatabaseClient databaseClient;
    private Context mContext;
    private AppDatabase appDatabase;

    private DatabaseClient(Context mContext){
        this.mContext = mContext;
        appDatabase = Room.databaseBuilder(mContext,AppDatabase.class,"alldata")
                .fallbackToDestructiveMigration()
                .build();
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
