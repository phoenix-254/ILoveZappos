package sh.phoenix.ilovezappos.ui.orderbook.orderbooklist

import androidx.recyclerview.widget.DiffUtil
import sh.phoenix.ilovezappos.model.OrderBookItem

class OrderBookDiffUtilCallback: DiffUtil.ItemCallback<OrderBookItem>() {
    override fun areItemsTheSame(oldItem: OrderBookItem, newItem: OrderBookItem): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: OrderBookItem, newItem: OrderBookItem): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }
}