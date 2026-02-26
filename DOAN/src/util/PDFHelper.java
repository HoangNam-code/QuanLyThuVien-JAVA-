package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import dto.DocGiaDTO;

import javax.swing.JTable;
import java.io.File;
import java.io.FileOutputStream;

public class PDFHelper {

    // Đường dẫn tới font Arial hỗ trợ tiếng Việt trên Windows
    private static final String FONT_PATH = "C:\\Windows\\Fonts\\arial.ttf";

    // 1. HÀM XUẤT DANH SÁCH ĐỘC GIẢ RA PDF
    public static void exportDanhSach(JTable table, String filePath) {
        Document document = new Document(PageSize.A4.rotate()); // Giấy A4 nằm ngang
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Cài đặt Font tiếng Việt
            BaseFont bf = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bf, 20, Font.BOLD, BaseColor.BLUE);
            Font fontHeader = new Font(bf, 12, Font.BOLD, BaseColor.WHITE);
            Font fontNormal = new Font(bf, 11, Font.NORMAL, BaseColor.BLACK);

            // Thêm Tiêu đề
            Paragraph title = new Paragraph("DANH SÁCH ĐỘC GIẢ THƯ VIỆN", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Tạo bảng PDF
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);

            // Lấy tiêu đề cột
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), fontHeader));
                cell.setBackgroundColor(new BaseColor(25, 118, 210)); 
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                pdfTable.addCell(cell);
            }

            // Lấy dữ liệu
            for (int rows = 0; rows < table.getRowCount(); rows++) {
                for (int cols = 0; cols < table.getColumnCount(); cols++) {
                    String val = table.getValueAt(rows, cols) != null ? table.getValueAt(rows, cols).toString() : "";
                    PdfPCell cell = new PdfPCell(new Phrase(val, fontNormal));
                    cell.setPadding(5);
                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close(); // Luôn đảm bảo file được đóng an toàn
            }
        }
    }

    // 2. HÀM IN THẺ ĐỘC GIẢ RA PDF
    public static void exportTheDocGia(DocGiaDTO dg, String filePath) {
        Document document = new Document(PageSize.A5.rotate());
        try {
            // LƯU Ý: Chỉ tạo PdfWriter và FileOutputStream 1 lần duy nhất
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            BaseFont bf = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontSchool = new Font(bf, 14, Font.BOLD, BaseColor.DARK_GRAY);
            Font fontTitle = new Font(bf, 24, Font.BOLD, BaseColor.BLUE);
            Font fontNormal = new Font(bf, 14, Font.NORMAL, BaseColor.BLACK);
            Font fontBold = new Font(bf, 14, Font.BOLD, BaseColor.BLACK);

            // Tên Trường
            Paragraph schoolName = new Paragraph("ĐẠI HỌC SÀI GÒN - THƯ VIỆN TRUNG TÂM", fontSchool);
            schoolName.setAlignment(Element.ALIGN_CENTER);
            document.add(schoolName);

            // Tiêu đề
            Paragraph title = new Paragraph("THẺ ĐỘC GIẢ", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Thông tin độc giả
            document.add(new Paragraph("Mã Độc Giả: " + dg.getMaDG(), fontBold));
            document.add(new Paragraph("Họ và Tên: " + (dg.getHoDem() != null ? dg.getHoDem() : "") + " " + (dg.getTen() != null ? dg.getTen() : ""), fontNormal));
            document.add(new Paragraph("Giới Tính: " + (dg.getGioiTinh() != null ? dg.getGioiTinh() : ""), fontNormal));
            document.add(new Paragraph("Ngày Sinh: " + (dg.getNgaySinh() != null ? dg.getNgaySinh().toString() : ""), fontNormal));
            document.add(new Paragraph("Số Điện Thoại: " + (dg.getSdt() != null ? dg.getSdt() : ""), fontNormal));
            document.add(new Paragraph("Loại Thành Viên: " + (dg.getMaLoaiTV() != null ? dg.getMaLoaiTV() : ""), fontNormal));
            
            // Ngày hết hạn
            String han = (dg.getNgayHetHan() != null) ? dg.getNgayHetHan().toString() : "Chưa xác định";
            Paragraph expired = new Paragraph("Ngày Hết Hạn: " + han, new Font(bf, 14, Font.BOLD, BaseColor.RED));
            expired.setSpacingBefore(10);
            document.add(expired);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close(); // Luôn đảm bảo file được đóng để chống lỗi corrupt
            }
        }
    }
}