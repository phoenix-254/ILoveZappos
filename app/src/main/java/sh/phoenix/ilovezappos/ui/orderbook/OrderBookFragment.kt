package sh.phoenix.ilovezappos.ui.orderbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_order_book.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.model.Ask
import sh.phoenix.ilovezappos.model.Bid
import sh.phoenix.ilovezappos.model.OrderBookItem
import sh.phoenix.ilovezappos.service.data.OrderBookResponse
import sh.phoenix.ilovezappos.ui.BaseFragment
import sh.phoenix.ilovezappos.ui.orderbook.orderbooklist.OrderBookListAdapter
import kotlin.math.min

class OrderBookFragment : BaseFragment(), KodeinAware {
    override val kodein by closestKodein()

    private lateinit var mContext: Context

    private lateinit var adapter: OrderBookListAdapter

    private val viewModel: OrderBookViewModel by instance()

    override fun onCreateViewBase(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_book, container, false)
    }

    override fun onRetryClick() {
        viewModel.getOrderBookData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDestroyView() {
        super.onDestroyView()
        orderList.adapter = null
    }

    override fun onStart() {
        super.onStart()

        setupAdapter()

        observeViewModel()

        viewModel.getOrderBookData()
    }

    private fun setupAdapter() {
        adapter = OrderBookListAdapter()
        orderList.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.loadingState.observe(this, Observer {
            if(it) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        })

        viewModel.response.observe(this, Observer {response ->
            if(response.success) {
                val orderBookResponse = response.data as OrderBookResponse

                val items = getFormattedOrderBookData(orderBookResponse)

                if(!items.isNullOrEmpty()) adapter.submitList(items)
                else super.showErrorDialog(null)
            } else {
                super.showErrorDialog(response.error?.errorType)
            }
        })
    }

    private fun getFormattedOrderBookData(orderBookResponse: OrderBookResponse?) : List<OrderBookItem>? {
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

            items.add(
                OrderBookItem(
                    orderBookResponse.timestamp,
                    Bid(bid[0].toString(), bid[1].toString()),
                    Ask(ask[0].toString(), ask[1].toString())
                )
            )

            i++
        }

        return items
    }
}