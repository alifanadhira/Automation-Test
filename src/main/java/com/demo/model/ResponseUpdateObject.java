package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseUpdateObject {

    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonIgnoreProperties(ignoreUnknown = true)

    @JsonProperty("data")
    public Data data;

        public static class Data {
        @JsonProperty("cpu_model")
        public String cpuModel;

        @JsonProperty("capacity")
        public String capacity;

        @JsonProperty("screen_size")
        public String screenSize;
    }

    public ResponseUpdateObject() {}

}
