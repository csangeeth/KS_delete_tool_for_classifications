import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class containsConcepts {
    private static final String ID = "5be89ead-e625-4521-b585-771c964ddeb0";

    public static void main(String[] args) {
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .pathParam("id", ID)
                .get("http://ks-knowledge-services-api-qa.aws.wiley.com:8080/ks/v2/ArticleProducts/{id}/ClassificationMetadata/containsConcept");

        List<String> list = response.getBody().jsonPath().getList("content.id");
        for (String s : list) {
            System.out.println("Unlinking concept: " + s);
            given()
                    .header("Content-Type", "application/json")
                    .when()
                    .pathParam("id", s)
                    .delete("http://ks-knowledge-services-api-qa.aws.wiley.com:8080/ks/v2/ClassificationMetadata/{id}")
                    .then()
                    .statusCode(202);
        }

        Response response1 = given()
                .header("Literal-Language", "*")
                .pathParam("id", ID)
                .get("http://ks-knowledge-services-api-qa.aws.wiley.com:8080/ks/v2/ArticleProducts/{id}/Concepts/containsConcept");

        List<String> ids = response1.jsonPath().getList("content.id");

        List<Map<String, String>> deleteBody = new ArrayList<>();
        for (String id : ids) {
            Map<String, String> map = new HashMap<>();
            map.put("id", id);
            deleteBody.add(map);
        }

        given()
                .header("Content-Type", "application/json")
                .pathParam("id", ID)
                .body(deleteBody)
                .post("http://ks-knowledge-services-api-qa.aws.wiley.com:8080/ks/v2/ArticleProducts/{id}/Concepts/containsConcept/batchDelete")
                .then()
                .statusCode(200);
    }
}

