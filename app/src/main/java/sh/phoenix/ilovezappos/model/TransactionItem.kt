package sh.phoenix.ilovezappos.model

data class TransactionDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minutes: Int
)

data class TransactionItem(
    val date: TransactionDateTime,
    val price: Float
)