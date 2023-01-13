package rest.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.coutinho.rest.core.BaseTest;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.DateUtils;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class BankAccountTests extends BaseTest {

    private static final String ACCOUNT_NAME_TO_EDIT = "Account QA Test v2";
    private static final String ACCOUNT_NAME_TO_ADD = "Account QA Test v1";

    private AccountTransactions getValidAccountTransaction(Integer accountId, Integer userId) {
        AccountTransactions account = new AccountTransactions();

        account.setConta_id(accountId);
        account.setUsuario_id(userId);
        account.setDescricao("Transaction description");
        account.setEnvolvido("Transaction involved");
        account.setTipo("REC");
        account.setData_transacao(DateUtils.getDateBetweenDaysAndCurrentDate(0));
        account.setData_pagamento(DateUtils.getDateBetweenDaysAndCurrentDate(60));
        account.setValor(1000.99f);
        account.setStatus(true);

        return account;
    }

    private Response createAccountAndReturnItsData() {
        return
                given()
                        .body(createAccountName(ACCOUNT_NAME_TO_ADD + System.nanoTime()))
                        .when()
                        .post("/contas")
                        .then()
                        .statusCode(201).extract().response();
    }

    private HashMap<String, String> createAccountName(String name) {
        HashMap<String, String> accountName = new HashMap<>();
        accountName.put("nome", name);
        return accountName;
    }

    private Response insertTransactionIntoAnAccountCreated(){
        Response account = createAccountAndReturnItsData();
        AccountTransactions transaction = getValidAccountTransaction(account.path("id"), account.path("usuario_id"));
        transaction.setConta_id(account.path("id"));
        transaction.setUsuario_id(account.path("usuario_id"));

        return given()
                .body(transaction)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201).extract().response();
    }

    @BeforeAll
    public static void login() {
        HashMap<String, String> login = new HashMap<>();
        login.put("email", "coutinho@test");
        login.put("senha", "123456");

        String authToken =
                given()
                        .body(login)
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");

        RestAssured.requestSpecification.header("Authorization", "JWT " + authToken);

        RestAssured.get("/reset").then().statusCode(200);
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
        HashMap<String, String> accountName = createAccountName(ACCOUNT_NAME_TO_ADD);

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
    public void shouldEditAnAccount() {
        Response accountToEdit = createAccountAndReturnItsData();
        HashMap<String, String> accountName = createAccountName(ACCOUNT_NAME_TO_EDIT);

        given()
                .body(accountName)
                .when()
                .put("/contas/{id}", accountToEdit.path("id").toString())
                .then()
                .statusCode(200)
                .body("nome", is(accountName.get("nome")));
    }

    @Test
    public void shouldNotInsertAccountAlreadyExisted() {
        Response accountAlreadyExists = createAccountAndReturnItsData();
        HashMap<String, String> accountName = createAccountName(accountAlreadyExists.path("nome"));

        given()
                .body(accountName)
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void shouldInsertAccountTransaction() {
        Response account = createAccountAndReturnItsData();
        AccountTransactions transaction = getValidAccountTransaction(account.path("id"), account.path("usuario_id"));

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
        Response account = createAccountAndReturnItsData();
        AccountTransactions transaction = getValidAccountTransaction(account.path("id"), account.path("usuario_id"));
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
    public void shouldInsertMovementDateEqualToCurrentDate() {
        Response account = createAccountAndReturnItsData();
        AccountTransactions transaction = getValidAccountTransaction(account.path("id"), account.path("usuario_id"));
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
    public void shouldNotRemoveAccountWithTransaction() {
        Response transaction = insertTransactionIntoAnAccountCreated();

        given()
                .when()
                .delete("/contas/{id}", transaction.path("conta_id").toString())
                .then()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"));
    }

    @Test
    public void shouldCheckAccountBalance() {
        //WIP
    }

    @Test
    public void shouldDeleteATransaction() {
        //WIP
    }
}
