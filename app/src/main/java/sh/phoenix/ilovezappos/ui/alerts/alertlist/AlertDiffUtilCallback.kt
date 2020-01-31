package sh.phoenix.ilovezappos.ui.alerts.alertlist

import androidx.recyclerview.widget.DiffUtil
import sh.phoenix.ilovezappos.model.AlertItem

class AlertDiffUtilCallback: DiffUtil.ItemCallback<AlertItem>() {
    override fun areItemsTheSame(oldItem: AlertItem, newItem: AlertItem): Boolean {
        return oldItem.createdDate == newItem.createdDate
    }

    override fun areContentsTheSame(oldItem: AlertItem, newItem: AlertItem): Boolean {
        return oldItem.createdDate == newItem.createdDate
    }
}