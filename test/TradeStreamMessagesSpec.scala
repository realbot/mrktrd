import messages.TradeStreamMessages
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TradeStreamMessagesSpec extends Specification {

  "TradeStreamMessages" should {

    "convert top five countries in json - empty" in {
      val actual = TradeStreamMessages.geoChart(List())
      actual must_==
        """{
          |"geochart":{
          |"cols":[{"id":"A","label":"Country","type":"string"},{"id":"B","label":"Trades","type":"number"}],
          |"rows":[]
          |}
          |}""".stripMargin.replaceAll("\\n","")
    }

    "convert top five in json - some data" in {
      val actual = TradeStreamMessages.geoChart(List(("IT", 200L), ("US", 100L)))
      actual must_==
        """{
          |"geochart":{
          |"cols":[{"id":"A","label":"Country","type":"string"},{"id":"B","label":"Trades","type":"number"}],
          |"rows":[{"c":[{"v":"IT"},{"v":200}]},{"c":[{"v":"US"},{"v":100}]}]
          |}
          |}""".stripMargin.replaceAll("\\n","")
    }

    "convert currency in json - empty" in {
      val actual = TradeStreamMessages.currencyChart(List(), 0L, 0L, "")
      actual must_==
        """{
          |"currencychart":{
          |"cols":[{"id":"A","label":"Currency","type":"string"},{"id":"B","label":"Total","type":"number"}],
          |"rows":[]
          |},
          |"trendchart":{"eurToUsd":0,"usdToEur":0,"timestamp":""}
          |}""".stripMargin.replaceAll("\\n","")
    }

    "convert currency in json - some data" in {
      val actual = TradeStreamMessages.currencyChart(List(("EUR", 57L), ("USD",99L)), 32L, 42L, "24-JAN-15 10:44:44")
      actual must_==
        """{
          |"currencychart":{
          |"cols":[{"id":"A","label":"Currency","type":"string"},{"id":"B","label":"Total","type":"number"}],
          |"rows":[{"c":[{"v":"EUR"},{"v":57}]},{"c":[{"v":"USD"},{"v":99}]}]
          |},
          |"trendchart":{"eurToUsd":32,"usdToEur":42,"timestamp":"24-JAN-15 10:44:44"}
          |}""".stripMargin.replaceAll("\\n","")
    }

  }
}
