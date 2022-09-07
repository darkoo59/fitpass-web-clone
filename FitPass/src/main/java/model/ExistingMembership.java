package model;

public class ExistingMembership {

    private String id;
    private String name;
    private String membershipType;
    private int price;
    private String dailyTerms;

    public ExistingMembership(String name, String membershipType, int price, String dailyTerms) {
        this.name = name;
        this.membershipType = membershipType;
        this.price = price;
        this.dailyTerms = dailyTerms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDailyTerms() {
        return dailyTerms;
    }

    public void setDailyTerms(String dailyTerms) {
        this.dailyTerms = dailyTerms;
    }
}
