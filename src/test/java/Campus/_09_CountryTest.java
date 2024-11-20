package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class _09_CountryTest {

    Faker randomUreteci = new Faker();
    RequestSpecification reqSpec;
    String CountryName;
    String Countrycode;
    String countryID;

    @BeforeClass
    public void Setup() {

        baseURI = "https://test.mersys.io/";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when()
                        .post("auth/login")

                        .then()
                        // .log().all()
                        .statusCode(200)
                        .extract().response().detailedCookies();

        // System.out.println("cookies = " + cookies);

        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();

    }

    @Test
    public void CreateCountry() {
        String rndFullName = randomUreteci.address().country() + randomUreteci.number().digits(5);
        String rndCode = randomUreteci.address().countryCode() + randomUreteci.number().digits(5);

        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("name", rndFullName);
        newCountry.put("code", rndCode);

        Response response =
                given()
                        .spec(reqSpec)
                        .body(newCountry)

                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().response();

        CountryName = response.jsonPath().getString("name");
        Countrycode = response.jsonPath().getString("code");
        countryID = response.jsonPath().getString("id");
        System.out.println(countryID);
    }


    @Test(dependsOnMethods = "CreateCountry")
    public void CreateCountryNegative() {

        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("name", CountryName);
        newCountry.put("code", Countrycode);


        given()
                .spec(reqSpec)
                .body(newCountry)

                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"))
        ;

    }

    @Test(dependsOnMethods = "CreateCountryNegative")
    public void updateCountry() {


        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("id", countryID);
        newCountry.put("name", "Turkiyett4477");
        newCountry.put("code", "3788");

        given()
                .spec(reqSpec)
                .body(newCountry)

                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo("Turkiyett4477"))
        ;
    }

    @Test(dependsOnMethods = "updateCountry")
    public void DeleteCountry() {

        given()
                .spec(reqSpec)

                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .statusCode(200)

        ;
    }

  /*  @Test(dependsOnMethods = "DeleteCountry")
    public void DeleteCountryNegative() {

        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .statusCode(400)
        ;
    }*/


}
