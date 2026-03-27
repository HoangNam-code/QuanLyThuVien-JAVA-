package dao;

import config.DBconnection;
import dto.TaiKhoanDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {
    
    public TaiKhoanDTO checkLogin(String user, String pass) {
        TaiKhoanDTO tk = null;
        String sql = "SELECT * FROM TAI_KHOAN WHERE TEN_DANG_NHAP = ? AND MAT_KHAU = ? AND TRANG_THAI = '1'";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn == null) return null;
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoanDTO();
                    tk.setTenDangNhap(rs.getString("TEN_DANG_NHAP"));
                    tk.setMaNV(rs.getString("MA_NV"));
                    tk.setMatKhau(rs.getString("MAT_KHAU"));
                    tk.setQuyenHan(rs.getString("QUYEN_HAN"));
                    tk.setTrangThai(rs.getString("TRANG_THAI"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return tk;
    }

    public int insert(TaiKhoanDTO tk) {
        String sql = "INSERT INTO TAI_KHOAN (TEN_DANG_NHAP, MA_NV, MAT_KHAU, QUYEN_HAN, TRANG_THAI) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMaNV());
            ps.setString(3, tk.getMatKhau());
            ps.setString(4, tk.getQuyenHan());
            ps.setString(5, tk.getTrangThai()); 
            return ps.executeUpdate(); 
        } catch (SQLException e) {
            System.err.println("Lỗi insert tài khoản: " + e.getMessage());
        }
        return 0;
    }

    public TaiKhoanDTO selectByMaNV(String maNV) {
        String sql = "SELECT * FROM TAI_KHOAN WHERE MA_NV = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoanDTO tk = new TaiKhoanDTO();
                    tk.setTenDangNhap(rs.getString("TEN_DANG_NHAP"));
                    tk.setMaNV(rs.getString("MA_NV"));
                    tk.setMatKhau(rs.getString("MAT_KHAU"));
                    tk.setQuyenHan(rs.getString("QUYEN_HAN"));
                    tk.setTrangThai(rs.getString("TRANG_THAI"));
                    return tk;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public int updateMatKhauQuyen(String user, String passMoi, String quyenMoi) {
        int ketQua = 0;
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "UPDATE TAI_KHOAN SET MAT_KHAU = ?, QUYEN_HAN = ? WHERE TEN_DANG_NHAP = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, passMoi);
            ps.setString(2, quyenMoi);
            ps.setString(3, user);
            ketQua = ps.executeUpdate();
            ps.close(); conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return ketQua;
    }

    public int updateTrangThai(String maNV, String trangThaiMoi) {
        int ketQua = 0;
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "UPDATE TAI_KHOAN SET TRANG_THAI = ? WHERE MA_NV = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, trangThaiMoi); 
            ps.setString(2, maNV);
            ketQua = ps.executeUpdate();
            ps.close(); conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return ketQua;
    }
}