package com.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class KullaniciResourceTest {

    private String create(String isim, String email) {
        return given().contentType(ContentType.JSON)
                .body("{\"isim\":\"" + isim + "\",\"email\":\"" + email + "\"}")
                .when().post("/kullanici")
                .then().statusCode(201)
                .header("Location", containsString("/kullanici/"))
                .body("id", notNullValue())
                .extract().header("Location");
    }

    @Test
    void crudLifecycle() {
        String location = create("Ayşe", "ayse@example.com");

        given().when().get(location).then().statusCode(200).body("isim", is("Ayşe"));
        given().when().get("/kullanici").then().statusCode(200).body("email", hasItem("ayse@example.com"));

        given().contentType(ContentType.JSON).body("{\"isim\":\"Ayşe K.\",\"email\":\"ayse.k@example.com\"}")
                .when().put(location)
                .then().statusCode(200).body("isim", is("Ayşe K."));

        given().when().delete(location).then().statusCode(204);
        given().when().get(location).then().statusCode(404);
        given().when().delete(location).then().statusCode(404);
    }

    @Test
    void clientCannotChooseTheId() {
        given().contentType(ContentType.JSON).body("{\"id\":999999,\"isim\":\"Ali\",\"email\":\"ali@example.com\"}")
                .when().post("/kullanici")
                .then().statusCode(201).body("id", not(is(999999)));
    }

    @Test
    void invalidInputIsRejected() {
        given().contentType(ContentType.JSON).body("{\"isim\":\"\",\"email\":\"not-an-email\"}")
                .when().post("/kullanici")
                .then().statusCode(400);
    }

    @Test
    void duplicateEmailIsConflict() {
        create("Mehmet", "mehmet@example.com");
        given().contentType(ContentType.JSON).body("{\"isim\":\"Başka\",\"email\":\"mehmet@example.com\"}")
                .when().post("/kullanici")
                .then().statusCode(409);
    }

    @Test
    void updatingMissingUserIsNotFound() {
        given().contentType(ContentType.JSON).body("{\"isim\":\"X\",\"email\":\"x@example.com\"}")
                .when().put("/kullanici/424242")
                .then().statusCode(404);
    }

    @Test
    void healthIsUp() {
        given().when().get("/q/health").then().statusCode(200).body("status", is("UP"));
    }
}
