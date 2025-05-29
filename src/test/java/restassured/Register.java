package restassured;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Register{
    @Test
    public void register(){
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Create Register request
        String requestBody = "{\n" +
                "  \"email\": \"alifatesting1@mail.com\",\n" +
                "  \"full_name\": \"alifa\",\n" +
                "  \"password\": \"testing123!\",\n" +
                "  \"department\": \"Executive\",\n" +
                "  \"phone_number\": \"08123456789\"\n" +
            "}";



        // Send a POST request to the register endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("webhook/api/register");
        System.out.println("Response: " + response.asPrettyString());
    }
}