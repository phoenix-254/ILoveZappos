package sh.phoenix.ilovezappos.ui.transactions.chartutils

import com.github.mikephil.charting.formatter.ValueFormatter
import sh.phoenix.ilovezappos.model.TransactionDateTime
import sh.phoenix.ilovezappos.model.TransactionItem

class DateFormatter(list: List<TransactionItem>) : ValueFormatter() {
    private val chartDataListItems: List<TransactionItem> = list

    override fun getFormattedValue(value: Float): String {
        val item: TransactionDateTime = chartDataListItems[value.toInt()].date
        val hour: String =
            if(item.hour.toString().length > 1) item.hour.toString()
            else ("0" + item.hour.toString())
        val minutes: String =
            if(item.minutes.toString().length > 1) item.minutes.toString()
            else ("0" + item.minutes.toString())
        return "$hour:$minutes"
    }
}