package utils.others;

import java.time.LocalDate;
import java.util.Date;

public class Filter {
    public String searchInput;
    public String location;
    public String type;
    public String rating;
    public String sort;
    public String facilityType;
    public String trainingType;

    public String price;
//    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//    Date parsed = format.parse(date);
//    LocalDate localDateInFinalObject = LocalDate.parse(DateAsStringFromDTOObject)
    public LocalDate fromApplicationDate;
    public LocalDate toApplicationDate;
}
