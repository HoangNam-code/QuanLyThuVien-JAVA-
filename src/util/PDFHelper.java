package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import dto.DocGiaDTO;
import dto.PhieuMuonDTO;
import dto.ChiTietPhieuMuonDTO;

import javax.swing.JTable;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PDFHelper {

    // Đường dẫn tới font Arial hỗ trợ tiếng Việt trên Windows
    private static final String FONT_PATH = "C:\\Windows\\Fonts\\arial.ttf";

    // =========================================================================
    // 1. HÀM XUẤT DANH SÁCH DẠNG BẢNG RA PDF (DÙNG CHUNG)
    // =========================================================================
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
            Paragraph title = new Paragraph("DANH SÁCH XUẤT TỪ HỆ THỐNG", fontTitle);
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

    // =========================================================================
    // 2. HÀM IN THẺ ĐỘC GIẢ RA PDF
    // =========================================================================
    public static void exportTheDocGia(DocGiaDTO dg, String filePath) {
        Document document = new Document(PageSize.A5.rotate());
        try {
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
                document.close(); 
            }
        }
    }

    // =========================================================================
    // 3. ĐÃ BỔ SUNG: HÀM IN PHIẾU MƯỢN SÁCH RA PDF CHI TIẾT
    // =========================================================================
    public static void exportPhieuMuon(PhieuMuonDTO pm, ArrayList<ChiTietPhieuMuonDTO> chiTietList, String filePath) {
        Document document = new Document(PageSize.A4); // Giấy A4 khổ đứng
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Cài đặt Font
            BaseFont bf = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontSchool = new Font(bf, 12, Font.BOLD, BaseColor.DARK_GRAY);
            Font fontTitle = new Font(bf, 22, Font.BOLD, new BaseColor(25, 118, 210));
            Font fontNormal = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);
            Font fontBold = new Font(bf, 12, Font.BOLD, BaseColor.BLACK);
            Font fontHeader = new Font(bf, 12, Font.BOLD, BaseColor.WHITE);

            // Tên trường
            Paragraph schoolName = new Paragraph("ĐẠI HỌC SÀI GÒN\nTHƯ VIỆN TRUNG TÂM", fontSchool);
            schoolName.setAlignment(Element.ALIGN_CENTER);
            schoolName.setSpacingAfter(20);
            document.add(schoolName);

            // Tiêu đề Phiếu
            Paragraph title = new Paragraph("PHIẾU MƯỢN SÁCH", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Thông tin chung của phiếu
            document.add(new Paragraph("Mã Phiếu Mượn: " + pm.getMaPM(), fontBold));
            document.add(new Paragraph("Mã Độc Giả: " + pm.getMaDG(), fontNormal));
            document.add(new Paragraph("Nhân Viên Lập Phiếu: " + pm.getMaNV(), fontNormal));
            document.add(new Paragraph("Ngày Mượn: " + pm.getNgayMuon(), fontNormal));
            document.add(new Paragraph("Ngày Hẹn Trả: " + pm.getNgayTraDuKien(), fontNormal));
            
            Paragraph spacing = new Paragraph(" ");
            spacing.setSpacingAfter(15);
            document.add(spacing);

            // Vẽ Bảng Danh Sách Các Sách Đã Mượn
            PdfPTable pdfTable = new PdfPTable(5); // Có 5 cột
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new float[]{1f, 3f, 2f, 3f, 2f}); // Tỷ lệ độ rộng các cột

            // Header bảng
            String[] headers = {"STT", "Mã Sách", "Số Lượng", "Tình Trạng", "Trạng Thái"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontHeader));
                cell.setBackgroundColor(new BaseColor(25, 118, 210)); 
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                pdfTable.addCell(cell);
            }

            // Đổ dữ liệu chi tiết sách vào bảng
            int stt = 1;
            for (ChiTietPhieuMuonDTO ct : chiTietList) {
                // Cột STT
                PdfPCell cell1 = new PdfPCell(new Phrase(String.valueOf(stt++), fontNormal));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setPadding(5);
                pdfTable.addCell(cell1);

                // Cột Mã Sách
                PdfPCell cell2 = new PdfPCell(new Phrase(ct.getMaSach(), fontNormal));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPadding(5);
                pdfTable.addCell(cell2);

                // Cột Số Lượng
                PdfPCell cell3 = new PdfPCell(new Phrase(String.valueOf(ct.getSoLuong()), fontNormal));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setPadding(5);
                pdfTable.addCell(cell3);

                // Cột Tình Trạng
                PdfPCell cell4 = new PdfPCell(new Phrase(ct.getTinhTrangSach(), fontNormal));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setPadding(5);
                pdfTable.addCell(cell4);

                // Cột Trạng Thái
                String trangThai = ct.getDaTra() == 1 ? "Đã trả" : "Chưa trả";
                PdfPCell cell5 = new PdfPCell(new Phrase(trangThai, fontNormal));
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setPadding(5);
                pdfTable.addCell(cell5);
            }

            document.add(pdfTable);

            // Phần ký tên xác nhận ở góc dưới
            PdfPTable signTable = new PdfPTable(2);
            signTable.setWidthPercentage(100);
            signTable.setSpacingBefore(30);
            
            PdfPCell cellSign1 = new PdfPCell(new Phrase("Người mượn\n(Ký và ghi rõ họ tên)", fontBold));
            cellSign1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellSign1.setBorder(Rectangle.NO_BORDER);
            
            PdfPCell cellSign2 = new PdfPCell(new Phrase("Thủ thư\n(Ký và ghi rõ họ tên)", fontBold));
            cellSign2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellSign2.setBorder(Rectangle.NO_BORDER);
            
            signTable.addCell(cellSign1);
            signTable.addCell(cellSign2);
            
            document.add(signTable);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close(); 
            }
        }
    }
}