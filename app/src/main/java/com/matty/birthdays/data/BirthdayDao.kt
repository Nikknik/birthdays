package com.matty.birthdays.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BirthdayDao {

    @Query("SELECT * FROM birthday")
    fun getBirthdays(): Flow<List<Birthday>>

    @Query("SELECT * FROM birthday WHERE day=(:day) AND month=(:month)")
    fun getBirthdays(day: Int, month: Int): List<Birthday>

    @Insert(onConflict = IGNORE)
    suspend fun addBirthdays(birthday: List<Birthday>)

    @Query("DELETE FROM birthday WHERE contactId in (:id)")
    suspend fun deleteBirthdays(id: List<Long>)
}