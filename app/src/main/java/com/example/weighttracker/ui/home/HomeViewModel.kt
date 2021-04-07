package com.example.weighttracker.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weighttracker.database.WeightDatabaseDao

class HomeViewModel(val database: WeightDatabaseDao, application: Application)
        : AndroidViewModel(application) {

//    private var today = MutableLiveData<WeightEntry?>()

//    init {
//        initializeToday()
//    }
//
//    private fun initializeToday() {
//        viewModelScope.launch {
//            today.value = getTodayFromDB()
//        }
//    }
//
//    private suspend fun getTodayFromDB(): WeightEntry? {
//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
//        val formattedDay = current.format(formatter).toString()
//        println("formatted day for today $formattedDay")
//        return database.getDay(formattedDay)
//    }

}