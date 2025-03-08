package com.example.hanyarunrun.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BencanaRepository(private val bencanaDao: BencanaDao) {

    fun getAllBencana(): LiveData<List<BencanaEntity>> {
        return bencanaDao.getAllBencana()
    }

    suspend fun fetchAndSaveBencana() {
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getBencana().execute()
                if (response.isSuccessful) {
                    response.body()?.data?.let { data ->
                        val entities = data.map { bencana ->
                            BencanaEntity(
                                id = bencana.id,
                                nama_kabupaten_kota = bencana.nama_kabupaten_kota,
                                jumlah_kebakaran = bencana.jumlah_kebakaran,
                                satuan = bencana.satuan,
                                tahun = bencana.tahun
                            )
                        }
                        bencanaDao.insertAll(entities)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
