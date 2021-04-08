package com.example.weighttracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeightDatabaseDao {

    @Insert
    suspend fun insert(weight: WeightEntry) : Long

    @Update
    suspend fun update(weight: WeightEntry)

    @Query("SELECT * FROM weight_entries ORDER BY date DESC")
    fun getAllEntries(): LiveData<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries ORDER BY date ASC")
    fun getEntriesForGraph(): LiveData<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries WHERE weightId = :key")
    suspend fun getEntry(key: Long): WeightEntry?

    @Query("SELECT * FROM weight_entries WHERE date = :day")
    suspend fun getDay(day: String): WeightEntry?

    @Query("SELECT * FROM weight_entries ORDER BY weightId LIMIT 1")
    suspend fun getRecent(): WeightEntry?
}