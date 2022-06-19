package service;

import dao.IDao;
import dao.SportsFacilityDAO;
import model.SportsFacility;

import java.io.IOException;
import java.util.ArrayList;

public class SportsFacilityService {
    private IDao<SportsFacility> facilitiesDao;
    public SportsFacilityService()
    {
        facilitiesDao = new SportsFacilityDAO();
    }

    public ArrayList<SportsFacility> getAll() throws IOException {
        return facilitiesDao.getAll();
    }
}
