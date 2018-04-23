package tw.jacklai.cobinhood.model.api

import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import tw.jacklai.cobinhood.model.Response
import java.util.concurrent.TimeUnit

interface CobinhoodService {
    @GET("market/tickers")
    fun getTickers(): Observable<Response>

    companion object {
        fun create(): CobinhoodService {
            val client = OkHttpClient.Builder()
                    .readTimeout(8, TimeUnit.SECONDS)
                    .connectTimeout(8, TimeUnit.SECONDS)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl("https://api.cobinhood.com/v1/")
                    .build()

            return retrofit.create(CobinhoodService::class.java)
        }
    }
}