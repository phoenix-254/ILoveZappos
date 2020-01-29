package sh.phoenix.ilovezappos.ui.orderbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sh.phoenix.ilovezappos.model.Ask
import sh.phoenix.ilovezappos.model.Bid
import sh.phoenix.ilovezappos.model.OrderBookItem
import sh.phoenix.ilovezappos.service.BitStampServiceFactory
import sh.phoenix.ilovezappos.servicedata.OrderBook
import sh.phoenix.ilovezappos.ui.orderbook.orderbooklist.OrderBookEvent
import sh.phoenix.ilovezappos.utility.Constants
import kotlin.math.min

class OrderBookViewModel :  ViewModel() {
    private val _orderBookList = MutableLiveData<List<OrderBookItem>>()
    val orderBookList: LiveData<List<OrderBookItem>> get() = _orderBookList

    private val _errorState = MutableLiveData<Boolean>()
    val errorState: LiveData<Boolean> get() = _errorState

    fun handleEvent(event: OrderBookEvent) {
        when (event) {
            is OrderBookEvent.OnStart -> getOrderBookData()
            is OrderBookEvent.OnUpdate -> getOrderBookData()
        }
    }

    private fun getOrderBookData() {
        GlobalScope.launch(Dispatchers.Main) {
            val client = BitStampServiceFactory.BIT_STAMP_API_CLIENT
            val serviceResponse = client.getOrderBook(Constants.CURRENCY_PAIR)

            if(serviceResponse.isSuccessful) {
                val items = getFormattedOrderBookData(serviceResponse.body())
                if(items != null) {
                    _errorState.value = false
                    _orderBookList.value = items
                } else {
                    _errorState.value = true
                }
            } else {
                _errorState.value = true
            }
        }
    }

    private fun getFormattedOrderBookData(orderBookResponse: OrderBook?) : List<OrderBookItem>? {
        if(orderBookResponse == null) {
            return null
        }

        val items = mutableListOf<OrderBookItem>()

        val bids: List<Any>? = orderBookResponse.bids
        val asks: List<Any>? = orderBookResponse.asks

        if(bids == null || asks == null) return items

        val size = min(bids.size, asks.size)
        var i = 0
        while(i < size) {
            val bid: List<*> = bids[i] as List<*>
            val ask: List<*> = asks[i] as List<*>

            items.add(OrderBookItem(
                orderBookResponse.timestamp,
                Bid(bid[0].toString(), bid[1].toString()),
                Ask(ask[0].toString(), ask[1].toString())
            ))

            i++
        }

        return items
    }
}