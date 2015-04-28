package models

import play.api.libs.json.Json

object TradeModels {

  case class Trade(userId: String, currencyFrom: String, currencyTo: String,
                   amountSell: BigDecimal, amountBuy: BigDecimal, rate: BigDecimal,
                   timePlaced: String, originatingCountry: String)

  implicit val tradeWrites = Json.writes[Trade]
  implicit val tradeReads = Json.reads[Trade]
}
