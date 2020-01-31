package sh.phoenix.ilovezappos.model

enum class AlertType(val value: Int) {
    BUY(0),
    SELL(1)
}

data class AlertItem(
    val createdDate: String,
    val title: String,
    val description: String,
    val triggerPrice: String,
    val alertType: AlertType
)