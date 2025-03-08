package com.example.hanyarunrun.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bencana_table")
data class BencanaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama_kabupaten_kota: String,
    val jumlah_kebakaran: Int,
    val satuan: String,
    val tahun: Int
)
