package service;

import dao.IDao;
import dao.SportsFacilityDAO;
import model.SportsFacility;
import utils.others.Filter;
import spark.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static main.main.gson;

public class SportsFacilityService {
    private IDao<SportsFacility> facilitiesDao;
    public SportsFacilityService()
    {
        facilitiesDao = new SportsFacilityDAO();
    }

    public ArrayList<SportsFacility> getAll() throws IOException {
        return facilitiesDao.getAll();
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
            if (rating(filter, facility)) {
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
                    return false;
                break;
            case "2":
                if (4.0 <= facility.getAverageRating() && facility.getAverageRating() <= 4.5)
                    return false;
                break;
            case "3":
                if (3.5 <= facility.getAverageRating() && facility.getAverageRating() <= 4.0)
                    return false;
                break;
            case "4":
                if (3.0 <= facility.getAverageRating() && facility.getAverageRating() <= 3.5)
                    return false;
                break;
            case "5":
                if (facility.getAverageRating() <= 3.0)
                    return false;
                break;
            default:
                return false;
        }
        return true;
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
}
