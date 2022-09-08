package service;

import dao.PromoCodesDAO;
import model.PromoCodes;
import spark.Request;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PromoCodesService {
    private PromoCodesDAO promoCodesDAO;
    public PromoCodesService(){
        this.promoCodesDAO = new PromoCodesDAO();
    }

    public ArrayList<PromoCodes> getPromoCodes() throws IOException {
        ArrayList<PromoCodes> allCodes = promoCodesDAO.getAll();
        ArrayList<PromoCodes> codesToReturn = new ArrayList<PromoCodes>();
        for(PromoCodes code : allCodes) {
            if(code.getMaxUsage() >= 1 && code.getValidUntil().isAfter(LocalDateTime.now()))
                codesToReturn.add(code);
        }
        return codesToReturn;
    }

    public void getDecrementSelectedPromoCode(Request req) throws IOException {
        ArrayList<PromoCodes> allCodes = promoCodesDAO.getAll();
        for(PromoCodes code : allCodes) {
            if(code.getId().equals(req.queryParams("id"))){
                code.setMaxUsage(code.getMaxUsage() - 1);
            }
        }
        promoCodesDAO.save(allCodes);
        return;
    }
}
