package com.matty.birthdays.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface BirthdayDao {

    @Query("SELECT * FROM birthday")
    suspend fun getBirthdays(): List<Birthday>

    @Insert(onConflict = REPLACE)
    suspend fun addBirthdays(birthday: List<Birthday>)

    @Query("DELETE FROM birthday WHERE id in (:id)")
    suspend fun deleteBirthdays(id: List<Long>)
}