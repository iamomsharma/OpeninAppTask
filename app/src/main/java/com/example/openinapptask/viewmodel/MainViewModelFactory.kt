package com.example.openinapptask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.openinapptask.repository.DashboardRepository
import com.example.openinapptask.viewmodel.DashboardViewModel

class MainViewModelFactory(private val repository: DashboardRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return DashboardViewModel(repository) as T
    }
}