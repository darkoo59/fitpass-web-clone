package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.PromoCodesDAO;
import model.Credentials;
import model.PromoCodes;
import spark.Request;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PromoCodesService {
    private PromoCodesDAO promoCodesDAO;
    private ObjectMapper mapper;
    public PromoCodesService(){
        this.promoCodesDAO = new PromoCodesDAO();
        mapper = new ObjectMapper();
    }

    public String addPromoCode(Request req) throws IOException {
        String code = req.queryParams("code");
        int maxUsage = Integer.parseInt(req.queryParams("maxUsage"));
        //LocalDateTime dateTime =
        double discountPercentage = Double.parseDouble(req.queryParams("maxUsage"));
        String[] dateSplited = req.queryParams("validUntil").split("T");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateSplited[0] + " " + dateSplited[1], formatter);
        PromoCodes newCode = new PromoCodes(code,dateTime,maxUsage,discountPercentage);
        newCode.setId(promoCodesDAO.getNewId());
        ArrayList<PromoCodes> allCodes = promoCodesDAO.getAll();
        for(PromoCodes codeIte : allCodes){
            if(codeIte.getCode().equals(code))
                return "1";
        }
        allCodes.add(newCode);
        promoCodesDAO.save(allCodes);
        return "0";
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
