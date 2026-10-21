package com.learn.restassured;

import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Demonstrates request/response logging, filters, JSON schema validation,
 * multipart file upload, and relaxed HTTPS validation.
 */
public class LoggingFilterSchemaMultipartTests extends BaseTest {

    @Test
    public void logAllRequestAndResponseDetails() {
        given()
                .spec(requestSpec)
                .log().all()
        .when()
                .get("/posts/1")
        .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void logOnlyIfValidationFails() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts/1")
        .then()
                .log().ifValidationFails()
                .statusCode(200);
    }

    @Test
    public void explicitLoggingFiltersOnRequestSpecification() {
        given()
                .baseUri(JSON_PLACEHOLDER_BASE)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
        .when()
                .get("/posts/2")
        .then()
                .statusCode(200);
    }

    @Test
    public void validateResponseAgainstJsonSchema() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("post-schema.json"));
    }

    @Test
    public void multipartFileUpload() {
        File file = new File("src/test/resources/sample-upload.txt");

        given()
                .baseUri(HTTPBIN_BASE)
                .multiPart("file", file)
        .when()
                .post("/post")
        .then()
                .statusCode(200)
                .body("files.file", org.hamcrest.Matchers.containsString("sample file"));
    }

    @Test
    public void multipartWithControlNameAndMimeType() {
        File file = new File("src/test/resources/sample-upload.txt");

        given()
                .baseUri(HTTPBIN_BASE)
                .multiPart("document", file, "text/plain")
        .when()
                .post("/post")
        .then()
                .statusCode(200)
                .body("files.document", org.hamcrest.Matchers.notNullValue());
    }

    @Test
    public void relaxedHttpsValidationSkipsCertificateChecks() {
        // Useful for self-signed certs on internal/staging environments.
        given()
                .relaxedHTTPSValidation()
                .baseUri(JSON_PLACEHOLDER_BASE)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200);
    }

    @Test
    public void customTimeoutConfiguration() {
        RestAssuredConfig config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 5000)
                        .setParam("http.socket.timeout", 5000));

        given()
                .baseUri(JSON_PLACEHOLDER_BASE)
                .config(config)
        .when()
                .get("/posts/1")
        .then()
                .statusCode(200);
    }
}
