package com.example.openinapptask

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Calendar


class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityDashboardBinding

    private lateinit var viewModel: DashboardViewModel

    companion object {
        var myResponse: DashboardModel? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        initialization()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialization() {

        setGreeting()
        getApiData()

        handleClick()

       // getLinkList()

    }

    private fun handleClick() {

        binding.btnTopLinks.setOnClickListener(this)
        binding.btnRecentLinks.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getApiData() {
        val repository = DashboardRepository()
        viewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(DashboardViewModel::class.java)

        viewModel.dataList.observe(this, Observer { dataList ->

            if (dataList.isSuccessful) {
                if (dataList.body() != null) {

                    myResponse = dataList.body()
                    binding.clReload.visibility = View.GONE
                    myResponse?.isTopLinkCheck = true

                    setupLineChart()
                    showLinksList(true)

                    //  Check for link button active or not
                    if (myResponse?.isTopLinkCheck == true) {
                        binding.btnTopLinks.background = ContextCompat.getDrawable(this, R.drawable.custom_blue_button)
                        Log.d("TAG", "getLinkList >> top_links >>: {${myResponse?.data?.top_links}}")

                    } else {
                        binding.btnRecentLinks.background = ContextCompat.getDrawable(this, R.drawable.custom_blue_button)
                        Log.d("TAG", "getLinkList >> recent_links >>: {${myResponse?.data?.recent_links}}")
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

    private fun showLinksList(topLinkList: Boolean) {

        if (topLinkList) {
            binding.rvLinks.adapter = TopLinkListAdapter(myResponse?.data?.top_links as ArrayList<DashboardModel.Data.TopLink>)
            binding.rvLinks.layoutManager = LinearLayoutManager(this)

        } else {
            binding.rvLinks.adapter = RecentLinkListAdapter(myResponse?.data?.recent_links as ArrayList<DashboardModel.Data.RecentLink>)
            binding.rvLinks.layoutManager = LinearLayoutManager(this)

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupLineChart() {

        val overallUrlChart = myResponse?.data?.overall_url_chart

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


        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val sumByMonth = mutableMapOf<Month, Int>()
        val countByMonth = mutableMapOf<Month, Int>()

        if (overallUrlChart != null) {
            for ((date, value) in overallUrlChart) {
                val localDate = LocalDate.parse(date, formatter)
                val month = localDate.month

                sumByMonth[month] = sumByMonth.getOrDefault(month, 0) + value
                countByMonth[month] = countByMonth.getOrDefault(month, 0) + 1
            }
        }

        val averageByMonth = mutableMapOf<Month, Double>()
        for ((month, sum) in sumByMonth) {
            val average = sum.toDouble() / 100.0
            averageByMonth[month] = average
        }


        for ((month, average) in averageByMonth) {
            println("$month: $average")
           // Entry(1f, 20f).apply { "$month: $average" }

        }

        // Set Static Value into Line chart
        val entries = listOf(
            Entry(0f, 40f),
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

    override fun onClick(view: View?) {
        when (view?.id)
        {
            R.id.btnTopLinks -> {
                myResponse?.isTopLinkCheck = true
                myResponse?.isRecentLinkCheck = false

                binding.btnTopLinks.background = ContextCompat.getDrawable(this, R.drawable.custom_blue_button)
                binding.btnTopLinks.setTextColor(ContextCompat.getColor(this, R.color.white)
                )

                // inActive Button Design
                binding.btnRecentLinks.background = ContextCompat.getDrawable(this, R.drawable.custom_transprint_button)
                binding.btnRecentLinks.setTextColor(ContextCompat.getColor(this, R.color.txt_gray_color)
                )

                showLinksList(true)
            }

            R.id.btnRecentLinks -> {
                // Set Button Checked Value
                myResponse?.isRecentLinkCheck = true
                myResponse?.isTopLinkCheck = false

                // isActive Button Design
                binding.btnRecentLinks.background = ContextCompat.getDrawable(this, R.drawable.custom_blue_button)
                binding.btnRecentLinks.setTextColor(ContextCompat.getColor(this, R.color.white)
                )

                // inActive Button Design
                binding.btnTopLinks.background = ContextCompat.getDrawable(this, R.drawable.custom_transprint_button)
                binding.btnTopLinks.setTextColor(ContextCompat.getColor(this, R.color.txt_gray_color)
                )

                showLinksList(false)
            }
        }
    }

}