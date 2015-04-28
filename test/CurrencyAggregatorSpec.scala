import actors.CurrencyAggregator
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CurrencyAggregatorSpec extends Specification {

  "Aggregator" should {

    "add a new currency" in {
      val aggregator = new CurrencyAggregator
      aggregator.addCurrency("EUR", "USD", "24-JAN-15 10:44:44")
      aggregator.currenciesFromTotals must_== List(("EUR", 1L))
    }

    "add a new currency - ignoring case" in {
      val aggregator = new CurrencyAggregator
      aggregator.addCurrency("eur", "USD", "24-JAN-15 10:44:44")
      aggregator.currenciesFromTotals must_== List(("EUR", 1L))
    }

    "result a sorted list" in {
      val aggregator = new CurrencyAggregator
      aggregator.addCurrency("USD", "EUR", "24-JAN-15 10:44:44")
      aggregator.addCurrency("EUR", "USD", "24-JAN-15 10:44:44")
      aggregator.currenciesFromTotals must_== List(("EUR", 1L),("USD", 1L))
    }

  }
}
