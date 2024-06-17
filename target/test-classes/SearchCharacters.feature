@CharacterSearch
Feature: Search characters from the api

    @ByName
    Scenario: Get characters by name
        Given the user hit the url of get characters api endpoint
        When the user pass the name of a character "Ri"
        Then the user receive the characters list
        And the user receive the response code as 200

    @ByPartialName
    Scenario: Get characters by partial-name
        Given the user hit the url of get characters api endpoint
        When the user pass the partial-name of a character "BL"
        Then the user receive the character suggestions



