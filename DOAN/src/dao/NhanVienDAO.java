package dao;

import config.DBconnection;
import dto.NhanVienDTO;
import java.sql.*;
import java.util.ArrayList;

public class NhanVienDAO {

    public ArrayList<NhanVienDTO> selectAll() {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "SELECT * FROM NHAN_VIEN";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new NhanVienDTO(
                    rs.getString("Ma_NV"),
                    rs.getString("Ho_Dem"),
                    rs.getString("Ten"),
                    rs.getDate("Ngay_Sinh"),
                    rs.getString("Gioi_Tinh"),
                    rs.getString("SDT"),
                    rs.getString("Dia_Chi"),
                    rs.getString("Email"),
                    rs.getString("CHUC_VU")
                ));
            }
            rs.close(); ps.close(); conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public int insert(NhanVienDTO nv) {
        int ketQua = 0;
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "INSERT INTO NHAN_VIEN VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getHoDem());
            ps.setString(3, nv.getTen());
            ps.setDate(4, nv.getNgaySinh());
            ps.setString(5, nv.getGioiTinh());
            ps.setString(6, nv.getSdt());
            ps.setString(7, nv.getDiaChi());
            ps.setString(8, nv.getEmail());
            ps.setString(9, nv.getChucVu());
            ketQua = ps.executeUpdate();
            ps.close(); conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return ketQua;
    }

    public int update(NhanVienDTO nv) {
        int ketQua = 0;
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "UPDATE NHAN_VIEN SET Ho_Dem=?, Ten=?, Ngay_Sinh=?, Gioi_Tinh=?, SDT=?, Dia_Chi=?, Email=?, CHUC_VU=? WHERE Ma_NV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getHoDem());
            ps.setString(2, nv.getTen());
            ps.setDate(3, nv.getNgaySinh());
            ps.setString(4, nv.getGioiTinh());
            ps.setString(5, nv.getSdt());
            ps.setString(6, nv.getDiaChi());
            ps.setString(7, nv.getEmail());
            ps.setString(8, nv.getChucVu());
            ps.setString(9, nv.getMaNV()); // Điều kiện WHERE
            ketQua = ps.executeUpdate();
            ps.close(); conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return ketQua;
    }

    public int delete(String maNV) {
        int ketQua = 0;
        try {
            Connection conn = DBconnection.getConnection();
            // Lưu ý: Cần xử lý khóa ngoại bên bảng TAI_KHOAN hoặc PHIEU_MUON trước khi xóa (Nếu có ràng buộc)
            String sql = "DELETE FROM NHAN_VIEN WHERE Ma_NV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNV);
            ketQua = ps.executeUpdate();
            ps.close(); conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return ketQua;
    }
}