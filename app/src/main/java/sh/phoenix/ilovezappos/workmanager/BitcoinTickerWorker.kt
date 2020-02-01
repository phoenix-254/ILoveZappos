package sh.phoenix.ilovezappos.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sh.phoenix.ilovezappos.AppConstants
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.database.AppDatabase
import sh.phoenix.ilovezappos.database.DatabaseManagerImpl
import sh.phoenix.ilovezappos.model.AlertItem
import sh.phoenix.ilovezappos.model.AlertType
import sh.phoenix.ilovezappos.service.BitStampApiService
import sh.phoenix.ilovezappos.service.interceptor.ConnectivityInterceptorImpl
import sh.phoenix.ilovezappos.ui.MainActivity

class BitcoinTickerWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        GlobalScope.launch(Dispatchers.IO) {
            val service = BitStampApiService(ConnectivityInterceptorImpl(applicationContext))
            val response = service.getHourlyTicker(AppConstants.CURRENCY_PAIR)
            if(response.isSuccessful) {
                validatePriceForAlertsAndSendNotifications(response.body()?.last)
            }
        }

        return Result.success()
    }

    private fun validatePriceForAlertsAndSendNotifications(price: String?) {
        if(price == null) return

        GlobalScope.launch(Dispatchers.IO) {
            val manager = DatabaseManagerImpl(AppDatabase(applicationContext).alertDao())
            val alerts = manager.getAlerts()
            displayNotifications(alerts, price.toDouble())
        }
    }

    private fun displayNotifications(alerts: List<AlertItem>, price: Double) {
        val manager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(AppConstants.APP_NAME, AppConstants.APP_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)


        var title: String? = null
        var text: String? = null
        var id = 0
        for(alert in alerts) {
            if(alert.triggerPrice.substring(1).toDouble() > price && alert.alertType == AlertType.BUY) {
                title = AppConstants.BUY_NOTIFICATION_TITLE
                text = "${AppConstants.BUY_NOTIFICATION_TEXT}$$price."
            }
            else if(alert.triggerPrice.substring(1).toDouble() < price && alert.alertType == AlertType.SELL) {
                title = AppConstants.SELL_NOTIFICATION_TITLE
                text = "${AppConstants.SELL_NOTIFICATION_TEXT_1}$$price${AppConstants.SELL_NOTIFICATION_TEXT_2}"
            }

            if(!title.isNullOrEmpty() && !text.isNullOrEmpty()) {
                val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, AppConstants.APP_NAME)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                manager.notify(id, builder.build())
                id++
            }

            title = null
            text = null
        }
    }
}