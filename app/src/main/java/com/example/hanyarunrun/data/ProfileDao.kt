package com.example.hanyarunrun.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ProfileDao {
    @Insert
    suspend fun insert(data: ProfileEntity)

    @Update
    suspend fun update(data: ProfileEntity)

    @Query("SELECT * FROM profile_table ORDER BY id DESC")
    fun getAll(): LiveData<List<ProfileEntity>>

    @Query("SELECT * FROM profile_table WHERE id = :dataId LIMIT 1")
    suspend fun getById(dataId: Int): ProfileEntity?

    @Delete
    suspend fun delete(data: ProfileEntity)

}
