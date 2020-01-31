package sh.phoenix.ilovezappos.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import sh.phoenix.ilovezappos.model.AlertType

@Entity(
    tableName = "alerts",
    indices = [Index("creation_date")]
)
data class AlertEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "creation_date")
    val createdDate: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "trigger_price")
    val triggerPrice: String,

    @ColumnInfo(name = "alert_type")
    val alertType: AlertType
)