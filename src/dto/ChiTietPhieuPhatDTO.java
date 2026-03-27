package dto;

public class ChiTietPhieuPhatDTO {
    private String maPP;
    private String maLyDoPhat;
    private String maSach;
    private long soTienPhat;

    public ChiTietPhieuPhatDTO() {
    }

    public ChiTietPhieuPhatDTO(String maPP, String maLyDoPhat, String maSach, long soTienPhat) {
        this.maPP = maPP;
        this.maLyDoPhat = maLyDoPhat;
        this.maSach = maSach;
        this.soTienPhat = soTienPhat;
    }

    public String getMaPP() {
        return maPP;
    }

    public void setMaPP(String maPP) {
        this.maPP = maPP;
    }

    public String getMaLyDoPhat() {
        return maLyDoPhat;
    }

    public void setMaLyDoPhat(String maLyDoPhat) {
        this.maLyDoPhat = maLyDoPhat;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public long getSoTienPhat() {
        return soTienPhat;
    }

    public void setSoTienPhat(long soTienPhat) {
        this.soTienPhat = soTienPhat;
    }
}
