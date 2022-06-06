package meli;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class RoutesTest {

    @Test
    void testGetStats(){
        given()
                .when().get("/stats")
                .then()
                .statusCode(200);
    }

    @Test
    void testPostCorrectMutant(){
        given()
                .when()
                .body("{\n\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]\n}")
                .header("Content-Type", "application/json")
                .post("/mutant")
                .then()
                .statusCode(200)
                .body("response", containsString("Is mutant"));
    }

    @Test
    void testPostCorrectHuman(){
        given()
                .when()
                .body("{\n\"dna\":[\"ATGCGA\",\"CCGTGC\",\"TTATGT\",\"AGAACG\",\"CCACTA\",\"TCACTG\"]\n}")
                .header("Content-Type", "application/json")
                .post("/mutant")
                .then()
                .statusCode(403)
                .body("response", containsString("Is not mutant"));
    }

    @Test
    void testPostIncorrectForDnaInvalid(){
        given()
                .when()
                .body("{\n\"dna\":[\"ATGCGAA\",\"CCGTGC\",\"TTATGT\",\"AGAAGG\",\"CCACTA\",\"TCACTG\"]\n}")
                .header("Content-Type", "application/json")
                .post("/mutant")
                .then()
                .statusCode(400)
                .body("response", containsString("Dna invalid"));
    }

    @Test
    void testPostIncorrectForDnaInvalid2(){
        given()
                .when()
                .body("{\n\"dna\":[\"ATBCGAA\",\"CCGTGC\",\"TTATGT\",\"AGAAGG\",\"CCACTA\",\"TCACTG\"]\n}")
                .header("Content-Type", "application/json")
                .post("/mutant")
                .then()
                .statusCode(400)
                .body("response", containsString("Dna invalid"));
    }
}
