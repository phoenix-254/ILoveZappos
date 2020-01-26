package sh.phoenix.ilovezappos.ui.orderbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderBookViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is order book Fragment"
    }

    val text: LiveData<String> = _text
}