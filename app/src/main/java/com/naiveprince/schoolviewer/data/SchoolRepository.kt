package com.naiveprince.schoolviewer.data

//import com.naiveprince.schoolviewer.model.SchoolV2Dto
import com.naiveprince.schoolviewer.model.SchoolV2Dto
import com.naiveprince.schoolviewer.model.VersionDto
import com.naiveprince.schoolviewer.network.SchoolApi

class SchoolRepository(
    private val api: SchoolApi
) {
    suspend fun getSchools(): List<SchoolV2Dto> {
        return api.getSchools()
    }
}
