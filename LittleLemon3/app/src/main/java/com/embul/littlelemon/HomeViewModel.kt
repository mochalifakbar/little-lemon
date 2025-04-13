package com.embul.littlelemon

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val menuDao = database.menuDao()

    private val _allMenuItems = MutableStateFlow<List<Menu>>(emptyList())
    private val _filteredMenuItems = MutableStateFlow<List<Menu>>(emptyList())

    val filteredMenuItems: StateFlow<List<Menu>> = _filteredMenuItems.asStateFlow()
    var searchQuery by mutableStateOf("")
    var selectedCategories by mutableStateOf<Set<String>>(emptySet())

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            if (menuDao.getAllMenu().isEmpty()) {
                try {
                    val apiData = Api.fetchMenu().map { it.toMenu() }
                    menuDao.insertAll(*apiData.toTypedArray())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            _allMenuItems.value = menuDao.getAllMenu()
            updateFilter()
        }
    }

    fun updateFilter() {
        _filteredMenuItems.value = _allMenuItems.value
            .filter { menu ->
                menu.title.contains(searchQuery, ignoreCase = true) ||
                        menu.description.contains(searchQuery, ignoreCase = true)
            }
            .filter { menu ->
                selectedCategories.isEmpty() || menu.category in selectedCategories
            }
    }
}