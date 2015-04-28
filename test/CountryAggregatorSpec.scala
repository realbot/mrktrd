import actors.CountryAggregator
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CountryAggregatorSpec extends Specification {

  "Aggregator" should {

    "add a new country" in {
      val aggregator= new CountryAggregator
      aggregator.addCountry("IT")
      aggregator.topFive must_== List(("IT", 1L))
    }

    "add a new country - ignoring case" in {
      val aggregator= new CountryAggregator
      aggregator.addCountry("it")
      aggregator.topFive must_== List(("IT", 1L))
    }

    "to the top" in {
      val aggregator: CountryAggregator = new CountryAggregator
      aggregator.addCountry("IT")
      aggregator.addCountry("US")
      aggregator.addCountry("IT")
      aggregator.topFive must_== List(("IT", 2L), ("US", 1L))
    }

    "just five, ordered" in {
      val aggregator: CountryAggregator = new CountryAggregator
      aggregator.addCountry("IT")
      aggregator.addCountry("US")
      aggregator.addCountry("FR")
      aggregator.addCountry("GB")
      aggregator.addCountry("DE")
      aggregator.addCountry("SP")
      aggregator.addCountry("IT")
      aggregator.topFive must_== List(("IT", 2L), ("DE", 1L), ("FR", 1L), ("GB", 1L), ("SP", 1L))
    }

  }
}
