package service;

import dao.CommentDAO;
import dao.IDAO;
import model.Comment;
import utils.enums.CommentStatus;

import java.util.ArrayList;

public class CommentService {
    private IDAO<Comment> commentDAO;

    public CommentService() {
        commentDAO = new CommentDAO();
    }

    public ArrayList<Comment> getAllComments() throws Exception {
        return commentDAO.getAll();
    }

    public ArrayList<Comment> getCommentsFromFacility(String id, boolean accepted) throws Exception {
        ArrayList<Comment> allComments = getAllComments();
        ArrayList<Comment> comments = new ArrayList<>();
        for (Comment comment : allComments) {
            if (accepted) {
                if (comment.getSportsFacilityId().equals(id) && comment.getStatus() == CommentStatus.ACCEPTED) {
                    comments.add(comment);
                }
            } else {
                if (comment.getSportsFacilityId().equals(id)) {
                    comments.add(comment);
                }
            }
        }
        return comments;
    }

}
