package bus;

import dao.ChiTietPhieuPhatDAO;
import dto.ChiTietPhieuPhatDTO;
import java.util.ArrayList;

public class ChiTietPhieuPhatBUS {

    private final ChiTietPhieuPhatDAO dao = new ChiTietPhieuPhatDAO();

    public boolean them(ChiTietPhieuPhatDTO ct) {
        if (ct == null
                || ct.getMaPP() == null || ct.getMaPP().trim().isEmpty()
                || ct.getMaLyDoPhat() == null || ct.getMaLyDoPhat().trim().isEmpty()
                || ct.getMaSach() == null || ct.getMaSach().trim().isEmpty()) {
            return false;
        }

        ct.setMaPP(ct.getMaPP().trim());
        ct.setMaLyDoPhat(ct.getMaLyDoPhat().trim());
        ct.setMaSach(ct.getMaSach().trim());
        return dao.insert(ct);
    }

    public ArrayList<ChiTietPhieuPhatDTO> getByMaPP(String maPP) {
        if (maPP == null || maPP.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return dao.getByMaPP(maPP.trim());
    }
}
