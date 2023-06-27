package com.example.openinapptask.retrofitservice

import com.example.openinapptask.model.DashboardModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface OpeninAppApi {

  //  @GET("/api/v1/dashboardNew")
    //suspend fun getDashboardData(@Header("Token") val token) : Response<ArrayList<DashboardModel>>
  //  fun getDashboardData(@Header("Authorization") authHeader: String?): Response<ArrayList<DashboardModel>>

    @GET("/api/v1/dashboardNew")
    suspend fun getDashboardData(@Header("Authorization") token: String): Response<DashboardModel>


}