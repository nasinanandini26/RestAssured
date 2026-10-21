package com.learn.restassured;

import com.learn.restassured.models.Post;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Demonstrates the POST method using three body forms: raw JSON string,
 * a Map, and a POJO (object serialized automatically by REST Assured/Jackson).
 */
public class PostRequestTests extends BaseTest {

    @Test
    public void postWithRawJsonStringBody() {
        String body = "{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }";

        given()
                .spec(requestSpec)
                .body(body)
        .when()
                .post("/posts")
        .then()
                .statusCode(201)
                .body("title", equalTo("foo"))
                .body("userId", equalTo(1))
                .body("id", notNullValue());
    }

    @Test
    public void postWithMapBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("title", "learning rest assured");
        body.put("body", "map body example");
        body.put("userId", 2);

        given()
                .spec(requestSpec)
                .body(body)
        .when()
                .post("/posts")
        .then()
                .statusCode(201)
                .body("title", equalTo("learning rest assured"));
    }

    @Test
    public void postWithPojoBody() {
        Post newPost = new Post(3, "POJO title", "POJO body content");

        Post created = given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(newPost)
        .when()
                .post("/posts")
        .then()
                .statusCode(201)
                .extract().as(Post.class);

        org.testng.Assert.assertEquals(created.getTitle(), "POJO title");
        org.testng.Assert.assertEquals(created.getUserId(), Integer.valueOf(3));
    }

    @Test
    public void postWithFormParams() {
        // httpbin echoes posted form fields back under "form"
        given()
                .baseUri(HTTPBIN_BASE)
                .contentType(ContentType.URLENC)
                .formParam("username", "nandini")
                .formParam("role", "qa-engineer")
        .when()
                .post("/post")
        .then()
                .statusCode(200)
                .body("form.username", equalTo("nandini"))
                .body("form.role", equalTo("qa-engineer"));
    }
}
