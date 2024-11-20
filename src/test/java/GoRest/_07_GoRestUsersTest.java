package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _07_GoRestUsersTest {
    Faker randomUreteci = new Faker();
    RequestSpecification reqSpec;
    int userID=0;


    @BeforeClass
    public  void  Setup() {
        baseURI = "https://gorest.co.in/public/v2/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer b172960641ed109271c536e95cbda6dc83a3ed77056f4c4dda363f05376d328d")
                .setContentType(ContentType.JSON)
                .build();

    }


    @Test
    public void CreateUser() {
        String rndFullName = randomUreteci.name().fullName();
        String rndEmail = randomUreteci.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>(); // postman deki body kısmı map olarak hazırlandı
        newUser.put("name", rndFullName);
        newUser.put("gender", "Male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");


        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)

                        .when()
                        .post("users")    // htpp ile başlamıyorsa baseURI başına geliyor

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
        ;
        System.out.println("userID = " + userID);
    }


    @Test(dependsOnMethods = "CreateUser")
    public void getUserByID() {


        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .get("users/" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "getUserByID")
    public void updateUser() {

        String rndFullName = randomUreteci.name().fullName();
        String rndEmail = randomUreteci.internet().emailAddress();

        Map<String, String> newUser1 = new HashMap<>();
        newUser1.put("name", rndFullName);
        newUser1.put("email", rndEmail);

        given()
                .spec(reqSpec)
                .body(newUser1)
                .when()
                .put("users/"+userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo(rndFullName))
        ;

    }


    @Test(dependsOnMethods = "updateUser")
    public void deleteUser(){

        given()
                .spec(reqSpec)

                .when()
                .delete("users/"+userID)

                .then()
                .statusCode(204)
        ;

    }

   @Test(dependsOnMethods = "deleteUser")
    public void DeleteUserNegative(){

        given()
                .spec(reqSpec)
                .log().uri()

                .when()

                .delete("users/"+userID)

                .then()
                .log().body()
                .statusCode(404)

        ;
    }

}
