package com.example.openinapptask.viewmodel

import ResponseToChartMapper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openinapptask.model.DashboardModel
import com.example.openinapptask.repository.DashboardRepository
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository,
    private val mapper: ResponseToChartMapper
) : ViewModel() {

    private val _dataList = MutableLiveData<Response<DashboardModel>>()
    val dataList: LiveData<Response<DashboardModel>> = _dataList

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    val chartData = MutableLiveData<List<Entry>>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchData(token: String) {
        viewModelScope.launch {
            try {
                val response = dashboardRepository.fetchData(token)
                val mappedResponse = mapper.convert(response)
                chartData.postValue(mappedResponse)
                _dataList.value = response
            } catch (e: Exception) {
                Log.d("TAG", "fetchData: {${e.message}}")
                launch(Dispatchers.Main) {
                    handleError(e)
                }
            }
        }
    }

    private fun handleError(error: Exception) {
        val errorMessage = error.message ?: "Unknown error occurred"
        _errorLiveData.postValue(errorMessage)
    }

}