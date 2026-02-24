package bus;

import dao.TaiKhoanDAO;
import dto.TaiKhoanDTO;

public class TaiKhoanBUS {
    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    
    public TaiKhoanDTO dangNhap(String user, String pass) {
        if (user == null || user.trim().isEmpty()) return null; 
        if (pass == null || pass.trim().isEmpty()) return null; 
        
        return taiKhoanDAO.checkLogin(user, pass);
    }
}