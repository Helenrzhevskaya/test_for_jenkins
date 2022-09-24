package homework4;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetPostDeleteTest extends AbstractTest {
    @Test
    void getRecipePositiveTest() {
        given().spec(getRequestSpecification())
                .when()
                .get(getBaseUrl() + "recipes/716429/information")
                .then()
                .spec(responseSpecification);
    }
    @Test
    void getIncludeNutrition() {
        given().spec(getRequestSpecification())
                .queryParam("includeNutrition", "false")
                .when()
                .get(getBaseUrl() + "recipes/716429/information")
                .then()
                .spec(responseSpecification);
    }


    @Test
    void postRecipesCuisine(){
        ResponseCuisine response = given().spec(requestSpecification)
                .when()
                .formParam("title","Burger")
                .post(getBaseUrl() + "recipes/cuisine").prettyPeek()
                .then()
                .extract()
                .response()
                .body()
                .as(ResponseCuisine.class);
        assertThat(response.getCuisine(),containsString("American"));

    }

    @Test
    void postAddMealPlanner(){
        MealPlanResponse response = given().spec(requestSpecification)
                .when()
                .queryParam("hash", getHash())
                .queryParam("apiKey", getApiKey())
                .post(getBaseUrl() + "mealplanner/ice/items").prettyPeek()
                .then()
                .extract()
                .response()
                .body()
                .as(MealPlanResponse.class);
        assertThat(response.status, containsString("success"));
    }
    @Test
    void test(){
        given().spec(requestSpecification)
                .when()
                .formParam("title","Burger")
                .formParam("language", "en")
                .post(getBaseUrl() + "recipes/cuisine").prettyPeek()
                .then()
                .statusCode(200);
    }
    @Test
    void ShoppingList() {
        String id = given().spec(requestSpecification)
                .queryParam("hash", getHash())
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + " \"aisles\": [\n"
                        + " {\n"
                        + " \"aisle\": \"Baking\",\n"
                        + " \"items\": [\n"
                        + " {\n"
                        + " \"id\": 115388,\n"
                        + " \"name\": \"baking powder\",\n"
                        + " \"measures\": {\n"
                        + " \"original\": {\n"
                        + " \"amount\": 1.0,\n"
                        + " \"unit\": \"package\"\n"
                        + " }\n"
                        + " },\n"
                        + "\"pantryItem\": false,\n"
                        + " \"aisle\": \"Baking\",\n"
                        + " \"cost\": 0.71,\n"
                        + " \"ingredientId\": 18369\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "],\n"
                        + " \"cost\":0.71,\n"
                        + " \"startDate\":1588291200,\n"
                        + " \"endDate\":1588896000\n"
                        + "}")
                .when()
                .post(getBaseUrl()+ "mealplanner/ice/shopping-list/items")
                .then()
                .spec(responseSpecification)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
                System.out.println("id: " + id);

                given()
                .queryParam("hash",getHash())
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getBaseUrl()+ "mealplanner/ice/shopping-list/")
                .then()
                .spec(responseSpecification);

        given()
                .queryParam("hash", getHash())
                .queryParam("apiKey", getApiKey())
                .delete("https://api.spoonacular.com/mealplanner/ice/shopping-list/items/" + id)
                .then()
                .spec(responseSpecification);


    }

}


