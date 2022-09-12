package lesson3;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetAndPostTest extends AbstractTest {

    @BeforeAll
    static void setUp(){

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }


    @Test
    void getIncludeNutrition(){
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
                .then().statusCode(200);
    }
    @Test
    void getSearchRecipesGeneral() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .body()
                .jsonPath();

        assertThat(response.get("offset"), CoreMatchers.is(0));
        assertThat(response.get("number"), CoreMatchers.is(10));
        assertThat(response.get("totalResults"), CoreMatchers.is(5224));
        assertThat(response.get("results[1].id"), CoreMatchers.is(715594));

        }


    @Test
    void getVerifyingResponseData() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .get(getBaseUrl() + "recipes/716426/information?" + getApiKey())
                .body()
                .jsonPath();
        assertThat(response.get("vegetarian"), is(true));
        assertThat(response.get("vegan"), is(true));
        assertThat(response.get("glutenFree"),  is(true));
        assertThat(response.get("pricePerServing"), equalTo(112.39F));
        assertThat(response.get("extendedIngredients[0].aisle"), equalTo("Produce"));
        assertThat(response.get("extendedIngredients[0].consistency"), equalTo("SOLID"));
    }
    @Test
    void getSearchRecipesCuisine() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .pathParam("cuisine", "African")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch?cuisine={cuisine}")
                .body()
                .jsonPath();

        assertThat(response.get("offset"), CoreMatchers.is(0));
        assertThat(response.get("number"), CoreMatchers.is(10));
        assertThat(response.get("totalResults"), CoreMatchers.is(4));
        assertThat(response.get("results[0].title"), CoreMatchers.is("African Chicken Peanut Stew"));

    }

    @Test
    void getSearchRecipesExcludeCuisine() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .pathParam("excludeCuisine", "Caribbean")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch?excludeCuisine={excludeCuisine}")
                .body()
                .jsonPath();

        assertThat(response.get("offset"), CoreMatchers.is(0));
        assertThat(response.get("number"), CoreMatchers.is(10));
        assertThat(response.get("totalResults"), CoreMatchers.is(5219));
        assertThat(response.get("results[0].title"), CoreMatchers.is("Cauliflower, Brown Rice, and Vegetable Fried Rice"));

    }
    @Test
    void getComplexSearchDiet() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .pathParam("diet", "Paleo")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch?diet={diet}")
                .body()
                .jsonPath();
        assertThat(response.get("offset"), CoreMatchers.is(0));
        assertThat(response.get("number"), CoreMatchers.is(10));
        assertThat(response.get("totalResults"), CoreMatchers.is(282));
        assertThat(response.get("results[1].title"), CoreMatchers.is("Crunchy Brussels Sprouts Side Dish"));


    }
    @Test
    void postRecipes() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Pork roast with green beans")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK");
    }
    @Test
    void postCheckMethodAndParams() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("ingredientList", "3 oz pork shoulder")
                .log().method()
                .log().params()
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK");
    }
    @Test
    void postCheckCuisineAndConfidence() {
        JsonPath response = given()
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .body()
                .jsonPath();
        assertThat(response.get("cuisine"), CoreMatchers.is("Mediterranean"));
        assertThat(response.get("confidence"), equalTo(0.0F));
    }

    @Test
    void postCheckVisualizeIngredients() {
         given()
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseUrl() + "recipes/visualizeIngredients")
                 .then()
                 .statusCode(200)
                 .statusLine("HTTP/1.1 200 OK");
    }

    @Test
    void MealPlan() {

        String id = given()
                .queryParam("hash", "e8809f83c48dc0146751f6051b0ff206821fe10f")
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + " \"date\": 1644881179,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/lena10/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();
        System.out.println("id: " + id);

        given()
                .queryParam("hash", "e8809f83c48dc0146751f6051b0ff206821fe10f")
                .queryParam("apiKey", getApiKey())
                .delete("https://api.spoonacular.com/mealplanner/lena10/items/" + id)
                .then()
                .statusCode(200);


    }
}



