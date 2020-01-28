package sh.phoenix.ilovezappos.ui.transactions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.data.Transaction
import sh.phoenix.ilovezappos.data.TransactionDateTime
import sh.phoenix.ilovezappos.data.TransactionHistoryChartData
import sh.phoenix.ilovezappos.service.BitStampServiceFactory
import sh.phoenix.ilovezappos.ui.transactions.chartutils.*
import java.util.*

class TransactionFragment : Fragment() {
    private enum class ChartType {
        BAR, LINE
    }

    private lateinit var mContext: Context

    private lateinit var selectedChartType: ChartType

    private lateinit var root: View

    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_transaction, container, false)

        initBarChartView()

        initLineChartView()

        selectedChartType = ChartType.BAR

        val barChartSelector: MaterialButton = root.findViewById(R.id.chartTypeBarButton)
        barChartSelector.addOnCheckedChangeListener { _, isChecked ->
            if(isChecked && selectedChartType != ChartType.BAR) {
                selectedChartType = ChartType.BAR
                loadTransactionHistory()
            }
        }

        val lineChartSelector: MaterialButton = root.findViewById(R.id.chartTypeLineButton)
        lineChartSelector.addOnCheckedChangeListener { _, isChecked ->
            if(isChecked && selectedChartType != ChartType.LINE) {
                selectedChartType = ChartType.LINE
                loadTransactionHistory()
            }
        }

        loadTransactionHistory()

        return root
    }

    private fun initBarChartView() {
        barChart = root.findViewById(R.id.barChart)
        barChart.setPinchZoom(false)
        barChart.setDrawBarShadow(false)
        barChart.setDrawGridBackground(false)
        barChart.description.isEnabled = false

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.labelCount = 6

        barChart.axisLeft.valueFormatter = PriceFormatter()
        barChart.axisLeft.labelCount = 10

        barChart.axisRight.isEnabled = false

        barChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        barChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        barChart.legend.form = LegendForm.CIRCLE
        barChart.legend.setDrawInside(false)
        barChart.legend.textSize = 12f
    }

    private fun initLineChartView() {
        lineChart = root.findViewById(R.id.lineChart)
        lineChart.setPinchZoom(false)
        lineChart.setDrawGridBackground(false)
        lineChart.description.isEnabled = false

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.labelCount = 6

        lineChart.axisLeft.valueFormatter = PriceFormatter()
        lineChart.axisLeft.labelCount = 10

        lineChart.axisRight.isEnabled = false

        lineChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        lineChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        lineChart.legend.form = LegendForm.CIRCLE
        lineChart.legend.setDrawInside(false)
        lineChart.legend.textSize = 12f
    }

    private fun loadTransactionHistory() {
        GlobalScope.launch(Dispatchers.Main) {
            val client = BitStampServiceFactory.BIT_STAMP_API_CLIENT
            val serviceResponse = client.getTransactionHistory("btcusd")

            if(serviceResponse.isSuccessful) {
                val transactions: List<Transaction>? = serviceResponse.body()

                val chartData = getFormattedChartData(transactions)

                if(chartData != null) {
                    if(selectedChartType == ChartType.BAR) {
                        showBarChartData(chartData)
                    } else if(selectedChartType == ChartType.LINE) {
                        showLineChartData(chartData)
                    }
                }
            } else {
                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFormattedChartData(transactions: List<Transaction>?): List<TransactionHistoryChartData>? {
        if(transactions == null || transactions.isEmpty()) {
            Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show()
            return null
        }

        // Filter data with same unix timestamp date
        val filteredTransactions: List<Transaction> = transactions.distinctBy { it.date }

        val chartDataList = mutableListOf<TransactionHistoryChartData>()

        for(transaction in filteredTransactions) {
            val cal: Calendar = Calendar.getInstance()
            cal.time = Date(transaction.date.toLong() * 1000)

            val transactionDateTime = TransactionDateTime(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DATE), cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE))

            val chartData = TransactionHistoryChartData(
                transactionDateTime,
                transaction.price.toFloat()
            )

            chartDataList.add(chartData)
        }

        // Here we'll merge the data for the same minute in a single object
        // and set the price value to be average price for that minute.
        // This way we are limiting the number of bars to 60 i.e 1 bar for each minute in that hour.0
        val copy = chartDataList.toList()
        chartDataList.clear()

        var dateTime: TransactionDateTime? = null
        var averagePrice = 0.0f
        var count = 0

        for(i in copy.size - 1 downTo 0 step 1) {
            if(dateTime == null || dateTime == copy[i].date) {
                dateTime = copy[i].date
                count++
                averagePrice += copy[i].price
                continue
            }

            averagePrice = (averagePrice / count)

            chartDataList.add(TransactionHistoryChartData(dateTime, averagePrice))

            // Reset for the next minute
            dateTime = copy[i].date
            averagePrice = copy[i].price
            count = 1
        }

        return chartDataList
    }

    private fun showBarChartData(chartData: List<TransactionHistoryChartData>) {
        val barEntries = mutableListOf<BarEntry>()

        for((index, value) in chartData.withIndex()) {
            barEntries.add(BarEntry(index.toFloat(), value.price))
        }

        barChart.xAxis.valueFormatter = DateFormatter(chartData)

        val barDataSet = BarDataSet(barEntries, resources.getString(R.string.chart_label))
        barDataSet.valueFormatter = PriceFormatter()
        barDataSet.color = ContextCompat.getColor(mContext, R.color.colorPrimaryLight)
        barDataSet.barBorderWidth = 1f

        val barData = BarData(barDataSet)
        barData.barWidth = 0.9f

        barChart.data = barData

        barChart.invalidate()

        lineChart.visibility = View.INVISIBLE
        barChart.visibility = View.VISIBLE
    }

    private fun showLineChartData(chartData: List<TransactionHistoryChartData>) {
        val lineEntries = mutableListOf<Entry>()

        for((index, value) in chartData.withIndex()) {
            lineEntries.add(BarEntry(index.toFloat(), value.price))
        }

        lineChart.xAxis.valueFormatter = DateFormatter(chartData)

        val lineDataSet = LineDataSet(lineEntries, resources.getString(R.string.chart_label))
        lineDataSet.valueFormatter = PriceFormatter()
        lineDataSet.color = ContextCompat.getColor(mContext, R.color.colorPrimaryLight)
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawCircles(false)

        val lineData = LineData(lineDataSet)

        lineChart.data = lineData

        lineChart.invalidate()

        barChart.visibility = View.INVISIBLE
        lineChart.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}