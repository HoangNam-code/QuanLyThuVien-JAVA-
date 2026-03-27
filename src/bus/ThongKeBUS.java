package bus;
import dto.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import dao.SachDAO;
import dao.ThongKeDAO;

public class ThongKeBUS {

    public int docSach() {
        ThongKeDAO data = new ThongKeDAO();
        return data.docSach();
    }

    public int docDocGia() {
        ThongKeDAO data = new ThongKeDAO();
        return data.docDocGia();
    }

    public int docNhanVien()    {
        ThongKeDAO data = new ThongKeDAO();
        return data.docNhanVien();
    }

    public double docDoanhThu() {
        ThongKeDAO data = new ThongKeDAO();
        return data.docDoanhThu();
    }

    public ArrayList<Double> docTheoQuy(int nam) {
        ThongKeDAO data = new ThongKeDAO();
        return data.docTheoQuy(nam);
    }

    public ArrayList<SachMuonDTO> docSachTop() {
        ThongKeDAO data = new ThongKeDAO();
        return data.docTopSach();
    }

    
    public ArrayList<Object[]> docChiTietTienDocGiaTongTheoQuy(int nam) {
        return new ThongKeDAO().docChiTietTienDocGiaTongTheoQuy(nam);
    }

    public ArrayList<Object[]> docChiTietTienNhanVienTheoQuy(int nam) {
        return new ThongKeDAO().docChiTietTienNhanVienTheoQuy(nam);
    }

    public ArrayList<Object[]> docChiTietSachMuonTheoQuy(int nam) {
        return new ThongKeDAO().docChiTietSachMuonTheoQuy(nam);
    }
}
