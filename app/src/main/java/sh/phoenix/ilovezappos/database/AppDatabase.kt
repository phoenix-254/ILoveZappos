package sh.phoenix.ilovezappos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE = "alerts"

@Database(
    entities = [AlertEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AlertTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alertDao() : AlertDao

    companion object {
        @Volatile
        private var instance : AppDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) : AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE).build()
        }
    }
}