# ILoveZappos
An Android application displaying cryptocurrency price information using bitstamp API - Zappos intern challenge

## 3rd party sources used.
- Data source : [Bitstamp](http://bitstamp.com/) public APIs
- Chart library : [mpAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- Task Scheduling : [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager/)

## The application is having following components.
1. Transaction history: 
   - This screen displays line and a bar graph of price history over time.
   - API Url : https://www.bitstamp.net/api/v2/transactions/btcusd/
2. Order Book:
   - This screen displays two tables using RecyclerViews; Bids and Asks.
   - API Url : https://www.bitstamp.net/api/v2/order_book/btcusd/
3. Price Alert:
   - This screen allows the user to create price alert notification which notifies the user when the current price goes below a user selected value.
   - It also allows to delete or update previsouly set alerts.
   - A background service runs every hour that makes API request to get the current price and takes action based on the response received.
   - API Url : https://www.bitstamp.net/api/v2/ticker_hour/btcusd/

## License

[![License](http://img.shields.io/:license-mit-blue.svg?style=flat-square)](http://badges.mit-license.org)
- **[MIT license](http://opensource.org/licenses/mit-license.php)**
