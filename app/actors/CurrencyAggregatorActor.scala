package actors

import akka.actor.Actor

class CurrencyAggregatorActor extends Actor {
  import CurrencyAggregatorActor._

  private val aggregator = new CurrencyAggregator

  override def receive = {
    case AddCurrency(currencyFrom, currencyTo, timePlaced) => aggregator.addCurrency(currencyFrom, currencyTo, timePlaced)

    case _: CurrencyRequest =>
      sender() ! CurrencyResult(aggregator.currenciesFromTotals, aggregator.eurToUsd, aggregator.usdToEur, aggregator.timestamp)
  }
}

object CurrencyAggregatorActor {
  case class AddCurrency(currencyFrom: String, currencyTo: String, timePlaced: String)

  case class CurrencyRequest()

  case class CurrencyResult(currenciesFrom: List[(String, Long)], eurToUsd: Long, usdToEur: Long, timestamp: String)
}

class CurrencyAggregator {

  private val currenciesFrom = scala.collection.mutable.Map[String, Long]()

  private[actors] var eurToUsd = 0L
  private[actors] var usdToEur = 0L
  private[actors] var timestamp = ""

  def addCurrency(currencyFrom: String, currencyTo: String, timePlaced: String) {
    val from = currencyFrom.toUpperCase
    val to = currencyTo.toUpperCase
    currenciesFrom += ((from, currenciesFrom.getOrElse(from, 0L) + 1))
    if ("EUR".equalsIgnoreCase(from) && "USD".equalsIgnoreCase(to)) {
      eurToUsd += 1
      timestamp = timePlaced
    }
    else if ("USD".equalsIgnoreCase(from) && "EUR".equalsIgnoreCase(to)) {
      usdToEur += 1
      timestamp = timePlaced
    }
  }

  def currenciesFromTotals: List[(String, Long)] = {
    currenciesFrom.toList.sorted
  }

}