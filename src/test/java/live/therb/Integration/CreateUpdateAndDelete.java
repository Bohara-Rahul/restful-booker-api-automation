package live.therb.Integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import live.therb.Models.Auth;
import live.therb.Models.Booking;
import live.therb.Models.BookingDates;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateUpdateAndDelete {

     RequestSpecification requestSpecification = given().baseUri("https://restful-booker.herokuapp.com");
     Response response;
     ValidatableResponse validatableResponse;
     String token;
     Integer booking_id;

     @Test(priority = 1)
    public void getToken() {

         Auth auth = new Auth("admin", "password123");

         requestSpecification.basePath("/auth");
         requestSpecification.contentType(ContentType.JSON);
         requestSpecification.body(auth);

         response = requestSpecification.post();

         validatableResponse = response.then();

         token = validatableResponse
                    .statusCode(200)
                    .extract()
                    .body()
                    .path("token");
         assertThat(token, notNullValue());
         assertThat(token.length(), equalTo(15));
     }

     @Test(priority = 2)
     public void createBooking() {

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
         requestSpecification.cookie("cookie", "token="+token);
         requestSpecification.body(booking).log().all();

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

         booking_id = validatableResponse.extract().body().path("bookingid");
     }

     @Test(priority = 3)
     public void updateBooking() {
         Booking updatedBooking = new Booking();
         updatedBooking.setFirstname("John");
         updatedBooking.setLastname("Doe");

         requestSpecification.basePath("/booking/"+booking_id);
         requestSpecification.contentType(ContentType.JSON);
         requestSpecification.cookie("cookie", "token="+token);
//         requestSpecification.auth().preemptive().basic("admin", "password123");
         requestSpecification.body(updatedBooking);

         response = requestSpecification.when().put();

         validatableResponse = response.then();

         validatableResponse
                 .statusCode(200)
                 .body("firstname", equalTo("John"),
                         "lastname", equalTo("Doe"),
                         "totalprice", equalTo(111),
                         "depositpaid", equalTo(true),
                         "bookingdates.checkin", equalTo("2018-01-01"),
                         "bookingdates.checkout", equalTo("2019-01-01"),
                         "additionalneeds", equalTo("breakfast")
                 );

     }

     @Test(priority = 4)
    public void deleteBooking() {
        requestSpecification.basePath("/booking/"+booking_id);
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.cookie("cookie", "token"+token);

        validatableResponse = requestSpecification
                .delete()
                .then()
                .statusCode(201);
     }

}
