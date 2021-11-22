Feature: Client Adding
  As a Developer
  I want to add Client through API
  So that It can be available to applications

  Background:
    Given The Endpoint "http://localhost:8080/api/v1/clients" is available for clients

  @client-adding
  Scenario: Add Client
    When A Client Request is sent with values "Juan", "Perez", "San isidro", 987654321, 4, 4, 4
    Then A Response with Status 200 is received for the client
    And A Client Resource with values "Juan", "Perez", "San isidro", 987654321, 4, 4, 4