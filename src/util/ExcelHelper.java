package util;

import dto.NhanVienDTO;
import dto.SachDTO; // Đã thêm Import
import java.io.*;
import java.util.ArrayList;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

    // ================= XUẤT EXCEL =================
    public static void exportExcel(JTable table, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Dữ liệu");

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < table.getColumnCount(); i++) {
            headerRow.createCell(i).setCellValue(table.getColumnName(i));
        }

        for (int i = 0; i < table.getRowCount(); i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < table.getColumnCount(); j++) {
                Object val = table.getValueAt(i, j);
                if (val != null) row.createCell(j).setCellValue(val.toString());
            }
        }

        FileOutputStream out = new FileOutputStream(filePath);
        workbook.write(out);
        workbook.close();
        out.close();
    }

    // ================= NHẬP NHÂN VIÊN =================
    public static ArrayList<NhanVienDTO> importNhanVien(String filePath) throws IOException {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        FileInputStream file = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            NhanVienDTO nv = new NhanVienDTO();
            nv.setMaNV(getSafeString(row, 0));
            nv.setHoDem(getSafeString(row, 1));
            nv.setTen(getSafeString(row, 2));
            // Các trường khác tương tự...
            
            list.add(nv);
        }
        workbook.close();
        file.close();
        return list;
    }

    // ================= NHẬP SÁCH =================
    public static ArrayList<SachDTO> importSach(String filePath) throws Exception {
        ArrayList<SachDTO> list = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        
        // Bỏ qua dòng tiêu đề (i = 1)
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                // Kiểm tra xem dòng này có phải dòng trống hoàn toàn không
                String maSachCheck = getSafeString(row, 0);
                if (maSachCheck.isEmpty()) continue; 

                SachDTO s = new SachDTO();
                s.setMaSach(maSachCheck);
                s.setTenSach(getSafeString(row, 1));
                
                // Ép kiểu an toàn, nếu ô trống thì mặc định là 0
                s.setNamXB(parseInteger(getSafeString(row, 2)));
                s.setMaTL(getSafeString(row, 3));
                s.setDonGia(parseDouble(getSafeString(row, 4)));
                s.setSoLuong(parseInteger(getSafeString(row, 5)));
                s.setMaTG(getSafeString(row, 6));
                s.setMaNXB(getSafeString(row, 7));
                s.setSoTrang(parseInteger(getSafeString(row, 8)));
                
                list.add(s);
            }
        }
        workbook.close();
        fis.close();
        return list;
    }

    // ================= HÀM BẢO VỆ DỮ LIỆU ĐỌC TỪ EXCEL =================
    
    // Hàm này chống lỗi đọc cell (Dù người dùng nhập chữ hay số vào ô Excel)
    private static String getSafeString(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return "";
        
        CellType type = cell.getCellType();
        if (type == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (type == CellType.NUMERIC) {
            double val = cell.getNumericCellValue();
            // Nếu là số nguyên (VD: 2023.0) thì chuyển thành "2023" thay vì "2023.0"
            if (val == Math.floor(val)) {
                return String.valueOf((long) val);
            }
            return String.valueOf(val);
        }
        return cell.toString().trim();
    }

    // Hàm an toàn khi parse Int (Chống lỗi trống)
    private static int parseInteger(String str) {
        if (str == null || str.isEmpty()) return 0;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) { return 0; }
    }

    // Hàm an toàn khi parse Double (Chống lỗi trống)
    private static double parseDouble(String str) {
        if (str == null || str.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) { return 0.0; }
    }
}