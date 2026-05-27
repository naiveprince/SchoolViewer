package com.yonishik.schoolviewer.model

data class SchoolV2Dto(
    val id: Long,
    val schoolName: String?,
    val category: String?,
    val capacity: String?,
    val examDates: String?,
    val subjects: String?,
    val alternateSubjects: String?,
    val interview: String?,
    val englishQualificationBenefit: String?,
    val notes: String?,
    val infoLink: String?
)
