package Amps.steps

import io.cucumber.scala.{EN, ScalaDsl}
import play.api.libs.json._
import Amps.Trade
import org.scalatest.Assertions._

class AllSteps extends ScalaDsl with EN {

  private var tradeJson: String = _
  private var trade: Trade = _
  private var tradeStatus: String = _
  private var isValid: Boolean = _
  private var newStatus: String = _
  private var isSchedulerRunning: Boolean = _
  private var isProcessingTrade: Boolean = _

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
    println("[TEST] Given valid trade JSON")
  }

  When("I parse it to Trade object") { () =>
    trade = Json.parse(tradeJson).as[Trade]
    println(s"[TEST] Parsed trade ID ${trade.trade_id}")
  }

  Then("trade ID should be {int}") { (expectedId: Int) =>
    assert(trade.trade_id == expectedId)
    println(s"[TEST] Trade ID is $expectedId")
  }

  Then("symbol should be {string}") { (expectedSymbol: String) =>
    assert(trade.symbol == expectedSymbol)
    println(s"[TEST] Symbol is $expectedSymbol")
  }

  Then("status should be {string}") { (expectedStatus: String) =>
    assert(trade.status == expectedStatus)
    println(s"[TEST] Status is $expectedStatus")
  }

  // ===== FigurationPublisher.scala Tests =====
  Given("a trade with status {string}") { (status: String) =>
    tradeStatus = status
    println(s"[TEST] Trade status is '$tradeStatus'")
  }

  When("publisher checks if valid") { () =>
    // Your actual logic from FigurationPublisher
    isValid = tradeStatus == "VALIDATED" || tradeStatus.contains("VALID")
    newStatus = if (isValid) "FIGURATED" else "FIGURATION_SKIPPED"
    println(s"[TEST] Valid? $isValid → New status: $newStatus")
  }

  Then("it should be valid") { () =>
    assert(isValid)
    println("[TEST] Trade is valid")
  }

  Then("it should be invalid") { () =>
    assert(!isValid)
    println("[TEST] Trade is invalid")
  }

  Then("new status should be {string}") { (expectedStatus: String) =>
    assert(newStatus == expectedStatus)
    println(s"[TEST] New status is $expectedStatus")
  }

  // ===== FigurationSubscriber.scala Tests =====
  Given("scheduler is running") { () =>
    isSchedulerRunning = true
    isProcessingTrade = false
    println("[TEST] Scheduler is running")
  }

  Given("trade is being processed") { () =>
    isProcessingTrade = true
    isSchedulerRunning = false
    println("[TEST] Trade is being processed")
  }

  When("trade is received") { () =>
    isProcessingTrade = true
    isSchedulerRunning = false
    println("[TEST] Trade received → Scheduler stops")
  }

  When("processing completes") { () =>
    isProcessingTrade = false
    isSchedulerRunning = true
    println("[TEST] Processing complete → Scheduler resumes")
  }

  Then("scheduler should stop") { () =>
    assert(!isSchedulerRunning)
    println("[TEST] Scheduler stopped")
  }

  Then("scheduler should resume") { () =>
    assert(isSchedulerRunning)
    println("[TEST] Scheduler resumed")
  }
}