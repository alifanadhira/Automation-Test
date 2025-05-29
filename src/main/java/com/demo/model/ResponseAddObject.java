package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseAddObject {
    /*
 * [
    {
        "id": 769,
        "name": "Apple MacBook Pro 16 - Alifa Final",
        "data": {
            "year": "2019",
            "price": 1849.99,
            "cpu_model": "Intel Core i9",
            "hard_disk_size": "1 TB",
            "color": "red",
            "capacity": "2 cpu",
            "screen_size": "14 Inch"
        }
    }
]
 */

    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("data")
    public Data data;

    public static class Data {
        @JsonProperty("year")
        public String year;

        @JsonProperty("color")
        public String color;
    }
    
}
