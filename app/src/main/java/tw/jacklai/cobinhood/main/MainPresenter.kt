package tw.jacklai.cobinhood.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tw.jacklai.cobinhood.model.api.CobinhoodService

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

        cobinhoodService.getTickers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response -> mainView?.showTickers(response.result.tickers) },
                        { _ -> mainView?.showConnectionFailed() },
                        { /* onComplete */ },
                        { disposable -> this.disposable = disposable }
                )
    }
}