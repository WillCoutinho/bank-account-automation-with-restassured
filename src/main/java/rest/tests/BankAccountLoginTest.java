package rest.tests;

import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.coutinho.rest.core.BaseTest;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DateUtils;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class BankAccountLoginTest extends BaseTest {

    private String TOKEN;

    private AccountTransactions getValidAccountTransaction() {
        AccountTransactions account = new AccountTransactions();

        account.setConta_id(1551350);
        account.setDescricao("Transaction description");
        account.setEnvolvido("Transaction involved");
        account.setTipo("REC");
        account.setData_transacao(DateUtils.getDateBetweenDaysAndCurrentDate(0));
        account.setData_pagamento(DateUtils.getDateBetweenDaysAndCurrentDate(60));
        account.setValor(100.99f);
        account.setStatus(true);

        return account;
    }

    @BeforeEach
    public void login() {
        HashMap<String, String> login = new HashMap<>();
        login.put("email", "coutinho@test");
        login.put("senha", "123456");

        TOKEN =
                given()
                        .body(login)
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");

        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);
    }

    @Test
    public void shouldNotAccessAccountsWithoutToken() {
        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");

        given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldAddNewAccount() {
        given()
                .body("{\"nome\":\"teste qa\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(201);
    }

    @Test
    public void shouldEditAnAccount() {
        given()
                .body("{\"nome\":\"teste qa alterado\"}")
                .when()
                .put("/contas/1551350")
                .then()
                .statusCode(200)
                .body("nome", is("teste qa alterado"));
    }

    @Test
    public void shouldNotInsertAccountAlreadyExisted() {
        given()
                .body("{\"nome\":\"teste qa alterado\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));

    }

    @Test
    public void shouldInsertAccountTransaction() {
        AccountTransactions account = getValidAccountTransaction();

        given()
                .body(account)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("descricao", is(account.getDescricao()))
                .body("envolvido", is(account.getEnvolvido()))
                .body("tipo", is(account.getTipo()))
                .body("valor", is(account.getValor().toString()))
                .body("status", is(account.getStatus()))
                .body("conta_id", is(account.getConta_id()))
                .body("usuario_id", is(notNullValue()));
    }

    @Test
    public void shouldValidateAllMandatoryFields() {
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
    public void shouldNotInsertMovementDateGreaterThanCurrentDate() {
        AccountTransactions account = getValidAccountTransaction();
        account.setData_transacao(DateUtils.getDateBetweenDaysAndCurrentDate(90));

        given()
                .body(account)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
    }

    @Test
    public void shouldInsertMovementDateEqualToCurrentDate() {
        AccountTransactions account = getValidAccountTransaction();
        account.setData_transacao(DateUtils.getDateBetweenDaysAndCurrentDate(0));

        given()
                .body(account)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("descricao", is(account.getDescricao()))
                .body("envolvido", is(account.getEnvolvido()))
                .body("tipo", is(account.getTipo()))
                .body("valor", is(account.getValor().toString()))
                .body("status", is(account.getStatus()))
                .body("conta_id", is(account.getConta_id()))
                .body("usuario_id", is(notNullValue()));
    }

    @Test
    public void shouldNotRemoveAccountWithTransaction() {
        given()
                .when()
                .delete("/contas/1551350")
                .then()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void shouldCheckAccountBalance() {
        ///1551350 - multi valores
        // 1551807 - um valor
//        List<String> saldo =
        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200);
//                    .extract().path("saldo");

        given().header("Authorization", "JWT " + TOKEN)
                .when()
                .get("/extrato")
                .then()
                .statusCode(201);
    }

    @Test
    public void shouldDeleteATransaction() {
        given()
                .when()
                .delete("/transacoes/");
    }

}
