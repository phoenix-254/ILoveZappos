package sh.phoenix.ilovezappos.servicedata
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Transaction(
    val amount: String,
    val date: String,
    val price: String,
    val tid: String,
    val type: String
)