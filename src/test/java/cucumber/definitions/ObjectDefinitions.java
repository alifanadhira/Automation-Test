package cucumber.definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.testng.Assert.*;


public class ObjectDefinitions {
    private static String baseUrl;
    private static Response response;
    private static String token;
    private static int id;

    @Given("The base URL is {string}")
    public void set_base_url(String baseUrl) {
        ObjectDefinitions.baseUrl = baseUrl;
    }

    @When("Login with method {string} request to {string} with body:")
    public void login(String method, String url, String body) {

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .request(method, baseUrl + url);

        token = response.jsonPath().getString("token");
    }

    @Then ("The response status code should be {int}")
    public void assert_response(int statusCode) {
        assert response.getStatusCode() == statusCode : "Error and the status code is " + response.getStatusCode();
    }

    @And ("Save the token from the response body to local storage")
    public void save_token() {
        assertNotNull(token, "Token was not found in the response");
        System.out.println("Saved token: " + token);
    }

    /*============================ Scenario 2 ============================ */
    @Given ("Make sure token in local storage not empty")
    public void assert_token_in_variable() {
        assert ObjectDefinitions.token != null : "Token null";
    }


    @When ("Add object with {string} request to {string} with body:")
    public void add_object(String method, String url, String body) {
        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .request(method, baseUrl + url);
        id = response.jsonPath().getInt("[0].id");
        System.out.println("ID: " + id);
    }

    @And("Name in the response should be {string}")
    public void assert_name(String expectedName) {
        String actualName = response.jsonPath().getString("[0].name");
        assertEquals(actualName, expectedName, "Name is not as expected");
    }

    @And("Year in the response should be {string}")
    public void assert_year(String expectedYear) {
        String actualYear = response.jsonPath().getString("[0].data.year");
        assertEquals(actualYear, expectedYear, "Year is not as expected");
    }

    @And("Color in the response should be {string}")
    public void assert_color(String expectedColor) {
        String actualColor = response.jsonPath().getString("[0].data.color");
        assertEquals(actualColor, expectedColor, "Color is not as expected");
    }

    /*============================ Delete Object ============================ */
    @When("Delete object with {string} request to {string}")
    public void delete_object(String method, String url) {
        String fullUrl = baseUrl + url + "/" + id;

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .request(method, fullUrl);
        System.out.println("Delete response status: " + response.getStatusCode());
    }
}
