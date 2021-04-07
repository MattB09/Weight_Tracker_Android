package com.example.weighttracker

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.weighttracker.database.WeightDatabase
import com.example.weighttracker.database.WeightDatabaseDao
import com.example.weighttracker.database.WeightEntry
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeightDatabaseTest {

    private lateinit var weightDao: WeightDatabaseDao
    private lateinit var db: WeightDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, WeightDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        weightDao = db.weightDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetWeight() {
        val entry = WeightEntry(date="2021/04/01", weight = 67.0)
        weightDao.insert(entry)
        val entered = weightDao.getRecent()
        Assert.assertEquals(entered?.weight, 67.0)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetDate() {
        val entry = WeightEntry(date="2021/04/01", weight = 67.0)
        weightDao.insert(entry)
        val entered = weightDao.getRecent()
        Assert.assertEquals(entered?.date, "2021/04/01")
    }

//    @Test
//    @Throws(Exception::class)
//    suspend fun insertAndGetDateFormatted() {
//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
//        val formattedDay = current.format(formatter).toString()
//        val entry = WeightEntry(id=2, date=formattedDay, weight = 67.0)
//        weightDao.insert(entry)
//        val entered = weightDao.getDay("2021/04/06")
//        Assert.assertEquals(entered?.date, "2021/04/06")
//    }

}