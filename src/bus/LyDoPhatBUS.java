package bus;

import dao.LyDoPhatDAO;
import dto.LyDoPhatDTO;
import java.util.ArrayList;

public class LyDoPhatBUS {
    private LyDoPhatDAO dao = new LyDoPhatDAO();
    public ArrayList<LyDoPhatDTO> getList() {
        return dao.selectAll();
    }
}
