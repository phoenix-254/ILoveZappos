package sh.phoenix.ilovezappos.service.data

import com.squareup.moshi.JsonClass
import sh.phoenix.ilovezappos.service.data.common.Data

@JsonClass(generateAdapter = true)
data class Transaction(
    val amount: String,
    val date: String,
    val price: String,
    val tid: String,
    val type: String
)

data class TransactionResponse(
    val transactions: List<Transaction>?
) : Data