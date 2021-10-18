package com.matty.birthdays.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

@Dao
interface BirthdayDao {

    @Query("SELECT * FROM birthday")
    suspend fun getBirthdays(): List<Birthday>

    @Insert(onConflict = IGNORE)
    suspend fun addBirthdays(birthday: List<Birthday>)
}