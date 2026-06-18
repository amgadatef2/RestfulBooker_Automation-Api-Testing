package api.tests;

import api.endpoints.BookingEndpoints;
import api.payload.Booking;
import api.utils.ConfigManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Restful-Booker API")
@Feature("Positive Scenarios")
public class PositiveTests extends BaseTest {


    private String token;
    private int    bookingId;


    @BeforeClass
    public void acquireToken() {
        Response response = BookingEndpoints.createToken(
                ConfigManager.get("USERNAME"),
                ConfigManager.get("PASSWORD")
        );
        Assert.assertEquals(response.getStatusCode(), 200,
                "Auth setup failed — cannot proceed with tests");

        token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Token must not be null");
        Assert.assertFalse(token.isEmpty(), "Token must not be empty");


        if (token.startsWith("token=")) {
            token = token.substring("token=".length());
        }
    }



    @Test(priority = 1)
    @Story("Health Check")
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC01 - API should respond 201 on /ping (health check)")
    public void TC01_HealthCheck() {
        Response response = BookingEndpoints.healthCheck();

        Assert.assertEquals(response.getStatusCode(), 201,
                "Health check failed — API may be down");
    }



    @Test(priority = 2)
    @Story("Create Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC02 - Create a booking with valid data and verify bookingid is returned")
    public void TC02_CreateBooking() {
        Booking booking = Booking.defaultBooking();

        Response response = BookingEndpoints.createBooking(booking);

        Assert.assertEquals(response.getStatusCode(), 200,
                "Expected 200 on booking creation");

        bookingId = response.jsonPath().getInt("bookingid");
        Assert.assertTrue(bookingId > 0,
                "bookingid should be a positive integer, got: " + bookingId);
    }



    @Test(priority = 3)
    @Story("Get Bookings")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC03 - GET /booking should return a non-empty list of booking IDs")
    public void TC03_GetAllBookingIds() {
        Response response = BookingEndpoints.getAllBookingIds();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(
                response.jsonPath().getList("bookingid").size() > 0,
                "Booking list should not be empty"
        );
    }



    @Test(priority = 4, dependsOnMethods = "TC02_CreateBooking")
    @Story("Get Booking By ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC04 - Get the created booking by ID and validate every field")
    public void TC04_GetBookingById() {
        Response response = BookingEndpoints.getBookingById(bookingId);

        Assert.assertEquals(response.getStatusCode(), 200);

        Assert.assertEquals(response.jsonPath().getString("firstname"),
                ConfigManager.get("FIRSTNAME"),        "firstname mismatch");
        Assert.assertEquals(response.jsonPath().getString("lastname"),
                ConfigManager.get("LASTNAME"),         "lastname mismatch");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"),
                ConfigManager.getInt("TOTALPRICE"),    "totalprice mismatch");
        Assert.assertEquals(response.jsonPath().getBoolean("depositpaid"),
                ConfigManager.getBoolean("DEPOSITPAID"), "depositpaid mismatch");
        Assert.assertEquals(response.jsonPath().getString("bookingdates.checkin"),
                ConfigManager.get("CHECKIN"),          "checkin mismatch");
        Assert.assertEquals(response.jsonPath().getString("bookingdates.checkout"),
                ConfigManager.get("CHECKOUT"),         "checkout mismatch");
        Assert.assertEquals(response.jsonPath().getString("additionalneeds"),
                ConfigManager.get("ADDITIONALNEEDS"),  "additionalneeds mismatch");
    }



    @Test(priority = 5, dependsOnMethods = "TC02_CreateBooking")
    @Story("Full Update (PUT)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC05 - Full update booking via PUT and verify updated firstname")
    public void TC05_UpdateBooking() {
        Booking updated = Booking.defaultBooking();
        updated.setFirstname("James");
        updated.setLastname("Smith");

        Response response = BookingEndpoints.updateBooking(bookingId, updated, token);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), "James",
                "firstname should be updated to James");
        Assert.assertEquals(response.jsonPath().getString("lastname"), "Smith",
                "lastname should be updated to Smith");
    }



    @Test(priority = 6, dependsOnMethods = "TC02_CreateBooking")
    @Story("Partial Update (PATCH)")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC06 - Partial update via PATCH — update only firstname and totalprice")
    public void TC06_PartialUpdateBooking() {

        java.util.Map<String, Object> partialBody = new java.util.HashMap<>();
        partialBody.put("firstname", "Ahmed");
        partialBody.put("totalprice", 500);

        Response response = BookingEndpoints.partialUpdateBooking(bookingId, partialBody, token);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), "Ahmed");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"), 500);
    }



    @Test(priority = 7, dependsOnMethods = "TC02_CreateBooking")
    @Story("Delete Booking")
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC07 - Delete the created booking — API returns 201 on success")
    public void TC07_DeleteBooking() {
        Response response = BookingEndpoints.deleteBooking(bookingId, token);


        Assert.assertEquals(response.getStatusCode(), 201,
                "Expected 201 on successful delete");
    }



    @Test(priority = 8, dependsOnMethods = "TC07_DeleteBooking")
    @Story("Verify Deletion")
    @Severity(SeverityLevel.NORMAL)
    @Description("TC08 - After deletion, GET by same ID should return 404")
    public void TC08_VerifyBookingDeleted() {
        Response response = BookingEndpoints.getBookingById(bookingId);

        Assert.assertEquals(response.getStatusCode(), 404,
                "Deleted booking should return 404");
    }
}
