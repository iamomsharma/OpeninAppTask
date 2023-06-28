import android.os.Build
import androidx.annotation.RequiresApi
import com.example.openinapptask.R
import com.example.openinapptask.model.DashboardModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import retrofit2.Response
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ResponseToChartMapper @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun convert(response: Response<DashboardModel>): List<Entry> {
        val overallUrlChart = response.body()?.data?.overall_url_chart

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

        val entries = mutableListOf<Entry>()
        averageByMonth.forEach { (monthName, value) ->
            val monthIndex = Month.valueOf(monthName.toString()).value - 1
            val entry = Entry(monthIndex.toFloat(), value.toFloat())
            entries.add(entry)
        }
        return entries
    }
}