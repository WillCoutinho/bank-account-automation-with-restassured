package org.coutinho.rest.core;
import io.restassured.RestAssured;
import java.util.HashMap;
import static io.restassured.RestAssured.given;

public class Authentication {

    private final String email = "coutinho@test";

    private final String password = "123456";

    private String getEmail(){
        return this.email;
    }

    private String getPassword() {
        return this.password;
    }

    public HashMap<String, String> getLoginBody(){
        HashMap<String, String> login = new HashMap<>();
        login.put("email", getEmail());
        login.put("senha", getPassword());

        return login;
    }

    public void login(){
        String authToken =
                given()
                        .body(getLoginBody())
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");

        RestAssured.requestSpecification.header("Authorization", "JWT " + authToken);
        RestAssured.get("/reset").then().statusCode(200);
    }
}
