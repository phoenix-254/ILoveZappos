package sh.phoenix.ilovezappos.service

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import sh.phoenix.ilovezappos.service.data.HourlyTickerResponse
import sh.phoenix.ilovezappos.service.data.OrderBookResponse
import sh.phoenix.ilovezappos.service.data.Transaction
import sh.phoenix.ilovezappos.service.interceptor.ConnectivityInterceptor
import sh.phoenix.ilovezappos.AppConstants

interface BitStampApiService {
    @GET("transactions/{currency_pair}")
    suspend fun getTransactionHistory(@Path("currency_pair") currentPair:String) : Response<List<Transaction>>

    @GET("order_book/{currency_pair}")
    suspend fun getOrderBook(@Path("currency_pair") currentPair:String) : Response<OrderBookResponse>

    @GET("ticker_hour/{currency_pair}")
    suspend fun getHourlyTicker(@Path("currency_pair") currentPair:String) : Response<HourlyTickerResponse>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor) : BitStampApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(BitStampApiService::class.java)
        }
    }
}