package com.example.openinapptask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openinapptask.model.DashboardModel
import com.example.openinapptask.repository.DashboardRepository
import com.example.openinapptask.retrofitservice.RetrofitHelper.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class DashboardViewModel(private val dashboardRepository: DashboardRepository) : ViewModel() {

    private val _dataList = MutableLiveData<Response<DashboardModel>>()
    val dataList: LiveData<Response<DashboardModel>> = _dataList

    fun fetchData(token: String) {
        viewModelScope.launch {
            try {
                val response = dashboardRepository.fetchData(token)
                _dataList.value = response
            } catch (e: Exception) {
                Log.d("TAG", "fetchData: {${e.message}}")
            }
        }
    }

}