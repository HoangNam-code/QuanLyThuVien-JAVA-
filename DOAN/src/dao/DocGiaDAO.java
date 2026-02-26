package dao;

import config.DBconnection;
import dto.DocGiaDTO;
import java.sql.*;
import java.util.ArrayList;

public class DocGiaDAO {
    
    public ArrayList<DocGiaDTO> selectAll() {
        ArrayList<DocGiaDTO> list = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection()) {
            if (conn == null) return list;
            String sql = "SELECT * FROM doc_gia";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DocGiaDTO(
                    rs.getString("Ma_DG"), rs.getString("Ho_Dem"), rs.getString("Ten"),
                    rs.getString("Gioi_Tinh"), rs.getDate("Ngay_Sinh"), rs.getString("Email"),
                    rs.getString("SDT"), rs.getString("Dia_Chi"), rs.getInt("So_Sach_Muon"),
                    rs.getDate("Ngay_Het_Han"), rs.getDouble("Tien_Phi_Thanh_Vien"), rs.getString("Ma_Loai_TV")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public int insert(DocGiaDTO dg) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "INSERT INTO doc_gia (Ma_DG, Ho_Dem, Ten, Gioi_Tinh, Ngay_Sinh, Email, SDT, Dia_Chi, So_Sach_Muon, Ngay_Het_Han, Tien_Phi_Thanh_Vien, Ma_Loai_TV) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dg.getMaDG());
            ps.setString(2, dg.getHoDem());
            ps.setString(3, dg.getTen());
            ps.setString(4, dg.getGioiTinh());
            ps.setDate(5, dg.getNgaySinh());
            ps.setString(6, dg.getEmail());
            ps.setString(7, dg.getSdt());
            ps.setString(8, dg.getDiaChi());
            ps.setInt(9, dg.getSoSachMuon());
            ps.setDate(10, dg.getNgayHetHan());
            ps.setDouble(11, dg.getTienPhiThanhVien());
            ps.setString(12, dg.getMaLoaiTV());
            return ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }
    // Hàm cập nhật thông tin độc giả (Không cập nhật So_Sach_Muon và Ngay_Het_Han)
    public int update(DocGiaDTO dg) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "UPDATE doc_gia SET Ho_Dem=?, Ten=?, Gioi_Tinh=?, Ngay_Sinh=?, Email=?, SDT=?, Dia_Chi=?, Tien_Phi_Thanh_Vien=?, Ma_Loai_TV=? WHERE Ma_DG=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dg.getHoDem());
            ps.setString(2, dg.getTen());
            ps.setString(3, dg.getGioiTinh());
            ps.setDate(4, dg.getNgaySinh());
            ps.setString(5, dg.getEmail());
            ps.setString(6, dg.getSdt());
            ps.setString(7, dg.getDiaChi());
            ps.setDouble(8, dg.getTienPhiThanhVien());
            ps.setString(9, dg.getMaLoaiTV());
            ps.setString(10, dg.getMaDG()); // Điều kiện WHERE
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int delete(String maDG) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "DELETE FROM doc_gia WHERE Ma_DG = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDG);
            return ps.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return 0; 
        }
    }
}