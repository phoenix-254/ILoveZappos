package sh.phoenix.ilovezappos.database

import androidx.room.TypeConverter
import sh.phoenix.ilovezappos.model.AlertType

class AlertTypeConverter {
    @TypeConverter
    fun fromEnumToInt(value: AlertType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun fromIntToEnum(value: Int): AlertType? {
        return when(value) {
            0 -> AlertType.BUY
            1 -> AlertType.SELL
            else -> null
        }
    }
}