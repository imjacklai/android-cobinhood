package tw.jacklai.cobinhood.model

data class Response(
        val success: Boolean,
        val result: Result
)

data class Result(
        val tickers: List<Ticker>
)