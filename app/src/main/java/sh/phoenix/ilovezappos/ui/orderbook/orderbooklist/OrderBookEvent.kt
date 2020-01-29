package sh.phoenix.ilovezappos.ui.orderbook.orderbooklist

sealed class OrderBookEvent {
    object OnStart : OrderBookEvent()

    object OnUpdate : OrderBookEvent()
}