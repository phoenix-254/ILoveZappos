package sh.phoenix.ilovezappos.ui.alerts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sh.phoenix.ilovezappos.database.DatabaseManager
import sh.phoenix.ilovezappos.model.AlertItem
import sh.phoenix.ilovezappos.ui.alerts.alertlist.AlertItemEvent
import java.lang.Exception

class AlertsViewModel(private val databaseManager: DatabaseManager) : ViewModel() {
    private val _alertList = MutableLiveData<List<AlertItem>>()
    val alertList: LiveData<List<AlertItem>> get() = _alertList

    private val _errorState = MutableLiveData<Boolean>()
    val errorState: LiveData<Boolean> get() = _errorState

    fun handleEvent(event: AlertItemEvent) {
        when (event) {
            is AlertItemEvent.OnStart -> getAlertsFromDatabase()
        }
    }

    fun deleteAlertFromDatabase(alert: AlertItem) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                _alertList.value = databaseManager.deleteAlert(alert)
            } catch (e : Exception) {
                _errorState.value = true
            }
        }
    }

    fun insertAlertToDatabase(alert: AlertItem) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                _alertList.value = databaseManager.insertOrUpdateAlert(alert)
            } catch (e : Exception) {
                _errorState.value = true
            }
        }
    }

    private fun getAlertsFromDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                _alertList.value = databaseManager.getAlerts()
            } catch (e : Exception) {
                _errorState.value = true
            }
        }
    }
}