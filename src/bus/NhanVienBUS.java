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
//THÊM nv
    public String themNhanVien(NhanVienDTO nv) {
        for (NhanVienDTO n : getList()) {
            if (n.getMaNV().trim().equalsIgnoreCase(nv.getMaNV().trim())) {
                return "Mã nhân viên đã tồn tại! Vui lòng nhập mã khác.";
            }
        }
        if (nvDAO.insert(nv) > 0) {
            listNV.add(nv);
            return "Thêm thành công!";
        }
        return "Thêm thất bại!";
    }
//SỬA nv
    public String suaNhanVien(NhanVienDTO nv) {
        if (nvDAO.update(nv) > 0) {
            for (int i = 0; i < listNV.size(); i++) {
                if (listNV.get(i).getMaNV().equalsIgnoreCase(nv.getMaNV())) {
                    nv.setTrangThaiTaiKhoan(listNV.get(i).getTrangThaiTaiKhoan());
                    listNV.set(i, nv);
                    break;
                }
            }
            return "Cập nhật thành công!";
        }
        return "Cập nhật thất bại!";
    }
//XOÁ nv
    public String xoaNhanVien(String maNV) {
        if (nvDAO.delete(maNV) > 0) {
            listNV.removeIf(n -> n.getMaNV().equalsIgnoreCase(maNV));
            return "Xóa thành công!";
        }
        return "Xóa thất bại!";
    }
//TÌM KIẾM nv
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

// Hàm tìm kiếm nâng cao
    public ArrayList<NhanVienDTO> timKiemNangCao(String ma, String ho, String ten, String sdt, String email, 
                                                 String gioiTinh, String chucVu, 
                                                 java.sql.Date tuNgay, java.sql.Date denNgay) {
        ArrayList<NhanVienDTO> ketQua = new ArrayList<>();
        if (listNV == null) return ketQua;
        for (NhanVienDTO nv : listNV) {
            boolean match = true;
            if (!ma.isEmpty() && !nv.getMaNV().toLowerCase().contains(ma.toLowerCase())) match = false;
            if (!ho.isEmpty() && !nv.getHoDem().toLowerCase().contains(ho.toLowerCase())) match = false;
            if (!ten.isEmpty() && !nv.getTen().toLowerCase().contains(ten.toLowerCase())) match = false;
            if (!sdt.isEmpty() && !nv.getSdt().contains(sdt)) match = false;
            if (!email.isEmpty() && !nv.getEmail().toLowerCase().contains(email.toLowerCase())) match = false;
            if (!gioiTinh.equals("Tất cả") && !nv.getGioiTinh().equalsIgnoreCase(gioiTinh)) match = false;
            if (!chucVu.equals("Tất cả") && !nv.getChucVu().equalsIgnoreCase(chucVu)) match = false;
            if (nv.getNgaySinh() != null) {
                if (tuNgay != null && nv.getNgaySinh().before(tuNgay)) match = false;
                if (denNgay != null && nv.getNgaySinh().after(denNgay)) match = false;
            } else if (tuNgay != null || denNgay != null) {
                match = false; 
            }
            if (match) ketQua.add(nv);
        }
        return ketQua;
    }
}