package com.example.hanyarunrun.data

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api-backend/bigdata/bpbd/od_17604_jml_kejadian_bencana_kebakaran__kabupatenkota_v1")
    fun getBencana(): Call<BencanaResponse>
}
