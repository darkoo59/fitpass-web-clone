package model;

import utils.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Membership {
    private String id;
    private String membershipType;
    private LocalDate paymentDate;
    private LocalDateTime validityDateTime;
    private Double price;
    private Customer customer;
    private MembershipStatus status;
    private String dailyTerms;

    public Membership(String id, String membershipType, LocalDate paymentDate, LocalDateTime validityDateTime,
                      Double price, Customer customer, MembershipStatus status, String dailyTerms) {
        this.id = id;
        this.membershipType = membershipType;
        this.paymentDate = paymentDate;
        this.validityDateTime = validityDateTime;
        this.price = price;
        this.customer = customer;
        this.status = status;
        this.dailyTerms = dailyTerms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDateTime getValidityDateTime() {
        return validityDateTime;
    }

    public void setValidityDateTime(LocalDateTime validityDateTime) {
        this.validityDateTime = validityDateTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public String getDailyTerms() {
        return dailyTerms;
    }

    public void setDailyTerms(String dailyTerms) {
        this.dailyTerms = dailyTerms;
    }
}
