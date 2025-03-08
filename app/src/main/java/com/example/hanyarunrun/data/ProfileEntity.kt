package com.example.hanyarunrun.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val profilePicture: String?,
    val nama: String,
    val nim: String,
    val email: String
)