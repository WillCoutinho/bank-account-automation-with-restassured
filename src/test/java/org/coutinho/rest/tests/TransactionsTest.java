package org.coutinho.rest.tests;

import io.restassured.response.Response;
import org.coutinho.rest.core.Accounts;
import org.coutinho.rest.core.BaseTest;
import org.coutinho.rest.core.Transactions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.coutinho.rest.utils.DateUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@DisplayName("Transaction Scenarios")
@Tag("TransactionsRegressionTest")
public class TransactionsTest extends BaseTest {

    @Test
    @DisplayName("Should insert a Transaction into an Account successfully")
    public void shouldInsertATransaction() {
        Response account = Accounts.createAccountAndReturnItsData();
        Transactions transaction = Transactions.createTransaction(account.path("id"), account.path("usuario_id"));

        given()
                .body(transaction)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("descricao", is(transaction.getDescricao()))
                .body("envolvido", is(transaction.getEnvolvido()))
                .body("tipo", is(transaction.getTipo()))
                .body("valor", is(transaction.getValor().toString()))
                .body("status", is(transaction.getStatus()))
                .body("conta_id", is(transaction.getConta_id()))
                .body("usuario_id", is(notNullValue()));
    }

    @Test
    @DisplayName("Should check all transaction mandatory fields")
    public void shouldValidateAllTransactionMandatoryFields() {
        given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(8))
                .body("msg", hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"
                ));
    }

    @Test
    @DisplayName("Should not insert a Movement Date greater than Current Date")
    public void shouldNotInsertMovementDateGreaterThanCurrentDate() {
        Response account = Accounts.createAccountAndReturnItsData();
        Transactions transaction = Transactions.createTransaction(account.path("id"), account.path("usuario_id"));
        transaction.setData_transacao(DateUtils.getDateBetweenDaysAndCurrentDate(90));

        given()
                .body(transaction)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
    }

    @Test
    @DisplayName("Should insert a Movement Date equal to Current Date")
    public void shouldInsertMovementDateEqualToCurrentDate() {
        Response account = Accounts.createAccountAndReturnItsData();
        Transactions transaction = Transactions.createTransaction(account.path("id"), account.path("usuario_id"));
        transaction.setData_transacao(DateUtils.getDateBetweenDaysAndCurrentDate(0));

        given()
                .body(transaction)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("descricao", is(transaction.getDescricao()))
                .body("envolvido", is(transaction.getEnvolvido()))
                .body("tipo", is(transaction.getTipo()))
                .body("valor", is(transaction.getValor().toString()))
                .body("status", is(transaction.getStatus()))
                .body("conta_id", is(transaction.getConta_id()))
                .body("usuario_id", is(notNullValue()));
    }

    @Test
    @DisplayName("Should not allow remove an account with transaction")
    public void shouldNotRemoveAccountWithTransaction() {
        Response transaction = Transactions.insertTransactionIntoAnAccountCreated();

        given()
                .when()
                .delete("/contas/{id}", transaction.path("conta_id").toString())
                .then()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    @DisplayName("Should remove a transaction successfully")
    public void shouldRemoveTransaction(){
        Integer transactionId = Transactions.getTransactionIdByDescription("Movimentacao para exclusao");

        given()
                .pathParam("id", transactionId)
                .when()
                .delete("/transacoes/{id}")
                .then().statusCode(204);

        assertThat(Transactions.getTransactionIdByDescription("Movimentacao para exclusao"), is(nullValue()));
    }
}
