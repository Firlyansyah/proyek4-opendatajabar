package com.example.hanyarunrun.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.hanyarunrun.data.AppDatabase
import com.example.hanyarunrun.data.BencanaEntity
import com.example.hanyarunrun.data.BencanaRepository
import com.example.hanyarunrun.data.DataEntity
import com.example.hanyarunrun.data.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val bencanaDao = AppDatabase.getDatabase(application).bencanaDao()
    private val repository = BencanaRepository(bencanaDao)

    val bencanaList: LiveData<List<BencanaEntity>> = repository.getAllBencana()

    private val dao = AppDatabase.getDatabase(application).dataDao()
    private val profileDao = AppDatabase.getDatabase(application).profileDao()
    val dataList: LiveData<List<DataEntity>> = dao.getAll()
    val profileList: LiveData<List<ProfileEntity>> = profileDao.getAll()

    fun fetchAndSaveBencana() {
        viewModelScope.launch {
            repository.fetchAndSaveBencana()
        }
    }

    fun insertProfile(
        nama: String,
        nim: String,
        email: String,
        profilePicture: String?
    ) {
        viewModelScope.launch {
            profileDao.insert(
                ProfileEntity(
                    profilePicture = profilePicture,
                    nama = nama,
                    nim = nim,
                    email = email
                )
            )
        }
    }

    fun insertData(
        namaProvinsi: String,
        namaKabupatenKota: String,
        jumlahKebakaran: String,
        satuan: String,
        tahun: String
    ) {
        viewModelScope.launch {
            val totalValue = jumlahKebakaran.toIntOrNull() ?: 0
            val tahunValue = tahun.toIntOrNull() ?: 0
            dao.insert(
                DataEntity(
                    namaProvinsi = namaProvinsi,
                    namaKabupatenKota = namaKabupatenKota,
                    jumlahKebakaran = totalValue,
                    satuan = satuan,
                    tahun = tahunValue
                )
            )
        }
    }

    fun updateData(data: DataEntity) {
        viewModelScope.launch {
            dao.update(data)
        }
    }

    fun updateProfile(data: ProfileEntity) {
        viewModelScope.launch {
            profileDao.update(data)
        }
    }

    fun deleteDataById(id: Int) {
        viewModelScope.launch {
            val dataToDelete = dao.getById(id)
            if (dataToDelete != null) {
                dao.delete(dataToDelete)
            }
        }
    }


    suspend fun getDataById(id: Int): DataEntity? {
        return withContext(Dispatchers.IO) {
            dao.getById(id)
        }
    }

    fun inserOrUpdateBencana(
        namaKabupatenKota: String,
        jumlahKebakaran: Int,
        satuan: String,
        tahun: String
    ) {
        viewModelScope.launch {
            bencanaDao.insertOrUpdateBencana(
                namaKabupatenKota = namaKabupatenKota,
                jumlahKebakaran = jumlahKebakaran,
                satuan = satuan,
                tahun = tahun.toIntOrNull() ?: 0
            )
        }
    }

    fun updateBencanaEdit(
        namaKabupaten: String,
        tahun: String,
        jumlahKebakaranBaru: String,
        jumlahKebakaranLama: String
    ) {
        viewModelScope.launch {
            bencanaDao.updateBencanaEdit(
                namaKabupaten = namaKabupaten,
                tahun = tahun.toIntOrNull() ?: 0,
                jumlahKebakaranBaru = jumlahKebakaranBaru.toIntOrNull() ?: 0,
                jumlahKebakaranLama = jumlahKebakaranLama.toIntOrNull() ?: 0
            )
        }
    }

    fun updateDataWithCheck(
        dataId: Int,
        selectedKabupaten: String?,
        namaKabupatenKota: String?,
        selectedTahun: String?,
        satuan: String,
        tahunInt: Int,
        jumlahKebakaran: String,
        jumlahKebakaranLama: String,
        context: Context,
        navController: NavHostController
    ) {
        viewModelScope.launch {
            val dataLama = getDataById(dataId) // Panggil suspend function di dalam coroutine

            val newNamaKabupaten = selectedKabupaten ?: namaKabupatenKota
            val newTahun = selectedTahun?.toIntOrNull() ?: tahunInt
            val newJumlahKebakaran = jumlahKebakaran.toIntOrNull() ?: 0

            if (dataLama != null) {
                val oldNamaKabupaten = dataLama.namaKabupatenKota
                val oldTahun = dataLama.tahun
                val oldJumlahKebakaran = dataLama.jumlahKebakaran

                if (oldNamaKabupaten != newNamaKabupaten || oldTahun != newTahun) {
                    inserOrUpdateBencana(
                        newNamaKabupaten ?: "",
                        newJumlahKebakaran,
                        satuan,
                        newTahun.toString()
                    )
                    deleteCreatedData(
                        oldNamaKabupaten ?: "",
                        oldTahun.toString(),
                        oldJumlahKebakaran
                    )
                } else {
                    updateBencanaEdit(
                        oldNamaKabupaten ?: "",
                        oldTahun.toString(),
                        newJumlahKebakaran.toString(),
                        oldJumlahKebakaran.toString()
                    )
                    Log.d("BENCANA_DEBUG","KOTA/KABUPATEN: ${oldNamaKabupaten}")
                    Log.d("BENCANA_DEBUG","TAHUN: ${oldTahun}")
                    Log.d("BENCANA_DEBUG","NEW JUMLAH KEBAKARAN: ${newJumlahKebakaran}")
                    Log.d("BENCANA_DEBUG","OLD JUMLAH KEBAKARAN: ${oldJumlahKebakaran}")
                }
            }

            val updatedData = DataEntity(
                id = dataId,
                namaProvinsi = dataLama?.namaProvinsi ?: "",
                namaKabupatenKota = newNamaKabupaten,
                jumlahKebakaran = jumlahKebakaran.toIntOrNull() ?: 0,
                satuan = dataLama?.satuan ?: "",
                tahun = newTahun
            )

            updateData(updatedData)
            Toast.makeText(context, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    fun deleteCreatedData(
        namaKabupatenKota: String,
        tahun: String,
        jumlahKebakaran: Int
    ) {
        viewModelScope.launch {
            bencanaDao.deleteCreatedData(
                namaKabupatenKota,
                tahun,
                jumlahKebakaran
            )
        }
    }
}
