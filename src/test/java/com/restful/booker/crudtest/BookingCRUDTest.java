package com.restful.booker.crudtest;

import com.restful.booker.model.BookingPojo;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class BookingCRUDTest extends TestBase {
    static String firstName = "Kiara" + TestUtils.getRandomValue();
    static String updateFirstName = "Mishka" + TestUtils.getRandomValue();
    static String lastName = "Parikh" + TestUtils.getRandomValue();
    static String additionalNeeds = "Breakfast";
    static String username = "admin";
    static String password = "password123";
    static int userId;
    static String token;

    @BeforeClass
    public static void inIt() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    public void test001() {
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setUsername(username);
        bookingPojo.setPassword(password);
   ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .when()
                .body(bookingPojo)
                .post("/auth")
                .then().log().all().statusCode(200);
        token = response.extract().path("token");


    }

    @Test()
    public void test002() {
        System.out.println("====================" + token);
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(firstName);
        bookingPojo.setLastname(lastName);
        bookingPojo.setTotalprice(111);
        bookingPojo.setDepositpaid(true);
        bookingPojo.setCheckin("2024-11-16");
        bookingPojo.setCheckout("2024-12-08");
        bookingPojo.setAdditionalneeds(additionalNeeds);
      ValidatableResponse response = given().log().all()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .header("Connection", "keep-alive")
                .when()
                .body(bookingPojo)
                .post("/booking")
                .then().log().all().statusCode(200);
        userId = response.extract().path("id");

    }

    @Test
    public void test003() {

        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname(updateFirstName);
        bookingPojo.setLastname(lastName);
        bookingPojo.setTotalprice(111);
        bookingPojo.setDepositpaid(true);
        bookingPojo.setCheckin("2024-11-16");
        bookingPojo.setCheckout("2024-12-09");
        bookingPojo.setAdditionalneeds(additionalNeeds);
        Response response = given().log().all()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .header("Connection", "keep-alive")
                .when()
                .body(bookingPojo)
                .put("/booking/"+userId);
        response.then().log().all().statusCode(200);

    }

    @Test
    public void test004() {

        given().log().all()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .header("Connection", "keep-alive")
                .pathParam("id", userId)
                .when()
                .get("/booking/{id}")
                .then()
                .statusCode(200);

    }

    @Test
    public void test005() {

        given()
                .headers("Content-Type", "application/json", "Cookie", "token=" + token)
                .header("Connection", "keep-alive")
                .pathParam("id", userId)
                .when()
                .delete("/booking/{id}")
                .then()
                .statusCode(201);

    }

}
