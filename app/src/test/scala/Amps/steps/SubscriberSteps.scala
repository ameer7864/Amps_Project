package Amps.steps

import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.Assertions._

class SubscriberSteps extends ScalaDsl with EN {

  private var isSchedulerRunning: Boolean = true
  private var isProcessingTrade: Boolean = false

  Given("scheduler is running") { () =>
    isSchedulerRunning = true
    isProcessingTrade = false
    println("Given: Scheduler is running")
  }

  Given("trade is being processed") { () =>
    isProcessingTrade = true
    isSchedulerRunning = false
    println("Given: Trade is being processed, scheduler stopped")
  }

  When("trade is received") { () =>
    isProcessingTrade = true
    isSchedulerRunning = false
    println("When: Trade received -> Scheduler stops")
  }

  When("processing completes") { () =>
    isProcessingTrade = false
    isSchedulerRunning = true
    println("When: Processing complete -> Scheduler resumes")
  }

  Then("scheduler should stop") { () =>
    println(s"Then: Scheduler running? $isSchedulerRunning (expected false)")
    assert(!isSchedulerRunning)
  }

  Then("scheduler should resume") { () =>
    println(s"Then: Scheduler running? $isSchedulerRunning (expected true)")
    assert(isSchedulerRunning)
  }
}