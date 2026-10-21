package com.learn.restassured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Demonstrates the GET method: plain get(), path params, query params,
 * status/header assertions, and response extraction.
 */
public class GetRequestTests extends BaseTest {

    @Test
    public void simpleGet() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", notNullValue());
    }

    @Test
    public void getWithPathParam() {
        given()
                .spec(requestSpec)
                .pathParam("postId", 5)
        .when()
                .get("/posts/{postId}")
        .then()
                .statusCode(200)
                .body("id", equalTo(5));
    }

    @Test
    public void getWithMultiplePathParams() {
        given()
                .spec(requestSpec)
                .pathParams("postId", 1, "commentField", "comments")
        .when()
                .get("/posts/{postId}/{commentField}")
        .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void getWithSingleQueryParam() {
        given()
                .spec(requestSpec)
                .queryParam("userId", 1)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("userId", everyItem(equalTo(1)));
    }

    @Test
    public void getWithMultipleQueryParams() {
        given()
                .spec(requestSpec)
                .queryParams("userId", 1, "id", 1)
        .when()
                .get("/comments")
        .then()
                .statusCode(200);
    }

    @Test
    public void getAllPostsAndVerifyListSize() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("size()", equalTo(100));
    }

    @Test
    public void getAndValidateResponseHeadersAndStatusLine() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200)
                .statusLine(containsString("200"))
                .header("Content-Type", containsString("application/json"));
    }

    @Test
    public void getAndExtractResponseAsObject() {
        Response response = given()
                .spec(requestSpec)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200)
                .extract().response();

        int id = response.path("id");
        assertEquals(id, 1);
        assertTrue(response.getTime() >= 0);
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void getAndExtractSinglePathValue() {
        String title = given()
                .spec(requestSpec)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200)
                .extract().path("title");

        assertTrue(title != null && !title.isEmpty());
    }

    @Test
    public void getResponseTimeIsWithinSla() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts/1")
        .then()
                .time(lessThan(5000L));
    }
    @Test public void getResponse(){
        Response response =  given()
                .baseUri("https://automationexercise.com/api/productsList")
        .when()
             .get()

        .then()
            .statusCode(200)
            .body("products", notNullValue())
            .body("products.brand", notNullValue())
            .extract().response();

        JsonPath jsonPath = response.jsonPath();

        List<Map<String, Object>> filteredProducts = jsonPath.getList(
                "products.findAll { it.brand == 'H&M' && it.price.replaceAll('[^0-9.]', '').toDouble() < 3000 }");

        assertTrue(!filteredProducts.isEmpty(), "No H&M products found under 3000");
        filteredProducts.forEach(product -> assertEquals(product.get("brand"), "H&M"));

        filteredProducts.forEach(System.out::println);
    }

}
