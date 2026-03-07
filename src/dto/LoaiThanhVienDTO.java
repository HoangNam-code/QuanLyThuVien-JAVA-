package dto;

public class LoaiThanhVienDTO {
    private String maLoaiTV;
    private String tenLoaiTV;
    private double phi;

    public LoaiThanhVienDTO() {}

    public LoaiThanhVienDTO(String maLoaiTV, String tenLoaiTV, double phi) {
        this.maLoaiTV = maLoaiTV;
        this.tenLoaiTV = tenLoaiTV;
        this.phi = phi;
    }

    public String getMaLoaiTV() { return maLoaiTV; }
    public void setMaLoaiTV(String maLoaiTV) { this.maLoaiTV = maLoaiTV; }
    public String getTenLoaiTV() { return tenLoaiTV; }
    public void setTenLoaiTV(String tenLoaiTV) { this.tenLoaiTV = tenLoaiTV; }
    public double getPhi() { return phi; }
    public void setPhi(double phi) { this.phi = phi; }
}
