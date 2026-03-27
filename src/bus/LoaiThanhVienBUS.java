package bus;

import dao.LoaiThanhVienDAO;
import dto.LoaiThanhVienDTO;
import java.util.ArrayList;

public class LoaiThanhVienBUS {
    private LoaiThanhVienDAO dao = new LoaiThanhVienDAO();
    public ArrayList<LoaiThanhVienDTO> getList() {
        return dao.selectAll();
    }
}