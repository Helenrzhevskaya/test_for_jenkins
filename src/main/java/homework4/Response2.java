package homework4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "results",
        "offset",
        "number",
        "totalResults"

})
@Data
public class Response2 {

    @JsonProperty("results")
    private List<Result> results = new ArrayList<Result>();
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("totalResults")
    private Integer totalResults;

    public Response2(Integer offset) {
        this.offset = offset;
    }

    @Data
    public class Result {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("image")
        private String image;
        @JsonProperty("imageType")
        private String imageType;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }



}

