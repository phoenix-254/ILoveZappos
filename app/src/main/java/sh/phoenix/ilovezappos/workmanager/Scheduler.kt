package sh.phoenix.ilovezappos.workmanager

import android.content.Context
import android.content.SharedPreferences
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class Scheduler(private val context: Context) {
    private val sharePrefsFileName = "ZapposSharedPrefs"
    private val scheduleStatusKey = "SCHEDULE_STATUS"

    private val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = context.getSharedPreferences(sharePrefsFileName, Context.MODE_PRIVATE)
    }

    fun schedule() {
        val isScheduled = sharedPrefs.getBoolean(scheduleStatusKey, false)
        if(!isScheduled) {
            val request =
                PeriodicWorkRequestBuilder<BitcoinTickerWorker>(1, TimeUnit.HOURS).build()

            WorkManager.getInstance(context).enqueue(request)

            sharedPrefs.edit().putBoolean(scheduleStatusKey, true).apply()
        }
    }

    fun cancel() {
        WorkManager.getInstance(context).cancelAllWork()
        sharedPrefs.edit().putBoolean(scheduleStatusKey, false).apply()
    }
}