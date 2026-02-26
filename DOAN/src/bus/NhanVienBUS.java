package bus;

import dao.NhanVienDAO;
import dto.NhanVienDTO;
import java.util.ArrayList;

public class NhanVienBUS {
    private NhanVienDAO nvDAO = new NhanVienDAO();
    private ArrayList<NhanVienDTO> listNV = null;

    public NhanVienBUS() {
        docDanhSach();
    }

    public void docDanhSach() {
        listNV = nvDAO.selectAll();
    }

    public ArrayList<NhanVienDTO> getList() {
        if (listNV == null) docDanhSach();
        return listNV;
    }

    public String themNhanVien(NhanVienDTO nv) {
        if (nv.getMaNV().isEmpty() || nv.getTen().isEmpty()) return "Mã và Tên không được để trống!";
        // Kiểm tra trùng mã
        for (NhanVienDTO x : listNV) {
            if (x.getMaNV().equals(nv.getMaNV())) return "Mã nhân viên đã tồn tại!";
        }
        if (nvDAO.insert(nv) > 0) {
            listNV.add(nv);
            return "Thêm thành công!";
        }
        return "Thêm thất bại!";
    }

    public ArrayList<NhanVienDTO> timKiem(String tuKhoa) {
        ArrayList<NhanVienDTO> ketQua = new ArrayList<>();
        
        // Chuyển từ khóa về chữ thường để tìm kiếm không phân biệt hoa thường
        String keyword = tuKhoa.toLowerCase();
        
        for (NhanVienDTO nv : listNV) {
            // Tìm theo Tên hoặc Mã nhân viên (tùy bạn chọn thuộc tính)
            if (nv.getTen().toLowerCase().contains(keyword) || 
                nv.getMaNV().toLowerCase().contains(keyword)) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }
    
    public String suaNhanVien(NhanVienDTO nv) {
        if (nvDAO.update(nv) > 0) {
            // Cập nhật lại trong list bộ nhớ đệm
            for (int i = 0; i < listNV.size(); i++) {
                if (listNV.get(i).getMaNV().equals(nv.getMaNV())) {
                    listNV.set(i, nv);
                    break;
                }
            }
            return "Cập nhật thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String xoaNhanVien(String maNV) {
        if (nvDAO.delete(maNV) > 0) {
            listNV.removeIf(x -> x.getMaNV().equals(maNV));
            return "Xóa thành công!";
        }
        return "Xóa thất bại (Có thể nhân viên này đang có Tài khoản hoặc Phiếu mượn)!";
    }
}