@CharacterDetails
Feature: Seeing character details

  @CheckResult
  Scenario: Check bring a result with ID
    Given the user hit the url of get characters api endpoint
    When the user pass the id of a character 1
    Then the response bring a result

  @CheckName
  Scenario: Check name as expected
    Given the user hit the url of get characters api endpoint
    When the user pass the id of a character 1
    Then the response bring the details with the name "Rick Sanchez"

  @CheckAllDetails
  Scenario: Check all details
    Given the user hit the url of get characters api endpoint
    When the user pass the id of a character 1
    Then the response bring all the details