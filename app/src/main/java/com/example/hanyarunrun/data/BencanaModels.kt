package com.example.hanyarunrun.data

data class BencanaResponse(
    val data: List<Bencana>
)

data class Bencana(
    val id: Int,
    val nama_kabupaten_kota: String,
    val jumlah_kebakaran: Int,
    val satuan: String,
    val tahun: Int
)
