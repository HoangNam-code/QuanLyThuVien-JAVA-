package dto;

public class TaiKhoanDTO {
    private String tenDangNhap;
    private String matKhau;
    private String maNV;
    private String quyenHan;
    private String trangThai; // "Hiệu lực" hoặc "Bị khóa"

    public TaiKhoanDTO() {}

    public TaiKhoanDTO(String tenDangNhap, String matKhau, String maNV, String quyenHan, String trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maNV = maNV;
        this.quyenHan = quyenHan;
        this.trangThai = trangThai;
    }

    // --- GETTER & SETTER ---
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getQuyenHan() { return quyenHan; }
    public void setQuyenHan(String quyenHan) { this.quyenHan = quyenHan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}