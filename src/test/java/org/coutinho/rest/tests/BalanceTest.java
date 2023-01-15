package org.coutinho.rest.tests;

import org.coutinho.rest.core.Accounts;
import org.coutinho.rest.core.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
@DisplayName("Account Balance Scenarios")
@Tag("BalanceRegressionTest")
public class BalanceTest extends BaseTest {

    @Test
    @DisplayName("Should validate if account value is available")
    public void shouldValidateAccountBalance() {
        Integer accountId = Accounts.getAccountIdByName("Conta para saldo");

        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id == " + accountId + "}.saldo", is("534.00"));
    }
}
