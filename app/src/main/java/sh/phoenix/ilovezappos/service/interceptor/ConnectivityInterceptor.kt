package sh.phoenix.ilovezappos.service.interceptor

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import sh.phoenix.ilovezappos.NetworkException

// Empty interface
interface ConnectivityInterceptor : Interceptor

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {
    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(isConnectedToNetwork()) {
            return chain.proceed(chain.request())
        }

        throw NetworkException()
    }

    private fun isConnectedToNetwork(): Boolean {
        val manager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}