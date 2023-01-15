package org.coutinho.rest.core;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import static io.restassured.RestAssured.given;

public final class Accounts {

    public static HashMap<String, String> generateAccountName(String name) {
        HashMap<String, String> accountName = new HashMap<>();
        accountName.put("nome", name);
        return accountName;
    }

    public static Response createAccountAndReturnItsData() {
        return
                given()
                        .body(generateAccountName(RandomStringUtils.randomAlphabetic(5) + System.nanoTime()))
                        .when()
                        .post("/contas")
                        .then()
                        .statusCode(201).extract().response();
    }

    public static Integer getAccountIdByName(String name){
        return RestAssured.get("/contas?nome="+name).then().extract().path("id[0]");
    }

}
