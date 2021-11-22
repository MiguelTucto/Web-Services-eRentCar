package com.acme.webserviceserentcar.test.step.car;

import com.acme.webserviceserentcar.car.resource.CarBrandResource;
import com.acme.webserviceserentcar.car.resource.CreateCarBrandResource;
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

public class CategoryCarAddingStepDefinition {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomServerPort;

    private String endPointPath;
    private ResponseEntity<String> responseEntity;

    @Given("The Endpoint {string} is available for category cars")
    public void theEndpointIsAvailableForCategoryCars(String endPointPath) {
        this.endPointPath = String.format("http://localhost:%d/api/v1/cateogoryCars", randomServerPort);
    }

    @When("A Category Car Request is sent with values {string}")
    public void aCategoryCarRequestIsSentWithValues(String name) {
        CreateCarBrandResource resource = new CreateCarBrandResource()
                .withName(name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateCarBrandResource> request = new HttpEntity<>(resource, headers);
        responseEntity = testRestTemplate.postForEntity(endPointPath, request, String.class);
    }

    @Then("A Response with Status {int} is received for the category car")
    public void aResponseWithStatusIsReceivedForTheCategoryCar(int expectedStatus) {
        int actualStatusCode = responseEntity.getStatusCodeValue();
        assertThat(expectedStatus).isEqualTo(actualStatusCode);
    }

    @And("A Category Car Resource with values {string}")
    public void aCategoryCarResourceWithValues(String name) {
        CarBrandResource expectedResource = new CarBrandResource()
                .withName(name);
        String value = responseEntity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        CarBrandResource actualResource;
        try {
            actualResource = mapper.readValue(value, CarBrandResource.class);
        } catch (JsonProcessingException | NullPointerException e) {
            actualResource = new CarBrandResource();
        }
        expectedResource.setId(actualResource.getId());
        assertThat(expectedResource).usingRecursiveComparison()
                .isEqualTo(actualResource);
    }
}
