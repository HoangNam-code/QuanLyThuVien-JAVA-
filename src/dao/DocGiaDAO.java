package dao;

import dto.DocGiaDTO;
import config.DBconnection; // Đường dẫn import có thể thay đổi tùy cấu trúc thư mục của bạn
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocGiaDAO {

    // Lấy danh sách tất cả độc giả từ Database
    public ArrayList<DocGiaDTO> selectAll() {
        ArrayList<DocGiaDTO> ketQua = new ArrayList<>();
        try {
            Connection con = DBconnection.getConnection();
            String sql = "SELECT * FROM DOC_GIA";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                DocGiaDTO dg = new DocGiaDTO(
                        rs.getString("Ma_DG"),
                        rs.getString("Ho_Dem"),
                        rs.getString("Ten"),
                        rs.getString("Gioi_Tinh"),
                        rs.getDate("Ngay_Sinh"),
                        rs.getString("Email"),
                        rs.getString("SDT"),
                        rs.getString("Dia_Chi"),
                        rs.getInt("So_Sach_Muon"),
                        rs.getDate("Ngay_Dang_Ky"), // Đã thêm cột NgayDangKy
                        rs.getDate("Ngay_Het_Han"), // Đã thêm cột NgayHetHan
                        rs.getDouble("Tien_Phi_Thanh_Vien"),
                        rs.getString("Ma_Loai_TV"));
                ketQua.add(dg);
            }
            DBconnection.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(DocGiaDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return ketQua;
    }

    // Thêm một độc giả mới vào Database
    public boolean insert(DocGiaDTO dg) {
        boolean result = false;
        try {
            Connection con = DBconnection.getConnection();
            // Đã bổ sung NgayDangKy và NgayHetHan vào câu lệnh INSERT (Tổng 13 cột)
            String sql = "INSERT INTO DOC_GIA (Ma_DG, Ho_Dem, Ten, Gioi_Tinh, Ngay_Sinh, Email, SDT, Dia_Chi, So_Sach_Muon, Ngay_Dang_Ky, Ngay_Het_Han, Tien_Phi_Thanh_Vien, Ma_Loai_TV) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, dg.getMaDG());
            pst.setString(2, dg.getHoDem());
            pst.setString(3, dg.getTen());
            pst.setString(4, dg.getGioiTinh());
            pst.setDate(5, dg.getNgaySinh());
            pst.setString(6, dg.getEmail());
            pst.setString(7, dg.getSdt());
            pst.setString(8, dg.getDiaChi());
            pst.setInt(9, dg.getSoSachMuon());
            pst.setDate(10, dg.getNgayDangKy()); // Truyền dữ liệu NgayDangKy
            pst.setDate(11, dg.getNgayHetHan()); // Truyền dữ liệu NgayHetHan
            pst.setDouble(12, dg.getTienPhiThanhVien());
            pst.setString(13, dg.getMaLoaiTV());

            result = pst.executeUpdate() > 0;
            DBconnection.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(DocGiaDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    // Cập nhật thông tin độc giả trong Database
    // Cập nhật thông tin độc giả trong Database
    public boolean update(DocGiaDTO dg) {
        boolean result = false;
        try {
            Connection con = DBconnection.getConnection();

            // Chú ý: Đảm bảo tên cột ở đây KHỚP CHÍNH XÁC với tên cột trong MySQL của bạn
            String sql = "UPDATE DOC_GIA SET Ho_Dem=?, Ten=?, Gioi_Tinh=?, Ngay_Sinh=?, Email=?, SDT=?, Dia_Chi=?, So_Sach_Muon=?, Ngay_Dang_Ky=?, Ngay_Het_Han=?, Tien_Phi_Thanh_Vien=?, Ma_Loai_TV=? WHERE Ma_DG=?";

            PreparedStatement pst = con.prepareStatement(sql);

            // Gán dữ liệu vào các dấu "?" theo đúng thứ tự 1 -> 13
            pst.setString(1, dg.getHoDem()); // ? thứ 1
            pst.setString(2, dg.getTen()); // ? thứ 2
            pst.setString(3, dg.getGioiTinh()); // ? thứ 3
            pst.setDate(4, dg.getNgaySinh()); // ? thứ 4
            pst.setString(5, dg.getEmail()); // ? thứ 5
            pst.setString(6, dg.getSdt()); // ? thứ 6
            pst.setString(7, dg.getDiaChi()); // ? thứ 7
            pst.setInt(8, dg.getSoSachMuon()); // ? thứ 8
            pst.setDate(9, dg.getNgayDangKy()); // ? thứ 9
            pst.setDate(10, dg.getNgayHetHan()); // ? thứ 10
            pst.setDouble(11, dg.getTienPhiThanhVien()); // ? thứ 11
            pst.setString(12, dg.getMaLoaiTV()); // ? thứ 12
            pst.setString(13, dg.getMaDG()); // ? thứ 13 (Điều kiện WHERE)

            int rowsAffected = pst.executeUpdate();
            result = rowsAffected > 0;

            // In ra console để debug (kiểm tra xem có cập nhật được dòng nào không)
            System.out.println("Đã cập nhật " + rowsAffected + " dòng trong CSDL.");

            DBconnection.closeConnection(con);
        } catch (SQLException e) {
            // In lỗi ra màn hình console để biết chính xác SQL đang sai ở đâu
            System.err.println("LỖI SQL KHI UPDATE:");
            e.printStackTrace();
        }
        return result;
    }

    // Xóa một độc giả khỏi Database
    public boolean delete(String maDG) {
        boolean result = false;
        try {
            Connection con = DBconnection.getConnection();
            String sql = "DELETE FROM DOC_GIA WHERE Ma_DG=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maDG);

            result = pst.executeUpdate() > 0;
            DBconnection.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(DocGiaDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    // Bạn có thể giữ lại các hàm tìm kiếm theo SQL riêng của mình dưới đây (nếu có)
    // Ví dụ: searchByMaDG, searchByTen...
}