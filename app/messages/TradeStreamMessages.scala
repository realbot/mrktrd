package messages

import play.api.libs.json._

object TradeStreamMessages {
  def geoChart(data: List[(String, Long)]): String = {
    Json.obj(
      "geochart" -> encodeDataTableJson("Country", "Trades", data)
    ).toString()
  }

  def currencyChart(currencyFrom: List[(String, Long)], eurToUsd: Long, usdToEur: Long, timestamp:String): String = {
    Json.obj(
      "currencychart" -> encodeDataTableJson("Currency", "Total", currencyFrom),
      "trendchart" -> Json.obj("eurToUsd" -> eurToUsd, "usdToEur" -> usdToEur, "timestamp" -> timestamp)
    ).toString()
  }

  private def encodeDataTableJson(labelForString:String, labelForLong: String,
                                  rowsData: List[(String, Long)]): JsValue = {
    val rows: JsValue = Json.toJson(
      rowsData.map { r =>
          Json.obj(
            "c" -> Json.arr(Json.obj("v" -> r._1), Json.obj("v" -> r._2))
          )
        }
      )

      Json.obj(
        "cols" -> Json.arr(
          Json.obj(
            "id" -> "A",
            "label" -> labelForString,
            "type" -> "string"
          ),
          Json.obj(
            "id" -> "B",
            "label" -> labelForLong,
            "type" -> "number"
          )
        ),
        "rows" -> rows
      )
  }

}
