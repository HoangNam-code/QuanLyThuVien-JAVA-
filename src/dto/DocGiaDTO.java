package dto;

import java.sql.Date;

public class DocGiaDTO {
    private String maDG;
    private String hoDem;
    private String ten;
    private String gioiTinh;
    private Date ngaySinh;
    private String email;
    private String sdt;
    private String diaChi;
    private int soSachMuon;
    private Date ngayHetHan;
    private double tienPhiThanhVien;
    private String maLoaiTV;

    public DocGiaDTO() {}

    // Constructor đầy đủ để lấy dữ liệu từ DB lên
    public DocGiaDTO(String maDG, String hoDem, String ten, String gioiTinh, Date ngaySinh, String email, String sdt, String diaChi, int soSachMuon, Date ngayHetHan, double tienPhiThanhVien, String maLoaiTV) {
        this.maDG = maDG;
        this.hoDem = hoDem;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.email = email;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.soSachMuon = soSachMuon;
        this.ngayHetHan = ngayHetHan;
        this.tienPhiThanhVien = tienPhiThanhVien;
        this.maLoaiTV = maLoaiTV;
    }

    // Getters và Setters
    public String getMaDG() { return maDG; }
    public void setMaDG(String maDG) { this.maDG = maDG; }
    public String getHoDem() { return hoDem; }
    public void setHoDem(String hoDem) { this.hoDem = hoDem; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public int getSoSachMuon() { return soSachMuon; }
    public void setSoSachMuon(int soSachMuon) { this.soSachMuon = soSachMuon; }
    public Date getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(Date ngayHetHan) { this.ngayHetHan = ngayHetHan; }
    public double getTienPhiThanhVien() { return tienPhiThanhVien; }
    public void setTienPhiThanhVien(double tienPhiThanhVien) { this.tienPhiThanhVien = tienPhiThanhVien; }
    public String getMaLoaiTV() { return maLoaiTV; }
    public void setMaLoaiTV(String maLoaiTV) { this.maLoaiTV = maLoaiTV; }
}