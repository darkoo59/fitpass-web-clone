package service;

import dao.PromoCodesDAO;
import model.PromoCodes;

import java.io.IOException;
import java.util.ArrayList;

public class PromoCodesService {
    private PromoCodesDAO promoCodesDAO;
    public PromoCodesService(){
        this.promoCodesDAO = new PromoCodesDAO();
    }

    public ArrayList<PromoCodes> getPromoCodes() throws IOException {
        return promoCodesDAO.getAll();
    }
}
