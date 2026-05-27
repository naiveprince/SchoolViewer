package com.naiveprince.schoolviewer.network

//import com.naiveprince.schoolviewer.model.SchoolV2Dto
import com.naiveprince.schoolviewer.model.SchoolV2Dto
import com.naiveprince.schoolviewer.model.VersionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SchoolApi {

    @GET("api/schools")
    suspend fun getSchools(): List<SchoolV2Dto>

    @GET("api/schools")
    suspend fun searchByName(
        @Query("name") name: String
    ): List<SchoolV2Dto>
}