package tw.jacklai.cobinhood

interface BasePresenter<in T> {
    fun attachView(view: T)
    fun detachView()
}