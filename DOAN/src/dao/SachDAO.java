package dao;

import config.DBconnection;
import dto.SachDTO;
import java.sql.*;
import java.util.ArrayList;

public class SachDAO {

    public ArrayList<SachDTO> selectAll() {
        ArrayList<SachDTO> list = new ArrayList<>();
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "SELECT * FROM SACH";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new SachDTO(
                    rs.getString("Ma_Sach"), 
                    rs.getString("Ten_Sach"),
                    rs.getInt("Nam_XB"),
                    rs.getString("Ma_TL"),
                    rs.getDouble("Don_Gia"),
                    rs.getInt("So_Luong"),
                    rs.getString("Ma_TG"), 
                    rs.getString("Ma_NXB"), 
                    rs.getInt("So_Trang")
                ));
            }
            conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public int insert(SachDTO s) {
        try {
            Connection conn = DBconnection.getConnection();
            // Lưu ý: Thứ tự dấu ? phải khớp với thứ tự setString bên dưới
            String sql = "INSERT INTO SACH (Ma_Sach, Ten_Sach, Nam_XB, Ma_TL, Don_Gia, So_Luong, Ma_TG, Ma_NXB, So_Trang) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getMaSach());
            ps.setString(2, s.getTenSach());
            ps.setInt(3, s.getNamXB());
            ps.setString(4, s.getMaTL());
            ps.setDouble(5, s.getDonGia());
            ps.setInt(6, s.getSoLuong());
            ps.setString(7, s.getMaTG());
            ps.setString(8, s.getMaNXB());
            ps.setInt(9, s.getSoTrang());
            
            int res = ps.executeUpdate();
            conn.close();
            return res;
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }

    public int update(SachDTO s) {
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "UPDATE SACH SET Ten_Sach=?, Nam_XB=?, Ma_TL=?, Don_Gia=?, So_Luong=?, Ma_TG=?, Ma_NXB=?, So_Trang=? WHERE Ma_Sach=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getTenSach());
            ps.setInt(2, s.getNamXB());
            ps.setString(3, s.getMaTL());
            ps.setDouble(4, s.getDonGia());
            ps.setInt(5, s.getSoLuong());
            ps.setString(6, s.getMaTG());
            ps.setString(7, s.getMaNXB());
            ps.setInt(8, s.getSoTrang());
            ps.setString(9, s.getMaSach()); // Điều kiện WHERE
            
            int res = ps.executeUpdate();
            conn.close();
            return res;
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }

    public int delete(String maSach) {
        try {
            Connection conn = DBconnection.getConnection();
            // Lưu ý: Nếu sách đã có trong Phiếu Mượn/Nhập, lệnh này sẽ lỗi do ràng buộc Khóa Ngoại.
            // Lúc đó nên thông báo: "Không thể xóa sách đã phát sinh giao dịch!"
            String sql = "DELETE FROM SACH WHERE Ma_Sach=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maSach);
            int res = ps.executeUpdate();
            conn.close();
            return res;
        } catch (SQLException e) { 
            System.out.println("Lỗi xóa sách (có thể do ràng buộc khóa ngoại): " + e.getMessage());
            return 0; 
        }
    }

    // [Mục 9] Tìm kiếm nâng cao (Search Engine)
    public ArrayList<SachDTO> selectByCondition(String ten, String maTL, String maTG, double minGia, double maxGia) {
        ArrayList<SachDTO> list = new ArrayList<>();
        try {
            Connection conn = DBconnection.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM SACH WHERE 1=1"); // 1=1 để dễ nối chuỗi AND
            
            if (!ten.isEmpty()) sql.append(" AND Ten_Sach LIKE ?");
            if (maTL != null && !maTL.equals("Tất cả")) sql.append(" AND Ma_TL = ?");
            if (maTG != null && !maTG.equals("Tất cả")) sql.append(" AND Ma_TG = ?");
            if (maxGia > 0) sql.append(" AND Don_Gia BETWEEN ? AND ?");

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            
            int index = 1;
            if (!ten.isEmpty()) ps.setString(index++, "%" + ten + "%");
            if (maTL != null && !maTL.equals("Tất cả")) ps.setString(index++, maTL);
            if (maTG != null && !maTG.equals("Tất cả")) ps.setString(index++, maTG);
            if (maxGia > 0) {
                ps.setDouble(index++, minGia);
                ps.setDouble(index++, maxGia);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new SachDTO(
                    rs.getString("Ma_Sach"), rs.getString("Ten_Sach"),
                    rs.getInt("Nam_XB"), rs.getString("Ma_TL"),
                    rs.getDouble("Don_Gia"), rs.getInt("So_Luong"),
                    rs.getString("Ma_TG"), rs.getString("Ma_NXB"), 
                    rs.getInt("So_Trang")
                ));
            }
            conn.close();
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}