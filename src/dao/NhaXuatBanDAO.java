package dao;

import config.DBconnection;
import dto.NhaXuatBanDTO;
import java.sql.*;
import java.util.ArrayList;

public class NhaXuatBanDAO {
    
    // Đây chính là hàm selectAll() mà Java đang báo thiếu
    public ArrayList<NhaXuatBanDTO> selectAll() {
        ArrayList<NhaXuatBanDTO> list = new ArrayList<>();
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "SELECT * FROM NHA_XUAT_BAN";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new NhaXuatBanDTO(
                    rs.getString("Ma_NXB"),
                    rs.getString("Ten_NXB")
                ));
            }
            conn.close();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }
}