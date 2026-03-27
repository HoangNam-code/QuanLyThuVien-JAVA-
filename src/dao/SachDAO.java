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
            ps.setString(9, s.getMaSach().trim()); 
            
            int res = ps.executeUpdate();
            conn.close();
            return res;
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }

    public int delete(String maSach) {
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "DELETE FROM SACH WHERE Ma_Sach=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maSach.trim());
            int res = ps.executeUpdate();
            conn.close();
            return res;
        } catch (SQLException e) { 
            System.out.println("Lỗi xóa sách (có thể do ràng buộc khóa ngoại): " + e.getMessage());
            return 0; 
        }
    }
    public boolean giamSoLuong(String maSach, int soLuong) {
        String sql = "UPDATE SACH SET So_Luong = So_Luong - ? WHERE Ma_Sach = ? AND So_Luong >= ?";
            try {
                Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, soLuong);
                ps.setString(2, maSach);
                ps.setInt(3, soLuong);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    //tăng số lượng trong tồn kho
    public boolean tangSoLuong(String maSach, int soLuong) {
        String sql = "UPDATE SACH SET So_Luong = So_Luong + ? WHERE Ma_Sach = ?";
        try {
            Connection conn = DBconnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, soLuong);
            ps.setString(2, maSach);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

   //tìm kiếm nâng cao
   public ArrayList<SachDTO> selectByCondition(String maTL, String maTG, String maNXB, int namXB, double minGia, double maxGia) {
    ArrayList<SachDTO> list = new ArrayList<>();
    try {
        Connection conn = DBconnection.getConnection();
        StringBuilder sql = new StringBuilder("SELECT * FROM SACH WHERE 1=1"); 
        
        if (maTL != null && !maTL.isEmpty() && !maTL.equals("Tất cả")) sql.append(" AND Ma_TL = ?");
        if (maTG != null && !maTG.isEmpty() && !maTG.equals("Tất cả")) sql.append(" AND Ma_TG = ?");
        if (maNXB != null && !maNXB.isEmpty() && !maNXB.equals("Tất cả")) sql.append(" AND Ma_NXB = ?");
        if (namXB > 0) sql.append(" AND Nam_XB = ?");
        if (maxGia > 0) sql.append(" AND Don_Gia BETWEEN ? AND ?");

        PreparedStatement ps = conn.prepareStatement(sql.toString());
        
        int index = 1;
        if (maTL != null && !maTL.isEmpty() && !maTL.equals("Tất cả")) ps.setString(index++, maTL);
        if (maTG != null && !maTG.isEmpty() && !maTG.equals("Tất cả")) ps.setString(index++, maTG);
        if (maNXB != null && !maNXB.isEmpty() && !maNXB.equals("Tất cả")) ps.setString(index++, maNXB);
        if (namXB > 0) ps.setInt(index++, namXB);
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