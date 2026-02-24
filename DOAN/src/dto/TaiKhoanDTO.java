package dto;

public class TaiKhoanDTO {
    private String tenDangNhap;
    private String maNV;
    private String matKhau;
    private String quyenHan; 
    private int trangThai;   

    public TaiKhoanDTO() {}
    
    public TaiKhoanDTO(String tenDangNhap, String maNV, String matKhau, String quyenHan, int trangThai) {
        this.tenDangNhap = tenDangNhap;
        this.maNV = maNV;
        this.matKhau = matKhau;
        this.quyenHan = quyenHan;
        this.trangThai = trangThai;
    }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getQuyenHan() { return quyenHan; }
    public void setQuyenHan(String quyenHan) { this.quyenHan = quyenHan; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "TaiKhoanDTO{" + "user=" + tenDangNhap + ", quyen=" + quyenHan + '}';
    }
}