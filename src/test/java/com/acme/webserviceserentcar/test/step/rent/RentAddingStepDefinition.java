package com.acme.webserviceserentcar.test.step.rent;

import com.acme.webserviceserentcar.client.resource.CommentResource;
import com.acme.webserviceserentcar.rent.resource.CreateRentResource;
import com.acme.webserviceserentcar.rent.resource.RentResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.bs.A;
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

public class RentAddingStepDefinition {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomServerPort;

    private String endPointPath;
    private ResponseEntity<String> responseEntity;

    @Given("The Endpoint {string} is available for rents")
    public void theEndpointIsAvailableForRents(String endPointPath) {
        this.endPointPath = String.format("http://localhost:%d/api/v1/rents", randomServerPort);
    }

    @When("A Rent Request is sent with values {string}, {string}, {int}, {int}")
    public void aRentRequestIsSentWithValues(String startDate, String finishDate, int paymentAmount, int rate) {
        CreateRentResource resource = new CreateRentResource()
                .withStartDate(startDate)
                .withFinishDate(finishDate)
                .withAmount(paymentAmount)
                .withRate(rate);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateRentResource> request = new HttpEntity<>(resource, headers);
        responseEntity = testRestTemplate.postForEntity(endPointPath, request, String.class);
    }

    @Then("A Response with Status {int} is received for the rent")
    public void aResponseIsReceivedWithStatus(int expectedStatusCode) {
        int actualStatusCode = responseEntity.getStatusCodeValue();
        assertThat(expectedStatusCode).isEqualTo(actualStatusCode);
    }

    @And("A Rent Resource with values {string}, {string}, {int}, {int}")
    public void aRentResourceWithValues(String startDate, String finishDate, int paymentAmount, int rate) {
        RentResource expectedResource = new RentResource()
                .withStartDate(startDate)
                .withFinishDate(finishDate)
                .withAmount(paymentAmount)
                .withRate(rate);
        String value = responseEntity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        RentResource actualResource;
        try {
            actualResource = mapper.readValue(value, RentResource.class);
        } catch (JsonProcessingException | NullPointerException e) {
            actualResource = new RentResource();
        }
        expectedResource.setId(actualResource.getId());
        assertThat(expectedResource).usingRecursiveComparison()
                .isEqualTo(actualResource);
    }

}
