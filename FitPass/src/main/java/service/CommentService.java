package service;

import dao.CommentDAO;
import dao.IDAO;
import model.Comment;
import spark.Request;
import utils.enums.CommentStatus;

import java.util.ArrayList;

public class CommentService {
    private CommentDAO commentDAO;

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

    public void addCommentToList(Comment comment) throws Exception {
        ArrayList<Comment> comments = getAllComments();
        comments.add(comment);
        commentDAO.save(comments);
    }

    public void deleteComment(Request req) throws Exception {
        String id = req.params(":id");
        ArrayList<Comment> comments = commentDAO.getAllAndDeleted();
        for (Comment comment : comments) {
            if (comment.getId().equals(id)) {
                comment.setDeleted(true);
            }
        }
        commentDAO.save(comments);
    }

    public void acceptComment(Request req) throws Exception {
        String id = req.params(":id");
        ArrayList<Comment> comments = commentDAO.getAll();
        for (Comment comment : comments) {
            if (comment.getId().equals(id)) {
                comment.setStatus(CommentStatus.ACCEPTED);
            }
        }
        commentDAO.save(comments);
    }

    public void rejectComment(Request req) throws Exception {
        String id = req.params(":id");
        ArrayList<Comment> comments = commentDAO.getAll();
        for (Comment comment : comments) {
            if (comment.getId().equals(id)) {
                comment.setStatus(CommentStatus.REJECTED);
            }
        }
        commentDAO.save(comments);
    }
}
