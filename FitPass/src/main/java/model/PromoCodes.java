package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PromoCodes {
    private String id;
    private String code;
    private LocalDateTime validUntil;
    private int maxUsage;

    private double discountPercentage;

    public PromoCodes(String code,LocalDateTime validUntil,int maxUsage,Double discount)
    {
        this.code = code;
        this.validUntil = validUntil;
        this.maxUsage = maxUsage;
        this.discountPercentage = discount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public int getMaxUsage() {
        return maxUsage;
    }

    public void setMaxUsage(int maxUsage) {
        this.maxUsage = maxUsage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

}
