package scenario;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.demo.model.RequestAddObject;
import com.demo.model.RequestUpdateObject;
import com.demo.model.ResponseAddObject;
import com.demo.model.ResponseUpdateObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class RestAssuredE2ETest {
    /*
     * Scenario: Rest Assured E2E Test
     * Test Case 001: Add a new object
     * 1. Hit the endpoint Login to get the token
     * 2. Hit the endpoint AddObject with valid data
     * 3. Hit the endpoint ListOfObjectByIds with the ID from the response of AddObject
     *
     * Test Case 002: Update an object from Test Case 001
     * 1. Hit the endpoint Login to get the token
     * 2. Hit the endpoint UpdateObject with valid data
     * 3. Hit the endpoint ListOfObjectByIds with valid data to verify the object is updated with the same ID
     */
    String token;
    int id;
        
        //=====================Setup Method: Login to get the token=========================
    @BeforeClass()
    public void setup() {
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        String requestBodyLogin = "{\n" +
                "  \"email\": \"alifatesting1@mail.com\",\n" +
                "  \"password\": \"testing123!\"\n" +
                "}";

        Response responseLogin = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBodyLogin)
                .log().all()
                .when()
                .post("webhook/api/login");
        System.out.println("Response: " + responseLogin.asPrettyString());
        token = responseLogin.jsonPath().getString("token");
        System.out.println("Token: " + token);
    }

         //=====================Test Case 001: Add a new object=========================

    @Test()
    public void addNewObject() throws JsonProcessingException{
        RequestAddObject.Data data = new RequestAddObject.Data(
                2019, 
                1849.99, 
                "Intel Core i9", 
                "1 TB", 
                "2 cpu", 
                "14 Inch", 
                "red");

        RequestAddObject request = new RequestAddObject(
                "Apple MacBook Pro 16 - Alifa", data);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonAddObject = objectMapper.writeValueAsString(request);

        Response responseAddObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(jsonAddObject) // Convert to JSON string
                .log().all()
                .when()
                .post("webhook/api/objects");
        System.out.println("Response: " + responseAddObject.asPrettyString());
        id = responseAddObject.jsonPath().getInt("[0].id");
        System.out.println("ID: " + id);
        
        ResponseAddObject[] resAdd = responseAddObject.as(ResponseAddObject[].class);

        Assert.assertEquals(responseAddObject.getStatusCode(), 200, responseAddObject.getStatusCode());
        Assert.assertNotNull(resAdd[0].id, "Expected ID to be present, but got null");
        Assert.assertEquals(resAdd[0].name,"Apple MacBook Pro 16 - Alifa");
        Assert.assertEquals(resAdd[0].data.year, "2019");
        Assert.assertEquals(resAdd[0].data.color, "red");

          //===Hit the endpoint ListOfObjectByIds with the ID from the response of AddObject===
        Response responseListObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/" + id);
        System.out.println("Response: " + responseListObject.asPrettyString());
        
        Assert.assertEquals(responseListObject.getStatusCode(), 200,
        "Expected status code 200, but got " + responseListObject.getStatusCode());
        Assert.assertEquals(resAdd[0].id, id);
        Assert.assertEquals(resAdd[0].name,"Apple MacBook Pro 16 - Alifa");
        Assert.assertEquals(resAdd[0].data.year, "2019");
        Assert.assertEquals(resAdd[0].data.color, "red");

    }

        //=====================Test Case 002: Update the new object=========================

    @Test(dependsOnMethods = "addNewObject")
    public void UpdateObject() throws JsonProcessingException {
        RequestUpdateObject.Data data = new RequestUpdateObject.Data(
                2020,
                1999.99,
                "M3",
                "2 TB",
                "1 cpu",
                "13 Inch",
                "grey");

        RequestUpdateObject request = new RequestUpdateObject(
                "Apple MacBook Pro 16 - Alifa Updated!", data);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUpdateObject = objectMapper.writeValueAsString(request);

        Response responseUpdateObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(jsonUpdateObject)
                .log().all()
                .when()
                .put("webhook/37777abe-a5ef-4570-a383-c99b5f5f7906/api/objects/" + id);
        System.out.println("Response: " + responseUpdateObject.asPrettyString());

        ResponseUpdateObject[] resAdd = responseUpdateObject.as(ResponseUpdateObject[].class);

        Assert.assertEquals(responseUpdateObject.getStatusCode(), 200,
        "Expected status code 200, but got " + responseUpdateObject.getStatusCode());
        Assert.assertEquals(resAdd[0].id, id);
        // Assert.assertEquals(resAdd[0].data.cpuModel, "M3");
        Assert.assertEquals(resAdd[0].data.capacity, "1 cpu");
        Assert.assertEquals(resAdd[0].data.screenSize, "13 Inch");

        //3. Hit the endpoint ListOfObjectByIds with valid data to verify the object is updated with the same ID
        Response responseListObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/" + id);
        System.out.println("Response: " + responseListObject.asPrettyString());
       
        Assert.assertEquals(responseListObject.getStatusCode(), 200,
        "Expected status code 200, but got " + responseListObject.getStatusCode());
        Assert.assertEquals(resAdd[0].id, id);
        // Assert.assertEquals(resAdd[0].data.cpuModel, "M3");
        Assert.assertEquals(resAdd[0].data.capacity, "1 cpu");
        Assert.assertEquals(resAdd[0].data.screenSize, "13 Inch");
    }

    @AfterClass
    public void tearDown() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .delete("webhook/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects/" + id);
        System.out.println("Response: " + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), 200,
        "Expected status code 200, but got " + response.getStatusCode());
    }

}
