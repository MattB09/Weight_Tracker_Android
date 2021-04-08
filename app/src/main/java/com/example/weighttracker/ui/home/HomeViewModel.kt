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
import java.text.ParseException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel(val database: WeightDatabaseDao, application: Application)
        : AndroidViewModel(application) {

    var weightString = MutableLiveData<String>("Today's weight: not yet logged...");
    val weightInput = ObservableField<String>()
    val dateInput = ObservableField<String>()

    private val todayString = makeTodayString()

    private fun makeTodayString(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return current.format(formatter).toString()
    }

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
        return database.getDay(todayString)
    }

    fun onSaveWeight() {
        viewModelScope.launch {
            // get data and validations
            var weight: Double = weightInput.get()?.toDouble() ?: return@launch
            var date: String = dateInput.get() ?: return@launch
            if (date > todayString) return@launch

            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            try {
                val isDate = LocalDate.parse(date, formatter)
                date = isDate.format(formatter).toString()
            } catch(e: ParseException) {
                return@launch
            }

            // check for duplicate
            val exists = database.getDay(date)

            // insert or update
            if (exists != null) {
                val editWeight = WeightEntry(exists.weightId, weight, date)
                update(editWeight)
            } else {
                val newWeight = WeightEntry(0L, weight, date)
                insert(newWeight)
            }
            if (date == todayString) {
                weightString.value = "Today's weight: ${weight.toString()}kgs"
            }
        }
    }

    private fun checkExists(days: List<WeightEntry>?, date: String): Long? {
        var entry = days?.find{it.date == date}
        if (entry != null) return entry.weightId
        return null
    }

    private suspend fun insert(weight: WeightEntry) {
        database.insert(weight)
    }

    private suspend fun update(weight: WeightEntry) {
        database.update(weight)
    }
}