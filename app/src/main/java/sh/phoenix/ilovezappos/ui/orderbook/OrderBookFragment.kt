package sh.phoenix.ilovezappos.ui.orderbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import sh.phoenix.ilovezappos.R

class OrderBookFragment : Fragment() {
    private lateinit var orderBookViewModel: OrderBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderBookViewModel =
            ViewModelProviders.of(this).get(OrderBookViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_order_book, container, false)

        val textView: TextView = root.findViewById(R.id.text_order_book)
        orderBookViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }
}