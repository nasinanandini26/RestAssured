package com.learn.restassured;

import com.learn.restassured.models.Post;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Demonstrates PUT (full update), PATCH (partial update), DELETE,
 * HEAD and OPTIONS.
 */
public class PutPatchDeleteTests extends BaseTest {

    @Test
    public void putFullyReplacesResource() {
        Post replacement = new Post(1, "fully updated title", "fully updated body");

        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(replacement)
        .when()
                .put("/posts/1")
        .then()
                .statusCode(200)
                .body("title", equalTo("fully updated title"))
                .body("body", equalTo("fully updated body"));
    }

    @Test
    public void patchPartiallyUpdatesResource() {
        Map<String, Object> partialUpdate = new HashMap<>();
        partialUpdate.put("title", "patched title only");

        given()
                .spec(requestSpec)
                .body(partialUpdate)
        .when()
                .patch("/posts/1")
        .then()
                .statusCode(200)
                .body("title", equalTo("patched title only"))
                .body("id", equalTo(1));
    }

    @Test
    public void deleteResource() {
        given()
                .spec(requestSpec)
        .when()
                .delete("/posts/1")
        .then()
                .statusCode(200);
    }

    @Test
    public void headRequestReturnsNoBodyButHasHeaders() {
        Response response = given()
                .spec(requestSpec)
        .when()
                .head("/posts/1");

        org.testng.Assert.assertEquals(response.statusCode(), 200);
        org.testng.Assert.assertTrue(response.getHeaders().hasHeaderWithName("Content-Type"));
    }

    @Test
    public void optionsRequestReturnsAllowedMethods() {
        Response response = given()
                .baseUri(HTTPBIN_BASE)
        .when()
                .options("/post");

        org.testng.Assert.assertEquals(response.statusCode(), 200);
        org.testng.Assert.assertTrue(response.getHeaders().hasHeaderWithName("Allow"));
    }
}
