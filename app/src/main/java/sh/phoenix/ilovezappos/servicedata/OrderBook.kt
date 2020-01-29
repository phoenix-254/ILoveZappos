package sh.phoenix.ilovezappos.servicedata
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderBook(
    val timestamp: String,
    val bids: List<Any>,
    val asks: List<Any>
)