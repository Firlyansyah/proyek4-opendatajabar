package com.example.hanyarunrun.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaProvinsi: String,
    val namaKabupatenKota: String?,
    val jumlahKebakaran: Int,
    val satuan: String,
    val tahun: Int
)


