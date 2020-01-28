package sh.phoenix.ilovezappos.data
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Transaction(
    val amount: String,
    val date: String,
    val price: String,
    val tid: String,
    val type: String
)

data class TransactionDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minutes: Int
)

data class TransactionHistoryChartData(
    val date: TransactionDateTime,
    val price: Float
)