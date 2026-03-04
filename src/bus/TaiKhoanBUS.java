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

    public String capTaiKhoanMoi(TaiKhoanDTO tk) {
        // Kiểm tra xem tên đăng nhập đã bị trùng chưa (nếu cần)
        if (taiKhoanDAO.insert(tk) > 0) {
            return "Cấp tài khoản thành công!";
        }
        return "Cấp tài khoản thất bại (Có thể Tên đăng nhập đã bị trùng)!";
    }

    public TaiKhoanDTO getTaiKhoanTheoMaNV(String maNV) {
        return taiKhoanDAO.selectByMaNV(maNV);
    }

    public String doiMatKhauVaQuyen(String user, String pass, String quyen) {
        if (taiKhoanDAO.updateMatKhauQuyen(user, pass, quyen) > 0) {
            return "Cập nhật Mật khẩu/Quyền thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String thayDoiTrangThai(String maNV, String trangThai) {
        if (taiKhoanDAO .updateTrangThai(maNV, trangThai) > 0) {
            return "Thao tác thành công!";
        }
        return "Thao tác thất bại!";
    }
}