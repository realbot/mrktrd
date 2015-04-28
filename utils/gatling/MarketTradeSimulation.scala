package mrktrd


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import java.util.Date
import java.text.SimpleDateFormat

class MarketTradeSimulation extends Simulation {

  object Insert {

    val feederData = Array(
      Map("currency" -> "EUR", "country" -> "IT"), Map("currency" -> "EUR", "country" -> "FR"),
      Map("currency" -> "EUR", "country" -> "DE"), Map("currency" -> "EUR", "country" -> "AT"),
      Map("currency" -> "EUR", "country" -> "SP"), Map("currency" -> "EUR", "country" -> "GR"),
      Map("currency" -> "EUR", "country" -> "NL"), Map("currency" -> "EUR", "country" -> "IE"),
      Map("currency" -> "EUR", "country" -> "IT"), Map("currency" -> "EUR", "country" -> "FR"),
      Map("currency" -> "USD", "country" -> "US"), Map("currency" -> "BRL", "country" -> "BR"),
      Map("currency" -> "GBP", "country" -> "GB"), Map("currency" -> "AUD", "country" -> "AU"),
      Map("currency" -> "CAD", "country" -> "CA"), Map("currency" -> "MXN", "country" -> "MX"),
      Map("currency" -> "JPY", "country" -> "JP"), Map("currency" -> "RUB", "country" -> "RU"))

    val feederFrom = feederData.random
    val feederTo = (feederData.map{ m => (Map("currencyTo" -> m("currency"))) }).random
    val dateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:ss")
    val feederDate = Iterator.continually(Map("timePlaced" -> (dateFormat.format(new Date()).toUpperCase)))  //"24-JAN-15 10:44:44"

    val insertTrade =
      feed(feederFrom).feed(feederTo).feed(feederDate)
      .exec(http("POST Trade")
        .post("/trademsg")
        .header("Content-Type", "application/json")
        .body(StringBody("""{"userId": "134256", "currencyFrom": "${currency}", "currencyTo": "${currencyTo}", "amountSell": 1000, "amountBuy": 747.10, "rate": 0.7471, "timePlaced" : "${timePlaced}", "originatingCountry" : "${country}"}"""))
        .check(status.is(200)
      )
    )
  }

  val httpConf = http
    .baseURL("http://127.0.0.1:9000")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    //.proxy(Proxy("127.0.0.1", 3128))

  val users = scenario("Users").exec(Insert.insertTrade)

  setUp(
    users.inject(
      rampUsers(10) over (10 seconds),
      constantUsersPerSec(10) during(600 seconds)
    )
  ).protocols(httpConf)
}
