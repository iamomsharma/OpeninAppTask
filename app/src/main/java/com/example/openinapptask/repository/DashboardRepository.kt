package com.example.openinapptask.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.openinapptask.model.DashboardModel
import com.example.openinapptask.retrofitservice.OpeninAppApi
import com.example.openinapptask.retrofitservice.RetrofitHelper
import retrofit2.Response

class DashboardRepository() {

    suspend fun fetchData(token: String): Response<DashboardModel> {
        return RetrofitHelper.apiService.getDashboardData(token)
    }
}