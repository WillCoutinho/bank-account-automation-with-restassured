package org.coutinho.rest.core;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.coutinho.rest.utils.DateUtils;
import org.jetbrains.annotations.NotNull;

import static io.restassured.RestAssured.given;

public final class Transactions {
    private String descricao;
    private String envolvido;
    private String tipo;
    private String data_transacao;
    private String data_pagamento;
    private Float valor;
    private Boolean status;
    private Integer conta_id;
    private Integer usuario_id;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEnvolvido() {
        return envolvido;
    }

    public void setEnvolvido(String envolvido) {
        this.envolvido = envolvido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData_transacao() {
        return data_transacao;
    }

    public void setData_transacao(String data_transacao) {
        this.data_transacao = data_transacao;
    }

    public String getData_pagamento() {
        return data_pagamento;
    }

    public void setData_pagamento(String data_pagamento) {
        this.data_pagamento = data_pagamento;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getConta_id() {
        return conta_id;
    }

    public void setConta_id(Integer conta_id) {
        this.conta_id = conta_id;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    @NotNull
    public static Transactions createTransaction(Integer accountId, Integer userId) {
        Transactions transactions = new Transactions();

        transactions.setConta_id(accountId);
        transactions.setUsuario_id(userId);
        transactions.setDescricao("Transaction description");
        transactions.setEnvolvido("Transaction involved");
        transactions.setTipo("REC");
        transactions.setData_transacao(DateUtils.getDateBetweenDaysAndCurrentDate(0));
        transactions.setData_pagamento(DateUtils.getDateBetweenDaysAndCurrentDate(60));
        transactions.setValor(1000.99f);
        transactions.setStatus(true);

        return transactions;
    }

    public static Response insertTransactionIntoAnAccountCreated() {
        Response account = Accounts.createAccountAndReturnItsData();
        Transactions transaction = Transactions.createTransaction(account.path("id"), account.path("usuario_id"));
        transaction.setConta_id(account.path("id"));
        transaction.setUsuario_id(account.path("usuario_id"));

        return given()
                .body(transaction)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201).extract().response();
    }

    public static Integer getTransactionIdByDescription(String description) {
        return RestAssured.get("/transacoes?descricao="+description).then().extract().path("id[0]");
    }
}
