package dto;

import utils.enums.CommentStatus;
import utils.enums.Rating;

public class CommentDTO {
    public String id;
    public String username;
    public String text;
    public Rating rating;
    public CommentStatus status;
}
