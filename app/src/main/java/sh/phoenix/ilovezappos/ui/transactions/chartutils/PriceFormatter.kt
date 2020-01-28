package sh.phoenix.ilovezappos.ui.transactions.chartutils

import com.github.mikephil.charting.formatter.ValueFormatter

class PriceFormatter : ValueFormatter() {
    private val prefix: String = "$"

    override fun getFormattedValue(value: Float): String {
        return prefix + "%.2f".format(value)
    }
}