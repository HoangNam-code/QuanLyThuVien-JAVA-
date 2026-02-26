package util;

import dto.NhanVienDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat; // Import thêm cái này để xử lý ngày VN
import java.util.ArrayList;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

    // 1. Xuất file Excel từ JTable
    public static void exportExcel(JTable table, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Dữ liệu Nhân Viên");

            // Tạo style cho tiêu đề (In đậm)
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // Tạo dòng tiêu đề
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < table.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(table.getColumnName(i));
                cell.setCellStyle(headerStyle);
            }

            // Ghi dữ liệu từ JTable vào Excel
            for (int i = 0; i < table.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < table.getColumnCount(); j++) {
                    Object value = table.getValueAt(i, j);
                    Cell cell = row.createCell(j);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

            // Tự động giãn cột cho đẹp
            for (int i = 0; i < table.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Lưu file
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                workbook.write(out);
            }
        }
    }

    // 2. Nhập file Excel (Đọc dữ liệu trả về List)
    public static ArrayList<NhanVienDTO> importNhanVien(String filePath) throws IOException {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        
        // Dùng try-with-resources để tự động đóng file
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
             
            Sheet sheet = workbook.getSheetAt(0);

            // Bỏ qua dòng tiêu đề (bắt đầu từ i = 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                NhanVienDTO nv = new NhanVienDTO();
                
                // --- ĐỌC DỮ LIỆU TỪNG CỘT ---
                // Lưu ý: Thứ tự cột phải khớp với file Excel của bạn
                // 0: Mã, 1: Họ đệm, 2: Tên, 3: Ngày sinh, 4: Giới tính, 5: SĐT, 6: Email, 7: Chức vụ
                
                nv.setMaNV(getCellValue(row.getCell(0)));
                nv.setHoDem(getCellValue(row.getCell(1)));
                nv.setTen(getCellValue(row.getCell(2)));

                // --- Xử lý Ngày Sinh (Đã nâng cấp) ---
                Cell cellNgaySinh = row.getCell(3);
                if (cellNgaySinh != null) {
                    // Trường hợp 1: Excel định dạng là Date (chuẩn nhất)
                    if (cellNgaySinh.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellNgaySinh)) {
                        java.util.Date dateUtil = cellNgaySinh.getDateCellValue();
                        nv.setNgaySinh(new java.sql.Date(dateUtil.getTime()));
                    } 
                    // Trường hợp 2: Excel định dạng là Text (Ví dụ: "1999-01-01" hoặc "20/10/2000")
                    else {
                        try {
                            String strDate = getCellValue(cellNgaySinh);
                            if (!strDate.isEmpty()) {
                                // Thử format chuẩn SQL: yyyy-MM-dd
                                try {
                                    nv.setNgaySinh(Date.valueOf(strDate));
                                } catch (IllegalArgumentException e) {
                                    // Nếu lỗi, thử format Việt Nam: dd/MM/yyyy
                                    java.util.Date parsed = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
                                    nv.setNgaySinh(new java.sql.Date(parsed.getTime()));
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Lỗi ngày sinh tại dòng " + (i+1) + ": " + e.getMessage());
                            nv.setNgaySinh(null); // Để null nếu không đọc được
                        }
                    }
                }
                
                nv.setGioiTinh(getCellValue(row.getCell(4)));
                nv.setSdt(getCellValue(row.getCell(5)));
                nv.setEmail(getCellValue(row.getCell(6)));
                nv.setChucVu(getCellValue(row.getCell(7)));

                list.add(nv);
            }
        }
        return list;
    }

    // Hàm phụ trợ lấy giá trị ô Excel (Đã xử lý mọi trường hợp)
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // Nếu là ngày tháng -> trả về chuỗi ngày
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // Nếu là số thường -> ép kiểu để bỏ số thập phân vô nghĩa (vd: 10.0 -> 10)
                double val = cell.getNumericCellValue();
                if (val == (long) val) {
                    return String.valueOf((long) val);
                } else {
                    return String.valueOf(val);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Nếu ô chứa công thức, cố gắng lấy kết quả String, nếu không được thì lấy số
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
            case ERROR:
            default:
                return "";
        }
    }
}