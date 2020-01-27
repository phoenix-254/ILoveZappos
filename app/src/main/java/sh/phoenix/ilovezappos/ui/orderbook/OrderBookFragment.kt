package sh.phoenix.ilovezappos.ui.orderbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.service.BitStampServiceFactory

class OrderBookFragment : Fragment() {
    private lateinit var orderBookViewModel: OrderBookViewModel

    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderBookViewModel =
            ViewModelProviders.of(this).get(OrderBookViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_order_book, container, false)

        textView = root.findViewById(R.id.text_order_book)
        orderBookViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        loadOrderBook()

        return root
    }

    private fun loadOrderBook() {
        GlobalScope.launch(Dispatchers.Main) {
            val client = BitStampServiceFactory.BIT_STAMP_API_CLIENT
            val serviceResponse = client.getOrderBook("btcusd")

            if(serviceResponse.isSuccessful) {
                val bids: List<Any>? = serviceResponse.body()?.bids
                val asks: List<Any>? = serviceResponse.body()?.asks

                val bid: List<*>? = bids?.get(0) as List<*>?

                textView.text = bid?.get(0) as CharSequence
            } else {
                textView.text = "Error ${serviceResponse.code()}"
            }
        }
    }
}