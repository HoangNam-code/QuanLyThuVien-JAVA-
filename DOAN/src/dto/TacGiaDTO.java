package dto;

public class TacGiaDTO {
    private String maTG;
    private String hoDem;
    private String ten;

    public TacGiaDTO() {}

    public TacGiaDTO(String maTG, String hoDem, String ten) {
        this.maTG = maTG;
        this.hoDem = hoDem;
        this.ten = ten;
    }

    public String getMaTG() { return maTG; }
    public void setMaTG(String maTG) { this.maTG = maTG; }
    public String getHoDem() { return hoDem; }
    public void setHoDem(String hoDem) { this.hoDem = hoDem; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    
    // Hàm phụ để hiển thị Tên đầy đủ trên ComboBox
    public String getHoTen() { return hoDem + " " + ten; }
}
