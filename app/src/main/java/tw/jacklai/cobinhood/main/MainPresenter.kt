package tw.jacklai.cobinhood.main

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tw.jacklai.cobinhood.model.api.CobinhoodService
import java.util.concurrent.TimeUnit

class MainPresenter(private val cobinhoodService: CobinhoodService) : MainContract.Presenter {
    private var mainView: MainContract.View? = null
    private var disposable: Disposable? = null

    override fun attachView(view: MainContract.View) {
        mainView = view
    }

    override fun detachView() {
        disposable?.dispose()
        mainView = null
    }

    override fun getTickers() {
        disposable?.dispose()

        val maxRetries = 3
        var retryCount = 0

        disposable = cobinhoodService.getTickers()
                .retryWhen { error ->
                    error.flatMap { e ->
                        if (++retryCount <= maxRetries) {
                            Observable.timer(Math.pow(2.0, retryCount.toDouble()).toLong(), TimeUnit.SECONDS)
                        } else {
                            Observable.error(e)
                        }
                    }
                }
                .repeatWhen { completed -> completed.delay(5, TimeUnit.SECONDS) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response ->
                            mainView?.showTickers(response.result.tickers)
                            retryCount = 0
                        },
                        { _ -> mainView?.showConnectionFailed() }
                )
    }
}