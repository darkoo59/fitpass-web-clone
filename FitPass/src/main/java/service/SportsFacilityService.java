package service;

import breeze.numerics.constants.Database;
import dao.IDao;
import dao.SportsFacilityDAO;
import model.SportsFacility;
import spark.Request;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class SportsFacilityService {
    private IDao<SportsFacility> facilitiesDao;
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
                System.out.println("DARKOOO");
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
        System.out.println("TEST : "+id);
        return facilitiesDao.get(id);
    }
}
