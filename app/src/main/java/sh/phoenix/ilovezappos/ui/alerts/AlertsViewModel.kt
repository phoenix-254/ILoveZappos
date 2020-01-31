package sh.phoenix.ilovezappos.ui.alerts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sh.phoenix.ilovezappos.model.AlertItem
import sh.phoenix.ilovezappos.model.AlertType
import sh.phoenix.ilovezappos.ui.alerts.alertlist.AlertItemEvent

class AlertsViewModel : ViewModel() {
    private val _alertList = MutableLiveData<List<AlertItem>>()
    val alertList: LiveData<List<AlertItem>> get() = _alertList

    private val _errorState = MutableLiveData<Boolean>()
    val errorState: LiveData<Boolean> get() = _errorState

    fun handleEvent(event: AlertItemEvent) {
        when (event) {
            is AlertItemEvent.OnStart -> getAlertsData()
        }
    }

    private fun getAlertsData() : List<AlertItem> {
        val list = mutableListOf<AlertItem>()

        list.add(AlertItem(System.currentTimeMillis().toString(),
            "Alert1", "Triggers when the Bitcoin price goes below $8012.", "$8012", AlertType.BUY))

        list.add(AlertItem(System.currentTimeMillis().toString(),
            "Alert2", "Triggers when the Bitcoin price goes above $9800.", "$9800", AlertType.SELL))

        list.add(AlertItem(System.currentTimeMillis().toString(),
            "Alert3", "Triggers when the Bitcoin price goes below $7500.", "$7500", AlertType.BUY))

        list.add(AlertItem(System.currentTimeMillis().toString(),
            "Alert4", "Triggers when the Bitcoin price goes below $7898.", "$7898", AlertType.BUY))

        _alertList.value = list

        return list
    }
}