# REST Assured Learning Project

A Maven project for learning [REST Assured](https://rest-assured.io/) by example.
All tests hit free public sandbox APIs — no local server or API keys needed:

- `https://jsonplaceholder.typicode.com` — fake REST API (posts/users)
- `https://httpbin.org` — request echo/inspection service

## Project structure

```
src/main/java/com/learn/restassured/models/   POJOs used for (de)serialization: Post, User, Address, Company
src/test/java/com/learn/restassured/
  BaseTest.java                        Shared RequestSpecification/ResponseSpecification
  GetRequestTests.java                 GET, path params, query params, extraction, status/header/time assertions
  PostRequestTests.java                POST with raw JSON, Map, POJO, and form params
  PutPatchDeleteTests.java             PUT, PATCH, DELETE, HEAD, OPTIONS
  HeadersCookiesAuthTests.java         Custom headers, cookies, CookieFilter, basic auth, bearer token auth
  SerializationExtractionTests.java    POJO (de)serialization, JsonPath, XmlPath, Hamcrest matchers
  LoggingFilterSchemaMultipartTests.java  Logging, filters, JSON schema validation, multipart upload, relaxed HTTPS, timeouts
src/test/resources/
  post-schema.json                     JSON schema used by the schema-validation test
  sample-upload.txt                    File used by the multipart upload test
testng.xml                             TestNG suite wiring all test classes together
```

## Requirements

- Java 17+
- Maven 3.6+
- Internet access (tests call live public APIs)

## Running

```bash
# download dependencies and compile
mvn clean compile

# run the full test suite (also downloads test-scope deps)
mvn clean test

# run a single class
mvn test -Dtest=GetRequestTests
```

Test reports are written to `target/surefire-reports/`.

## What's covered

- HTTP methods: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `HEAD`, `OPTIONS`
- Path params (`pathParam`/`pathParams`) and query params (`queryParam`/`queryParams`)
- Request bodies: raw JSON string, `Map`, and POJO (auto-serialized via Jackson)
- Response extraction: `.extract().as(Class)`, `.extract().path(...)`, `JsonPath`, `XmlPath`
- Headers and cookies (`header`, `headers`, `cookie`, `CookieFilter` for session persistence)
- Authentication: preemptive basic auth, bearer/OAuth2 token
- Assertions: Hamcrest matchers (`equalTo`, `hasItems`, `everyItem`, `containsString`, etc.), status code/line, response time
- Reusable `RequestSpecification`/`ResponseSpecification` via `RequestSpecBuilder`/`ResponseSpecBuilder`
- Logging: `log().all()`, `log().ifValidationFails()`, `RequestLoggingFilter`/`ResponseLoggingFilter`
- JSON Schema validation (`matchesJsonSchemaInClasspath`)
- Multipart file upload (`multiPart`)
- `relaxedHTTPSValidation()` and custom HTTP client timeout config
