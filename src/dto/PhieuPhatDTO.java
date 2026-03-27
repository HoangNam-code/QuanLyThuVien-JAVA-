package dto;

public class PhieuPhatDTO {
    private String maPP;
    private String maPM;
    private String maDG; 
    private String maNV;
    private String ngayGhi;
    private String lyDo;
    private long tongTien;

    public PhieuPhatDTO() {
    }

    public PhieuPhatDTO(String maPP, String maPM, String maDG, String maNV, String ngayGhi, String lyDo, long tongTien) {
        this.maPP = maPP;
        this.maPM = maPM;
        this.maDG = maDG; 
        this.maNV = maNV;
        this.ngayGhi = ngayGhi;
        this.lyDo = lyDo;
        this.tongTien = tongTien;
    }

    public String getMaPP() { return maPP; }
    public void setMaPP(String maPP) { this.maPP = maPP; }

    public String getMaPM() { return maPM; }
    public void setMaPM(String maPM) { this.maPM = maPM; }

    public String getMaDG() { return maDG; }
    public void setMaDG(String maDG) { this.maDG = maDG; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getNgayGhi() { return ngayGhi; }
    public void setNgayGhi(String ngayGhi) { this.ngayGhi = ngayGhi; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    public long getTongTien() { return tongTien; }
    public void setTongTien(long tongTien) { this.tongTien = tongTien; }
}