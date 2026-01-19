Feature: Figuration Publisher Logic

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