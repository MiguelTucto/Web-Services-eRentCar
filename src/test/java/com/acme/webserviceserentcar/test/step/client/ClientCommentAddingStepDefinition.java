package com.acme.webserviceserentcar.test.step.client;

import com.acme.webserviceserentcar.client.resource.CommentResource;
import com.acme.webserviceserentcar.client.resource.CreateCommentResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en_scouse.An;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientCommentAddingStepDefinition {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomServerPort;

    private String endPointPath;
    private ResponseEntity<String> responseEntity;

    @Given("The Endpoint {string} is available for client comments")
    public void theEndpointIsAvailableForClientComments(String endPointPath) {
        this.endPointPath = String.format("http://localhost:%d/api/v1/clientComments", randomServerPort);
    }

    @When("A Client Comment Request is sent with values {string}, {int}, {string}")
    public void aClientCommentRequestIsSentWithValues(String date, int starts, String comment) {
        CreateCommentResource resource = new CreateCommentResource()
                .withDate(date)
                .withStars(starts)
                .withComment(comment);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateCommentResource> request = new HttpEntity<>(resource, headers);
        responseEntity = testRestTemplate.postForEntity(endPointPath, request, String.class);
    }

    @Then("A Response with Status {int} is received for the clientComment")
    public void aResponseWithStatusIsReceivedForTheClientComment(int expectedStatusCode) {
        int actualStatusCode = responseEntity.getStatusCodeValue();
        assertThat(expectedStatusCode).isEqualTo(actualStatusCode);
    }

    @And("A Client Comment Resource with values {string}, {int}, {string}")
    public void aClientCommentResourceWithValues(String date, int starts, String comment) {
        CommentResource expectedResource = new CommentResource()
                .withDate(date)
                .withStars(starts)
                .withComment(comment);
        String value = responseEntity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        CommentResource actualResource;
        try{
            actualResource = mapper.readValue(value, CommentResource.class);
        } catch (JsonProcessingException | NullPointerException e) {
            actualResource = new CommentResource();
        }
        expectedResource.setId(actualResource.getId());
        assertThat(expectedResource).usingRecursiveComparison()
                .isEqualTo(actualResource);
    }
}
