package com.gujja.ajay.brucew.Room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface API_Dao {
    @Query("SELECT * FROM API_Data")
    List<API_Data> getAll();

    @Insert
    void insert(API_Data api_data);

    @Delete
    void deleteword(API_Data api_data);

    @Update
    void update(API_Data api_data );

}
