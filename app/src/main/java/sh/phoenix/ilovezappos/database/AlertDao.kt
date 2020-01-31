package sh.phoenix.ilovezappos.database

import androidx.room.*

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts")
    suspend fun getAlerts(): List<AlertEntity>

    @Delete
    suspend fun deleteAlert(alert: AlertEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAlert(alert: AlertEntity)
}