package dao;

import config.DBconnection;
import dto.PhieuMuonDTO;
import java.sql.*;
import java.util.ArrayList;

public class PhieuMuonDAO {

    private Connection conn = DBconnection.getConnection();
    public ArrayList<PhieuMuonDTO> getAll() {

        ArrayList<PhieuMuonDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM pm_phieu_muon";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Date ngayMuon = rs.getDate("Ngay_Muon");
                Date ngayTraDuKien = rs.getDate("Ngay_Tra_Du_Kien");
                Date ngayThucTra = rs.getDate("Ngay_Thuc_Tra");

                PhieuMuonDTO pm = new PhieuMuonDTO(
                    rs.getString("Ma_PM"),
                    rs.getString("Ma_DG"),
                    rs.getString("Ma_NV"),
                    ngayMuon != null ? ngayMuon.toString() : null,
                    ngayTraDuKien != null ? ngayTraDuKien.toString() : null,
                    ngayThucTra != null ? ngayThucTra.toString() : null,
                    rs.getString("Ghi_Chu")
                );

                list.add(pm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //hàm thêm
    public boolean them(PhieuMuonDTO pm) {

        String sql =
            "INSERT INTO pm_phieu_muon(Ma_PM, Ma_DG, Ma_NV, Ngay_Muon, Ngay_Tra_Du_Kien, Ngay_Thuc_Tra, Ghi_Chu) VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, pm.getMaPM());
            ps.setString(2, pm.getMaDG());
            ps.setString(3, pm.getMaNV());
            ps.setDate(4, Date.valueOf(pm.getNgayMuon()));
            ps.setDate(5, Date.valueOf(pm.getNgayTraDuKien()));
            if (pm.getNgayThucTra() == null || pm.getNgayThucTra().trim().isEmpty()) {
                ps.setNull(6, Types.DATE);
            } else {
                ps.setDate(6, Date.valueOf(pm.getNgayThucTra()));
            }
            ps.setString(7, pm.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //cập nhật ngày thực trả 
    public boolean capNhatNgayThucTra(String maPM, Date ngayTra) {
        String sql = "UPDATE pm_phieu_muon SET Ngay_Thuc_Tra = ? WHERE Ma_PM = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            if (ngayTra == null) {
                ps.setNull(1, Types.DATE);
            } else {
                ps.setDate(1, ngayTra);
            }
            ps.setString(2, maPM);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //xoá phiếu mượn
    public boolean xoa(String maPM) {
        String sql = "DELETE FROM pm_phieu_muon WHERE Ma_PM = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maPM);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}