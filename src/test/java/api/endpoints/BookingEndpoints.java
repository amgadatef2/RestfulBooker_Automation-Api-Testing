package api.endpoints;

import api.payload.Booking;
import api.utils.RestAssuredSetup;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class BookingEndpoints {

    private BookingEndpoints() {}



    public static Response healthCheck() {
        return given()
                .spec(RestAssuredSetup.requestSpec)
                .when()
                .get(Routes.PING)
                .then()
                .spec(RestAssuredSetup.responseSpec)
                .extract().response();
    }



    public static Response createToken(String username, String password) {
        String body = String.format(
                "{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        return given()
                .spec(RestAssuredSetup.requestSpec)
                .body(body)
                .when()
                .post(Routes.AUTH)
                .then()
                .spec(RestAssuredSetup.responseSpec)
                .extract().response();
    }



    public static Response createBooking(Booking booking) {
        return given()
                .spec(RestAssuredSetup.requestSpec)
                .body(booking)
                .when()
                .post(Routes.BOOKING)
                .then()
                .spec(RestAssuredSetup.responseSpec)
                .extract().response();
    }



    public static Response getAllBookingIds() {
        return given()
                .spec(RestAssuredSetup.requestSpec)
                .when()
                .get(Routes.BOOKING)
                .then()
                .spec(RestAssuredSetup.responseSpec)
                .extract().response();
    }

    public static Response getBookingById(int id) {
        return given()
                .spec(RestAssuredSetup.requestSpec)
                .pathParam("id", id)
                .when()
                .get(Routes.BOOKING_BY_ID)
                .then()
                .spec(RestAssuredSetup.responseSpec)
                .extract().response();
    }



    public static Response updateBooking(int id, Booking booking, String token) {
        return given()
                .spec(RestAssuredSetup.requestSpec)
                .cookie("token", token)
                .pathParam("id", id)
                .body(booking)
                .when()
                .put(Routes.BOOKING_BY_ID)
                .then()
                .spec(RestAssuredSetup.responseSpec)
                .extract().response();
    }



    public static Response partialUpdateBooking(int id, Object partialBody, String token) {
        return given()
                .spec(RestAssuredSetup.requestSpec)
                .cookie("token", token)
                .pathParam("id", id)
                .body(partialBody)
                .when()
                .patch(Routes.BOOKING_BY_ID)
                .then()
                .spec(RestAssuredSetup.responseSpec)
                .extract().response();
    }



    public static Response deleteBooking(int id, String token) {
        return given()
                .spec(RestAssuredSetup.requestSpec)
                .cookie("token", token)
                .pathParam("id", id)
                .when()
                .delete(Routes.BOOKING_BY_ID)
                .then()
                .spec(RestAssuredSetup.responseSpec)
                .extract().response();
    }
}
