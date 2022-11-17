package tgpr.bank.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateInterface {
    private static LocalDateTime date;

    public static boolean isHasChanged() {
        return hasChanged;
    }

    private static boolean  hasChanged;
    public static LocalDateTime getUsedDate() {
        return date;
    }

    public static void date(LocalDateTime InputDate) {
        date = InputDate;
    }

    public static void hasChanged(boolean inputBoolean){
        hasChanged=inputBoolean;
    }

//    public static LocalDateTime dateToOtherFormat(){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
//            return dateTime;
//    }
}
