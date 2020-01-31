package sh.phoenix.ilovezappos.service.data

import com.squareup.moshi.JsonClass
import sh.phoenix.ilovezappos.service.data.common.Data

@JsonClass(generateAdapter = true)
data class HourlyTickerResponse(
    val timestamp: String,
    val open: String,
    val high: String,
    val low: String,
    val last: String,
    val bid: String,
    val ask: String,
    val vwap: String,
    val volume: String
) : Data