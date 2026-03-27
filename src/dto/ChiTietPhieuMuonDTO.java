package dto;

public class ChiTietPhieuMuonDTO {

    private String maPM;
    private String maSach;
    private int soLuong;
    private String tinhTrangSach;
    private int daTra;

    public String getMaPM() {
        return maPM;
    }

    public void setMaPM(String maPM) {
        this.maPM = maPM;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTinhTrangSach() {
        return tinhTrangSach;
    }

    public void setTinhTrangSach(String tinhTrangSach) {
        this.tinhTrangSach = tinhTrangSach;
    }

    public int getDaTra() {
        return daTra;
    }

    public void setDaTra(int daTra) {
        this.daTra = daTra;
    }
}