package org.coutinho.rest.tests;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.coutinho.rest.core.Accounts;
import org.coutinho.rest.core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Account Scenarios")
@Tag("AccountRegressionTest")
public class AccountsTest extends BaseTest {

    @Test
    @DisplayName("Should add a new account successfully")
    public void shouldAddNewAccount() {
        HashMap<String, String> accountName = Accounts.generateAccountName(RandomStringUtils.randomAlphabetic(5));

        given()
                .body(accountName)
                .when()
                .post("/contas")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("nome", is(accountName.get("nome")))
                .body("usuario_id", is(notNullValue()));
    }

    @Test
    @DisplayName("Should edit an account successfully")
    public void shouldEditAnAccount() {
        Response accountToEdit = Accounts.createAccountAndReturnItsData();
        HashMap<String, String> accountName = Accounts.generateAccountName(RandomStringUtils.randomAlphabetic(10));

        given()
                .body(accountName)
                .when()
                .put("/contas/{id}", accountToEdit.path("id").toString())
                .then()
                .statusCode(200)
                .body("nome", is(accountName.get("nome")));
    }

    @Test
    @DisplayName("Should not allow insert an account with name that already exists")
    public void shouldNotInsertAccountAlreadyExisted() {
        Response accountAlreadyExists = Accounts.createAccountAndReturnItsData();

        given()
                .body(Accounts.generateAccountName(accountAlreadyExists.path("nome")))
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));
    }
}
