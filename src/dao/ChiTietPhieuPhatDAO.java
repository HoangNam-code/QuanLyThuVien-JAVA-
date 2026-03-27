package dao;

import config.DBconnection;
import dto.ChiTietPhieuPhatDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ChiTietPhieuPhatDAO {

    private final Connection conn = DBconnection.getConnection();

    public boolean insert(ChiTietPhieuPhatDTO ct) {
        String sql = "INSERT INTO ctpp_chi_tiet_pp(Ma_PP, Ma_Ly_Do_Phat, Ma_Sach, So_Tien_Phat) VALUES(?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ct.getMaPP());
            ps.setString(2, ct.getMaLyDoPhat());
            ps.setString(3, ct.getMaSach());
            ps.setLong(4, ct.getSoTienPhat());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<ChiTietPhieuPhatDTO> getByMaPP(String maPP) {
        ArrayList<ChiTietPhieuPhatDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ctpp_chi_tiet_pp WHERE Ma_PP = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPP);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ChiTietPhieuPhatDTO(
                            rs.getString("Ma_PP"),
                            rs.getString("Ma_Ly_Do_Phat"),
                            rs.getString("Ma_Sach"),
                            rs.getLong("So_Tien_Phat")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
