package org.coutinho.rest.tests;

import io.restassured.response.Response;
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
    private static final String ACCOUNT_NAME_TO_EDIT = "Account QA Test v2";
    private static final String ACCOUNT_NAME_TO_ADD = "Account QA Test v1";

    @Test
    @DisplayName("Should add a new account successfully")
    public void shouldAddNewAccount() {
        Accounts accountUtils = new Accounts();
        HashMap<String, String> accountName = accountUtils.createAccountName(ACCOUNT_NAME_TO_ADD);

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
        Response accountToEdit = new Accounts().createAccountAndReturnItsData();
        HashMap<String, String> accountName = new Accounts().createAccountName(ACCOUNT_NAME_TO_EDIT);

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
        Response accountAlreadyExists = new Accounts().createAccountAndReturnItsData();

        given()
                .body(new Accounts().createAccountName(accountAlreadyExists.path("nome")))
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));
    }
}
