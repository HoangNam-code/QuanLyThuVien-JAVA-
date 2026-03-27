package dto;

import java.util.Date;

public class PhieuNhapDTO {

    private String maPN;
    private String maNCC;
    private String maNV;
    private String ghiChu;
    private Date ngayNhap;
    private double tongTien;
    

    public PhieuNhapDTO() {
    }

    public PhieuNhapDTO(String maPN, String maNCC, String maNV, Date ngayNhap, double tongTien,String ghiChu) {
        this.maPN = maPN;
        this.maNCC = maNCC;
        this.maNV = maNV;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
    }
    public String getGhiChu()                   {   return this.ghiChu;     }   
    public void setGhiChu(String ghiChu)        {   this.ghiChu = ghiChu;   }

    public String getMaPN()          {        return maPN;          }
    public void setMaPN(String maPN) {        this.maPN = maPN;     }

    public String getMaNCC()            {        return maNCC;          }
    public void setMaNCC(String maNCC)  {        this.maNCC = maNCC;    }

    public String getMaNV()             {        return maNV;         }
    public void setMaNV(String maNV)    {        this.maNV = maNV;    }

    public Date getNgayNhap()                   {        return ngayNhap;             }
    public void setNgayNhap(Date ngayNhap)      {        this.ngayNhap = ngayNhap;    }

    public double getTongTien()                 {        return tongTien;               }
    public void setTongTien(double tongTien)    {        this.tongTien = tongTien;      }
}