package restassured;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RestAssuredTask {
    String token;

    @BeforeSuite
    public void login(){
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        //Create Login request
        String requestBody = "{\n" +
                "  \"email\": \"alifatesting1@mail.com\",\n" +
                "  \"password\": \"testing123!\"\n" +
                "}";

        // Send a POST request to the login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post("webhook/api/login");
        System.out.println("Response: " + response.asPrettyString());
        token = response.jsonPath().getString("token");
        System.out.println("Token: " + token);
    }

    @Test(priority = 2) // This test depends on the successful execution of the login method
    public void getListAllObjects(){
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Create Get list all objects request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("webhook/api/objects");
        System.out.println("Response: " + response.asPrettyString());   
        assert response.getStatusCode() == 200 : "Expected status code 200, but got " + response.getStatusCode();
    }

    @Test(priority = 3)
    public void getListOfObjectsById(){
        int id = 9;
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Create Get list of objects by ID request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .queryParam("id", id)
                .log().all()
                .when()
                .get("webhook/api/objects");

        System.out.println("Response: " + response.asPrettyString());
        assert response.getStatusCode() == 200 : "Expected status code 200, but got " + response.getStatusCode();
        assert response.jsonPath().getString("[0].id").equals(String.valueOf(id)) : "Expected ID " + id + ", but got " + response.jsonPath().getString("[0].id");

    }

    @Test(priority = 4)
    public void getSingleObject(){
        int id = 9;
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Create Get single object request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("webhook/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/" + id);

        System.out.println("Response: " + response.asPrettyString());
        assert response.getStatusCode() == 200 : "Expected status code 200, but got " + response.getStatusCode();
        assert response.jsonPath().getString("id").equals(String.valueOf(id)) : 
        "Expected ID " + id + ", but got " + response.jsonPath().getString("id");
    }

    @Test(priority = 1)
    public void addObject(){
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Create Add object request
        String requestBody = "{\n" +
                "  \"name\": \"Apple MacBook Pro 16 - Alifa Final\",\n" +
                "  \"data\": {\n" +
                "    \"year\": 2019,\n" +
                "    \"price\": 1849.99,\n" +
                "    \"cpu_model\": \"Intel Core i9\",\n" +
                "    \"hard_disk_size\": \"1 TB\",\n" +
                "    \"capacity\": \"2 cpu\",\n" +
                "    \"screen_size\": \"14 Inch\",\n" +
                "    \"color\": \"red\"\n" +
                "  }\n" +
            "}";

        // Send a POST request to the add object endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .log().all()
                .when()
                .post("webhook/api/objects");
        System.out.println("Response: " + response.asPrettyString());
        
        String name = response.jsonPath().getString("[0].name").trim();
        assert name.equals("Apple MacBook Pro 16 - Alifa Final") :
        "Expected name 'Apple MacBook Pro 16 - Alifa Final', but got '" + name + "'";

        String year = response.jsonPath().getString("[0].data.year").trim();
        assert year.equals("2019") :
        "Expected year '2019', but got '" + year + "'";
    }

    @Test(priority = 5)
    public void updateObject(){
        int id = 9;
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Create Update object request
        String requestBody = "{\n" +
                "  \"name\": \"Apple MacBook Pro 16 - Alifa Updated!\",\n" +
                "  \"data\": {\n" +
                "    \"year\": 2020,\n" +
                "    \"price\": 1999.99,\n" +
                "    \"cpu_model\": \"M3\",\n" +
                "    \"hard_disk_size\": \"2 TB\",\n" +
                "    \"capacity\": \"1 cpu\",\n" +
                "    \"screen_size\": \"13 Inch\",\n" +
                "    \"color\": \"grey\"\n" +
                "  }\n" +
            "}";

        // Send a PUT request to the update object endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .log().all()
                .when()
                .put("webhook/37777abe-a5ef-4570-a383-c99b5f5f7906/api/objects/" + id);
        System.out.println("Response: " + response.asPrettyString());
        
        String name = response.jsonPath().getString("[0].name").trim();
        assert name.equals("Apple MacBook Pro 16 - Alifa Updated!") :
        "Expected name 'Apple MacBook Pro 16 - Alifa Updated!', but got '" + name + "'";

        String year = response.jsonPath().getString("[0].data.year").trim();
        assert year.equals("2020") :
        "Expected year '2020', but got '" + year + "'";
    }

    @Test(priority = 8)
    public void deleteObject(){
        int id =15;
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Send a DELETE request to the delete object endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .delete("webhook/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects/" + id);
        System.out.println("Response: " + response.asPrettyString());
        assert response.getStatusCode() == 200 : "Expected status code 200, but got " + response.getStatusCode();
    }

    @Test(priority = 7)
    public void getAllDepartments(){
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Create Get all departments request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("webhook/api/department");
        System.out.println("Response: " + response.asPrettyString());
        assert response.getStatusCode() == 200 : "Expected status code 200, but got " + response.getStatusCode();
    }

    @Test(priority = 6)
    public void partiallyUpdatedObject(){
        int id = 9;
        // Set the base URI for the REST API
        RestAssured.baseURI = "https://whitesmokehouse.com/";

        // Create Partially update object request
        String requestBody = "{\n" +
                "  \"name\": \"Apple MacBook Pro 25 - Alifa Partially Updated\",\n" +
                "  \"year\": \"2024\"\n" +
                "}";


        // Send a PATCH request to the partially update object endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .log().all()
                .when()
                .patch("webhook/39a0f904-b0f2-4428-80a3-391cea5d7d04/api/object/" + id);
        System.out.println("Response: " + response.asPrettyString());
        
        assert response.jsonPath().getString("name").trim().equals("Apple MacBook Pro 25 - Alifa Partially Updated") :
        "Expected name 'Apple MacBook Pro 25 - Alifa Partially Updated', but got '" 
        + response.jsonPath().getString("name").trim() + "'";

        assert response.jsonPath().getString("data.year").trim().equals("2024") :
        "Expected year to be '2024', but got '" + response.jsonPath().getString("data.year").trim() + "'";
    }
}
