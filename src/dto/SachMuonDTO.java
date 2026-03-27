package dto;

public class SachMuonDTO {
    String maSach;
    String tenSach;
    int soLanMuon;

    public SachMuonDTO() {}

    public SachMuonDTO(String maSach,String tenSach,int soLanMuon) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.soLanMuon = soLanMuon;
    }

    public SachMuonDTO(SachMuonDTO sach) {
        this.maSach = sach.maSach;
        this.tenSach = sach.tenSach;
        this.soLanMuon = sach.soLanMuon;
    }

    public String getMaSach()       {   return this.maSach;     }
    public String getTenSach()      {   return this.tenSach;    }
    public int  getSoLanMuon()      {   return this.soLanMuon;  }

    public void setMaSach(String maSach)        {   this.maSach = maSach;       }
    public void setTenSach(String tenSach)      {   this.tenSach = tenSach;     }
    public void setSoLanMuon(int soLanMuon)     {   this.soLanMuon = soLanMuon; }

}
