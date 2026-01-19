Feature: Subscriber Scheduler Logic

  Scenario: Scheduler stops when trade arrives
    Given scheduler is running
    When trade is received
    Then scheduler should stop

  Scenario: Scheduler resumes after processing
    Given trade is being processed
    When processing completes
    Then scheduler should resume