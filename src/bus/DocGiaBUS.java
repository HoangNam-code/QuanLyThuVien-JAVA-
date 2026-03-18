package bus;

import dao.DocGiaDAO;
import dto.DocGiaDTO;
import java.util.ArrayList;
import java.util.Date;

public class DocGiaBUS {
    private DocGiaDAO dgDAO = new DocGiaDAO();
    private ArrayList<DocGiaDTO> listDocGia = new ArrayList<>();

    public DocGiaBUS() {
        docList();
    }

    public void docList() {
        this.listDocGia = dgDAO.selectAll();
    }

    public ArrayList<DocGiaDTO> getList() {
        if (listDocGia == null) {
            docList();
        }
        return listDocGia;
    }

    // --- CÁC HÀM THÊM, SỬA, XÓA ---

    public String add(DocGiaDTO dg) {
        // Có thể thêm code kiểm tra trùng mã độc giả ở đây nếu cần
        for (DocGiaDTO d : listDocGia) {
            if (d.getMaDG().equals(dg.getMaDG())) {
                return "Mã độc giả đã tồn tại!";
            }
        }
        
        if (dgDAO.insert(dg)) {
            listDocGia.add(dg);
            return "Thêm độc giả thành công!";
        }
        return "Thêm độc giả thất bại!";
    }

    public String update(DocGiaDTO dg) {
        if (dgDAO.update(dg)) {
            docList(); // Lấy lại dữ liệu mới nhất từ DB
            return "Cập nhật thông tin thành công!";
        }
        return "Cập nhật thông tin thất bại!";
    }

    public String delete(String maDG) {
        if (dgDAO.delete(maDG)) {
            docList(); // Lấy lại dữ liệu mới nhất từ DB
            return "Xóa độc giả thành công!";
        }
        return "Xóa độc giả thất bại!";
    }

    // --- CÁC HÀM TÌM KIẾM ---

    // Tìm kiếm cơ bản (Theo Mã, Tên hoặc SĐT)
    public ArrayList<DocGiaDTO> timKiem(String tuKhoa) {
        ArrayList<DocGiaDTO> kq = new ArrayList<>();
        tuKhoa = tuKhoa.toLowerCase();
        for (DocGiaDTO dg : listDocGia) {
            if (dg.getMaDG().toLowerCase().contains(tuKhoa) ||
                dg.getTen().toLowerCase().contains(tuKhoa) ||
                dg.getSdt().contains(tuKhoa)) {
                kq.add(dg);
            }
        }
        return kq;
    }

    // Tìm kiếm nâng cao (Kết hợp Panel để lọc)
    public ArrayList<DocGiaDTO> timKiemNangCao(Date tuNgay, Date denNgay, String gioiTinh, String loaiTV, Double tuPhi, Double denPhi) {
        ArrayList<DocGiaDTO> kq = new ArrayList<>();
        
        for (DocGiaDTO dg : listDocGia) {
            boolean matchNgay = true;
            boolean matchGioiTinh = true;
            boolean matchLoaiTV = true;
            boolean matchPhi = true;

            // 1. Lọc theo khoảng ngày hết hạn
            if (tuNgay != null || denNgay != null) {
                Date ngayHH = dg.getNgayHetHan();
                if (ngayHH == null) {
                    matchNgay = false; 
                } else {
                    if (tuNgay != null && ngayHH.before(tuNgay)) {
                        matchNgay = false;
                    }
                    if (denNgay != null && ngayHH.after(denNgay)) {
                        matchNgay = false;
                    }
                }
            }

            // 2. Lọc theo giới tính
            if (!gioiTinh.equals("Tất cả")) {
                if (!dg.getGioiTinh().equalsIgnoreCase(gioiTinh)) {
                    matchGioiTinh = false;
                }
            }

            // 3. Lọc theo loại thành viên
            if (!loaiTV.equals("Tất cả")) {
                if (dg.getMaLoaiTV() == null || !dg.getMaLoaiTV().equalsIgnoreCase(loaiTV)) {
                    matchLoaiTV = false;
                }
            }

            // 4. Lọc theo mức phí
            if (tuPhi != null || denPhi != null) {
                double phi = dg.getTienPhiThanhVien();
                if (tuPhi != null && phi < tuPhi) matchPhi = false;
                if (denPhi != null && phi > denPhi) matchPhi = false;
            }

            // Gộp kết quả
            if (matchNgay && matchGioiTinh && matchLoaiTV && matchPhi) {
                kq.add(dg);
            }
        }
        return kq;
    }
}