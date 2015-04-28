package actors

import akka.actor.Actor

class CountryAggregatorActor extends Actor {
  import CountryAggregatorActor._

  private val aggregator = new CountryAggregator

  override def receive = {
    case AddCountry(country) => aggregator.addCountry(country)

    case _: TopFiveRequest => sender() ! TopFiveResult(aggregator.topFive)
  }
}

object CountryAggregatorActor {
  case class AddCountry(country: String)

  case class TopFiveRequest()

  case class TopFiveResult(topfive: List[(String, Long)])
}

class CountryAggregator {

  private val countries = scala.collection.mutable.Map[String, Long]()

  def addCountry(country: String) {
    countries += ((country.toUpperCase, countries.getOrElse(country.toUpperCase, 0L) + 1))
  }

  def topFive: List[(String, Long)] = {
    countries.toList.sortBy(c => (-c._2, c._1)).take(5)
  }
}