Feature: Category Car Adding
  As a Developer
  I want to add Category Car through API
  So that It can be available to applications

  Background:
    Given The Endpoint "http://localhost:8080/api/v1/categoryCars" is available for category cars

  @categoryCar-adding
  Scenario: Add Category Car
    When A Category Car Request is sent with values "Little"
    Then A Response with Status 200 is received for the category car
    And A Category Car Resource with values "Little"