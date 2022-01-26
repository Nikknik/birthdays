package com.matty.birthdays.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BirthdayDao {

    @Query("SELECT * FROM birthday")
    fun getAll(): Flow<List<Birthday>>

    @Query("SELECT * FROM birthday WHERE day=(:day) AND month=(:month)")
    fun getBirthdays(day: Int, month: Int): List<Birthday>

    @Insert(onConflict = IGNORE)
    suspend fun addAll(birthday: List<Birthday>)

    @Insert(onConflict = IGNORE)
    suspend fun add(birthday: Birthday)

    @Update
    suspend fun update(birthday: Birthday)

    @Query("DELETE FROM birthday WHERE contactId in (:id)")
    suspend fun deleteById(id: List<Long>)

    @Query("SELECT * FROM birthday where id = :id LIMIT 1")
    fun getById(id: Int): Flow<Birthday?>
}