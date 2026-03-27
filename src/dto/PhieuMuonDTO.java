package dto;

public class PhieuMuonDTO {

    private String maPM, maDG, maNV, ngayMuon, ngayTraDuKien, ngayThucTra, ghiChu;

    public PhieuMuonDTO() {}

    public PhieuMuonDTO(String maPM,
                        String maDG,
                        String maNV,
                        String ngayMuon,
                        String ngayTraDuKien) {

        this.maPM = maPM;
        this.maDG = maDG;
        this.maNV = maNV;
        this.ngayMuon = ngayMuon;
        this.ngayTraDuKien = ngayTraDuKien;
    }

    public PhieuMuonDTO(String maPM,
                        String maDG,
                        String maNV,
                        String ngayMuon,
                        String ngayTraDuKien,
                        String ngayThucTra,
                        String ghiChu) {

        this.maPM = maPM;
        this.maDG = maDG;
        this.maNV = maNV;
        this.ngayMuon = ngayMuon;
        this.ngayTraDuKien = ngayTraDuKien;
        this.ngayThucTra = ngayThucTra;
        this.ghiChu = ghiChu;
    }

    public String getMaPM() { return maPM; }
    public void setMaPM(String maPM) { this.maPM = maPM; }

    public String getMaDG() { return maDG; }
    public void setMaDG(String maDG) { this.maDG = maDG; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(String ngayMuon) { this.ngayMuon = ngayMuon; }

    public String getNgayTraDuKien() { return ngayTraDuKien; }
    public void setNgayTraDuKien(String ngayTraDuKien) {
        this.ngayTraDuKien = ngayTraDuKien;
    }

    public String getNgayThucTra() { return ngayThucTra; }
    public void setNgayThucTra(String ngayThucTra) { this.ngayThucTra = ngayThucTra; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}