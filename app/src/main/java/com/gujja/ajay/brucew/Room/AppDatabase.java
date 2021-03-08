package com.gujja.ajay.brucew.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {API_Data.class},version = 3,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract API_Dao api_dao();
}
