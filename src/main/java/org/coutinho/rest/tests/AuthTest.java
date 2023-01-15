package org.coutinho.rest.tests;

import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.coutinho.rest.core.Authentication;
import org.coutinho.rest.core.BaseTest;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

@DisplayName("Authentication Scenarios")
@Tag("AuthenticationRegressionTest")
public class AuthTest extends BaseTest {
    @Test
    public void shouldLogInWithValidCredentials(){
        HashMap<String, String> login = new Authentication().getLoginBody();
        given()
                .body(login)
                .when().post("/signin").then().statusCode(200)
                .body("id", is(notNullValue()))
                .body("nome", is(notNullValue()))
                .body("token", is(notNullValue()));
    }
    @Test
    public void shouldNotAccessServiceWithoutToken() {
        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");

        given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401);
    }
}
