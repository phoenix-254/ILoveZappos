package sh.phoenix.ilovezappos.ui.orderbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_order_book.*
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.ui.orderbook.orderbooklist.OrderBookEvent
import sh.phoenix.ilovezappos.ui.orderbook.orderbooklist.OrderBookListAdapter

class OrderBookFragment : Fragment() {
    private lateinit var mContext: Context

    private lateinit var viewModel: OrderBookViewModel

    private lateinit var adapter: OrderBookListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_order_book, container, false)

        viewModel = ViewModelProvider(this).get(OrderBookViewModel::class.java)

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDestroyView() {
        super.onDestroyView()
        order_list.adapter = null
    }

    override fun onStart() {
        super.onStart()

        setupAdapter()

        observeViewModel()

        viewModel.handleEvent(OrderBookEvent.OnStart)
    }

    private fun setupAdapter() {
        adapter = OrderBookListAdapter()
        order_list.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.errorState.observe(this, Observer {
            if(it) {
                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.orderBookList.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}