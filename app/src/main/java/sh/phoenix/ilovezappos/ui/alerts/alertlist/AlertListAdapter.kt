package sh.phoenix.ilovezappos.ui.alerts.alertlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.alert_list_item.view.*
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.model.AlertItem
import sh.phoenix.ilovezappos.model.AlertType

class AlertListAdapter(context: Context, val event: MutableLiveData<AlertItemEvent> = MutableLiveData()) :
    ListAdapter<AlertItem, AlertListAdapter.AlertItemViewHolder>(AlertDiffUtilCallback()) {

    private val mContext = context

    class AlertItemViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        var title: TextView = root.alertTitle
        var info: TextView = root.alertInfo
        var price: TextView = root.alertPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AlertItemViewHolder(inflater.inflate(R.layout.alert_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: AlertItemViewHolder, position: Int) {
        getItem(position).let {
            holder.title.text = it.title
            holder.info.text = it.description
            holder.price.text = it.triggerPrice

            if(it.alertType == AlertType.BUY) {
                holder.price.setTextColor(ContextCompat.getColor(mContext, R.color.green))
            } else if(it.alertType == AlertType.SELL) {
                holder.price.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            }

            holder.itemView.setOnClickListener {
                event.value = AlertItemEvent.OnClick(position)
            }
        }
    }
}