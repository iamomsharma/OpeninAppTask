package com.example.openinapptask

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openinapptask.adapter.RecentLinkListAdapter
import com.example.openinapptask.adapter.TopLinkListAdapter
import com.example.openinapptask.databinding.ActivityDashboardBinding
import com.example.openinapptask.model.DashboardModel
import com.example.openinapptask.repository.DashboardRepository
import com.example.openinapptask.viewmodel.DashboardViewModel
import com.example.openinapptask.viewmodel.MainViewModelFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import retrofit2.Response
import java.util.Calendar


class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding

    private lateinit var viewModel: DashboardViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        initialization()
    }

    private fun initialization() {

        setGreeting()
        getLinkList()
        setupLineChart()

    }

    private fun getLinkList() {
        val repository = DashboardRepository()
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(repository)
        ).get(DashboardViewModel::class.java)

        viewModel.dataList.observe(this, Observer { dataList ->

            if (dataList.isSuccessful) {
                if (dataList.body() != null) {
                    binding.clReload.visibility = View.GONE
                    dataList.body()?.isTopLinkCheck = true

                    if (dataList.body()?.isTopLinkCheck == true) {
                        binding.btnTopLinks.background =
                            ContextCompat.getDrawable(this, R.drawable.custom_blue_button)
                        Log.d(
                            "TAG",
                            "getLinkList >> top_links >>: {${dataList.body()?.data?.top_links}}"
                        )

                    } else {
                        binding.btnRecentLinks.background =
                            ContextCompat.getDrawable(this, R.drawable.custom_blue_button)
                        Log.d(
                            "TAG",
                            "getLinkList >> recent_links >>: {${dataList.body()?.data?.recent_links}}"
                        )
                    }

                    binding.btnTopLinks.setOnClickListener {
                        dataList.body()?.isTopLinkCheck = true
                        dataList.body()?.isRecentLinkCheck = false
                        binding.btnTopLinks.background =
                            ContextCompat.getDrawable(this, R.drawable.custom_blue_button)
                        binding.btnTopLinks.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.white
                            )
                        )

                        // isUnActive Button Design
                        binding.btnRecentLinks.background =
                            ContextCompat.getDrawable(this, R.drawable.custom_transprint_button)
                        binding.btnRecentLinks.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.txt_gray_color
                            )
                        )

                        showLinksList(true, dataList)
                    }

                    binding.btnRecentLinks.setOnClickListener {
                        dataList.body()?.isRecentLinkCheck = true
                        dataList.body()?.isTopLinkCheck = false

                        // isActive Button Design
                        binding.btnRecentLinks.background =
                            ContextCompat.getDrawable(this, R.drawable.custom_blue_button)
                        binding.btnRecentLinks.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.white
                            )
                        )

                        // isUnActive Button Design
                        binding.btnTopLinks.background =
                            ContextCompat.getDrawable(this, R.drawable.custom_transprint_button)
                        binding.btnTopLinks.setTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.txt_gray_color
                            )
                        )

                        showLinksList(false, dataList)

                    }

                } else {
                    binding.clReload.visibility = View.VISIBLE
                }
            } else {
                binding.clReload.visibility = View.VISIBLE
                Toast.makeText(this, dataList.message(), Toast.LENGTH_LONG).show()
            }

        })


        val AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
        viewModel.fetchData(AUTH_TOKEN)

    }

    private fun showLinksList(topLinkList: Boolean, dataList: Response<DashboardModel>) {

        if (topLinkList) {
            binding.rvLinks.adapter =
                TopLinkListAdapter(dataList.body()?.data?.top_links as ArrayList<DashboardModel.Data.TopLink>)
            binding.rvLinks.layoutManager = LinearLayoutManager(this)

        } else {
            binding.rvLinks.adapter =
                RecentLinkListAdapter(dataList.body()?.data?.top_links as ArrayList<DashboardModel.Data.RecentLink>)
            binding.rvLinks.layoutManager = LinearLayoutManager(this)

        }


    }

    private fun setupLineChart() {

        binding.lineChart.description.isEnabled = false
        binding.lineChart.setTouchEnabled(false)
        binding.lineChart.isDragEnabled = false
        binding.lineChart.setScaleEnabled(false)
        binding.lineChart.setPinchZoom(false)
        binding.lineChart.legend.isEnabled = false

        // Configure X axis
        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val labels = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawLabels(true)
        xAxis.gridColor = R.color.txt_gray_color
        xAxis.textColor = R.color.txt_gray_color
        xAxis.gridLineWidth = 0.5f
        xAxis.granularity = 1f

        // Configure Y axis
        binding.lineChart.axisRight.setDrawLabels(false)
        val yAxisLeft = binding.lineChart.axisLeft
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = 100f
        yAxisLeft.setGranularity(25f)
        yAxisLeft.textColor = R.color.txt_gray_color
        yAxisLeft.gridColor = R.color.txt_gray_color
        yAxisLeft.textSize = 12f
        yAxisLeft.gridLineWidth = 0.5f


        // Set Static Value into Line chart
        val entries = listOf(
            Entry(0f, 25f),
            Entry(1f, 20f),
            Entry(2f, 25f),
            Entry(3f, 30f),
            Entry(4f, 18f),
            Entry(5f, 22f)
        )

        // Create line data set
        val lineDataSet = LineDataSet(entries, "Line Data Set")
        lineDataSet.color = getColor(R.color.blue)
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable = getDrawable(R.drawable.linechart_gradient)

        // Create line chart data
        val lineData = LineData(lineDataSet)

        // Set line chart data
        binding.lineChart.data = lineData
        binding.lineChart.notifyDataSetChanged()
        binding.lineChart.invalidate()


        val list = listOf(7, 9, 0, 2, 2, 1, 0, 2, 1)

        val avg = list.average()
        println(avg) // 3.0

    }

    private fun setGreeting() {
        val calendar = Calendar.getInstance()
        val greeting = when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..4 -> "Good Night"
            in 5..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            in 18..20 -> "Good Evening"
            else -> "Good Night"
        }

        binding.tvGreeting.text = greeting


    }

}