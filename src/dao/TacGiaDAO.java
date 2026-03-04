package dao;

import config.DBconnection;
import dto.TacGiaDTO;
import java.sql.*;
import java.util.ArrayList;

public class TacGiaDAO {
    public ArrayList<TacGiaDTO> selectAll() {
        ArrayList<TacGiaDTO> list = new ArrayList<>();
        try {
            Connection conn = DBconnection.getConnection();
            String sql = "SELECT * FROM TAC_GIA";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                list.add(new TacGiaDTO(
                    rs.getString("Ma_TG"),
                    rs.getString("Ho_Dem"),
                    rs.getString("Ten")
                    ));

            }
            conn.close();
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
