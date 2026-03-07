package dto;

public class LyDoPhatDTO {
    private String maLyDoPhat;
    private String tenLyDoPhat;
    private String cachTinhPhat;
    private String ghiChu;

    public LyDoPhatDTO() {}

    public LyDoPhatDTO(String maLyDoPhat, String tenLyDoPhat, String cachTinhPhat, String ghiChu) {
        this.maLyDoPhat = maLyDoPhat;
        this.tenLyDoPhat = tenLyDoPhat;
        this.cachTinhPhat = cachTinhPhat;
        this.ghiChu = ghiChu;
    }

    public String getMaLyDoPhat() { return maLyDoPhat; }
    public void setMaLyDoPhat(String maLyDoPhat) { this.maLyDoPhat = maLyDoPhat; }
    public String getTenLyDoPhat() { return tenLyDoPhat; }
    public void setTenLyDoPhat(String tenLyDoPhat) { this.tenLyDoPhat = tenLyDoPhat; }
    public String getCachTinhPhat() { return cachTinhPhat; }
    public void setCachTinhPhat(String cachTinhPhat) { this.cachTinhPhat = cachTinhPhat; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
