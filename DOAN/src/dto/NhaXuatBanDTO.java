package dto;

public class NhaXuatBanDTO {
    private String maNXB;
    private String tenNXB;

    public NhaXuatBanDTO() {}

    public NhaXuatBanDTO(String maNXB, String tenNXB) {
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
    }

    public String getMaNXB() { return maNXB; }
    public void setMaNXB(String maNXB) { this.maNXB = maNXB; }
    public String getTenNXB() { return tenNXB; }
    public void setTenNXB(String tenNXB) { this.tenNXB = tenNXB; }
}
