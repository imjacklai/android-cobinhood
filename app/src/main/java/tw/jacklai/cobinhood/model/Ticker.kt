package tw.jacklai.cobinhood.model

import com.google.gson.annotations.SerializedName

data class Ticker(
        @SerializedName("trading_pair_id")
        val tradingPairId: String,

        val timestamp: Long,

        @SerializedName("24h_high")
        val high24h: String,

        @SerializedName("24h_low")
        val low24h: String,

        @SerializedName("24h_open")
        val open24h: String,

        @SerializedName("24h_volume")
        val volume24h: String,

        @SerializedName("last_trade_price")
        val lastTradePrice: String,

        @SerializedName("highest_bid")
        val highestBid: String,

        @SerializedName("lowest_ask")
        val lowestAsk: String
) {
    fun lastTradePrice(): String {
        val lastTradePriceDouble = lastTradePrice.toDouble()
        return if (lastTradePriceDouble == 0.0 || lastTradePriceDouble >= 1) {
            String.format("%.2f", lastTradePriceDouble)
        } else {
            String.format("%.6f", lastTradePriceDouble)
        }
    }

    fun changeRate(): String {
        return if (open24h.toDouble() == 0.0) {
            "0.00%"
        } else {
            String.format("%.2f%%", (lastTradePrice.toDouble() - open24h.toDouble()) / open24h.toDouble() * 100)
        }
    }
}