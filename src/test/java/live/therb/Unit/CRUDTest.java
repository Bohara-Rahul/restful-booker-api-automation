package live.therb.Unit;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import live.therb.Models.Auth;
import live.therb.Models.Booking;
import live.therb.Models.BookingDates;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CRUDTest {

    RequestSpecification requestSpecification = given().baseUri("https://restful-booker.herokuapp.com");
    ValidatableResponse validatableResponse;

    @Test
    public void get_bookings() {
        requestSpecification.basePath("/booking");
        validatableResponse = requestSpecification.get().then();
        validatableResponse
                .assertThat()
                    .statusCode(200)
                    .body("bookingid", notNullValue()).log().all();
    }

    @Test
    public void get_single_booking() {
        Booking booking = new Booking(1224);

        requestSpecification.basePath("/booking/"+booking.getId());
        validatableResponse = requestSpecification.get().then();
        validatableResponse
                .assertThat()
                .statusCode(200)
                .body("firstname", equalTo("Josh"),
                        "lastname", equalTo("Allen"),
                        "totalprice", equalTo(111),
                        "depositpaid", equalTo(true),
                        "bookingdates.checkin", equalTo("2018-01-01"),
                        "bookingdates.checkout", equalTo("2019-01-01"),
                        "additionalneeds", equalTo("super bowls")
                );
    }

    @Test
    public void create_token() {
        var auth = new Auth("admin", "password123");

        requestSpecification.basePath("/auth");
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.body(auth);

        validatableResponse = requestSpecification.post().then();
        validatableResponse
                .assertThat()
                    .statusCode(200)
                    .body("token", notNullValue());
    }

    @Test
    public void create_booking() {
        Booking booking = new Booking();
        booking.setFirstname("James");
        booking.setLastname("Doe");
        booking.setTotalprice(111);
        booking.setDepositpaid(true);
        booking.setAdditionalneeds("breakfast");

        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin("2018-01-01");
        bookingDates.setCheckout("2019-01-01");

        booking.setBookingDates(bookingDates);

        requestSpecification.basePath("/booking");
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.auth().preemptive().basic("admin", "password123");
        requestSpecification.body(booking);

        validatableResponse = requestSpecification.post().then();

        validatableResponse
                .statusCode(200)
                .body("firstname", equalTo("James"),
                        "lastname", equalTo("Brown"),
                        "totalprice", equalTo(111),
                        "depositpaid", equalTo(true),
                        "bookingdates.checkin", equalTo("2018-01-01"),
                        "bookingdates.checkout", equalTo("2019-01-01"),
                        "additionalneeds", equalTo("breakfast")
                );

    }

    @Test
    public void partial_update_booking() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setFirstname("John");
        booking.setLastname("Doe");

        requestSpecification.basePath("/booking/"+booking.getId());
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.auth().preemptive().basic("admin", "password123");
        requestSpecification.body(booking);

        validatableResponse = requestSpecification.put().then();

        validatableResponse
                .statusCode(200)
                .body("firstname", equalTo("John"),
                        "lastname", equalTo("Doe")
                );
    }
}
