package util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
    // 1. Định dạng tiền tệ (Ví dụ: 100000 -> 100,000 VNĐ)
    public static String FormatVND(double tien) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(tien) + " VNĐ";
    }

    // 2. Định dạng ngày tháng (Ví dụ: Date -> 24/02/2026)
    public static String FormatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}