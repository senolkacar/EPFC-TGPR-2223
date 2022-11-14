package tgpr.bank.model;

public abstract class DateInterface {
    private static String date;
    public static String getUsedDate() {
        return date;
    }

    public static void date(String InputDate) {
        date = InputDate;
    }
}
