Feature: All AMPS Trading Tests
  Scenario: Parse trade JSON
    Given I have a valid trade JSON
    When I parse it to Trade object
    Then trade ID should be 12345
    And symbol should be "AAPL"
    And status should be "VALIDATED"

  Scenario: Valid trade becomes FIGURATED
    Given a trade with status "VALIDATED"
    When publisher checks if valid
    Then it should be valid
    And new status should be "FIGURATED"

  Scenario: Invalid trade gets skipped
    Given a trade with status "REJECTED"
    When publisher checks if valid
    Then it should be invalid
    And new status should be "FIGURATION_SKIPPED"

  Scenario: Scheduler stops on trade arrival
    Given scheduler is running
    When trade is received
    Then scheduler should stop

  Scenario: Scheduler resumes after processing
    Given trade is being processed
    When processing completes
    Then scheduler should resume