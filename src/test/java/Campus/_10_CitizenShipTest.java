package Campus;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class _10_CitizenShipTest extends CampusParent {

    String citizenShipName;
    String shortName;
    String citizenID;


    @Test
    public void CreateCitizenShip() {
        String rndFullName = randomUreteci.name().fullName();
        String rndShortName = randomUreteci.name().name() + randomUreteci.number().digits(5);

        Map<String, String> newCitizenShip = new HashMap<>();
        newCitizenShip.put("name", rndFullName);
        newCitizenShip.put("code", rndShortName);

        Response response =
                given()
                        .spec(reqSpec)
                        .body(newCitizenShip)

                        .when()
                        .post("school-service/api/citizenships")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().response();

        citizenShipName = response.jsonPath().getString("name");
        shortName = response.jsonPath().getString("shortName");
        citizenID = response.jsonPath().getString("id");

    }

    @Test(dependsOnMethods = "CreateCitizenShip")
    public void CreateCitizenShipNegative() {

        Map<String, String> newCitizenShip = new HashMap<>();
        newCitizenShip.put("name", citizenShipName);
        newCitizenShip.put("code", shortName);


        given()
                .spec(reqSpec)
                .body(newCitizenShip)

                .when()
                .post("school-service/api/citizenships")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"))
        ;

    }


    @Test(dependsOnMethods = "CreateCitizenShipNegative")
    public void updateCitizenships() {

        String citizenName="kemeal4477";
        String shortName="sevinc342";

        Map<String, String> newCitizenShip = new HashMap<>();
        newCitizenShip.put("id", citizenID);
        newCitizenShip.put("name", citizenName);
        newCitizenShip.put("code", shortName);

        given()
                .spec(reqSpec)
                .body(newCitizenShip)

                .when()
                .put("school-service/api/citizenships")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(citizenName))
        ;
    }


    @Test(dependsOnMethods = "updateCitizenships")
    public void deleteCitizenShip() {

        given()
                .spec(reqSpec)

                .when()
                .delete("school-service/api/citizenships/" + citizenID)

                .then()
                .statusCode(200)
        ;
    }

}
