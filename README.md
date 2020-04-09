# ILoveZappos
An Android application displaying cryptocurrency price information using bitstamp API - Zappos intern challenge

## API used.
- [Bitstamp](http://bitstamp.com/) public APIs

## Libraries used.
- Chart library : [mpAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- Network handling : [Retrofit](https://square.github.io/retrofit/)
- JSON Parsing : [Moshi](https://github.com/square/moshi)
- Concurrency/Thread management : [Kotlin-Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- Database : [Room](https://developer.android.com/jetpack/androidx/releases/room)
- Dependency Injection : [Kodein](https://kodein.org/Kodein-DI/?5.0/android)
- Background task scheduler : [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager/)

## The application is having following components.
1. Splash screen: 
   - A basic static screen used to display some animations and provide an introcution to the app.
2. Transaction history: 
   - This screen displays line and a bar graph of bitcoin price history for the last one hour.
   - API Url : https://www.bitstamp.net/api/v2/transactions/btcusd/
3. Order Book:
   - This screen displays two tables using RecyclerViews; Bids and Asks.
   - API Url : https://www.bitstamp.net/api/v2/order_book/btcusd/
4. Price Alert:
   - This screen allows the user to create price alert notification which notifies the user when the current price goes below or above a user selected value. This allows the user to not monitor the much volatile Bitcoin market contnuously and get notified when the Bitcoin price hits the desired Buy/Sell value of the user.
   - It also allows to delete or update previsouly set alerts.
   - A background service runs every hour that makes API request to get the latest price and shows notification to the user if the latest price matches the alerts criteria set by user.
   - API Url : https://www.bitstamp.net/api/v2/ticker_hour/btcusd/

## Try out.
- Click [here](https://github.com/phoenix-254/ILoveZappos/blob/master/app/release/app-release.apk) to download APK file.
- Click [here](https://drive.google.com/file/d/1g1tRsBYwZz53KsKJiK-zuH4_woAea8Pf/view?usp=sharing) to view video demo.

## License
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
