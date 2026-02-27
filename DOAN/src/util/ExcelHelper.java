package util;

import dto.NhanVienDTO;
import dto.SachDTO; // Thêm thư viện này để dùng SachDTO
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

    // 1. Xuất file Excel từ JTable (Dùng chung cho cả Sách và Nhân Viên)
    public static void exportExcel(JTable table, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Dữ liệu");

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

    // 2. Nhập file Excel (Đọc dữ liệu Nhân Viên)
    public static ArrayList<NhanVienDTO> importNhanVien(String filePath) throws IOException {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
             
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(getCellValue(row.getCell(0)));
                nv.setHoDem(getCellValue(row.getCell(1)));
                nv.setTen(getCellValue(row.getCell(2)));

                Cell cellNgaySinh = row.getCell(3);
                if (cellNgaySinh != null) {
                    if (cellNgaySinh.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellNgaySinh)) {
                        java.util.Date dateUtil = cellNgaySinh.getDateCellValue();
                        nv.setNgaySinh(new java.sql.Date(dateUtil.getTime()));
                    } else {
                        try {
                            String strDate = getCellValue(cellNgaySinh);
                            if (!strDate.isEmpty()) {
                                try {
                                    nv.setNgaySinh(Date.valueOf(strDate));
                                } catch (IllegalArgumentException e) {
                                    java.util.Date parsed = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
                                    nv.setNgaySinh(new java.sql.Date(parsed.getTime()));
                                }
                            }
                        } catch (Exception e) {
                            nv.setNgaySinh(null); 
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

    // 3. Nhập file Excel (Đọc dữ liệu Sách trả về List) - PHẦN THÊM MỚI
    public static ArrayList<SachDTO> importSach(String filePath) throws IOException {
        ArrayList<SachDTO> list = new ArrayList<>();
        
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
             
            Sheet sheet = workbook.getSheetAt(0);

            // Bỏ qua dòng tiêu đề (bắt đầu từ i = 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                SachDTO s = new SachDTO();
                
                s.setMaSach(getCellValue(row.getCell(0)));
                s.setTenSach(getCellValue(row.getCell(1)));
                s.setMaTG(getCellValue(row.getCell(2)));
                s.setMaTL(getCellValue(row.getCell(3)));
                s.setMaNXB(getCellValue(row.getCell(4)));

                try { s.setNamXB(Integer.parseInt(getCellValue(row.getCell(5)))); } catch (Exception e) { s.setNamXB(0); }
                try { s.setSoTrang(Integer.parseInt(getCellValue(row.getCell(6)))); } catch (Exception e) { s.setSoTrang(0); }
                try { s.setSoLuong(Integer.parseInt(getCellValue(row.getCell(7)))); } catch (Exception e) { s.setSoLuong(0); }
                try { s.setDonGia(Double.parseDouble(getCellValue(row.getCell(8)))); } catch (Exception e) { s.setDonGia(0); }

                list.add(s);
            }
        }
        return list;
    }

    // Hàm phụ trợ lấy giá trị ô Excel 
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double val = cell.getNumericCellValue();
                if (val == (long) val) {
                    return String.valueOf((long) val);
                } else {
                    return String.valueOf(val);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
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