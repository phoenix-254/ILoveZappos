package sh.phoenix.ilovezappos

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import sh.phoenix.ilovezappos.service.BitStampApiService
import sh.phoenix.ilovezappos.service.interceptor.ConnectivityInterceptor
import sh.phoenix.ilovezappos.service.interceptor.ConnectivityInterceptorImpl
import sh.phoenix.ilovezappos.ui.orderbook.OrderBookViewModel
import sh.phoenix.ilovezappos.ui.transactions.TransactionViewModel

class ZapposApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        // Automatically imports android specific things for us such as Contexts etc.
        import(androidXModule(this@ZapposApplication))

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }

        bind() from singleton { BitStampApiService(instance()) }

        bind() from provider { TransactionViewModel(instance()) }

        bind() from provider { OrderBookViewModel(instance()) }
    }
}