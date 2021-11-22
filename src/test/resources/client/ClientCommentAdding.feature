Feature: Client Comment Adding
  As a Developer
  I want to add Client Comment through API
  So that It can be available to applications

  Background:
    Given The Endpoint "http://localhost:8080/api/v1/clientComments" is available for client comments

  @clientComment-adding
  Scenario: Add Client Comment
    When A Client Comment Request is sent with values "14/8/2021", 4, "I consider that she is a very responsible renter. In my attention she prepared all the papers and sanitary accessories for her."
    Then A Response with Status 200 is received for the clientComment
    And A Client Comment Resource with values "14/8/2021", 4, "I consider that she is a very responsible renter. In my attention she prepared all the papers and sanitary accessories for her."