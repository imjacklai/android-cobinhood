package tw.jacklai.cobinhood.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_ticker.view.*
import tw.jacklai.cobinhood.R
import tw.jacklai.cobinhood.model.Ticker

class MainAdapter : RecyclerView.Adapter<MainAdapter.TickerViewHolder>() {
    private val tickers = ArrayList<Ticker>()

    fun setData(tickers: List<Ticker>) {
        this.tickers.clear()
        this.tickers.addAll(tickers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticker, parent, false)
        return TickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TickerViewHolder, position: Int) {
        holder.bindView(tickers[position])
    }

    override fun getItemCount(): Int {
        return tickers.size
    }

    inner class TickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(ticker: Ticker) {
            with (itemView) {
                tradingPairId.text = ticker.tradingPairId
                lastTradePrice.text = ticker.lastTradePrice()
                changeRate.text = ticker.changeRate()
            }
        }
    }
}