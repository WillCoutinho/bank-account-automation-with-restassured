package rest.tests;

import org.coutinho.rest.core.BaseTest;

import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class BankAccountLoginTest extends BaseTest {

    private String TOKEN;

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
    }

    @Test
    public void shouldNotAccessAccountsWithoutToken() {
        given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldAddNewAccount() {
        given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\":\"teste qa\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(201);
    }

    @Test
    public void shouldEditAnAccount(){
        given().header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\":\"teste qa alterado\"}")
                .when()
                .put("/contas/1551350")
                .then()
                .statusCode(200)
                .body("nome", is("teste qa alterado"));
    }

    @Test
    public void shouldNotInsertAccountAlreadyExisted(){
        given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\":\"teste qa alterado\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));

    }

    @Test
    public void shouldInsertAccountTransaction(){
        //TODO:WIP
        //        given()
        //                .header("Authorization", "JWT " + TOKEN)
        //                .when()
        //                .post("/transacoes")
        //                .then().statusCode(201);

    }
}
