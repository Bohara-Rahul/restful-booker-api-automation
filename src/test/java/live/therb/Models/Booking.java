package live.therb.Models;

import java.util.List;

public class Booking {
    private int id;
    private String firstname;
    private String lastname;
    private int totalprice;
    private Boolean depositpaid;
    private String additionalneeds;

    private BookingDates bookingDates;

    public Booking() {}

    public Booking(int id) {
        setId(id);
    }

    public Booking(String firstname, String lastname, Integer totalprice, Boolean depositpaid, String additionalneeds) {
        setFirstname(firstname);
        setLastname(lastname);
        setTotalprice(totalprice);
        setDepositpaid(depositpaid);
        setAdditionalneeds(additionalneeds);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Integer totalprice) {
        this.totalprice = totalprice;
    }

    public Boolean getDepositpaid() {
        return depositpaid;
    }

    public void setDepositpaid(Boolean depositpaid) {
        this.depositpaid = depositpaid;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }

    public BookingDates getBookingDates() {
        return bookingDates;
    }

    public void setBookingDates(BookingDates bookingDates) {
        this.bookingDates = bookingDates;
    }

}
