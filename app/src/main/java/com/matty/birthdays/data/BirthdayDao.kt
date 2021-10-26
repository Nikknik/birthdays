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

    @Insert(onConflict = IGNORE)
    suspend fun addBirthdays(birthday: List<Birthday>)

    @Query("DELETE FROM birthday WHERE id in (:id)")
    suspend fun deleteBirthdays(id: List<Long>)
}