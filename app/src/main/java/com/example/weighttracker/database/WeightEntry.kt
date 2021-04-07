package com.example.weighttracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="weight_entries")
data class WeightEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id: Int,

    @ColumnInfo(name="weight")
    var weight: Double,

    @ColumnInfo(name="date")
    var date: String
) {
    constructor(weight: Double, date: String): this(Int.MIN_VALUE, weight, date)
}
