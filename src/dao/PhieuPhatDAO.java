package dao;

import config.DBconnection;
import dto.PhieuPhatDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

public class PhieuPhatDAO {

    private final Connection conn = DBconnection.getConnection();

    public ArrayList<PhieuPhatDTO> getAll() {
        ArrayList<PhieuPhatDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM pp_phieu_phat ORDER BY Ngay_Ghi DESC, Ma_PP DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp ngayGhi = rs.getTimestamp("Ngay_Ghi");
                list.add(new PhieuPhatDTO(
                        rs.getString("Ma_PP"),
                        rs.getString("Ma_PM"),
                        rs.getString("Ma_DG"), 
                        rs.getString("Ma_NV"),
                        ngayGhi == null ? "" : ngayGhi.toString(),
                        rs.getString("Ly_Do"),
                        rs.getLong("Tong_Tien")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(PhieuPhatDTO pp) {
        String sql = "INSERT INTO pp_phieu_phat(Ma_PP, Ma_PM, Ma_DG, Ma_NV, Ngay_Ghi, Ly_Do, Tong_Tien) VALUES(?,?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pp.getMaPP());
            ps.setString(2, pp.getMaPM());
            ps.setString(3, pp.getMaDG());
            ps.setString(4, pp.getMaNV());

            if (pp.getNgayGhi() == null || pp.getNgayGhi().trim().isEmpty()) {
                ps.setNull(5, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(5, Timestamp.valueOf(pp.getNgayGhi()));
            }

            ps.setString(6, pp.getLyDo());
            ps.setLong(7, pp.getTongTien());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTongTienVaLyDo(String maPP, long tongTien, String lyDo) {
        String sql = "UPDATE pp_phieu_phat SET Tong_Tien = ?, Ly_Do = ? WHERE Ma_PP = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, tongTien);
            ps.setString(2, lyDo);
            ps.setString(3, maPP);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}