package dto;

public class ChiTietPhieuNhapDTO {

    private String maPN;
    private String maSach;
    private int soLuong;
    private double donGia;

    public ChiTietPhieuNhapDTO() {
    }

    public ChiTietPhieuNhapDTO(String maPN, String maSach, int soLuong, double donGia) {
        this.maPN = maPN;
        this.maSach = maSach;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Double getThanhTien()      {   return donGia * soLuong;}
    public String getMaPN() {        return maPN;    }
    public void setMaPN(String maPN) {        this.maPN = maPN;    }

    public String getMaSach() {        return maSach;    }
    public void setMaSach(String maSach) {        this.maSach = maSach;    }

    public int getSoLuong() {        return soLuong;    }
    public void setSoLuong(int soLuong) {        this.soLuong = soLuong;    }

    public double getDonGiaNhap() {        return donGia;    }
    public void setDonGiaNhap(double donGia) {        this.donGia = donGia;    }
}