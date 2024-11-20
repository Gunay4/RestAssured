
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _01_ApiTest {

    @Test
    public void Test() {

        // 1- Endpoint i çağırmadna önce hazırlıkların yapıldığı bölüm : Request, gidecek body
        //                                                           token
        // 2- Endpoint in çağrıldığı bölüm  : Endpoint in çağrılması(METOD: GET,POST ..)
        // 3- Endpoint çağrıldıktan sonraki bölüm : Response, Test(Assert), data

        given().
                //1.blümle ilgili işler : giden body,token
                        when().
                //2.blümle ilgili işler : metod , endpoint
                        then()
        //3.bölümle ilgili işler: gelen data, assert,test
        ;
    }

    @Test
    public void statusCodeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                //.log().all()
                .statusCode(200)
        ;
    }

    @Test
    public void contentTypeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }


    @Test
    public void checkCountryInResponseBody() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  // dönen data kısmı
                .statusCode(200)    // dönen değer 200 e eşitm i, assert
                .contentType(ContentType.JSON)   // dönen data nın tipi JSON mı
                .body("country", equalTo("United States"))// country yi dışarı almadan
        // bulundu yeri (path i) vererek içerde assertion yapıyorum.Bunu hamcrest kütüphanesi yapıyor

//        pm.test("Ulke Bulunamadı", function () {
//        pm.expect(pm.response.json().message).to.eql("Country not found");
//    });

        ;
    }


    @Test
    public void checkCountryInResponseBody2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu doğrulayınız

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state", equalTo("California"))
        ;
    }


    @Test
    public void checkHasItem() {
        // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
        // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
        // olduğunu doğrulayınız


        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))
        ;
    }


    @Test
    public void bodyArrayHasSizeTest() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1)) // places in eleman uzunluğuı 1 mi
        ;
    }


    @Test
    public void combininTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places.'place name'", hasItem("Beverly Hills"))
        ;
    }


   /*
    https://sonuc.osym.gov.tr/Sorgu.aspx?SonucID=9917
    Query Parameter yöntemi ? yani sorgu, query


    https://gorest.co.in/public/v2/users/6942409
    Path Parameter yöntemi   yani path ini yani yolunu

    */

    @Test
    public void pathParamTest() {
      // http://api.zippopotam.us/us/90210

        given()
                .pathParam("ulke", "us")
                .pathParam("postaKodu", 90210)
                .log().uri() // oluşacak endpoint i yazdıralım


                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKodu}") // path param

                .then()
                .log().body()
        ;
    }


    @Test
    public void queryParamTest() {
        // https://gorest.co.in/public/v1/users?page=3

        given()
                .param("page", 3)
                .log().uri() // oluşacak endpoint i yazdıralım


                .when()
                .get("https://gorest.co.in/public/v1/users") // URL kısmını buraya yazıldı

                .then()
                .log().body()
        ;
    }


    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)
                    .log().uri() // oluşacak endpoint i yazdıralım


                    .when()
                    .get("https://gorest.co.in/public/v1/users") // URL kısmını buraya yazıldı

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }
}
