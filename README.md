# Market Trade Dashboard

## Architecture
I've developed a simple Scala Play (https://www.playframework.com) web application to display the dashboard and provide the REST service to ingest trades.

Application utilizes Akka (http://akka.io) actors to model information flow.
 
`ProcessorActor` receives the decoded trade information and then it distributes the required information to "aggregator" actors (`CountryAggregatorActor`,`CurrencyAggregatorActor`).

No persistent layer has been developed, but it could be easily added by adding a new actor to `ProcessorActor` to store trades.

A `TradeStreamActor` is created to serve each websocket channel to send data to the dashboard widgets. 

## Dashboard
The dashboard has three widgets that are updated on messages received in the websocket channel.
* A world chart displaying the top five originating countries by number of trades  
* A pie chart displaying distribution of originating currencies
* A scrolling line chart displaying the relation between EUR->USD and USD->EUR trades

The first two widgets are implements with Google Charts (https://developers.google.com/chart) and the last one with Smoothie Charts (http://smoothiecharts.org).

A modern browser is required to display the dashboard.

## Build
The project is built by sbt or activator, which is included.
 
To run tests just execute 
```bash
./activator test
```

A Gatling (http://gatling.io) scenario is also included in `utils/gatling/MarketTradeSimulation.scala` to *"stress"* the application. 

## Deployment
Application has been deployed to Heroku (https://www.heroku.com) using the free plan, so be patient with the first request as there could be a few second delay for the dyno to start. 
Subsequent requests will perform normally.
