package service;

import dao.IDAO;
import dao.SportsFacilityDAO;
import dto.CommentDTO;
import model.Comment;
import model.SportsFacility;
import utils.others.Filter;
import spark.Request;

import java.io.IOException;;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static main.main.gson;

public class SportsFacilityService {
    private IDAO<SportsFacility> facilitiesDao;
    private CommentService commentService;

    public SportsFacilityService()
    {
        facilitiesDao = new SportsFacilityDAO();
    }

    public ArrayList<SportsFacility> getAllSorted() throws IOException {
        ArrayList<SportsFacility> sortedFacilities = facilitiesDao.getAll();
        Collections.sort(sortedFacilities,new Comparator<SportsFacility>() {
            @Override
            public int compare(SportsFacility o1, SportsFacility o2) {
                /*
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
        return lhs.customInt > rhs.customInt ? -1 : (lhs.customInt < rhs.customInt) ? 1 : 0;
                 */
                if(o1.getWorkHour().getStart().compareTo(LocalTime.now()) < 0 &&
                        o1.getWorkHour().getEnd().compareTo(LocalTime.now()) > 0)
                    return -1;
                else if(o2.getWorkHour().getStart().compareTo(LocalTime.now()) < 0 &&
                        o2.getWorkHour().getEnd().compareTo(LocalTime.now()) > 0)
                    return 1;
                else
                    return 0;
            }
        });
        return sortedFacilities;
    }

    public SportsFacility getSportFacilityById(Request req) throws IOException {
        String id = req.queryParams("id");
        return facilitiesDao.get(id);
    }

    public ArrayList<SportsFacility> filter(Request req) throws Exception {
        Filter filter = gson.fromJson(req.body(), Filter.class);
        ArrayList<SportsFacility> facilities = facilitiesDao.getAll();
        ArrayList<SportsFacility> filtered = new ArrayList<>();
        for (SportsFacility facility : facilities) {
            if (name(filter, facility)) {
                if (!filtered.contains(facility)) {
                    filtered.add(facility);
                }
            }
            if (!location(filter, facility)) {
                if (filtered.contains(facility)) {
                    filtered.remove(facility);
                }
            }
            if (!type(filter, facility)) {
                if (filtered.contains(facility)) {
                    filtered.remove(facility);
                }
            }
            if (!rating(filter, facility)) {
                if (filtered.contains(facility)) {
                    filtered.remove(facility);
                }
            }
        }
        return sort(filter, filtered);
    }

    private Boolean name(Filter filter, SportsFacility facility) {
        return facility.getName().toLowerCase().contains(filter.searchInput.toLowerCase());
    }

    private Boolean location(Filter filter, SportsFacility facility) {
        if (filter.location.equals("Location"))
            return true;
        return facility.getLocation().getAddress().getPlace().equals(filter.location);
    }

    private Boolean type(Filter filter, SportsFacility facility) {
        if (filter.type.equals("Type"))
            return true;
        return facility.getType().equals(filter.type);
    }

    private Boolean rating(Filter filter, SportsFacility facility) {
        switch (filter.rating) {
            case "1":
                if (4.5 <= facility.getAverageRating() && facility.getAverageRating() <= 5.0)
                    return true;
                break;
            case "2":
                if (4.0 <= facility.getAverageRating() && facility.getAverageRating() <= 4.5)
                    return true;
                break;
            case "3":
                if (3.5 <= facility.getAverageRating() && facility.getAverageRating() <= 4.0)
                    return true;
                break;
            case "4":
                if (3.0 <= facility.getAverageRating() && facility.getAverageRating() <= 3.5)
                    return true;
                break;
            case "5":
                if (facility.getAverageRating() <= 3.0)
                    return true;
                break;
            default:
                return true;
        }
        return false;
    }

    private ArrayList<SportsFacility> sort(Filter filter, ArrayList<SportsFacility> filtered) {
        switch (filter.sort) {
            case "1":
                Collections.sort(filtered, Comparator.comparing(SportsFacility::getName));
                break;
            case "2":
                Collections.sort(filtered, Comparator.comparing(SportsFacility::getName, Comparator.reverseOrder()));
                break;
            case "3":
                Collections.sort(filtered, Comparator.comparing(SportsFacility::getAverageRating));
                break;
            case "4":
                Collections.sort(filtered, Comparator.comparing(SportsFacility::getAverageRating, Comparator.reverseOrder()));
                break;
            default:
                return filtered;
        }
        return filtered;
    }
    public ArrayList<CommentDTO> getSportFacilityComments(Request req) throws Exception {
        String id = req.params(":id");
        ArrayList<Comment> comments;
        commentService = new CommentService();
        if (req.body().equals("ADMINISTRATOR") || req.body().equals("MANAGER")) {
            comments = commentService.getCommentsFromFacility(id, false);
        } else {
            comments = commentService.getCommentsFromFacility(id, true);
        }
        ArrayList<CommentDTO> commentsDTO = new ArrayList<>();
        commentService = null;
        mapCommentsToDTO(comments, commentsDTO);
        return commentsDTO;
    }

    private void mapCommentsToDTO(ArrayList<Comment> comments, ArrayList<CommentDTO> commentsDTO) throws Exception {
        UserService userService = new UserService();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.id = comment.getId();
            commentDTO.username = userService.getUsernameById(comment.getCustomerId());
            commentDTO.status = comment.getStatus();
            commentDTO.text = comment.getText();
            commentDTO.rating = comment.getRating();
            commentsDTO.add(commentDTO);
        }
    }
}
