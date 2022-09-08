package utils.enums;

import com.google.gson.annotations.SerializedName;

public enum CommentStatus {
    @SerializedName("0")
    PENDING,
    @SerializedName("1")
    ACCEPTED,
    @SerializedName("2")
    REJECTED
}
