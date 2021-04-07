package com.example.weighttracker.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weighttracker.database.WeightDatabaseDao
import com.example.weighttracker.database.WeightEntry
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel(val database: WeightDatabaseDao, application: Application)
        : AndroidViewModel(application) {

    private var today = MutableLiveData<WeightEntry?>()

    init {
        initializeToday()
    }

    private fun initializeToday() {
        viewModelScope.launch {
            today.value = getTodayFromDB()
            if (today.value != null) {

                // today_weight_text_view "Today's weight ${today.value}")
            }
        }
    }

    private suspend fun getTodayFromDB(): WeightEntry? {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formattedDay = current.format(formatter).toString()
        println("formatted day for today $formattedDay")
        return database.getDay(formattedDay)
    }

}