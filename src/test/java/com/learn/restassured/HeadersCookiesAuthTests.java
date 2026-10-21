package com.learn.restassured;

import io.restassured.filter.cookie.CookieFilter;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Demonstrates custom headers, cookies, and HTTP authentication
 * (basic + preemptive basic) using httpbin.org as the echo server.
 */
public class HeadersCookiesAuthTests extends BaseTest {

    @Test
    public void sendSingleCustomHeader() {
        given()
                .baseUri(HTTPBIN_BASE)
                .header("X-Custom-Header", "rest-assured-learning")
        .when()
                .get("/headers")
        .then()
                .statusCode(200)
                .body("headers.X-Custom-Header", equalTo("rest-assured-learning"));
    }

    @Test
    public void sendMultipleHeadersAtOnce() {
        given()
                .baseUri(HTTPBIN_BASE)
                .headers("X-Env", "qa", "X-Requested-By", "learning-project")
        .when()
                .get("/headers")
        .then()
                .statusCode(200)
                .body("headers.X-Env", equalTo("qa"))
                .body("headers.X-Requested-By", equalTo("learning-project"));
    }

    @Test
    public void sendAndReceiveCookies() {
        given()
                .baseUri(HTTPBIN_BASE)
                .cookie("sessionId", "abc123")
        .when()
                .get("/cookies")
        .then()
                .statusCode(200)
                .body("cookies.sessionId", equalTo("abc123"));
    }

    @Test
    public void cookieFilterPersistsCookiesAcrossRequests() {
        CookieFilter cookieFilter = new CookieFilter();

        // /cookies/set responds with a redirect to /cookies; disable redirect-following
        // here so the CookieFilter can see the Set-Cookie header on this response directly.
        given()
                .baseUri(HTTPBIN_BASE)
                .filter(cookieFilter)
                .redirects().follow(false)
        .when()
                .get("/cookies/set?cookieName=cookieValue");

        // CookieFilter automatically resends it on the next request
        given()
                .baseUri(HTTPBIN_BASE)
                .filter(cookieFilter)
        .when()
                .get("/cookies")
        .then()
                .statusCode(200)
                .body("cookies.cookieName", equalTo("cookieValue"));
    }

    @Test
    public void basicAuthenticationSucceeds() {
        given()
                .baseUri(HTTPBIN_BASE)
                .auth().preemptive().basic("user", "passwd")
        .when()
                .get("/basic-auth/user/passwd")
        .then()
                .statusCode(200)
                .body("authenticated", equalTo(true))
                .body("user", equalTo("user"));
    }

    @Test
    public void bearerTokenAuthentication() {
        given()
                .baseUri(HTTPBIN_BASE)
                .auth().oauth2("dummy-bearer-token-123")
        .when()
                .get("/bearer")
        .then()
                .statusCode(200)
                .body("authenticated", equalTo(true))
                .body("token", equalTo("dummy-bearer-token-123"));
    }
}
