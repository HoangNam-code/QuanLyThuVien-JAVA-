package dao;

import dto.DocGiaDTO;
import config.DBconnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocGiaDAO {

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
                        rs.getDate("Ngay_Dang_Ky"), 
                        rs.getDate("Ngay_Het_Han"), 
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

    //thêm độc giả 
    public boolean insert(DocGiaDTO dg) {
        boolean result = false;
        try {
            Connection con = DBconnection.getConnection();
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
            pst.setDate(10, dg.getNgayDangKy()); 
            pst.setDate(11, dg.getNgayHetHan()); 
            pst.setDouble(12, dg.getTienPhiThanhVien());
            pst.setString(13, dg.getMaLoaiTV());
            result = pst.executeUpdate() > 0;
            DBconnection.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(DocGiaDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    //Cập nhật thông tin độc giả 
    public boolean update(DocGiaDTO dg) {
        boolean result = false;
        try {
            Connection con = DBconnection.getConnection();
            String sql = "UPDATE DOC_GIA SET Ho_Dem=?, Ten=?, Gioi_Tinh=?, Ngay_Sinh=?, Email=?, SDT=?, Dia_Chi=?, So_Sach_Muon=?, Ngay_Dang_Ky=?, Ngay_Het_Han=?, Tien_Phi_Thanh_Vien=?, Ma_Loai_TV=? WHERE Ma_DG=?";
            PreparedStatement pst = con.prepareStatement(sql);            
            pst.setString(1, dg.getHoDem()); // ? thứ 1
            pst.setString(2, dg.getTen()); // ? thứ 2
            pst.setString(3, dg.getGioiTinh()); // ? thứ 3
            pst.setDate(4, dg.getNgaySinh()); // ? thứ 4
            pst.setString(5, dg.getEmail()); // ? thứ 5
            pst.setString(6, dg.getSdt()); // ? thứ 6
            pst.setString(7, dg.getDiaChi()); // ? thứ 7
            pst.setInt(8, dg.getSoSachMuon()); // ? thứ 8
            pst.setDate(9, dg.getNgayDangKy()); 
            pst.setDate(10, dg.getNgayHetHan()); 
            pst.setDouble(11, dg.getTienPhiThanhVien()); 
            pst.setString(12, dg.getMaLoaiTV()); 
            pst.setString(13, dg.getMaDG()); 
            int rowsAffected = pst.executeUpdate();
            result = rowsAffected > 0;
            System.out.println("Đã cập nhật " + rowsAffected + " dòng trong CSDL.");
            DBconnection.closeConnection(con);
        } catch (SQLException e) {
            System.err.println("LỖI SQL KHI UPDATE:");
            e.printStackTrace();
        }
        return result;
    }

    // Xóa một độc giả
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
}