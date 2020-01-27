package sh.phoenix.ilovezappos.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import sh.phoenix.ilovezappos.data.OrderBook
import sh.phoenix.ilovezappos.data.Transaction

interface BitStampApiClient {
    @GET("transactions/{currency_pair}")
    suspend fun getTransactionHistory(@Path("currency_pair") currentPair:String) : Response<List<Transaction>>

    @GET("order_book/{currency_pair}")
    suspend fun getOrderBook(@Path("currency_pair") currentPair:String) : Response<OrderBook>
}