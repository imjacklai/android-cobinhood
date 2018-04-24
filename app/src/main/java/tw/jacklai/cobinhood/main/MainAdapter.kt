package tw.jacklai.cobinhood.main

import android.graphics.Color
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
        holder.bindView(tickers[position], position)
    }

    override fun getItemCount(): Int {
        return tickers.size
    }

    inner class TickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(ticker: Ticker, position: Int) {
            with (itemView) {
                tradingPairId.text = ticker.tradingPairId
                lastTradePrice.text = ticker.lastTradePrice()

                val change = ticker.changeRate()

                when {
                    change == 0.0 -> {
                        changeRate.setTextColor(Color.parseColor("#5e6c75"))
                        changeRate.text = String.format("%.2f%%", change)
                    }
                    change > 0.0 -> {
                        changeRate.setTextColor(Color.parseColor("#12b691"))
                        changeRate.text = String.format("+%.2f%%", change)
                    }
                    else -> {
                        changeRate.setTextColor(Color.parseColor("#fc4359"))
                        changeRate.text = String.format("%.2f%%", change)
                    }
                }

                if (position % 2 == 0) itemView.setBackgroundColor(Color.parseColor("#122028"))
                else itemView.setBackgroundColor(Color.parseColor("#1b2931"))
            }
        }
    }
}