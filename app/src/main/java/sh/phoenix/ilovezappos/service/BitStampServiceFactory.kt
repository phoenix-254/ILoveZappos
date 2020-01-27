package sh.phoenix.ilovezappos.service

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import sh.phoenix.ilovezappos.AppConstants

object BitStampServiceFactory {
    val BIT_STAMP_API_CLIENT: BitStampApiClient by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        // Create Retrofit client
        return@lazy retrofit.create(BitStampApiClient::class.java)
    }
}