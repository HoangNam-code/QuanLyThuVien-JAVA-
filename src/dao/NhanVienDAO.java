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
            String sql = "SELECT nv.*, " +
                         "(SELECT tk.TRANG_THAI FROM TAI_KHOAN tk WHERE tk.Ma_NV = nv.Ma_NV LIMIT 1) AS TrangThaiTaiKhoan " +
                         "FROM NHAN_VIEN nv";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO(
                    rs.getString("Ma_NV"), rs.getString("Ho_Dem"), rs.getString("Ten"),
                    rs.getDate("Ngay_Sinh"), rs.getString("Gioi_Tinh"), rs.getString("SDT"),
                    rs.getString("Dia_Chi"), rs.getString("Email"), rs.getString("CHUC_VU")
                );
                nv.setTrangThaiTaiKhoan(rs.getString("TrangThaiTaiKhoan"));
                list.add(nv);
            }
            rs.close(); ps.close(); conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public int insert(NhanVienDTO nv) {
        int ketQua = 0;
        try {
            Connection conn = DBconnection.getConnection();
            // CHỈ ĐỊNH RÕ CỘT ĐỂ INSERT KHÔNG BỊ LỆCH
            String sql = "INSERT INTO NHAN_VIEN (Ma_NV, Ho_Dem, Ten, Ngay_Sinh, Gioi_Tinh, SDT, Dia_Chi, Email, CHUC_VU) VALUES (?,?,?,?,?,?,'',?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getMaNV().trim());
            ps.setString(2, nv.getHoDem().trim());
            ps.setString(3, nv.getTen().trim());
            ps.setDate(4, nv.getNgaySinh());
            ps.setString(5, nv.getGioiTinh());
            ps.setString(6, nv.getSdt().trim());
            ps.setString(7, nv.getEmail().trim());
            ps.setString(8, nv.getChucVu());
            ketQua = ps.executeUpdate();
            ps.close(); conn.close();
        } catch (SQLException e) { 
            System.out.println("Lỗi Insert: " + e.getMessage()); 
        }
        return ketQua;
    }

    public int update(NhanVienDTO nv) {
        int ketQua = 0;
        try {
            Connection conn = DBconnection.getConnection();
            // BỎ CỘT DIA_CHI RA KHỎI LỆNH UPDATE ĐỂ KHÔNG BỊ GHI ĐÈ BẰNG KHOẢNG TRỐNG
            String sql = "UPDATE NHAN_VIEN SET Ho_Dem=?, Ten=?, Ngay_Sinh=?, Gioi_Tinh=?, SDT=?, Email=?, CHUC_VU=? WHERE Ma_NV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getHoDem().trim());
            ps.setString(2, nv.getTen().trim());
            ps.setDate(3, nv.getNgaySinh());
            ps.setString(4, nv.getGioiTinh());
            ps.setString(5, nv.getSdt().trim());
            ps.setString(6, nv.getEmail().trim());
            ps.setString(7, nv.getChucVu());
            ps.setString(8, nv.getMaNV().trim()); 
            ketQua = ps.executeUpdate();
            ps.close(); conn.close();
        } catch (SQLException e) { 
            System.out.println("Lỗi Update: " + e.getMessage()); 
        }
        return ketQua;
    }

    public int delete(String maNV) {
        int ketQua = 0;
        try {
            Connection conn = DBconnection.getConnection();
            
            // BƯỚC BẢO VỆ 1: Xóa tài khoản liên kết (nếu có) trước để không bị lỗi khóa ngoại
            String sqlTK = "DELETE FROM TAI_KHOAN WHERE Ma_NV=?";
            PreparedStatement psTK = conn.prepareStatement(sqlTK);
            psTK.setString(1, maNV.trim());
            psTK.executeUpdate();
            psTK.close();

            // BƯỚC 2: Xóa nhân viên an toàn
            String sql = "DELETE FROM NHAN_VIEN WHERE Ma_NV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNV.trim());
            ketQua = ps.executeUpdate();
            ps.close(); conn.close();
        } catch (SQLException e) { 
            System.out.println("Lỗi Delete: " + e.getMessage()); 
        }
        return ketQua;
    }
}