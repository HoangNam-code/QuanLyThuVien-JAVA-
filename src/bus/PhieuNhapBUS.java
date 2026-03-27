package bus;
import dao.*;
import dto.*;
import java.util.ArrayList;

public class PhieuNhapBUS {


    public PhieuNhapBUS() {

    }

    // hàm đọc phiếu nhập
    public ArrayList<PhieuNhapDTO> docPhieuNhap() {
        PhieuNhapDAO dataPN = new PhieuNhapDAO();
            return dataPN.docPhieuNhap();
        
    }

    // hàm đọc chi tiết phiếu nhập
    public ArrayList<ChiTietPhieuNhapDTO> docChiTietPhieuNhap(String ma) {
        PhieuNhapDAO data = new PhieuNhapDAO();
        return data.docChiTietPhieuNhap(ma);
    }

    // hàm thêm phiếu nhập
    public boolean themPhieuNhap(PhieuNhapDTO pn,ArrayList<ChiTietPhieuNhapDTO> DSctpn) {
        PhieuNhapDAO data = new PhieuNhapDAO();
        return data.themPhieuNhap(pn,DSctpn);
    }

    // hàm đọc kho 
    public ArrayList<SachDTO> docKho() {
        PhieuNhapDAO data = new PhieuNhapDAO();
        return data.docKho();
    }


    // hàm đọc nhà cung cấp 
    public ArrayList<NhaCungCapDTO> docNCC() {
        PhieuNhapDAO data = new PhieuNhapDAO();
        return data.docNCC();
    }


    // hàm tìm theo ngày và giá 
    public ArrayList<PhieuNhapDTO> timPhieuNhap(String tuNgay,String denNgay,String giaTu,String giaToi) {
        PhieuNhapDAO data = new PhieuNhapDAO();
        return data.timPhieuNhap(tuNgay, denNgay, giaTu, giaToi);
    }


}
