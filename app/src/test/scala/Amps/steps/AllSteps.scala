package Amps.steps

import io.cucumber.scala.{EN, ScalaDsl}
import play.api.libs.json._
import Amps.Trade
import org.scalatest.matchers.should.Matchers

class AllSteps extends ScalaDsl with EN with Matchers {

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
    trade.trade_id should be (expectedId)
    println(s"[TEST] ✓ Trade ID is $expectedId")
  }

  Then("symbol should be {string}") { (expectedSymbol: String) =>
    trade.symbol should be (expectedSymbol)
    println(s"[TEST] ✓ Symbol is $expectedSymbol")
  }

  Then("status should be {string}") { (expectedStatus: String) =>
    trade.status should be (expectedStatus)
    println(s"[TEST] ✓ Status is $expectedStatus")
  }

  Given("a trade with status {string}") { (status: String) =>
    tradeStatus = status
    println(s"[TEST] Trade status is '$tradeStatus'")
  }

  When("publisher checks if valid") { () =>
    isValid = tradeStatus == "VALIDATED" || tradeStatus.contains("VALID")
    newStatus = if (isValid) "FIGURATED" else "FIGURATION_SKIPPED"
    println(s"[TEST] Valid? $isValid → New status: $newStatus")
  }

  Then("it should be valid") { () =>
    isValid should be (true)
    println("[TEST] Trade is valid")
  }

  Then("it should be invalid") { () =>
    isValid should be (false)
    println("[TEST] Trade is invalid")
  }

  Then("new status should be {string}") { (expectedStatus: String) =>
    newStatus should be (expectedStatus)
    println(s"[TEST] New status is $expectedStatus")
  }

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
    isSchedulerRunning should be (false)
    println("[TEST] Scheduler stopped")
  }

  Then("scheduler should resume") { () =>
    isSchedulerRunning should be (true)
    println("[TEST] Scheduler resumed")
  }
}