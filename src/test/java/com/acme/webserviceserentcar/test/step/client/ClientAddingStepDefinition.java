package com.acme.webserviceserentcar.test.step.client;

import com.acme.webserviceserentcar.client.resource.ClientResource;
import com.acme.webserviceserentcar.client.resource.CreateClientResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientAddingStepDefinition {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomServerPort;

    private String endPointPath;
    private ResponseEntity<String> responseEntity;

    @Given("The Endpoint {string} is available for clients")
    public void theEndpointIsAvailableForClients(String endPointPath) {
        this.endPointPath = String.format("http://localhost:%d/api/v1/clients", randomServerPort);
    }

    @When("A Client Request is sent with values {string}, {string}, {string}, {int}, {int}, {int}, {int}")
    public void aClientRequestIsSentWithValues(String names, String lastNames, String address, int cellphoneNumber, int averageResponsibility, int responseTime, int rate) {
        CreateClientResource resource = new CreateClientResource()
                .withNames(names)
                .withLastNames(lastNames)
                .withAddress(address)
                .withCellphoneNumber((long)cellphoneNumber)
                .withAverageResponsibility(averageResponsibility)
                .withResponseTime(responseTime)
                .withRate(rate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateClientResource> request = new HttpEntity<>(resource, headers);
        responseEntity = testRestTemplate.postForEntity(endPointPath, request, String.class);
    }

    @Then("A Response with Status {int} is received for the client")
    public void aResponseIsReceivedWithStatus(int expectedStatusCode) {
        int actualStatusCode = responseEntity.getStatusCodeValue();
        assertThat(expectedStatusCode).isEqualTo(actualStatusCode);
    }

    @And("A Client Resource with values {string}, {string}, {string}, {int}, {int}, {int}, {int}")
    public void aClientResourceWithValues(String names, String lastNames, String address, int cellphoneNumber, int averageResponsibility, int responseTime, int rate) {
        ClientResource expectedResource = new ClientResource()
                .withNames(names)
                .withLastNames(lastNames)
                .withAddress(address)
                .withCellphoneNumber((long)cellphoneNumber)
                .withAverageResponsibility(averageResponsibility)
                .withResponseTime(responseTime)
                .withRate(rate);
        String value = responseEntity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        ClientResource actualResource;
        try {
            actualResource = mapper.readValue(value, ClientResource.class);
        } catch (JsonProcessingException | NullPointerException e) {
            actualResource = new ClientResource();
        }
        expectedResource.setId(actualResource.getId());
        assertThat(expectedResource).usingRecursiveComparison()
                .isEqualTo(actualResource);
    }
}
