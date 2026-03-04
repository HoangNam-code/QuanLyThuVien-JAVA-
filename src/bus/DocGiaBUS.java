package bus;

import dao.DocGiaDAO;
import dto.DocGiaDTO;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class DocGiaBUS {
    private DocGiaDAO dgDAO = new DocGiaDAO();
    private ArrayList<DocGiaDTO> listDG;

    public DocGiaBUS() {
        listDG = dgDAO.selectAll();
    }

    public ArrayList<DocGiaDTO> getList() { return listDG; }

    public String add(DocGiaDTO dg) {
        if (dg.getMaDG().isEmpty() || dg.getTen().isEmpty()) {
            return "Mã và Tên không được để trống!";
        }
        
        // Mục 6: Kiểm tra trùng mã độc giả và SĐT
        for (DocGiaDTO x : listDG) {
            if (x.getMaDG().equalsIgnoreCase(dg.getMaDG())) return "Mã độc giả đã tồn tại!";
            if (x.getSdt().equals(dg.getSdt())) return "Số điện thoại này đã được đăng ký!";
        }

        // Yêu cầu: Tính ngày hết hạn thẻ tự động (Cộng thêm 1 năm từ ngày tạo)
        LocalDate today = LocalDate.now();
        LocalDate nextYear = today.plusYears(1);
        dg.setNgayHetHan(Date.valueOf(nextYear));
        
        // Mặc định số sách mượn ban đầu bằng 0
        dg.setSoSachMuon(0);

        if (dgDAO.insert(dg) > 0) {
            listDG.add(dg);
            return "Thêm độc giả thành công!";
        }
        return "Thêm thất bại do lỗi CSDL!";
    }

    public String delete(String maDG) {
        if (maDG == null || maDG.trim().isEmpty()) {
            return "Vui lòng chọn độc giả cần xóa!";
        }
        
        // Gọi DAO để xóa dưới CSDL
        if (dgDAO.delete(maDG) > 0) {
            // Nếu xóa CSDL thành công, xóa luôn trong danh sách hiển thị (listDG)
            listDG.removeIf(dg -> dg.getMaDG().equals(maDG));
            return "Xóa thành công!";
        }
        return "Xóa thất bại! Có thể độc giả này đang có dữ liệu mượn sách.";
    }
    
    // Hàm xử lý logic cập nhật
    public String update(DocGiaDTO dg) {
        if (dg.getMaDG().isEmpty() || dg.getTen().isEmpty()) {
            return "Mã và Tên không được để trống!";
        }
        
        // Kiểm tra trùng SĐT (nhưng bỏ qua chính người đang được cập nhật)
        for (DocGiaDTO x : listDG) {
            if (!x.getMaDG().equalsIgnoreCase(dg.getMaDG())) {
                if (x.getSdt().equals(dg.getSdt())) return "Số điện thoại này đã được đăng ký cho độc giả khác!";
            }
        }

        if (dgDAO.update(dg) > 0) {
            // Cập nhật lại trong danh sách bộ nhớ tạm (listDG)
            for (int i = 0; i < listDG.size(); i++) {
                if (listDG.get(i).getMaDG().equalsIgnoreCase(dg.getMaDG())) {
                    // Giữ nguyên Số sách mượn và Ngày hết hạn cũ đang có trong hệ thống
                    dg.setSoSachMuon(listDG.get(i).getSoSachMuon());
                    dg.setNgayHetHan(listDG.get(i).getNgayHetHan());
                    
                    listDG.set(i, dg);
                    break;
                }
            }
            return "Cập nhật thành công!";
        }
        return "Cập nhật thất bại do lỗi CSDL!";
        
    }
    // 1. TÌM KIẾM CƠ BẢN (Tìm theo Mã, Tên, hoặc SĐT)
    public ArrayList<DocGiaDTO> timKiem(String tuKhoa) {
        ArrayList<DocGiaDTO> result = new ArrayList<>();
        String tk = tuKhoa.toLowerCase().trim();
        for (DocGiaDTO dg : listDG) {
            if (dg.getMaDG().toLowerCase().contains(tk) || 
                dg.getTen().toLowerCase().contains(tk) || 
                dg.getHoDem().toLowerCase().contains(tk) ||
                dg.getSdt().contains(tk)) {
                result.add(dg);
            }
        }
        return result;
    }

    // 2. TÌM KIẾM NÂNG CAO (Lọc kết hợp nhiều điều kiện)
    public ArrayList<DocGiaDTO> timKiemNangCao(String tuKhoa, String gioiTinh, String loaiTV) {
        ArrayList<DocGiaDTO> result = new ArrayList<>();
        String tk = tuKhoa.toLowerCase().trim();
        
        for (DocGiaDTO dg : listDG) {
            // Điều kiện 1: Khớp từ khóa (Mã hoặc Tên)
            boolean matchTuKhoa = tk.isEmpty() || dg.getMaDG().toLowerCase().contains(tk) 
                                               || dg.getTen().toLowerCase().contains(tk) 
                                               || dg.getHoDem().toLowerCase().contains(tk);
            
            // Điều kiện 2: Khớp giới tính
            boolean matchGioiTinh = gioiTinh.equals("Tất cả") || dg.getGioiTinh().equalsIgnoreCase(gioiTinh);
            
            // Điều kiện 3: Khớp loại thành viên
            boolean matchLoaiTV = loaiTV.isEmpty() || dg.getMaLoaiTV().toLowerCase().contains(loaiTV.toLowerCase());
            
            // Nếu thỏa mãn TẤT CẢ điều kiện thì đưa vào danh sách kết quả
            if (matchTuKhoa && matchGioiTinh && matchLoaiTV) {
                result.add(dg);
            }
        }
        return result;
    }
}