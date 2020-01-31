package sh.phoenix.ilovezappos.service.data
import com.squareup.moshi.JsonClass
import sh.phoenix.ilovezappos.service.data.common.Data

@JsonClass(generateAdapter = true)
data class OrderBookResponse(
    val timestamp: String,
    val bids: List<Any>,
    val asks: List<Any>
) : Data