package sh.phoenix.ilovezappos.data
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderBook(
    val timestamp: String,
    val bids: List<Any>,
    val asks: List<Any>
)