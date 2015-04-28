package actors

import actors.CountryAggregatorActor.AddCountry
import actors.CurrencyAggregatorActor.AddCurrency
import akka.actor.Actor
import models.TradeModels.Trade

class ProcessorActor extends Actor {
  import ProcessorActor._

  override def receive = {
    case t: NewTrade =>
      context.actorSelection("/user/tradeCountryAggregator") ! AddCountry(t.trade.originatingCountry)
      context.actorSelection("/user/tradeCurrencyAggregator") ! AddCurrency(t.trade.currencyFrom, t.trade.currencyTo, t.trade.timePlaced)
  }
}

object ProcessorActor {
  case class NewTrade(trade: Trade)
}