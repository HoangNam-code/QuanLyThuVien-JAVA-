package dao;

import config.DBconnection;
import dto.ChiTietPhieuMuonDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ChiTietPhieuMuonDAO {

    private final Connection conn = DBconnection.getConnection();

    //THÊM CHI TIẾT PHIẾU MƯỢN 
    public boolean them(ChiTietPhieuMuonDTO ct) {
        return insert(ct);
    }

    public boolean insert(ChiTietPhieuMuonDTO ct) {
        String sql = "INSERT INTO ctpm_chi_tiet_pm(Ma_PM, Ma_Sach, So_Luong, Tinh_Trang_Sach, Da_Tra) VALUES(?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaPM());
            ps.setString(2, ct.getMaSach());
            ps.setInt(3, ct.getSoLuong());
            ps.setString(4, ct.getTinhTrangSach() == null || ct.getTinhTrangSach().trim().isEmpty() ? "Bình thường" : ct.getTinhTrangSach().trim());
            ps.setInt(5, ct.getDaTra());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<ChiTietPhieuMuonDTO> getByMaPM(String maPM) {
        ArrayList<ChiTietPhieuMuonDTO> list = new ArrayList<>();
        String sql = "SELECT Ma_PM, Ma_Sach, So_Luong, Tinh_Trang_Sach, Da_Tra FROM ctpm_chi_tiet_pm WHERE Ma_PM = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPM);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ChiTietPhieuMuonDTO ct = new ChiTietPhieuMuonDTO();
                    ct.setMaPM(rs.getString("Ma_PM"));
                    ct.setMaSach(rs.getString("Ma_Sach"));
                    ct.setSoLuong(rs.getInt("So_Luong"));
                    ct.setTinhTrangSach(rs.getString("Tinh_Trang_Sach"));
                    ct.setDaTra(rs.getInt("Da_Tra"));
                    list.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateTraSach(String maPM, String maSach) {
        String sql = "UPDATE ctpm_chi_tiet_pm SET Da_Tra = 1 WHERE Ma_PM = ? AND Ma_Sach = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPM);
            ps.setString(2, maSach);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean conSachChuaTra(String maPM) {
        String sql = "SELECT 1 FROM ctpm_chi_tiet_pm WHERE Ma_PM = ? AND Da_Tra = 0 LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPM);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //XÓA CHI TIẾT PHIẾU MƯỢN THEO Ma_PM 
    public boolean xoaByMaPM(String maPM) {
        String sql = "DELETE FROM ctpm_chi_tiet_pm WHERE Ma_PM = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPM);
            int result = ps.executeUpdate();
            System.out.println("[DEBUG] xoaByMaPM(" + maPM + ") - executeUpdate result: " + result);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Exception in xoaByMaPM: " + e.getMessage());
            System.err.println("[ERROR] SQL: " + sql);
            e.printStackTrace();
        }
        return false;
    }
}