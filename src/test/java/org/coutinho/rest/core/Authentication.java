package org.coutinho.rest.core;
import io.restassured.RestAssured;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import static io.restassured.RestAssured.given;

public final class Authentication {
    private static final String EMAIL_TEST = "coutinho@test";
    private static final String PASSWORD_TEST = "123456";

    @NotNull
    public static HashMap<String, String> getValidLoginBody(){
        HashMap<String, String> loginBody = new HashMap<>();
        loginBody.put("email", EMAIL_TEST);
        loginBody.put("senha", PASSWORD_TEST);

        return loginBody;
    }

    public static void login(){
        String authToken =
                given()
                        .body(getValidLoginBody())
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");

        RestAssured.requestSpecification.header("Authorization", "JWT " + authToken);
        RestAssured.get("/reset").then().statusCode(200);
    }
}
