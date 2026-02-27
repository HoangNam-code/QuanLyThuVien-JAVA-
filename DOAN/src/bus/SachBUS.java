package bus;

import dao.SachDAO;
import dto.SachDTO;
import java.util.ArrayList;

public class SachBUS {
    private SachDAO sachDAO = new SachDAO();
    private ArrayList<SachDTO> listSach = null;

    public SachBUS() {
        docDanhSach();
    }

    public void docDanhSach() {
        listSach = sachDAO.selectAll();
    }

    public ArrayList<SachDTO> getList() {
        if (listSach == null) docDanhSach();
        return listSach;
    }

    public String themSach(SachDTO s) {
        if (s.getMaSach().isEmpty() || s.getTenSach().isEmpty()) return "Mã và Tên sách không được để trống!";
        // Kiểm tra trùng mã
        for (SachDTO x : listSach) {
            if (x.getMaSach().equals(s.getMaSach())) return "Mã sách đã tồn tại!";
        }
        
        if (sachDAO.insert(s) > 0) {
            listSach.add(s); // Thêm vào RAM
            return "Thêm sách thành công!";
        }
        return "Thêm sách thất bại!";
    }

    public String suaSach(SachDTO s) {
        if (sachDAO.update(s) > 0) {
            // Cập nhật lại list trên RAM
            for (int i = 0; i < listSach.size(); i++) {
                if (listSach.get(i).getMaSach().equals(s.getMaSach())) {
                    listSach.set(i, s);
                    break;
                }
            }
            return "Cập nhật sách thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String xoaSach(String maSach) {
        if (sachDAO.delete(maSach) > 0) {
            listSach.removeIf(x -> x.getMaSach().equals(maSach));
            return "Xóa sách thành công!";
        }
        return "Xóa thất bại (Sách có thể đang nằm trong Phiếu Mượn/Nhập)!";
    }

    // --- [Mục 2] TÌM KIẾM TRÊN RAM ---
    public ArrayList<SachDTO> timKiemTrenRAM(String keyword) {
        ArrayList<SachDTO> ketQua = new ArrayList<>();
        keyword = keyword.toLowerCase().trim();
        for (SachDTO s : listSach) {
            if (s.getTenSach().toLowerCase().contains(keyword) || 
                s.getMaSach().toLowerCase().contains(keyword)) {
                ketQua.add(s);
            }
        }
        return ketQua;
    }

    // --- [Mục 9] TÌM KIẾM NÂNG CAO BẰNG SQL ---
    public ArrayList<SachDTO> timKiemNangCao(String ten, String maTL, String maTG, double min, double max) {
        return sachDAO.selectByCondition(ten, maTL, maTG, min, max);
    }
}
