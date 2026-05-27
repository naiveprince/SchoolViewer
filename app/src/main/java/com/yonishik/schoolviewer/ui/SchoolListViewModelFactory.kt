package com.yonishik.schoolviewer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yonishik.schoolviewer.data.SchoolRepository

class SchoolListViewModelFactory(
    private val repository: SchoolRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchoolListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SchoolListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
