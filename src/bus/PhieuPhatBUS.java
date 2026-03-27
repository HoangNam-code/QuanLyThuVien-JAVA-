package bus;

import dao.PhieuPhatDAO;
import dto.PhieuPhatDTO;
import java.util.ArrayList;

public class PhieuPhatBUS {

    private final PhieuPhatDAO dao = new PhieuPhatDAO();

    public ArrayList<PhieuPhatDTO> getList() {
        return dao.getAll();
    }

    public boolean them(PhieuPhatDTO pp) {
        if (pp == null
                || pp.getMaPP() == null || pp.getMaPP().trim().isEmpty()
                || pp.getMaPM() == null || pp.getMaPM().trim().isEmpty()
                || pp.getMaNV() == null || pp.getMaNV().trim().isEmpty()) {
            return false;
        }

        pp.setMaPP(pp.getMaPP().trim());
        pp.setMaPM(pp.getMaPM().trim());
        pp.setMaNV(pp.getMaNV().trim());
        if (pp.getLyDo() != null) {
            pp.setLyDo(pp.getLyDo().trim());
        }

        return dao.insert(pp);
    }

    public boolean capNhatTongTienVaLyDo(String maPP, long tongTien, String lyDo) {
        if (maPP == null || maPP.trim().isEmpty()) {
            return false;
        }
        return dao.updateTongTienVaLyDo(maPP.trim(), Math.max(0, tongTien), lyDo == null ? "" : lyDo.trim());
    }
}
