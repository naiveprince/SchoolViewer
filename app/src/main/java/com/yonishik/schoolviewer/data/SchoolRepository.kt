package com.yonishik.schoolviewer.data

//import com.yonishik.schoolviewer.model.SchoolV2Dto
import com.yonishik.schoolviewer.model.SchoolV2Dto
import com.yonishik.schoolviewer.model.VersionDto
import com.yonishik.schoolviewer.network.SchoolApi

class SchoolRepository(
    private val api: SchoolApi
) {
    suspend fun getSchools(): List<SchoolV2Dto> {
        return api.getSchools()
    }
}
