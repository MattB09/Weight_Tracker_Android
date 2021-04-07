package com.example.weighttracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeightDatabaseDao {

    @Insert
    suspend fun insert(weight: WeightEntry)

    @Update
    suspend fun update(weight: WeightEntry)

    @Query("SELECT * FROM weight_entries ORDER BY date")
    fun getAllEntries(): LiveData<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries WHERE id = :key")
    suspend fun getEntry(key: Int): WeightEntry?

    @Query("SELECT * FROM weight_entries WHERE date = :day")
    suspend fun getDay(day: String): WeightEntry?

    @Query("SELECT * FROM weight_entries ORDER BY id LIMIT 1")
    suspend fun getRecent(): WeightEntry?
}