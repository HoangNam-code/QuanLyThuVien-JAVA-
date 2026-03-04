package gui;

import bus.NhanVienBUS;
import dto.NhanVienDTO;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class TimKiemNangCaoDialog extends JDialog {

    private QuanLyNhanVienPanel parentPanel;
    private NhanVienBUS nvBUS;

    // Nhóm 1
    private JTextField txtMa, txtHo, txtTen, txtSDT, txtEmail;
    // Nhóm 2
    private JComboBox<String> cboGioiTinh, cboChucVu;
    // Nhóm 3
    private JTextField txtTuNgay, txtDenNgay;

    private final Color COLOR_PRIMARY = new Color(0, 102, 204);
    private final Color COLOR_WARNING = new Color(255, 153, 0);

    public TimKiemNangCaoDialog(QuanLyNhanVienPanel parentPanel, NhanVienBUS nvBUS) {
        this.parentPanel = parentPanel;
        this.nvBUS = nvBUS;
        
        setTitle("Tìm kiếm nhân viên nâng cao");
        setSize(550, 550);
        setLocationRelativeTo(null); // Giữa màn hình
        setModal(true); // Chặn thao tác màn hình dưới khi mở dialog này
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ================= NHÓM 1: THÔNG TIN CÁ NHÂN =================
        JPanel pnlGroup1 = createGroupPanel("Nhóm 1: Thông tin cá nhân");
        pnlGroup1.setLayout(new GridLayout(5, 2, 10, 10));
        
        pnlGroup1.add(new JLabel("Mã nhân viên:")); txtMa = new JTextField(); pnlGroup1.add(txtMa);
        pnlGroup1.add(new JLabel("Họ đệm:")); txtHo = new JTextField(); pnlGroup1.add(txtHo);
        pnlGroup1.add(new JLabel("Tên:")); txtTen = new JTextField(); pnlGroup1.add(txtTen);
        pnlGroup1.add(new JLabel("Số điện thoại:")); txtSDT = new JTextField(); pnlGroup1.add(txtSDT);
        pnlGroup1.add(new JLabel("Email:")); txtEmail = new JTextField(); pnlGroup1.add(txtEmail);

        // ================= NHÓM 2: PHÂN LOẠI =================
        JPanel pnlGroup2 = createGroupPanel("Nhóm 2: Phân loại");
        pnlGroup2.setLayout(new GridLayout(2, 2, 10, 10));
        
        pnlGroup2.add(new JLabel("Giới tính:"));
        cboGioiTinh = new JComboBox<>(new String[]{"Tất cả", "Nam", "Nữ"});
        cboGioiTinh.setBackground(Color.WHITE);
        pnlGroup2.add(cboGioiTinh);

        pnlGroup2.add(new JLabel("Chức vụ:"));
        cboChucVu = new JComboBox<>(new String[]{"Tất cả", "Thu Thu", "Quan Ly", "Bao Ve", "Tap Vu"});
        cboChucVu.setBackground(Color.WHITE);
        pnlGroup2.add(cboChucVu);

        // ================= NHÓM 3: NGÀY SINH =================
        JPanel pnlGroup3 = createGroupPanel("Nhóm 3: Khoảng ngày sinh (yyyy-mm-dd)");
        pnlGroup3.setLayout(new GridLayout(2, 2, 10, 10));
        
        pnlGroup3.add(new JLabel("Từ ngày:")); 
        txtTuNgay = new JTextField(); 
        txtTuNgay.setToolTipText("VD: 1990-01-01");
        pnlGroup3.add(txtTuNgay);

        pnlGroup3.add(new JLabel("Đến ngày:")); 
        txtDenNgay = new JTextField(); 
        txtDenNgay.setToolTipText("VD: 2005-12-31");
        pnlGroup3.add(txtDenNgay);

        // Thêm các nhóm vào Panel chính
        pnlMain.add(pnlGroup1);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlMain.add(pnlGroup2);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlMain.add(pnlGroup3);

        add(pnlMain, BorderLayout.CENTER);

        // ================= PANEL NÚT BẤM =================
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnTimKiem = new JButton("Tìm kiếm ngay");
        btnTimKiem.setBackground(COLOR_WARNING); // Màu cam
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTimKiem.setFocusPainted(false);
        
        JButton btnLamMoi = new JButton("Làm mới bộ lọc");
        btnLamMoi.setBackground(Color.LIGHT_GRAY);
        btnLamMoi.setFocusPainted(false);

        pnlButtons.add(btnTimKiem);
        pnlButtons.add(btnLamMoi);
        add(pnlButtons, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN NÚT BẤM ---
        btnLamMoi.addActionListener(e -> {
            txtMa.setText(""); txtHo.setText(""); txtTen.setText("");
            txtSDT.setText(""); txtEmail.setText("");
            cboGioiTinh.setSelectedIndex(0); cboChucVu.setSelectedIndex(0);
            txtTuNgay.setText(""); txtDenNgay.setText("");
        });

        btnTimKiem.addActionListener(e -> thucHienTimKiem());
    }

    private JPanel createGroupPanel(String title) {
        JPanel pnl = new JPanel();
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_PRIMARY, 1), title, 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 13), COLOR_PRIMARY
        ));
        return pnl;
    }

    private void thucHienTimKiem() {
        Date tuNgay = null, denNgay = null;
        try {
            if (!txtTuNgay.getText().trim().isEmpty()) {
                tuNgay = Date.valueOf(txtTuNgay.getText().trim());
            }
            if (!txtDenNgay.getText().trim().isEmpty()) {
                denNgay = Date.valueOf(txtDenNgay.getText().trim());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày! Vui lòng nhập yyyy-mm-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Gọi hàm BUS đã tạo ở Bước 1
        ArrayList<NhanVienDTO> ketQua = nvBUS.timKiemNangCao(
            txtMa.getText().trim(),
            txtHo.getText().trim(),
            txtTen.getText().trim(),
            txtSDT.getText().trim(),
            txtEmail.getText().trim(),
            cboGioiTinh.getSelectedItem().toString(),
            cboChucVu.getSelectedItem().toString(),
            tuNgay,
            denNgay
        );

        if (ketQua.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào phù hợp!");
        }

        // Truyền dữ liệu về Bảng ở Form chính
        parentPanel.loadDataLenBang(ketQua);
        
        // Tắt dialog
        this.dispose();
    }
}