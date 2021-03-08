package com.gujja.ajay.brucew.Room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface API_Dao {
    @Query("SELECT * FROM task")
    List<API_Data> getAll();

    @Insert
    void insert(API_Data api_data);

}
