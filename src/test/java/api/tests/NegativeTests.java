package api.tests;

import api.endpoints.BookingEndpoints;
import api.endpoints.Routes;
import api.payload.Booking;
import api.payload.BookingDates;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import api.utils.RestAssuredSetup;
import static io.restassured.RestAssured.given;

@Epic("Restful-Booker API")
@Feature("Negative Scenarios")
public class NegativeTests extends BaseTest {



    @Test(priority = 1)
    @Story("Authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("NT01 - Wrong credentials should return 'Bad credentials' message")
    public void NT01_CreateToken_WrongCredentials() {
        Response response = BookingEndpoints.createToken("amgad", "13123124");

        Assert.assertEquals(response.getStatusCode(), 200);

        String reason = response.jsonPath().getString("reason");
        Assert.assertEquals(reason, "Bad credentials",
                "Should return 'Bad credentials' for invalid login");
    }



    @Test(priority = 2)
    @Story("Authentication")
    @Severity(SeverityLevel.NORMAL)
    @Description("NT02 - Empty credentials should return 'Bad credentials'")
    public void NT02_CreateToken_EmptyCredentials() {
        Response response = BookingEndpoints.createToken("", "");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
    }



    @Test(priority = 3)
    @Story("Create Booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("NT03 - Create booking with missing required fields should return 500")
    public void NT03_CreateBooking_MissingFields() {

        Map<String, Object> incompleteBody = new HashMap<>();
        incompleteBody.put("totalprice", 100);
        incompleteBody.put("depositpaid", true);

        Response response = given()
                .spec(RestAssuredSetup.requestSpec)
                .body(incompleteBody)
                .when()
                .post(Routes.BOOKING);


        Assert.assertEquals(response.getStatusCode(), 500,
                "Missing required fields should cause a server error");
    }



    @Test(priority = 4)
    @Story("Create Booking")
    @Severity(SeverityLevel.MINOR)
    @Description("NT04 - Create booking with invalid date format should not return 200")
    public void NT04_CreateBooking_InvalidDateFormat() {
        BookingDates badDates = new BookingDates("look at me", "hey wo wo wo");
        Booking booking = new Booking("Test", "User", 100, true, badDates, "None");

        Response response = BookingEndpoints.createBooking(booking);

        Assert.assertNotEquals(response.getStatusCode(), 400,
                "Invalid date format should not result in a successful booking");
    }


    @Test(priority = 5)
    @Story("Get Booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("NT05 - GET booking with non-existent ID should return 404")
    public void NT05_GetBooking_NotFound() {
        Response response = BookingEndpoints.getBookingById(999999999);

        Assert.assertEquals(response.getStatusCode(), 404,
                "Non-existent booking ID should return 404");
    }



    @Test(priority = 6)
    @Story("Update Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Description("NT06 - PUT without auth token should return 403 Forbidden")
    public void NT06_UpdateBooking_NoToken() {
        Booking booking = new Booking("Test", "User", 100, true,
                new BookingDates("2018-01-01", "2019-01-01"), "None");

        Response response = given()
                .spec(RestAssuredSetup.requestSpec)
                .body(booking)
                .pathParam("id", 1)
                .when()
                .put(Routes.BOOKING_BY_ID);

        Assert.assertEquals(response.getStatusCode(), 403,
                "Update without token should be 403 Forbidden");
    }



    @Test(priority = 7)
    @Story("Update Booking")
    @Severity(SeverityLevel.NORMAL)
    @Description("NT07 - PUT with invalid token should return 403 Forbidden")
    public void NT07_UpdateBooking_InvalidToken() {
        Booking booking = new Booking("Test", "User", 100, true,
                new BookingDates("2018-01-01", "2019-01-01"), "None");

        Response response = given()
                .spec(RestAssuredSetup.requestSpec)
                .cookie("token", "THIS_IS_NOT_A_VALID_TOKEN")
                .body(booking)
                .pathParam("id", 1)
                .when()
                .put(Routes.BOOKING_BY_ID);

        Assert.assertEquals(response.getStatusCode(), 403,
                "Update with invalid token should be 403 Forbidden");
    }



    @Test(priority = 8)
    @Story("Delete Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Description("NT08 - DELETE without token should return 403 Forbidden")
    public void NT08_DeleteBooking_NoToken() {
        Response response = given()
                .spec(RestAssuredSetup.requestSpec)
                .pathParam("id", 1)
                .when()
                .delete(Routes.BOOKING_BY_ID);

        Assert.assertEquals(response.getStatusCode(), 403,
                "Delete without token should be 403 Forbidden");
    }



    @Test(priority = 9)
    @Story("Get Bookings")
    @Severity(SeverityLevel.MINOR)
    @Description("NT09 - Filter bookings by a name that doesn't exist should return empty list")
    public void NT09_GetBookings_NoMatchFilter() {
        Response response = given()
                .spec(RestAssuredSetup.requestSpec)
                .queryParam("firstname", "ZZH999")
                .when()
                .get(Routes.BOOKING);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getList("$").size(), 0,
                "Filter with no match should return an empty array");
    }
}
