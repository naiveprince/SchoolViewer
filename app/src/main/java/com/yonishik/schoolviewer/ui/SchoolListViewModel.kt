package com.yonishik.schoolviewer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonishik.schoolviewer.data.SchoolRepository
//import com.yonishik.schoolviewer.model.SchoolV2Dto
import com.yonishik.schoolviewer.model.SchoolV2Dto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SchoolListUiState(
    val isLoading: Boolean = false,
    val schools: List<SchoolV2Dto> = emptyList(),
    val filteredSchools: List<SchoolV2Dto> = emptyList(),
    val query: String = "",
    val dataVersion: String? = null,
    val errorMessage: String? = null
)

class SchoolListViewModel(
    private val repository: SchoolRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SchoolListUiState())
    val uiState: StateFlow<SchoolListUiState> = _uiState.asStateFlow()

    fun loadSchools() {
        refresh()
    }

fun refresh() {
    viewModelScope.launch {
        val currentQuery = _uiState.value.query

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        try {
            val schools = repository.getSchools()
            val filtered = filterSchools(schools, currentQuery)



            _uiState.value = _uiState.value.copy(
                isLoading = false,
                schools = schools,
                filteredSchools = filtered,
                // dataVersion = version?.dataVersion,
                errorMessage = null
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = e.message ?: "Unknown error"
            )
        }
    }
}
    fun updateQuery(query: String) {
        val base = _uiState.value.schools
        val filtered = filterSchools(base, query)

        _uiState.value = _uiState.value.copy(
            query = query,
            filteredSchools = filtered
        )
    }

    private fun filterSchools(
        schools: List<SchoolV2Dto>,
        query: String
    ): List<SchoolV2Dto> {
        if (query.isBlank()) return schools

        return schools.filter {
            (it.schoolName ?: "").contains(query, ignoreCase = true)
        }
    }
}
