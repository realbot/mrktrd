package controllers

import actors.{CurrencyAggregatorActor, TradeStreamActor, CountryAggregatorActor, ProcessorActor}
import actors.ProcessorActor.NewTrade
import play.api.libs.concurrent.Akka
import play.api.mvc._
import play.api.libs.json._
import models.TradeModels._
import akka.actor.Props
import play.api.Play.current

object Application extends Controller {

  private val tradeProcessor = Akka.system.actorOf(Props[ProcessorActor], name = "tradeProcessor")
  private val tradeCountryAggregator = Akka.system.actorOf(Props[CountryAggregatorActor], name = "tradeCountryAggregator")
  private val tradeCurrencyAggregator = Akka.system.actorOf(Props[CurrencyAggregatorActor], name = "tradeCurrencyAggregator")

  def index = Action {
    Ok(views.html.index())
  }

  def trade = Action(BodyParsers.parse.json) { request =>
    val b = request.body.validate[Trade]
    b.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      trade => {
        tradeProcessor ! NewTrade(trade)
        Ok(Json.obj("status" -> "OK"))
      }
    )
  }

  def tradeStream = WebSocket.acceptWithActor[String, String] { request => out =>
    Props(classOf[TradeStreamActor], out)
  }
}