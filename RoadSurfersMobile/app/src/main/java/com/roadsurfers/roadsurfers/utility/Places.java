package com.roadsurfers.roadsurfers.utility;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by raula on 1/21/2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Places {

    @JsonProperty("html_attributions")
    private List<Object> htmlAttributions = null;
    @JsonProperty("results")
    private List<Place> places = null;
    @JsonProperty("error_message")
     private Object error_message;
    @JsonProperty("status")
    private Object status;
    @JsonProperty("next_page_token")
    private  Object next_page_token;






    @JsonProperty("results")
    public List<Place> getPlaces() {
        return places;
    }

    @JsonProperty("results")
    public void setPlaces(List<Place> places) {
        this.places = places;
    }


    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getError_message() {
        return error_message;
    }

    public void setError_message(Object error_message) {
        this.error_message = error_message;
    }
}
