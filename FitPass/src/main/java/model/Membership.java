package model;

import utils.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Membership {
    private java.lang.String id;
    private String membershipId;
    private LocalDate paymentDate;
    private LocalDateTime validityDateTime;
    private String customerId;
    private MembershipStatus status;

    public Membership(String membershipId, LocalDate paymentDate, LocalDateTime validityDateTime,
                      String customerId, MembershipStatus status) {
        this.membershipId= membershipId;
        this.paymentDate = paymentDate;
        this.validityDateTime = validityDateTime;
        this.customerId = customerId;
        this.status = status;
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
}
