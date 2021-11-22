Feature: Rent Adding
  As a Developer
  I want to add Rent through API
  So that It can be available to applications

  Background:
    Given The Endpoint "http://localhost:8080/api/v1/rents" is available for rents

  @rent-adding
  Scenario: Add Rent
    When A Rent Request is sent with values "22/10/2021", "31/10/2021", 270, 3
    Then A Response with Status 200 is received for the rent
    And A Rent Resource with values "22/10/2021", "31/10/2021", 270, 3