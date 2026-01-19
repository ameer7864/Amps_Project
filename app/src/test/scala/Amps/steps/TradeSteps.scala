package Amps.steps

import io.cucumber.scala.{EN, ScalaDsl}
import play.api.libs.json._
import Amps.Trade
import org.scalatest.Assertions._

class TradeSteps extends ScalaDsl with EN {

  private var tradeJson: String = _
  private var trade: Trade = _

  Given("I have a valid trade JSON") { () =>
    tradeJson = """
    {
      "trade_id": 12345,
      "order_id": "ORD-001",
      "execution_id": "EXEC-001",
      "symbol": "AAPL",
      "side": "BUY",
      "quantity": 100.0,
      "price": 150.50,
      "trade_time": "2024-01-15T10:30:00Z",
      "venue": "NYSE",
      "currency": "USD",
      "account_id": "ACC-001",
      "broker_id": "BROK-001",
      "commission": 10.00,
      "tax": 5.00,
      "gross_amount": 15050.00,
      "net_amount": 15035.00,
      "received_time": "2024-01-15T10:30:05Z",
      "status": "VALIDATED"
    }
    """
    println("Given: Valid trade JSON")
  }

  When("I parse it to Trade object") { () =>
    trade = Json.parse(tradeJson).as[Trade]
    println(s"When: Parsed trade ID ${trade.trade_id}")
  }

  Then("trade ID should be {int}") { (expectedId: Int) =>
    println(s"Then: Checking trade ID ${trade.trade_id} == $expectedId")
    assert(trade.trade_id == expectedId)
  }

  Then("symbol should be {string}") { (expectedSymbol: String) =>
    println(s"Then: Checking symbol ${trade.symbol} == $expectedSymbol")
    assert(trade.symbol == expectedSymbol)
  }

  Then("status should be {string}") { (expectedStatus: String) =>
    println(s"Then: Checking status ${trade.status} == $expectedStatus")
    assert(trade.status == expectedStatus)
  }
}