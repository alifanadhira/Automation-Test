Feature: Object API

    Background:
    Given The base URL is "https://whitesmokehouse.com/"

    Scenario: 
    When Login with method "POST" request to "webhook/api/login" with body:
    """
        {
        "email": "alifatesting1@mail.com",
        "password": "testing123!"
        }
    """
    Then The response status code should be 200
    And Save the token from the response body to local storage

    Scenario:
    Given Make sure token in local storage not empty
    When Add object with "POST" request to "webhook/api/objects" with body:
    """
        {
            "name": "Apple MacBook Pro 16 - Alifa Cucumber",
            "data": {
                "year": 2019,
                "price": 1849.99,
                "cpu_model": "Intel Core i9",
                "hard_disk_size": "1 TB",
                "capacity": "2 cpu",
                "screen_size": "14 Inch",
                "color": "red"
            }
        }
    """
    Then The response status code should be 200
    And Name in the response should be "Apple MacBook Pro 16 - Alifa Cucumber"
    And Year in the response should be "2019"
    And Color in the response should be "red"

    Scenario: Delete added object
    Given Make sure token in local storage not empty
    When Delete object with "DELETE" request to "webhook/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects"
    Then The response status code should be 200
