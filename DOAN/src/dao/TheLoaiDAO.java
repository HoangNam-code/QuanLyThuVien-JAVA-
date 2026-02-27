package dao;

import config.DBconnection;
import dto.TheLoaiDTO;
import java.sql.*;
import java.util.ArrayList;

public class TheLoaiDAO {
    public ArrayList<TheLoaiDTO> selectAll() {
        ArrayList<TheLoaiDTO> list = new ArrayList<>();
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "SELECT * FROM THE_LOAI";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(new TheLoaiDTO(
                    rs.getString("Ma_TL"),
                    rs.getString("Ten_TL")
                ));
            }
            conn.close();
        } catch (SQLException e){e.printStackTrace();}
        return list;
    }
}
