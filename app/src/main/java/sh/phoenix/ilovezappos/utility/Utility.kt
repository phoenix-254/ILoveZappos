package sh.phoenix.ilovezappos.utility

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity

object Utility {
    fun isConnectedToNetwork(activity: AppCompatActivity): Boolean {
        val manager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}