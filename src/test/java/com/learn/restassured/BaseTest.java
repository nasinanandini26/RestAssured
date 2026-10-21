package com.learn.restassured;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

/**
 * Shared setup used by every test class: base URI + reusable request/response
 * specifications (the standard REST Assured pattern for avoiding repetition).
 */
public class BaseTest {

    protected static final String JSON_PLACEHOLDER_BASE = "https://jsonplaceholder.typicode.com";
    protected static final String HTTPBIN_BASE = "https://httpbin.org";
    protected static final String REQRES_BASE = "https://reqres.in/api";

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    @BeforeClass
    public void setupSpecs() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(JSON_PLACEHOLDER_BASE)
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }
}
