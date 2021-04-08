package com.example.weighttracker.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.weighttracker.database.WeightDatabaseDao
import com.example.weighttracker.database.WeightEntry
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DashboardViewModel(val database: WeightDatabaseDao, application: Application)
    : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Your Weight History"
    }
    val text: LiveData<String> = _text

    private fun getDaysFromToday(date: String): Int {
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val today = LocalDate.now()
        val endDate = LocalDate.parse(date, df)
        val period = Period.between(endDate, today)
        return (period.getYears() * 365) + (period.getMonths() * 30) + period.getDays()
    }

    private val days = database.getEntriesForGraph()
    private var series1 = Transformations.map(days) { days ->
        createSeries(days)
    }
    private fun createSeries(daysData: List<WeightEntry>): LineGraphSeries<DataPoint> {
        var newSeries = LineGraphSeries<DataPoint>()
        var x: Double = 0.0
        for (entry in daysData) {
            var y = entry.weight
            var offset = getDaysFromToday(entry.date)
            x = 0.0 - offset.toDouble()
            newSeries.appendData(DataPoint(x,y), true, 5000)
        }
        return newSeries
    }

    val series: LiveData<LineGraphSeries<DataPoint>> = series1
}
