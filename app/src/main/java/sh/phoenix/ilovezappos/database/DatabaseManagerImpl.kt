package sh.phoenix.ilovezappos.database

import sh.phoenix.ilovezappos.model.AlertItem
import sh.phoenix.ilovezappos.toAlertEntity
import sh.phoenix.ilovezappos.toAlertItemList

class DatabaseManagerImpl(private val dao: AlertDao) : DatabaseManager {
    override suspend fun getAlerts(): List<AlertItem> {
        return dao.getAlerts().toAlertItemList()
    }

    override suspend fun deleteAlert(alert: AlertItem): List<AlertItem> {
        dao.deleteAlert(alert.toAlertEntity)
        return dao.getAlerts().toAlertItemList()
    }

    override suspend fun insertOrUpdateAlert(alert: AlertItem): List<AlertItem> {
        dao.insertOrUpdateAlert(alert.toAlertEntity)
        return dao.getAlerts().toAlertItemList()
    }
}