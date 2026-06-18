package api.payload;

import api.utils.ConfigManager;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Booking {

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("totalprice")
    private int totalprice;

    @JsonProperty("depositpaid")
    private boolean depositpaid;

    @JsonProperty("bookingdates")
    private BookingDates bookingdates;

    @JsonProperty("additionalneeds")
    private String additionalneeds;


    public Booking() {}

    public Booking(String firstname, String lastname, int totalprice,
                   boolean depositpaid, BookingDates bookingdates, String additionalneeds) {
        this.firstname      = firstname;
        this.lastname       = lastname;
        this.totalprice     = totalprice;
        this.depositpaid    = depositpaid;
        this.bookingdates   = bookingdates;
        this.additionalneeds = additionalneeds;
    }


    public static Booking defaultBooking() {
        BookingDates dates = new BookingDates(
                ConfigManager.get("CHECKIN"),
                ConfigManager.get("CHECKOUT")
        );
        return new Booking(
                ConfigManager.get("FIRSTNAME"),
                ConfigManager.get("LASTNAME"),
                ConfigManager.getInt("TOTALPRICE"),
                ConfigManager.getBoolean("DEPOSITPAID"),
                dates,
                ConfigManager.get("ADDITIONALNEEDS")
        );
    }

    public String getFirstname()                { return firstname; }
    public void   setFirstname(String v)        { this.firstname = v; }

    public String getLastname()                 { return lastname; }
    public void   setLastname(String v)         { this.lastname = v; }

    public int    getTotalprice()               { return totalprice; }
    public void   setTotalprice(int v)          { this.totalprice = v; }

    public boolean isDepositpaid()              { return depositpaid; }
    public void    setDepositpaid(boolean v)    { this.depositpaid = v; }

    public BookingDates getBookingdates()              { return bookingdates; }
    public void         setBookingdates(BookingDates v){ this.bookingdates = v; }

    public String getAdditionalneeds()          { return additionalneeds; }
    public void   setAdditionalneeds(String v)  { this.additionalneeds = v; }
}
