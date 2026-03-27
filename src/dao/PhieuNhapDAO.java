package dao;

import config.DBconnection;
import dto.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PhieuNhapDAO {
    Connection conn = DBconnection.getConnection();


    // hàm đọc danh sách phieu nhap
    public ArrayList<PhieuNhapDTO> docPhieuNhap() {
    ArrayList<PhieuNhapDTO> DSpn = new ArrayList<>();
    try {

        String qry = "SELECT * FROM pn_phieu_nhap";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(qry);

        while (rs.next()) {

            PhieuNhapDTO pn = new PhieuNhapDTO();

            pn.setMaPN(rs.getString("Ma_PN"));
            pn.setMaNCC(rs.getString("Ma_NCC"));
            pn.setMaNV(rs.getString("Ma_NV"));      
            pn.setGhiChu(rs.getString("Ghi_Chu"));
            pn.setNgayNhap(rs.getDate("Ngay_Nhap"));
            pn.setTongTien(rs.getDouble("Tong_Tien"));
            

            DSpn.add(pn);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return DSpn;
}


// hàm đọc danh sách chi tiết phiếu nhập theo mã phiếu nhập
public ArrayList<ChiTietPhieuNhapDTO> docChiTietPhieuNhap(String maPN) {

    ArrayList<ChiTietPhieuNhapDTO> DSctpn = new ArrayList<>();

    try {

        String qry = "SELECT * FROM ctpn_chi_tiet_pn WHERE ma_PN = '" + maPN + "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(qry);

        while (rs.next()) {

            ChiTietPhieuNhapDTO ct = new ChiTietPhieuNhapDTO();

            ct.setMaPN(rs.getString("Ma_PN"));
            ct.setMaSach(rs.getString("Ma_Sach"));
            ct.setSoLuong(rs.getInt("So_Luong"));
            ct.setDonGiaNhap(rs.getDouble("Don_Gia_Nhap"));

            DSctpn.add(ct);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return DSctpn;
}

// Hàm đọc tồn kho sách
public ArrayList<SachDTO> docKho() {
    
    ArrayList<SachDTO> DSsach = new ArrayList<> ();
    try {
        String qry = "SELECT * FROM SACH";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(qry);

        while(rs.next()) {
            SachDTO sach = new SachDTO();
            sach.setMaSach(rs.getString("Ma_Sach"));                    
            sach.setTenSach(rs.getString("Ten_Sach"));
            sach.setSoLuong(rs.getInt("So_Luong"));
            DSsach.add(sach);
        }
    }
    catch(SQLException e) {
        JOptionPane.showMessageDialog(null, "loi");
    }
    return DSsach;
}

// hàm đọc mã nhà cung cấp
public ArrayList<NhaCungCapDTO> docNCC() {
    ArrayList<NhaCungCapDTO> DSncc = new ArrayList<> ();
    try {
        String qry = "SELECt * FROM nha_cung_cap";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(qry);

        while(rs.next()) {
            NhaCungCapDTO ncc = new NhaCungCapDTO();
            ncc.setMaNCC(rs.getString("Ma_NCC"));
            ncc.setTenNCC(rs.getString("Ten_NCC"));
            ncc.setDiaChi(rs.getString("Dia_Chi"));
            ncc.setSDT(rs.getString("SDT"));
            ncc.setEmail(rs.getString("Email"));
            DSncc.add(ncc);
        }
    }
    catch(SQLException e) {
        JOptionPane.showMessageDialog(null,"loi doc ma NCC");
    }
    return DSncc;

}

// hàm thêm phiếu nhập
public boolean themPhieuNhap(PhieuNhapDTO pn,ArrayList<ChiTietPhieuNhapDTO> ctpn) {

    try {
        String qry = "INSERT INTO pn_phieu_nhap(Ma_PN, Ma_NCC, Ngay_Nhap, Tong_Tien,Ghi_Chu,Ma_NV) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(qry);

        ps.setString(1, pn.getMaPN());
        ps.setString(2, pn.getMaNCC());
        ps.setDate(3, new java.sql.Date(pn.getNgayNhap().getTime()));
        ps.setDouble(4, pn.getTongTien());
        ps.setString(5,pn.getGhiChu());
        ps.setString(6,pn.getMaNV());

        int kq = ps.executeUpdate();
        if(kq > 0) {
            for(ChiTietPhieuNhapDTO ct: ctpn) {
                if(!themChiTietPhieuNhap(ct)) {
                    return false;
                }
            }
            return true;
        }

        

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;

}


// thêm chi tiêt phiếu nhập
    public boolean themChiTietPhieuNhap(ChiTietPhieuNhapDTO ct) {
    try {

        String sql = "INSERT INTO ctpn_chi_tiet_pn(Ma_PN, Ma_Sach, So_Luong, Don_Gia_Nhap,Thanh_Tien) VALUES(?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, ct.getMaPN());
        ps.setString(2, ct.getMaSach());
        ps.setInt(3, ct.getSoLuong());
        ps.setDouble(4, ct.getDonGiaNhap());
        ps.setDouble(5,ct.getThanhTien());

        ps.executeUpdate();

        String sql2 = "UPDATE SACH SET So_Luong = So_Luong + ? WHERE Ma_Sach = ?";
        PreparedStatement ps2 = conn.prepareStatement(sql2);

        ps2.setInt(1, ct.getSoLuong());
        ps2.setString(2, ct.getMaSach());

        return ps2.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}



// tìm kiếm phiếu nhập theo ngày và giờ 

public ArrayList<PhieuNhapDTO> timPhieuNhap(String tuNgay,String denNgay,String giaTu,String giaToi) {

    ArrayList<PhieuNhapDTO> DSpn = new ArrayList<> ();
    try {

    String qry = "SElECT * FROM pn_phieu_nhap WHERE 1=1";
    int index = 1;

    if(!tuNgay.equals("") && !denNgay.equals("")) {
        qry = qry + " AND Ngay_Nhap BETWEEN ? AND ?";
    }
    if(!giaTu.equals("") && !giaToi.equals("")) {
        qry = qry + " AND Tong_Tien BETWEEN ? AND ?";
    }
    
    PreparedStatement ps = conn.prepareStatement(qry);


    if(!tuNgay.equals("") && !denNgay.equals("")) {
        ps.setDate(index ++,java.sql.Date.valueOf(tuNgay));
        ps.setDate(index ++,java.sql.Date.valueOf(denNgay));       
    }
    if(!giaTu.equals("") && !giaToi.equals("")) {
        ps.setDouble(index ++,Double.parseDouble(giaTu));
        ps.setDouble(index ++,Double.parseDouble(giaToi));
    }
    ResultSet rs = ps.executeQuery();

    while(rs.next()) {
        PhieuNhapDTO pn = new PhieuNhapDTO();
        pn.setMaPN(rs.getString("Ma_PN"));
        pn.setMaNCC(rs.getString("Ma_NCC"));
        pn.setMaNV(rs.getString("Ma_NV"));
        pn.setGhiChu(rs.getString("Ghi_Chu"));
        pn.setNgayNhap(rs.getDate("Ngay_Nhap"));
        pn.setTongTien(rs.getDouble("Tong_Tien"));
        DSpn.add(pn);
    }



    }
    catch(SQLException e) {
        JOptionPane.showMessageDialog(null,"lỗi DAO");
    }
    return DSpn;
}


}
