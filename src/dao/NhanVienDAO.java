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
                    rs.getString("Dia_Chi"), rs.getString("Email"), rs.getString("CHUC_VU"),
                    rs.getDouble("Luong"), rs.getDate("Ngay_Vao_Lam"), rs.getDate("Ngay_Nghi_Viec") // Lấy 3 cột mới lên
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
            String sql = "INSERT INTO NHAN_VIEN (Ma_NV, Ho_Dem, Ten, Ngay_Sinh, Gioi_Tinh, SDT, Dia_Chi, Email, CHUC_VU, Luong, Ngay_Vao_Lam, Ngay_Nghi_Viec) VALUES (?,?,?,?,?,?,'',?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getMaNV().trim());
            ps.setString(2, nv.getHoDem().trim());
            ps.setString(3, nv.getTen().trim());
            ps.setDate(4, nv.getNgaySinh());
            ps.setString(5, nv.getGioiTinh());
            ps.setString(6, nv.getSdt().trim());
            ps.setString(7, nv.getEmail().trim());
            ps.setString(8, nv.getChucVu());
            ps.setDouble(9, nv.getLuong());
            ps.setDate(10, nv.getNgayVaoLam());
            ps.setDate(11, nv.getNgayNghiViec());
            
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
            String sql = "UPDATE NHAN_VIEN SET Ho_Dem=?, Ten=?, Ngay_Sinh=?, Gioi_Tinh=?, SDT=?, Email=?, CHUC_VU=?, Luong=?, Ngay_Vao_Lam=?, Ngay_Nghi_Viec=? WHERE Ma_NV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nv.getHoDem().trim());
            ps.setString(2, nv.getTen().trim());
            ps.setDate(3, nv.getNgaySinh());
            ps.setString(4, nv.getGioiTinh());
            ps.setString(5, nv.getSdt().trim());
            ps.setString(6, nv.getEmail().trim());
            ps.setString(7, nv.getChucVu());
            ps.setDouble(8, nv.getLuong());
            ps.setDate(9, nv.getNgayVaoLam());
            ps.setDate(10, nv.getNgayNghiViec());
            ps.setString(11, nv.getMaNV().trim()); 
            
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
            String sqlTK = "DELETE FROM TAI_KHOAN WHERE Ma_NV=?";
            PreparedStatement psTK = conn.prepareStatement(sqlTK);
            psTK.setString(1, maNV.trim());
            psTK.executeUpdate();
            psTK.close();

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