package com.snow.diary.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.snow.diary.core.database.model.PersonEntity
import com.snow.diary.core.database.model.cross.PersonWithDreams
import com.snow.diary.core.database.model.cross.PersonWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Transaction
    @Insert
    fun insert(vararg entity: PersonEntity): List<Long>

    @Transaction
    @Update
    fun update(vararg entity: PersonEntity)

    @Transaction
    @Delete
    fun delete(vararg entity: PersonEntity)

    @Transaction
    @Query("SELECT * FROM person")
    fun getAll(): Flow<List<PersonEntity>>

    @Transaction
    @Query("SELECT * FROM person WHERE personId = :id")
    fun getById(id: Long): Flow<PersonEntity?>

    @Transaction
    @Query("SELECT * FROM person WHERE personId = :id")
    fun getPersonWithDreamsById(id: Long): Flow<PersonWithDreams?>

    @Transaction
    @Query("SELECT * FROM person")
    fun getAllPersonsWithDreams(): Flow<List<PersonWithDreams>>

    @Transaction
    @Query("SELECT * FROM person WHERE personId = :id")
    fun getPersonWithRelationsById(id: Long): Flow<PersonWithRelations?>

    @Transaction
    @Query("SELECT * FROM person")
    fun getAllPersonsWithRelationsById(): Flow<List<PersonWithRelations>>

    @Transaction
    @Query("DELETE FROM person")
    fun deleteAll()


}