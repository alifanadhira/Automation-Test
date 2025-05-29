package com.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestUpdateObject {
    /*
     * {
            "name": "Apple MacBook Pro 16 - Alifa Final",
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
    */

    @JsonProperty("name")
    public String name;

    @JsonProperty("data")
    public Data data;

    public RequestUpdateObject(String name, Data data) {
        this.name = name;
        this.data = data;
    }

    public static class Data {

        @JsonProperty("year")
        public int year;

        @JsonProperty("price")
        public double price;

        @JsonProperty("cpu_model")
        public String cpu_model;

        @JsonProperty("hard_disk_size")
        public String hardDiskSize;

        @JsonProperty("capacity")
        public String capacity;

        @JsonProperty("screen_size")
        public String screenSize;

        @JsonProperty("color")
        public String color;

        public Data(int year, double price, String cpu_model, String hardDiskSize, String capacity, String screenSize, String color) {
            this.year = year;
            this.price = price;
            this.cpu_model = cpu_model;
            this.hardDiskSize = hardDiskSize;
            this.capacity = capacity;
            this.screenSize = screenSize;
            this.color = color;
        }
    }
}
