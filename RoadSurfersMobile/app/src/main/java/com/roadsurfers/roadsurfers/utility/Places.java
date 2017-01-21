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
@JsonPropertyOrder({
        "html_attributions",
        "results"
})
public class Places {

    @JsonProperty("html_attributions")
    private List<Object> htmlAttributions = null;
    @JsonProperty("results")
    private List<Place> places = null;



    @JsonProperty("results")
    public List<Place> getPlaces() {
        return places;
    }

    @JsonProperty("results")
    public void setPlaces(List<Place> places) {
        this.places = places;
    }


}
