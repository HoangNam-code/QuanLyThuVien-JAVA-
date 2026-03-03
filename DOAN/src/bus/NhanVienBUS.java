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
        // Nếu chưa có Database, bạn có thể comment dòng dưới và mở comment phần dữ liệu giả để test giao diện
        listNV = nvDAO.selectAll(); 
        
        // --- DỮ LIỆU GIẢ (MỞ RA NẾU CHƯA KẾT NỐI CSDL) ---
        // if (listNV == null) listNV = new ArrayList<>();
        // if (listNV.isEmpty()) {
        //    listNV.add(new NhanVienDTO("NV01", "Nguyễn Văn", "A", java.sql.Date.valueOf("2000-01-01"), "Nam", "0909123456", "HCM", "a@gmail.com", "Quan Ly"));
        // }
    }

    public ArrayList<NhanVienDTO> getList() {
        if (listNV == null) docDanhSach();
        return listNV;
    }

    public String themNhanVien(NhanVienDTO nv) {
        if (nvDAO.insert(nv) > 0) {
            listNV.add(nv);
            return "Thêm thành công!";
        }
        return "Thêm thất bại!";
    }

    public String suaNhanVien(NhanVienDTO nv) {
        if (nvDAO.update(nv) > 0) {
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
        return "Xóa thất bại!";
    }

    // --- HÀM QUAN TRỌNG: TÌM KIẾM ---
    public ArrayList<NhanVienDTO> timKiem(String keyword) {
        ArrayList<NhanVienDTO> ketQua = new ArrayList<>();
        if (listNV == null) return ketQua;
        
        keyword = keyword.toLowerCase().trim();
        for (NhanVienDTO nv : listNV) {
            if (nv.getMaNV().toLowerCase().contains(keyword) || 
                nv.getTen().toLowerCase().contains(keyword) ||
                nv.getSdt().contains(keyword)) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }
    
    // Tìm kiếm nâng cao
    public ArrayList<NhanVienDTO> timKiemNangCao(String ma, String ho, String ten, String sdt, String email, 
                                                 String gioiTinh, String chucVu, 
                                                 java.sql.Date tuNgay, java.sql.Date denNgay) {
        ArrayList<NhanVienDTO> ketQua = new ArrayList<>();
        if (listNV == null) return ketQua;

        for (NhanVienDTO nv : listNV) {
            boolean match = true;

            // Nhóm 1: So sánh chuỗi (chứa từ khóa - contains)
            if (!ma.isEmpty() && !nv.getMaNV().toLowerCase().contains(ma.toLowerCase())) match = false;
            if (!ho.isEmpty() && !nv.getHoDem().toLowerCase().contains(ho.toLowerCase())) match = false;
            if (!ten.isEmpty() && !nv.getTen().toLowerCase().contains(ten.toLowerCase())) match = false;
            if (!sdt.isEmpty() && !nv.getSdt().contains(sdt)) match = false;
            if (!email.isEmpty() && !nv.getEmail().toLowerCase().contains(email.toLowerCase())) match = false;

            // Nhóm 2: So sánh chính xác (equals)
            if (!gioiTinh.equals("Tất cả") && !nv.getGioiTinh().equalsIgnoreCase(gioiTinh)) match = false;
            if (!chucVu.equals("Tất cả") && !nv.getChucVu().equalsIgnoreCase(chucVu)) match = false;

            // Nhóm 3: So sánh khoảng thời gian (Ngày sinh)
            if (nv.getNgaySinh() != null) {
                if (tuNgay != null && nv.getNgaySinh().before(tuNgay)) match = false;
                if (denNgay != null && nv.getNgaySinh().after(denNgay)) match = false;
            } else if (tuNgay != null || denNgay != null) {
                // Nếu tìm theo ngày mà nhân viên không có ngày sinh thì loại
                match = false; 
            }

            // Nếu vượt qua mọi điều kiện thì thêm vào list kết quả
            if (match) {
                ketQua.add(nv);
            }
        }
        return ketQua;
    }
}
