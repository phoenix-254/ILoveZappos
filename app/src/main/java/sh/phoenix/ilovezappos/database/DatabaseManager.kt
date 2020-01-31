package sh.phoenix.ilovezappos.database

import sh.phoenix.ilovezappos.model.AlertItem

interface DatabaseManager {
    suspend fun getAlerts(): List<AlertItem>

    suspend fun deleteAlert(alert: AlertItem): List<AlertItem>

    suspend fun insertOrUpdateAlert(alert: AlertItem): List<AlertItem>
}