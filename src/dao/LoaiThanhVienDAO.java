package dao;

import config.DBconnection;
import dto.LoaiThanhVienDTO;
import java.sql.*;
import java.util.ArrayList;

public class LoaiThanhVienDAO {
    public ArrayList<LoaiThanhVienDTO> selectAll() {
        ArrayList<LoaiThanhVienDTO> list = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM qp_phi_thanh_vien";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new LoaiThanhVienDTO(
                    rs.getString("Ma_Loai_TV"),
                    rs.getString("Ten_Loai_TV"),
                    rs.getDouble("Phi")
                ));
            }
        } catch (SQLException e) { 
            System.err.println("LỖI LẤY PHÍ THÀNH VIÊN: " + e.getMessage()); 
        }
        return list;
    }
}