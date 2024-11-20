import Model.Location;
import Model.Place;
import Model.ToDo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class _05_Task {
    /**
     * Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */

    @Test
    public void Task1() {

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;

    }


    /**
     * Task 2
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * a) expect response completed status to be false(hamcrest)*
     * b) extract completed field and testNG assertion(testNG)
     */

    @Test
    public void Task2() {

        boolean completed =
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .log().body()
                        .contentType(ContentType.JSON)
                        .body("completed", equalTo(false))  // a
                        .extract().path("completed");

        Assert.assertEquals(completed, false);  // b
    }


    /**
     * Task 3
     * <p>
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * Converting Into POJO body data and write
     */


    @Test
    public void Task3() {

        ToDo toDoNesnesi =
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .log().body()
                        .extract().body().as(ToDo.class)  // Tüm body all Location.class (kalıba göre) çevir
                ;
        System.out.println("toDoNesnesi = " + toDoNesnesi);
        Assert.assertTrue(toDoNesnesi.getId() == 2);
    }

}
