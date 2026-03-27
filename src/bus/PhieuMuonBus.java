package bus;

import dao.*;
import dto.*;
import java.util.ArrayList;

public class PhieuMuonBus {
    private PhieuMuonDAO pmDAO = new PhieuMuonDAO();
    private ChiTietPhieuMuonDAO ctpmDAO = new ChiTietPhieuMuonDAO();
    private SachDAO sachDAO = new SachDAO();

    public ArrayList<PhieuMuonDTO> getList() {
        return pmDAO.getAll();
    }

    public void docDanhSach() {
        // Gọi để refresh dữ liệu từ database - không có cache
        pmDAO.getAll();
    }

    public boolean themPhieuMuon(PhieuMuonDTO pm) {
        return pmDAO.them(pm);
    }

    public boolean muonSach(PhieuMuonDTO pm, ChiTietPhieuMuonDTO ct) {
        // Lỗi gạch đỏ biến mất khi pmDAO có hàm them(PhieuMuonDTO)
        if (pmDAO.them(pm)) { 
            if (ctpmDAO.them(ct)) { 
                return sachDAO.giamSoLuong(ct.getMaSach(), ct.getSoLuong());
            }
        }
        return false;
    }

    // ================= XÓA PHIẾU MƯỢN (KỂM TẢI LẠI TỒN KHO) =================
    public boolean xoaPhieuMuon(String maPM) {
        System.out.println("\n========== DEBUG: xoaPhieuMuon(" + maPM + ") START ==========");
        try {
            // Lấy danh sách chi tiết để hoàn lại tồn kho
            ArrayList<ChiTietPhieuMuonDTO> danhSach = ctpmDAO.getByMaPM(maPM);
            System.out.println("[DEBUG] Tìm thấy " + danhSach.size() + " chi tiết phiếu");
            
            // Xóa chi tiết phiếu mươn
            System.out.println("[DEBUG] Đang xóa chi tiết phiếu...");
            if (!ctpmDAO.xoaByMaPM(maPM)) {
                System.err.println("[ERROR] Không thể xóa chi tiết phiếu mượn: " + maPM);
                return false;
            }
            System.out.println("[DEBUG] ✓ Xóa chi tiết thành công");
            
            // Hoàn lại tồn kho cho từng sách
            System.out.println("[DEBUG] Đang hoàn lại tồn kho...");
            for (ChiTietPhieuMuonDTO ct : danhSach) {
                if (!sachDAO.tangSoLuong(ct.getMaSach(), ct.getSoLuong())) {
                    System.err.println("[WARN] Lỗi hoàn lại tồn kho cho sách: " + ct.getMaSach());
                }
            }
            System.out.println("[DEBUG] ✓ Hoàn lại tồn kho xong");
            
            // Xóa phiếu mượn
            System.out.println("[DEBUG] Đang xóa phiếu mượn...");
            if (pmDAO.xoa(maPM)) {
                System.out.println("[OK] ✓✓✓ Xóa phiếu mượn thành công: " + maPM);
                System.out.println("========== DEBUG: xoaPhieuMuon(" + maPM + ") SUCCESS ==========\n");
                return true;
            } else {
                System.err.println("[ERROR] Không thể xóa phiếu mượn: " + maPM);
                return false;
            }
        } catch (Exception e) {
            System.err.println("[EXCEPTION] Lỗi trong xoaPhieuMuon: " + e.getMessage());
            e.printStackTrace();
            System.out.println("========== DEBUG: xoaPhieuMuon(" + maPM + ") FAILED ==========\n");
            return false;
        }
    }
}