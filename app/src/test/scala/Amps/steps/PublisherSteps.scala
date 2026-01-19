package Amps.steps

import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.Assertions._

class PublisherSteps extends ScalaDsl with EN {

  private var tradeStatus: String = _
  private var isValid: Boolean = _
  private var newStatus: String = _

  Given("a trade with status {string}") { (status: String) =>
    tradeStatus = status
    println(s"Given: Trade status is '$tradeStatus'")
  }

  When("publisher checks if valid") { () =>
    // Simulate the logic from your FigurationPublisher.publishFiguratedTrade
    isValid = tradeStatus == "VALIDATED" || tradeStatus.contains("VALID")
    newStatus = if (isValid) "FIGURATED" else "FIGURATION_SKIPPED"

    println(s"When: Checking if valid - is valid? $isValid")
    println(s"When: New status will be '$newStatus'")
  }

  Then("it should be valid") { () =>
    println(s"Then: Checking if valid (expected true, got $isValid)")
    assert(isValid)
  }

  Then("it should be invalid") { () =>
    println(s"Then: Checking if invalid (expected false, got $isValid)")
    assert(!isValid)
  }

  Then("new status should be {string}") { (expectedStatus: String) =>
    println(s"Then: Checking new status '$newStatus' == '$expectedStatus'")
    assert(newStatus == expectedStatus)
  }
}