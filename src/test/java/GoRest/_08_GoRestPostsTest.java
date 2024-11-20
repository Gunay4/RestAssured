package GoRest;


import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

// GoRest Posts kaynağındaki API leri test ediniz.
// create,getId,update, delete, deleteNegative
public class _08_GoRestPostsTest {

    Faker randomUreteci = new Faker();
    RequestSpecification reqSpec;
    int postID=0;

    @BeforeClass
    public void Setup() {

        baseURI ="https://gorest.co.in/public/v2/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer b172960641ed109271c536e95cbda6dc83a3ed77056f4c4dda363f05376d328d")
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void CreatePost(){

        String rndTitle = randomUreteci.name().title();
        String rndBody = randomUreteci.lorem().paragraph();

        Map<String, String> newUser = new HashMap<>(); // postman deki body kısmı map olarak hazırlandı
        newUser.put("user_id", "7530336");
        newUser.put("title", rndTitle);
        newUser.put("body", rndBody);

        postID=
        given()
                .spec(reqSpec)
                .body(newUser)

                .when()
                .post("posts")

                .then()
                .log().body()
                .statusCode(201)
                .body("title",equalTo(rndTitle))
                .extract().path("id")
        ;
        System.out.println("postID = " + postID);
    }

    @Test(dependsOnMethods = "CreatePost")
    public void GetPostByID() {


        given()
                .spec(reqSpec)

                .when()
                .get("posts/"+postID)

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "GetPostByID")
    public void UpdatePost() {

        String Title = "Hava durumu";
        String Body = "bu gün havanın sıcak olması bekleniyor";

        Map<String, String> newUser1 = new HashMap<>();
        newUser1.put("title", Title);
        newUser1.put("body", Body);

        given()
                .spec(reqSpec)
                .body(newUser1)
                .when()
                .put("posts/"+postID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(postID))
                .body("title", equalTo(Title))
        ;
    }

    @Test(dependsOnMethods = "UpdatePost")
    public void DeletePosts(){

        given()
                .spec(reqSpec)

                .when()
                .delete("posts/"+postID)

                .then()
                .statusCode(204)
        ;

    }


    @Test(dependsOnMethods = "DeletePosts")
    public void DeletePostsNegative(){

        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .delete("posts/"+postID)

                .then()
                .statusCode(404)
        ;

    }

}
