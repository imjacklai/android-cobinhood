package tw.jacklai.cobinhood.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import tw.jacklai.cobinhood.R
import tw.jacklai.cobinhood.ViewType
import tw.jacklai.cobinhood.model.Ticker
import tw.jacklai.cobinhood.model.api.CobinhoodService

class MainActivity : AppCompatActivity(), MainContract.View {
    private val cobinhoodService by lazy { CobinhoodService.create() }
    private val presenter by lazy { MainPresenter(cobinhoodService) }
    private var currentViewType = ViewType.LIST
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        refreshLayout.setOnRefreshListener {
            getTickers()
        }

        mainAdapter = MainAdapter()
        recyclerView.adapter = mainAdapter
        setRecyclerViewLayoutManager(currentViewType)

        presenter.attachView(this)

        getTickers()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_view_type)
                .setIcon(if (currentViewType == ViewType.LIST) R.drawable.ic_grid else R.drawable.ic_list)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null || item.itemId != R.id.action_view_type) {
            return super.onOptionsItemSelected(item)
        }

        if (currentViewType == ViewType.LIST) {
            currentViewType = ViewType.GRID
            item.setIcon(R.drawable.ic_list)
            header.visibility = View.GONE
        } else {
            currentViewType = ViewType.LIST
            item.setIcon(R.drawable.ic_grid)
            header.visibility = View.VISIBLE
        }

        setRecyclerViewLayoutManager(currentViewType)

        return super.onOptionsItemSelected(item)
    }

    override fun showTickers(tickers: List<Ticker>) {
        refreshLayout.isRefreshing = false
        mainAdapter.setData(tickers)
    }

    override fun showConnectionFailed() {
        refreshLayout.isRefreshing = false
        Snackbar.make(coordinatorLayout, getString(R.string.connection_failed), Snackbar.LENGTH_SHORT).show()
    }

    private fun setRecyclerViewLayoutManager(viewType: ViewType) {
        val layoutManager = recyclerView.layoutManager

        val firstVisiblePosition = when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstCompletelyVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findFirstCompletelyVisibleItemPosition()
            else -> 0
        }

        recyclerView.layoutManager = when (viewType) {
            ViewType.LIST -> LinearLayoutManager(this)
            ViewType.GRID -> GridLayoutManager(this, 2)
        }

        mainAdapter.setViewType(viewType)
        recyclerView.scrollToPosition(firstVisiblePosition)
    }

    private fun getTickers() {
        refreshLayout.isRefreshing = true
        presenter.getTickers()
    }
}
