package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dao.*;
import model.*;
import org.threeten.extra.Days;
import spark.Request;
import utils.enums.CustomerTypeName;
import utils.enums.MembershipStatus;
import utils.enums.RoleType;
import utils.others.RequestsUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static main.main.gson;
import static spark.Spark.get;

public class MembershipService {

    private MembershipDAO membershipDAO;
    private IDao<ExistingMembership> existingMembershipDAO;
    private ObjectMapper mapper;
    private TrainingService trainingService;
    private IUserDAO userDAO;
    private CustomerDAO customerDAO;

    public MembershipService() {
        userDAO = new UserDAO();
        customerDAO = new CustomerDAO();
        this.membershipDAO = new MembershipDAO();
        this.existingMembershipDAO = new ExistingMembershipDAO();
        this.trainingService = new TrainingService();
        mapper = new ObjectMapper();
    }

    public ArrayList<ExistingMembership> getExistingMemberships() throws IOException {
        return existingMembershipDAO.getAll();
    }

    public ExistingMembership getActiveMembership(Request request) throws ParseException, IOException {
        String payload = RequestsUtils.getPayload(request);
        String userId = RequestsUtils.getIdFromPayload(payload);
        for (Membership membership1 : membershipDAO.getAll()) {
            System.out.println("UserId : " + userId + ",MemStatus : " + membership1.getStatus());
            if (membership1.getCustomerId().equals(userId) && membership1.getStatus() == MembershipStatus.ACTIVE) {
                if(isMembershipStillActive(membership1))
                    return existingMembershipDAO.get(membership1.getMembershipId());
            }
        }
        return null;
    }

    public boolean isMembershipStillActive(Membership membership) throws IOException {
        if(membership.getValidityDateTime().isBefore(LocalDateTime.now())){
            ArrayList<Membership> allMemberships = membershipDAO.getAll();
            for(Membership mem : allMemberships)
            {
                if(mem.getId().equals(membership.getId()))
                    mem.setStatus(MembershipStatus.NOT_ACTIVE);
            }
            membershipDAO.save(allMemberships);
            int usedTerms = trainingService.getUsedTermsInInterval(membership.getCustomerId(),
                    membership.getPaymentDate().toLocalDate(),membership.getValidityDateTime().toLocalDate());
            Customer customer = customerDAO.get(membership.getCustomerId());
            double price = existingMembershipDAO.get(membership.getMembershipId()).getPrice();
            double totalMembershipPrice = (price - (membership.getPriceDiscountPercentage()/100)*price);
            double pointsGain = (double)totalMembershipPrice/1000 * usedTerms;
            customer.setCollectedPoints(customer.getCollectedPoints() + pointsGain);
            ExistingMembership existMem = existingMembershipDAO.get(membership.getMembershipId());
            if(!existMem.getDailyTerms().equals("neograniceno")) {
                int maxTerms = Integer.parseInt(existMem.getDailyTerms()) * (int)validityDaysNum(membership);
                if((((double)usedTerms) / maxTerms) < 0.33)
                {
                    double pointsLost = ((double)totalMembershipPrice/1000)*133*4;
                    customer.setCollectedPoints(customer.getCollectedPoints() - pointsLost);
                }
            }
            if(customer.getType().getType() == CustomerTypeName.BRONZE && customer.getCollectedPoints() > customer.getType().getPoints()){
                customer.setType(new CustomerType(CustomerTypeName.SILVER,3.0,4000.0));
            }else if(customer.getType().getType() == CustomerTypeName.SILVER && customer.getCollectedPoints() > customer.getType().getPoints())
                customer.setType(new CustomerType(CustomerTypeName.GOLD,5.0,5000.0));
            ArrayList<Customer> customers = customerDAO.getAll();
            for(Customer customerr : customers)
            {
                if(customerr.getId().equals(customer.getId())) {
                    customerr.setCollectedPoints(customer.getCollectedPoints());
                    customerr.setType(customer.getType());
                }
            }
            customerDAO.save(customers);
            return false;
        }
        return true;
    }

    long validityDaysNum(Membership membership) {
        LocalDate from = membership.getPaymentDate().toLocalDate();
        LocalDate to = membership.getValidityDateTime().toLocalDate();
        return ChronoUnit.DAYS.between(from, to);
    }

    public void postCreateMembership(Request request) throws IOException, ParseException {
        Membership membership = new Membership();
        mapper.registerModule(new JavaTimeModule());
        membership = mapper.readValue(request.body(), Membership.class);
        System.out.println("MemId=" + membership.getMembershipId() + ",CustId=" + membership.getCustomerId() + ",Id=" + membership.getId() +
                ",Status=" + membership.getStatus() + ",PayDat=" + membership.getPaymentDate() + ",ValdAT=" + membership.getValidityDateTime());
        membership.setCustomerId(RequestsUtils.getIdFromPayload(RequestsUtils.getPayload(request)));
        membership.setId(membershipDAO.getNewId());
        membership.setStatus(MembershipStatus.ACTIVE);
        membershipDAO.setNotActiveMembership(membership.getCustomerId());
        ArrayList<Membership> allMemberships = membershipDAO.getAll();
        allMemberships.add(membership);
        membershipDAO.save(allMemberships);

    }

    public ExistingMembership getExistingMembershipById(Request req) throws IOException {
        String id = req.queryParams("id");
        System.out.println("Id:" + id);
        return existingMembershipDAO.get(id);
    }

    public String getTodayTermsNum(Request req) throws IOException, ParseException {
        String payload = RequestsUtils.getPayload(req);
        String userId = RequestsUtils.getIdFromPayload(payload);
        ExistingMembership existingMembership = getMembershipProgramByCustomerId(userId);
        String maxTerms = existingMembership.getDailyTerms();
        if(maxTerms.equals("neograniceno"))
            return maxTerms;
        else{
            int maxTermsToday = Integer.parseInt(maxTerms);
            maxTermsToday = maxTermsToday - trainingService.getTodayTrainingsNum(userId);
            return String.valueOf(maxTermsToday);
        }

    }

    public ExistingMembership getMembershipProgramByCustomerId(String customerId) throws IOException {
        for (Membership mem : membershipDAO.getAll()) {
            if (mem.getCustomerId().equals(customerId) && mem.getStatus() == MembershipStatus.ACTIVE) {
                for (ExistingMembership exMembership : existingMembershipDAO.getAll()) {
                    if (exMembership.getId().equals(mem.getMembershipId()))
                        return exMembership;
                }
            }
        }
        return null;
    }

    public Membership getActiveMembershipByCustomerId(Request req) throws ParseException, IOException {
        String payload = RequestsUtils.getPayload(req);
        String userId = RequestsUtils.getIdFromPayload(payload);
        for (Membership mem : membershipDAO.getAll()) {
            if (mem.getCustomerId().equals(userId) && mem.getStatus() == MembershipStatus.ACTIVE) {
                return mem;
            }
        }
        return null;
    }
}
