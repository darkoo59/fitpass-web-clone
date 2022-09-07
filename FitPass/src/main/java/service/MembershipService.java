package service;

import dao.ExistingMembershipDAO;
import dao.IDao;
import dao.MembershipDAO;
import model.ExistingMembership;
import model.Membership;
import model.SportsFacility;
import model.TrainingHistory;
import spark.Request;
import utils.enums.RoleType;
import utils.others.RequestsUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import static main.main.gson;
import static spark.Spark.get;

public class MembershipService {

    private IDao<Membership> membershipDAO;
    private IDao<ExistingMembership> existingMembershipDAO;

    public MembershipService()
    {
        this.membershipDAO = new MembershipDAO();
        this.existingMembershipDAO = new ExistingMembershipDAO();
    }

    public ArrayList<ExistingMembership> getExistingMemberships() throws IOException {
        return existingMembershipDAO.getAll();
    }

    public ExistingMembership getActiveMembership(Request request) throws ParseException, IOException {
        String payload = RequestsUtils.getPayload(request);
        String userId = RequestsUtils.getIdFromPayload(payload);
        for(Membership membership1 : membershipDAO.getAll())
        {
            if(membership1.getCustomerId().equals(userId)){
                return existingMembershipDAO.get(membership1.getMembershipId());
            }
        }
        return null;
    }
}
