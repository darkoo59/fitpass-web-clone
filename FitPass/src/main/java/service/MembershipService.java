package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dao.ExistingMembershipDAO;
import dao.IDao;
import dao.MembershipDAO;
import model.*;
import spark.Request;
import utils.enums.MembershipStatus;
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
    private ObjectMapper mapper;

    public MembershipService()
    {
        this.membershipDAO = new MembershipDAO();
        this.existingMembershipDAO = new ExistingMembershipDAO();
        mapper = new ObjectMapper();
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

    public void postCreateMembership(Request request) throws IOException, ParseException {
        Membership membership = new Membership();
        mapper.registerModule(new JavaTimeModule());
        membership = mapper.readValue(request.body(),Membership.class);
        System.out.println("MemId="+membership.getMembershipId()+",CustId="+membership.getCustomerId()+",Id="+membership.getId()+
                ",Status="+membership.getStatus()+",PayDat="+membership.getPaymentDate()+",ValdAT="+membership.getValidityDateTime());
        membership.setCustomerId(RequestsUtils.getIdFromPayload(RequestsUtils.getPayload(request)));
        membership.setId(membershipDAO.getNewId());
        membership.setStatus(MembershipStatus.ACTIVE);
        ArrayList<Membership> allMemberships = membershipDAO.getAll();
        allMemberships.add(membership);
        membershipDAO.save(allMemberships);

    }
}
