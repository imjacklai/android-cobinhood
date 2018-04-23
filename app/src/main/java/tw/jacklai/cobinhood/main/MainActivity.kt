package tw.jacklai.cobinhood.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import tw.jacklai.cobinhood.R
import tw.jacklai.cobinhood.model.Ticker
import tw.jacklai.cobinhood.model.api.CobinhoodService

class MainActivity : AppCompatActivity(), MainContract.View {
    private val cobinhoodService by lazy { CobinhoodService.create() }
    private val presenter by lazy { MainPresenter(cobinhoodService) }

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        refreshLayout.setOnRefreshListener {
            getTickers()
        }

        mainAdapter = MainAdapter()

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mainAdapter

        presenter.attachView(this)

        getTickers()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showTickers(tickers: List<Ticker>) {
        refreshLayout.isRefreshing = false
        mainAdapter.setData(tickers)
    }

    override fun showConnectionFailed() {
        refreshLayout.isRefreshing = false
        Snackbar.make(coordinatorLayout, "Connection failed", Snackbar.LENGTH_SHORT).show()
    }

    private fun getTickers() {
        refreshLayout.isRefreshing = true
        presenter.getTickers()
    }
}
