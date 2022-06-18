package utils.enums;

import com.google.gson.annotations.SerializedName;

public enum RoleType {
    @SerializedName("0")
    ADMINISTRATOR,
    @SerializedName("1")
    MANAGER,
    @SerializedName("2")
    COACH,
    @SerializedName("3")
    CUSTOMER
}

