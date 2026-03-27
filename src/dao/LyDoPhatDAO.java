package dao;

import config.DBconnection;
import dto.LyDoPhatDTO;
import java.sql.*;
import java.util.ArrayList;

public class LyDoPhatDAO {
    public ArrayList<LyDoPhatDTO> selectAll() {
        ArrayList<LyDoPhatDTO> list = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM qp_phat";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new LyDoPhatDTO(
                    rs.getString("Ma_Ly_Do_Phat"),
                    rs.getString("Ten_Ly_Do_Phat"),
                    rs.getString("Cach_Tinh_Phat"),
                    rs.getString("Ghi_Chu")
                ));
            }
        } catch (SQLException e) { 
            System.err.println("LỖI LẤY LÝ DO PHẠT: " + e.getMessage()); 
        }
        return list;
    }
}