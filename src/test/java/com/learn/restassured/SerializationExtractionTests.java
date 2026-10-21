package com.learn.restassured;

import com.learn.restassured.models.User;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Demonstrates POJO deserialization, JsonPath/XmlPath extraction utilities,
 * and a broad set of Hamcrest matchers used with REST Assured's body().
 */
public class SerializationExtractionTests extends BaseTest {

    @Test
    public void deserializeResponseDirectlyIntoPojo() {
        User user = given()
                .spec(requestSpec)
        .when()
                .get("/users/1")
        .then()
                .statusCode(200)
                .extract().as(User.class);

        assertEquals(user.getId(), Integer.valueOf(1));
        assertTrue(user.getEmail().contains("@"));
        assertTrue(user.getAddress() != null && user.getAddress().getCity() != null);
        assertTrue(user.getCompany() != null && user.getCompany().getName() != null);
    }

    @Test
    public void deserializeResponseIntoListOfPojos() {
        List<User> users = given()
                .spec(requestSpec)
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", User.class);

        assertEquals(users.size(), 10);
        assertEquals(users.get(0).getId(), Integer.valueOf(1));
    }

    @Test
    public void useJsonPathUtilityDirectlyOnResponseBody() {
        Response response = given()
                .spec(requestSpec)
        .when()
                .get("/users/1");

        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String city = jsonPath.getString("address.city");

        assertTrue(city != null && !city.isEmpty());
    }

    @Test
    public void useXmlPathUtilityOnXmlResponse() {
        // httpbin.org/xml returns a static XML "slideshow" document
        Response response = given()
                .baseUri(HTTPBIN_BASE)
        .when()
                .get("/xml");

        XmlPath xmlPath = new XmlPath(response.getBody().asString());
        String title = xmlPath.getString("slideshow.@title");

        assertTrue(title != null && !title.isEmpty());
    }

    @Test
    public void hamcrestMatchersOnJsonArray() {
        given()
                .spec(requestSpec)
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .body("id", hasItems(1, 2, 3))
                .body("username", hasSize(10))
                .body("email", everyItem(containsString("@")));
    }

    @Test
    public void hamcrestMatchersOnScalarFields() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200)
                .body("title", not(emptyOrNullString()))
                .body("userId", instanceOf(Integer.class))
                .body("id", is(1));
    }
}
