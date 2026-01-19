Feature: Trade JSON Parsing

  Scenario: Parse valid trade JSON
    Given I have a valid trade JSON
    When I parse it to Trade object
    Then trade ID should be 12345
    And symbol should be "AAPL"
    And status should be "VALIDATED"