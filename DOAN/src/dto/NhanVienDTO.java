package dto;

import java.sql.Date;

public class NhanVienDTO {
    private String maNV;
    private String hoDem;
    private String ten;
    private Date ngaySinh;
    private String gioiTinh;
    private String sdt;
    private String diaChi;
    private String email;
    private String chucVu;

    public NhanVienDTO() {}

    public NhanVienDTO(String maNV, String hoDem, String ten, Date ngaySinh, String gioiTinh, String sdt, String diaChi, String email, String chucVu) {
        this.maNV = maNV;
        this.hoDem = hoDem;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
        this.chucVu = chucVu;
    }

    // Getter và Setter (Bắt buộc)
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoDem() { return hoDem; }
    public void setHoDem(String hoDem) { this.hoDem = hoDem; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    
    // Hàm phụ lấy Họ tên đầy đủ
    public String getHoTen() { return hoDem + " " + ten; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
}