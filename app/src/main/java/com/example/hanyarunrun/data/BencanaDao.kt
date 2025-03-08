package com.example.hanyarunrun.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.*
import androidx.lifecycle.LiveData

@Dao
interface BencanaDao {

    @Query("SELECT * FROM bencana_table WHERE nama_kabupaten_kota = :namaKabupaten AND tahun = :tahun")
    suspend fun getBencana(namaKabupaten: String, tahun: Int): BencanaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bencanaList: List<BencanaEntity>)

    @Query("SELECT * FROM bencana_table")
    fun getAllBencana(): LiveData<List<BencanaEntity>>

    @Insert
    suspend fun insertBencana(bencana: BencanaEntity)

    @Query("UPDATE bencana_table SET jumlah_kebakaran = jumlah_kebakaran + :jumlahKebakaran WHERE nama_kabupaten_kota = :namaKabupaten AND tahun = :tahun")
    suspend fun updateBencana(namaKabupaten: String, tahun: Int, jumlahKebakaran: Int)

    @Transaction
    suspend fun insertOrUpdateBencana(
        namaKabupatenKota: String,
        jumlahKebakaran: Int,
        satuan: String,
        tahun: Int
    ) {
        val existingBencana = getBencana(namaKabupatenKota, tahun)
        if (existingBencana != null) {
            updateBencana(namaKabupatenKota, tahun, jumlahKebakaran)
        } else {
            insertBencana(
                BencanaEntity(
                    nama_kabupaten_kota = namaKabupatenKota,
                    jumlah_kebakaran = jumlahKebakaran,
                    satuan = satuan,
                    tahun = tahun
                )
            )
        }
    }

    @Query("""
        UPDATE bencana_table 
        SET jumlah_kebakaran = jumlah_kebakaran + :selisih 
        WHERE nama_kabupaten_kota = :namaKabupaten AND tahun = :tahun
    """)
    suspend fun updateJumlahKebakaran(namaKabupaten: String, tahun: Int, selisih: Int)

    @Transaction
    suspend fun updateBencanaEdit(namaKabupaten: String, tahun: Int, jumlahKebakaranBaru: Int, jumlahKebakaranLama: Int) {
        val existingBencana = getBencana(namaKabupaten, tahun)
        if (existingBencana != null) {
            val selisih = jumlahKebakaranBaru - jumlahKebakaranLama
            if (selisih != 0) {
                updateJumlahKebakaran(namaKabupaten, tahun, selisih)
            }
        }
    }

    @Query("""
        UPDATE bencana_table 
        SET jumlah_kebakaran = jumlah_kebakaran - :jumlahKebakaran 
        WHERE nama_kabupaten_kota = :namaKabupaten AND tahun = :tahunNew
    """)
    suspend fun deleteCreatedData(namaKabupaten: String, tahunNew: String, jumlahKebakaran: Int)
}
