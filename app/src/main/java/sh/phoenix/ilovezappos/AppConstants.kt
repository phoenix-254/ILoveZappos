package sh.phoenix.ilovezappos

class AppConstants {
    companion object {
        const val APP_NAME = "ILoveZappos"

        const val BASE_URL = "https://www.bitstamp.net/api/v2/"

        // Dynamic way to select currency pair through UI - to be added
        const val CURRENCY_PAIR = "btcusd"

        const val SELL_NOTIFICATION_TITLE = "Time to make some profit $$$!"
        const val SELL_NOTIFICATION_TEXT_1 = "Sell your Bitcoin at the current price of "
        const val SELL_NOTIFICATION_TEXT_2 = " and earn some handsome profit."

        const val BUY_NOTIFICATION_TITLE = "Do not miss this opportunity!"
        const val BUY_NOTIFICATION_TEXT = "The time has come to invest into Bitcoins at very low price of "
    }
}