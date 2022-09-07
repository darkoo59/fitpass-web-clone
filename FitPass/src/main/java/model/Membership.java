package model;

import utils.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Membership {
    private java.lang.String id;
    private String membershipId;
    private LocalDateTime paymentDate;
    private LocalDateTime validityDateTime;
    private String customerId;
    private MembershipStatus status;

    private Double priceDiscountPercentage;

    public Membership()
    {
        this.priceDiscountPercentage = 0.0;
    }
    public Membership(String membershipId, LocalDateTime paymentDate, LocalDateTime validityDateTime,
                      String customerId, MembershipStatus status) {
        this.membershipId= membershipId;
        this.paymentDate = paymentDate;
        this.validityDateTime = validityDateTime;
        this.customerId = customerId;
        this.status = status;
        this.priceDiscountPercentage = 0.0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDateTime getValidityDateTime() {
        return validityDateTime;
    }

    public void setValidityDateTime(LocalDateTime validityDateTime) {
        this.validityDateTime = validityDateTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public Double getPriceDiscountPercentage() {
        return priceDiscountPercentage;
    }

    public void setPriceDiscountPercentage(Double priceDiscountPercentage) {
        this.priceDiscountPercentage = priceDiscountPercentage;
    }
}
