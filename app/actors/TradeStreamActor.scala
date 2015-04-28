package actors

import actors.CountryAggregatorActor.{TopFiveResult, TopFiveRequest}
import actors.CurrencyAggregatorActor.{CurrencyResult, CurrencyRequest}
import actors.TradeStreamActor.{CurrencyTick, CountryTick}
import akka.actor.{Props, ActorRef, Actor}
import messages.TradeStreamMessages
import play.api.libs.concurrent.Akka
import scala.concurrent.duration._
import scala.language.postfixOps
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._

class TradeStreamActor(out: ActorRef) extends Actor {

  def receive: Receive = {
    case s: String =>
      if ("start".equals(s)) {
        context.actorSelection("/user/tradeCountryAggregator") ! TopFiveRequest()
        Akka.system.scheduler.schedule(10 seconds, 10 seconds, self, CountryTick())
        context.actorSelection("/user/tradeCurrencyAggregator") ! CurrencyRequest()
        Akka.system.scheduler.schedule(3 seconds, 3 seconds, self, CurrencyTick())
      }

    case _: CountryTick =>
      context.actorSelection("/user/tradeCountryAggregator") ! TopFiveRequest()

    case _: CurrencyTick =>
      context.actorSelection("/user/tradeCurrencyAggregator") ! CurrencyRequest()

    case TopFiveResult(topFive) =>
      out ! (TradeStreamMessages.geoChart(topFive))

    case CurrencyResult(currenciesFrom, eurToUsd, usdToEur, timestamp) =>
      out ! (TradeStreamMessages.currencyChart(currenciesFrom, eurToUsd, usdToEur, timestamp))
  }

}

object TradeStreamActor {
  case class CountryTick()

  case class CurrencyTick()

  def props(out: ActorRef) = Props(new TradeStreamActor(out))
}