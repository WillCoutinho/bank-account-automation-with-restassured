package org.coutinho.rest.core;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import static io.restassured.RestAssured.given;

public class Accounts {

    private static final String ACCOUNT_NAME_TO_EDIT = "Account QA Test v2";
    private static final String ACCOUNT_NAME_TO_ADD = "Account QA Test v1";

    public HashMap<String, String> createAccountName(String name) {
        HashMap<String, String> accountName = new HashMap<>();
        accountName.put("nome", name);
        return accountName;
    }

    public Response createAccountAndReturnItsData() {
        return
                given()
                        .body(createAccountName(ACCOUNT_NAME_TO_ADD + System.nanoTime()))
                        .when()
                        .post("/contas")
                        .then()
                        .statusCode(201).extract().response();
    }

    public Integer getAccountIdByName(String name){
        return RestAssured.get("/contas?nome="+name).then().extract().path("id[0]");
    }

}
