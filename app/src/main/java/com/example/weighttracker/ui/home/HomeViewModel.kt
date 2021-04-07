package com.example.weighttracker.ui.home

import android.app.Application
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.weighttracker.database.WeightDatabaseDao
import com.example.weighttracker.database.WeightEntry
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel(val database: WeightDatabaseDao, application: Application)
        : AndroidViewModel(application) {

    var weightString = MutableLiveData<String>("You haven't weighed in yet");
    val weightInput = ObservableField<String>()
    val dateInput = ObservableField<String>()

    private val days = database.getAllEntries()
    val daysDisplay = Transformations.map(days) {days ->
        formatDays(days)
    }

    fun formatDays(days: List<WeightEntry>) : Spanned {
        val sb = StringBuilder()
        sb.apply {
            if (days.size > 0) {
                append("<h3>Your weight data: </h3>")
            }
            days.forEach {
                append("${it.date}  -  ${it.weight}kgs")
                append("<br/>")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    init {
        initializeToday()
    }

    private fun initializeToday() {
        viewModelScope.launch {
            var todayWeight = getTodayFromDB()
            if (todayWeight != null) {
               Log.d("WEIGHT_TEXT", todayWeight.toString())
               weightString.value = "Today's weight: ${todayWeight.weight.toString()}kgs"
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

    fun onSaveWeight() {
        viewModelScope.launch {
            // Log.d("WEIGHT_EDIT_VALUE", weightInput.get().toString())
            var weight: Double = weightInput.get()?.toDouble() ?: return@launch
            var date: String = dateInput.get() ?: return@launch
            var exists: WeightEntry? = null
            Transformations.map(days) {
                days -> exists = days.find{it.date == date}
            }

            //Log.d("DUPLICATE_CHECK", duplicate.toString())
            if (exists != null) {
                Log.d("DUPLICATE_CHECK", exists.toString())
                var id = exists!!.weightId
                val editWeight = WeightEntry(id.toLong(), weight, date)
                update(editWeight)
            } else {
                val newWeight = WeightEntry(0L, weight, date)
                insert(newWeight)
            }
        }
    }

    private suspend fun insert(weight: WeightEntry) {
        database.insert(weight)
    }

    private suspend fun update(weight: WeightEntry) {
        database.update(weight)
    }
}