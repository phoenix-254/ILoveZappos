package sh.phoenix.ilovezappos.ui.transactions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.model.TransactionDateTime
import sh.phoenix.ilovezappos.model.TransactionItem
import sh.phoenix.ilovezappos.service.data.Transaction
import sh.phoenix.ilovezappos.service.data.TransactionResponse
import sh.phoenix.ilovezappos.ui.BaseFragment
import sh.phoenix.ilovezappos.ui.transactions.chartutils.*
import java.util.*

class TransactionFragment : BaseFragment(), KodeinAware {
    override val kodein by closestKodein()

    private enum class ChartType {
        BAR, LINE
    }

    private lateinit var mContext: Context

    private lateinit var selectedChartType: ChartType

    private lateinit var barChart: BarChart
    private lateinit var barChartSelector: MaterialButton

    private lateinit var lineChart: LineChart
    private lateinit var lineChartSelector: MaterialButton

    private val viewModel: TransactionViewModel by instance()

    override fun onCreateViewBase(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)

        barChart = root.barChart

        lineChart = root.lineChart

        // Default selected chart type
        selectedChartType = ChartType.BAR

        barChartSelector = root.chartTypeBarButton
        lineChartSelector = root.chartTypeLineButton

        return root
    }

    override fun onStart() {
        super.onStart()

        setBarChartProperties()

        setLineChartProperties()

        setChartTypeButtonSelectListener()

        observeViewModel()

        viewModel.getTransactionsData()
    }

    override fun onRetryClick() {
        viewModel.getTransactionsData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun setBarChartProperties() {
        barChart.setPinchZoom(false)
        barChart.setDrawBarShadow(false)
        barChart.setDrawGridBackground(false)
        barChart.description.isEnabled = false

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
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

    private fun setLineChartProperties() {
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

    private fun setChartTypeButtonSelectListener() {
        barChartSelector.addOnCheckedChangeListener { _, isChecked ->
            if(isChecked && selectedChartType != ChartType.BAR) {
                selectedChartType = ChartType.BAR
                viewModel.getTransactionsData()
            }
        }

        lineChartSelector.addOnCheckedChangeListener { _, isChecked ->
            if(isChecked && selectedChartType != ChartType.LINE) {
                selectedChartType = ChartType.LINE
                viewModel.getTransactionsData()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loadingState.observe(this, Observer {
            if(it) progressBarContainer.visibility = View.VISIBLE
            else progressBarContainer.visibility = View.GONE
        })

        viewModel.response.observe(this, Observer {response ->
            if(response.success) {
                val transactionResponse = response.data as TransactionResponse

                val chartData = getFormattedChartData(transactionResponse.transactions)

                if(chartData != null) {
                    if(selectedChartType == ChartType.BAR) {
                        showBarChartData(chartData)
                    } else if(selectedChartType == ChartType.LINE) {
                        showLineChartData(chartData)
                    }
                }
                else {
                    super.showErrorDialog(null)
                }
            } else {
                super.showErrorDialog(response.error?.errorType)
            }
        })
    }

    private fun getFormattedChartData(transactions: List<Transaction>?): List<TransactionItem>? {
        if(transactions == null || transactions.isEmpty()) {
            return null
        }

        // Keep only single data for one unix timestamp date
        val filteredTransactions: List<Transaction> = transactions.distinctBy { it.date }

        val items = mutableListOf<TransactionItem>()

        for(transaction in filteredTransactions) {
            val cal: Calendar = Calendar.getInstance()
            cal.time = Date(transaction.date.toLong() * 1000)

            val transactionDateTime = TransactionDateTime(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DATE), cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE))

            items.add(TransactionItem(transactionDateTime, transaction.price.toFloat()))
        }

        // Here we'll merge the data for the same minute in a single object
        // and set the price value to be average price for that minute.
        // This way we are limiting the number of bars to 60 i.e 1 bar for each minute in that hour.0
        val copy = items.toList()
        items.clear()

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

            items.add(TransactionItem(dateTime, averagePrice))

            // Reset for the next minute
            dateTime = copy[i].date
            averagePrice = copy[i].price
            count = 1
        }

        return items
    }

    private fun showBarChartData(chartData: List<TransactionItem>) {
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

    private fun showLineChartData(chartData: List<TransactionItem>) {
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

        lineChart.data = LineData(lineDataSet)

        lineChart.invalidate()

        barChart.visibility = View.INVISIBLE
        lineChart.visibility = View.VISIBLE
    }
}