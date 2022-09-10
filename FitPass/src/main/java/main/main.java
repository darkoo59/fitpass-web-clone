package main;

import com.google.gson.Gson;
import controller.*;
import model.Manager;
import model.PromoCodes;
import service.*;
import utils.others.CustomGsonBuilder;

import java.io.IOException;

import static spark.Spark.*;

public class main {

    public static Gson gson;
    private static CustomGsonBuilder customGsonBuilder;
    private static AccountService accountService;
    private static SportsFacilityService facilitiesService;
    private static CustomerService customerService;
    private static AdministratorService administratorService;
    private static CoachService coachService;
    private static ManagerService managerService;
    private static MembershipService membershipService;
    private static PromoCodesService promoCodesService;
    private static CommentService commentService;
    private static ContentService contentService;

    public static void main(String[] args) throws Exception {

        initializeContext();
        gson = customGsonBuilder.getCustomGsonBuilder().create();
        staticFiles.location("/static/vue/dist");
        port(8081);

        CORSController.enableCORS();

        AccountController.postRegister();
        AccountController.postLogin();
        AccountController.getUserRole();
        AccountController.getUserInfo();
        AccountController.postUserInfoEdit();
        AccountController.getUsername();
        AccountController.getUserId();
        AccountController.getFilteredProfiles();
        AccountController.putChangePassword();

        AdministratorController.postRegister();
        AdministratorController.getAllProfiles();
        AdministratorController.getManagersForNewFacility();
        AdministratorController.getSearchedProfiles();
        AdministratorController.postCreateNewFacility();
        AdministratorController.postCreateNewFacilityLogo();

        SportsFacilityController.getSportsFacilites();
        SportsFacilityController.getSportFacility();
        SportsFacilityController.postSportsFacilitesFilter();
        SportsFacilityController.postSportFacilityComments();
        SportsFacilityController.postSportFacilityAddComment();
        SportsFacilityController.getSportFacilityAddCommentAllowed();

        CustomerController.getAllTrainings();
        CustomerController.getMyTrainingsHistory();
        CustomerController.getFacilityByTrainingId();
        CustomerController.postTrainingsFilter();
        CustomerController.getAddTrainingHistory();
        CustomerController.getCustomer();
        CustomerController.getAllCustomers();

        CoachController.getMyTrainingsHistory();
        CoachController.getAllTrainings();
        CoachController.getCancelPersonalTraining();
        CoachController.postTrainingsFilter();
        CoachController.getAllCoaches();

        ManagerController.getMyTrainingsHistory();
        ManagerController.getAllTrainings();
        ManagerController.getManagerFacilities();
        ManagerController.postTrainingsFilter();
        ManagerController.postAddNewTraining();

        MembershipController.getExistingMemberships();
        MembershipController.getActiveMembership();
        MembershipController.postCreateMembership();
        MembershipController.getExistingMembershipById();
        MembershipController.getTodayTermsNum();
        MembershipController.getMembershipByCustomerId();

        PromoCodesController.getPromoCodes();
        PromoCodesController.getDecrementSelectedPromoCode();
        PromoCodesController.postAddPromoCode();

        CommentController.getAllComments();
        CommentController.deleteComment();
        CommentController.putAcceptComment();
        CommentController.putRejectComment();

        ContentController.postCreateNewContentImage();
        ContentController.postAddNewContent();
    }

    private static void initializeContext() throws IOException {
        accountService = new AccountService();
        AccountController.initializeService(accountService);
        facilitiesService = new SportsFacilityService();
        SportsFacilityController.initializeService(facilitiesService);
        customGsonBuilder = new CustomGsonBuilder();
        administratorService = new AdministratorService();
        AdministratorController.initializeService(administratorService);
        customerService = new CustomerService();
        CustomerController.initializeService(customerService);
        coachService = new CoachService();
        CoachController.initializeService(coachService);
        managerService = new ManagerService();
        ManagerController.initializeService(managerService);
        membershipService = new MembershipService();
        MembershipController.initializeService(membershipService);
        promoCodesService = new PromoCodesService();
        PromoCodesController.initializeService(promoCodesService);
        commentService = new CommentService();
        CommentController.initializeService(commentService);
        contentService = new ContentService();
        ContentController.initializeService(contentService);
    }
}
