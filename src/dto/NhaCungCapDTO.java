package dto;



public class NhaCungCapDTO {
    String maNCC;
    String tenNCC;
    String diaChi;
    String SDT;
    String email;
    public  NhaCungCapDTO() {}

    public NhaCungCapDTO(String maNCC,String tenNCC,String diaChi,String SDT,String email) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.diaChi = diaChi;
        this.SDT = SDT;
        this.email = email;
    }

    public NhaCungCapDTO(NhaCungCapDTO NCC) {
        this.maNCC = NCC.maNCC;
        this.tenNCC = NCC.tenNCC;;
        this.diaChi = NCC.diaChi;
        this.SDT = NCC.SDT;
        this.email = NCC.email;
    }

    public String getMaNCC()    {   return this.maNCC;  }
    public String getTenNCC()   {   return this.tenNCC; }
    public String getDiaChi()   {   return this.diaChi; }
    public String getSDT()      {   return this.SDT;    }
    public String getEmail()    {   return this.email;  }

    public void setMaNCC(String maNCC)      {   this.maNCC = maNCC;     }
    public void setTenNCC(String tenNCC)    {   this.tenNCC = tenNCC;   }
    public void setDiaChi(String diaChi)    {   this.diaChi = diaChi;   }
    public void setSDT(String SDT)          {   this.SDT = SDT;         }
    public void setEmail(String email)      {   this.email = email;     }
    
}   