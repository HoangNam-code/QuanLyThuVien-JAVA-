package bus;

import dao.ChiTietPhieuMuonDAO;
import dao.PhieuMuonDAO;
import dto.ChiTietPhieuMuonDTO;
import java.sql.Date;
import java.util.ArrayList;

public class ChiTietPhieuMuonBus {

    private final ChiTietPhieuMuonDAO dao = new ChiTietPhieuMuonDAO();
    private final PhieuMuonDAO phieuMuonDAO = new PhieuMuonDAO();

    public ArrayList<ChiTietPhieuMuonDTO>
        getListByMaPM(String maPM){

        return dao.getByMaPM(maPM);
    }

    public boolean them(ChiTietPhieuMuonDTO ct){
        if (ct == null
                || ct.getMaPM() == null || ct.getMaPM().trim().isEmpty()
                || ct.getMaSach() == null || ct.getMaSach().trim().isEmpty()
                || ct.getSoLuong() <= 0) {
            return false;
        }

        ct.setMaPM(ct.getMaPM().trim());
        ct.setMaSach(ct.getMaSach().trim());
        if (ct.getTinhTrangSach() == null || ct.getTinhTrangSach().trim().isEmpty()) {
            ct.setTinhTrangSach("Bình thường");
        } else {
            ct.setTinhTrangSach(ct.getTinhTrangSach().trim());
        }
        ct.setDaTra(ct.getDaTra() == 1 ? 1 : 0);

        return dao.insert(ct);
    }

    public boolean traSach(String maPM, String maSach){
        boolean ok = dao.updateTraSach(maPM, maSach);
        if (!ok) {
            return false;
        }

        // Nếu tất cả sách đã trả thì cập nhật ngày thực trả cho phiếu mượn.
        if (!dao.conSachChuaTra(maPM)) {
            phieuMuonDAO.capNhatNgayThucTra(maPM, new Date(System.currentTimeMillis()));
        }
        return true;
    }
}