package dto;

public class SachDTO {
    private String maSach;
    private String tenSach;
    private int namXB;
    private String maTL;
    private double donGia;
    private int soLuong;
    private String maTG;
    private String maNXB;
    private int soTrang;
    
    public SachDTO() {}

    public SachDTO (String maSach, String tenSach, int namXB, String maTL,double donGia, int soLuong, String maTG, String maNXB, int soTrang ){
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.namXB = namXB;
        this.maTL = maTL;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.maTG = maTG;
        this.maNXB = maNXB;
        this.soTrang = soTrang;
    }

    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }
    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }
    public int getNamXB() { return namXB; }
    public void setNamXB(int namXB) { this.namXB = namXB; }
    public String getMaTL() { return maTL; }
    public void setMaTL(String maTL) { this.maTL = maTL; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public String getMaTG() { return maTG; }
    public void setMaTG(String maTG) { this.maTG = maTG; }
    public String getMaNXB() { return maNXB; }
    public void setMaNXB(String maNXB) { this.maNXB = maNXB; }
    public int getSoTrang() { return soTrang; }
    public void setSoTrang(int soTrang) { this.soTrang = soTrang; }


    
}