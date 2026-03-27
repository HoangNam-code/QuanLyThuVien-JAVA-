package dao;
import config.DBconnection;
import dto.SachMuonDTO;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class ThongKeDAO {

    public ThongKeDAO() {}
    Connection conn = DBconnection.getConnection();


    // tong so luong sach
    public int docSach() {
        int soLuong = 0;
        try {
            String qry = "SELECT COUNT(*) FROM SACH";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(qry);

            if(rs.next()) {
                soLuong = rs.getInt(1);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"lỗi đọc sách");
        }
        return soLuong;
    }

    // tong so doc gia 
    public int docDocGia() {
        int soLuong = 0;
        
        try {
            String qry = "SELECT COUNT(*) FROM doc_gia";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            
            if(rs.next()) {
                soLuong = rs.getInt(1);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"loi doc gia");
        }

        return soLuong;
    }



    // tong so luong nhan vien
    public int docNhanVien() {
        int soLuong = 0;
        try {
            String qry = "SELECT COUNT(*) FROM nhan_vien";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(qry);

            if(rs.next()) {
                soLuong = rs.getInt(1);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"loi doc nhan vien");
        }
        return soLuong;
    }

    // tinh doanh thu 
    public double docDoanhThu() {
        double doanhThu= 0.0;

        try {
        String qry = "SELECT " + 
                        "IFNULL((SELECT SUM(Tien_Phi_Thanh_Vien) FROM doc_gia),0) " + 
                        "+" + " IFNULL((SELECT SUM(Tong_tien) FROM pp_phieu_phat),0)" + 
                        " AS TongDoanhThu";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(qry);
            if(rs.next()) {
                doanhThu = rs.getDouble("TongDoanhThu");
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"loi doc doanh thu");
        }
        return doanhThu;
    }

    // top sach muon nhieu nhat
    public ArrayList<SachMuonDTO> docTopSach()  {
        ArrayList<SachMuonDTO> DSsach = new ArrayList<> ();
        try {
            String qry = "SELECT s.Ma_Sach,s.Ten_Sach,COUNT(CT.Ma_Sach) AS So_Lan_Muon " + 
                        "FROM ctpm_chi_tiet_pm ct " + 
                        "JOIN Sach s ON ct.Ma_Sach = s.Ma_sach " +
                        "GROUP BY s.Ma_sach,s.Ten_Sach " + 
                        "ORDER BY So_Lan_Muon DESC LIMIT 5";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(qry);

            while(rs.next()) {
                SachMuonDTO sach = new SachMuonDTO();
                sach.setMaSach(rs.getString("Ma_Sach"));
                sach.setTenSach(rs.getString("Ten_Sach"));
                sach.setSoLanMuon(rs.getInt("So_Lan_Muon"));
                DSsach.add(sach);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null,"Loi doc top sach");
        }
        return DSsach;
    }

        

    // doanh thu theo quý
    public ArrayList<Double> docTheoQuy(int nam) {
        ArrayList<Double> DSquy = new ArrayList<> ();
        int quy = 1;
        try {
            while(quy <= 4) {
                double doanhThu = 0.0;
                String qry = "SELECT " +
                    " IFNULL((SELECT SUM(Tien_Phi_Thanh_Vien) FROM doc_gia " +
                    "   WHERE QUARTER(Ngay_Dang_Ky)=? AND YEAR(Ngay_Dang_Ky)=?),0) " +
                    " + " +
                    " IFNULL((SELECT SUM(Tong_Tien) FROM pp_phieu_phat " +
                    "   WHERE QUARTER(Ngay_Ghi)=? AND YEAR(Ngay_Ghi)=?),0) " +
                    " AS DoanhThu";
                PreparedStatement ps = conn.prepareStatement(qry);

                ps.setInt(1, quy);
                ps.setInt(2, nam);
                ps.setInt(3, quy);
                ps.setInt(4, nam);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    doanhThu = rs.getDouble("DoanhThu");
                }

                DSquy.add(doanhThu);
                quy++;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return DSquy;
    }


    public ArrayList<Object[]> docChiTietTienDocGiaTongTheoQuy(int nam) {
        ArrayList<Object[]> list = new ArrayList<>();
        try {
            String qry = """
                SELECT 
                    dg.Ma_DG AS Ma_DG,
                    dg.Ten AS Ten_DG,
                    SUM(CASE WHEN QUARTER(dg.Ngay_Dang_Ky)=1 THEN dg.Tien_Phi_Thanh_Vien ELSE 0 END) +
                    SUM(CASE WHEN QUARTER(pp.Ngay_Ghi)=1 THEN COALESCE(pp.Tong_Tien,0) ELSE 0 END) AS Q1,
                    
                    SUM(CASE WHEN QUARTER(dg.Ngay_Dang_Ky)=2 THEN dg.Tien_Phi_Thanh_Vien ELSE 0 END) +
                    SUM(CASE WHEN QUARTER(pp.Ngay_Ghi)=2 THEN COALESCE(pp.Tong_Tien,0) ELSE 0 END) AS Q2,
                    
                    SUM(CASE WHEN QUARTER(dg.Ngay_Dang_Ky)=3 THEN dg.Tien_Phi_Thanh_Vien ELSE 0 END) +
                    SUM(CASE WHEN QUARTER(pp.Ngay_Ghi)=3 THEN COALESCE(pp.Tong_Tien,0) ELSE 0 END) AS Q3,
                    
                    SUM(CASE WHEN QUARTER(dg.Ngay_Dang_Ky)=4 THEN dg.Tien_Phi_Thanh_Vien ELSE 0 END) +
                    SUM(CASE WHEN QUARTER(pp.Ngay_Ghi)=4 THEN COALESCE(pp.Tong_Tien,0) ELSE 0 END) AS Q4,
                    
                    SUM(dg.Tien_Phi_Thanh_Vien) + SUM(COALESCE(pp.Tong_Tien,0)) AS Tong
                    
                FROM doc_gia dg
                LEFT JOIN pp_phieu_phat pp ON dg.Ma_DG = pp.Ma_DG
                WHERE YEAR(dg.Ngay_Dang_Ky) = ? OR YEAR(pp.Ngay_Ghi) = ?
                GROUP BY dg.Ma_DG, dg.Ten
                ORDER BY Tong DESC
                """;
            PreparedStatement ps = conn.prepareStatement(qry);
            ps.setInt(1, nam);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("Ma_DG"),
                    rs.getString("Ten_DG"),
                    rs.getDouble("Q1"), rs.getDouble("Q2"),
                    rs.getDouble("Q3"), rs.getDouble("Q4"),
                    rs.getDouble("Tong")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi đọc tổng tiền độc giả");
        }
        return list;
    }

    public ArrayList<Object[]> docChiTietTienNhanVienTheoQuy(int nam) {
        ArrayList<Object[]> list = new ArrayList<>();
        try {
            String qry = """
                SELECT Ma_NV AS Ma_Nhan_Vien, 
                       Ten, 
                       Luong AS Luong_Co_Ban,
                       (Luong / 3.0) AS LuongQuy
                FROM nhan_vien 
                WHERE Ngay_Vao_Lam <= LAST_DAY(MAKEDATE(?, 12)) 
                  AND (Ngay_Nghi_Viec IS NULL OR Ngay_Nghi_Viec >= MAKEDATE(?, 1))
                """;
            PreparedStatement ps = conn.prepareStatement(qry);
            ps.setInt(1, nam);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double luongQuy = rs.getDouble("LuongQuy");
                list.add(new Object[]{
                    rs.getString("Ma_Nhan_Vien"),
                    rs.getString("Ten"),
                    luongQuy, luongQuy, luongQuy, luongQuy,
                    luongQuy * 4
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi đọc lương nhân viên");
        }
        return list;
    }


    public ArrayList<Object[]> docChiTietSachMuonTheoQuy(int nam) {
        ArrayList<Object[]> list = new ArrayList<>();
        try {
            String qry = """
                SELECT s.Ma_Sach, s.Ten_Sach,
                       SUM(CASE WHEN QUARTER(pm.Ngay_Muon)=1 THEN 1 ELSE 0 END) AS Q1,
                       SUM(CASE WHEN QUARTER(pm.Ngay_Muon)=2 THEN 1 ELSE 0 END) AS Q2,
                       SUM(CASE WHEN QUARTER(pm.Ngay_Muon)=3 THEN 1 ELSE 0 END) AS Q3,
                       SUM(CASE WHEN QUARTER(pm.Ngay_Muon)=4 THEN 1 ELSE 0 END) AS Q4,
                       COUNT(*) AS Tong
                FROM ctpm_chi_tiet_pm ct
                JOIN pm_phieu_muon pm ON ct.Ma_PM = pm.Ma_PM
                JOIN sach s ON ct.Ma_Sach = s.Ma_Sach
                WHERE YEAR(pm.Ngay_Muon) = ?
                GROUP BY s.Ma_Sach, s.Ten_Sach
                ORDER BY Tong DESC
                """;
            PreparedStatement ps = conn.prepareStatement(qry);
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("Ma_Sach"),
                    rs.getString("Ten_Sach"),
                    rs.getInt("Q1"), rs.getInt("Q2"),
                    rs.getInt("Q3"), rs.getInt("Q4"),
                    rs.getInt("Tong")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi đọc sách mượn theo quý");
        }
        return list;
    }


}


