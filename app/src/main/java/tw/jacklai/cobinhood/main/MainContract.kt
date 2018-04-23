package tw.jacklai.cobinhood.main

import tw.jacklai.cobinhood.BasePresenter
import tw.jacklai.cobinhood.model.Ticker

interface MainContract {
    interface View {
        fun showTickers(tickers: List<Ticker>)
        fun showConnectionFailed()
    }

    interface Presenter : BasePresenter<View> {
        fun getTickers()
    }
}