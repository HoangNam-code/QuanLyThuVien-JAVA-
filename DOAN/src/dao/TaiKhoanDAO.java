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
        // Đảm bảo tên cột trong SQL khớp với DB của bạn
        String sql = "SELECT * FROM TAI_KHOAN WHERE TEN_DANG_NHAP = ? AND MAT_KHAU = ? AND TRANG_THAI = 1";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            if (conn == null) return null; // Tránh lỗi null pointer nếu ko kết nối được DB

            ps.setString(1, user);
            ps.setString(2, pass);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoanDTO();
                    tk.setTenDangNhap(rs.getString("TEN_DANG_NHAP"));
                    tk.setMaNV(rs.getString("MA_NV")); // Check kỹ: MA_NV hay Ma_NV trong database
                    tk.setMatKhau(rs.getString("MAT_KHAU"));
                    tk.setQuyenHan(rs.getString("QUYEN_HAN"));
                    tk.setTrangThai(rs.getInt("TRANG_THAI"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tk;
    }
}