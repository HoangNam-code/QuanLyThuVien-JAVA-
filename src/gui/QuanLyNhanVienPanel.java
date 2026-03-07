package gui;

import bus.NhanVienBUS;
import dto.NhanVienDTO;
import dto.TaiKhoanDTO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import util.ExcelHelper;

// Đổi kế thừa JPanel thành BackgroundPanel
public class QuanLyNhanVienPanel extends BackgroundPanel {

    private NhanVienBUS nvBUS = new NhanVienBUS();
    private bus.TaiKhoanBUS tkBUS = new bus.TaiKhoanBUS();
    private JTable tblNhanVien;
    private DefaultTableModel model;
    
    private JTextField txtMa, txtHo, txtTen, txtSDT, txtEmail, txtNgaySinh, txtTimKiem;
    private JComboBox<String> cboGioiTinh, cboChucVu;
    
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnNhapExcel, btnXuatExcel;
    private JButton btnTimKiem, btnTimNangCao, btnHuyTim;
    private JButton btnCapTaiKhoan, btnMatKhauQuyen, btnKhoaTaiKhoan;

    private final Color COLOR_PRIMARY = new Color(0, 102, 204);   
    private final Color COLOR_WARNING = new Color(255, 153, 0);   
    private final Color COLOR_DANGER = new Color(204, 0, 0);      
    private final Color COLOR_SUCCESS = new Color(40, 167, 69);   
    private final Color COLOR_GRAY = new Color(108, 117, 125);    
    private final Color COLOR_TEXT_BLUE = new Color(0, 102, 204); 
    private final Color COLOR_ACCOUNT = new Color(156, 39, 176); 

    public QuanLyNhanVienPanel() {
        initComponents();
        if (nvBUS.getList() != null) {
            loadDataLenBang(nvBUS.getList());
        }
    }

    private void initComponents() {
        // TẠO KHE HỞ ĐỂ LỘ ẢNH NỀN & LÀM TRONG SUỐT PANEL GỐC
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Color.WHITE); // Khối nền trắng bóc
        pnlTop.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_BLUE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(Color.WHITE);
        pnlTitle.add(lblTitle);
        pnlTop.add(pnlTitle);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_TEXT_BLUE, 1), " Thông tin chi tiết ", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
                new Font("Segoe UI", Font.BOLD, 14), COLOR_TEXT_BLUE
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; pnlInput.add(createLabel("Mã NV:"), gbc);
        txtMa = createTextField(); gbc.gridx = 1; pnlInput.add(txtMa, gbc);

        gbc.gridx = 2; pnlInput.add(createLabel("Họ đệm:"), gbc);
        txtHo = createTextField(); gbc.gridx = 3; pnlInput.add(txtHo, gbc);

        gbc.gridx = 4; pnlInput.add(createLabel("Tên:"), gbc);
        txtTen = createTextField(); gbc.gridx = 5; pnlInput.add(txtTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlInput.add(createLabel("Giới tính:"), gbc);
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setBackground(Color.WHITE); gbc.gridx = 1; pnlInput.add(cboGioiTinh, gbc);

        gbc.gridx = 2; pnlInput.add(createLabel("Ngày sinh (yyyy-mm-dd):"), gbc);
        txtNgaySinh = createTextField(); gbc.gridx = 3; pnlInput.add(txtNgaySinh, gbc);

        gbc.gridx = 4; pnlInput.add(createLabel("SĐT:"), gbc);
        txtSDT = createTextField(); gbc.gridx = 5; pnlInput.add(txtSDT, gbc);

        gbc.gridx = 0; gbc.gridy = 2; pnlInput.add(createLabel("Email:"), gbc);
        txtEmail = createTextField(); gbc.gridx = 1; pnlInput.add(txtEmail, gbc);

        gbc.gridx = 2; pnlInput.add(createLabel("Chức vụ:"), gbc);
        cboChucVu = new JComboBox<>(new String[]{"Thu Thu", "Quan Ly", "Bao Ve", "Tap Vu", "IT Admin", "Ke Toan", "Quan Ly Kho"});
        cboChucVu.setBackground(Color.WHITE); gbc.gridx = 3; pnlInput.add(cboChucVu, gbc);

        pnlTop.add(pnlInput);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pnlButtons = new JPanel(new GridLayout(2, 1, 0, 10)); 
        pnlButtons.setBackground(Color.WHITE);

        JPanel pnlRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlRow1.setBackground(Color.WHITE);
        btnThem = createButton("Thêm Mới", COLOR_PRIMARY);
        btnSua = createButton("Cập Nhật", COLOR_WARNING);
        btnXoa = createButton("Xóa", COLOR_DANGER);
        btnLamMoi = createButton("Làm Mới Form", COLOR_GRAY);
        btnNhapExcel = createButton("Nhập Excel", COLOR_SUCCESS);
        btnXuatExcel = createButton("Xuất Excel", COLOR_SUCCESS);
        
        pnlRow1.add(btnThem); pnlRow1.add(btnSua); pnlRow1.add(btnXoa); pnlRow1.add(btnLamMoi);
        pnlRow1.add(Box.createHorizontalStrut(20));
        pnlRow1.add(btnNhapExcel); pnlRow1.add(btnXuatExcel);

        JPanel pnlRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlRow2.setBackground(Color.WHITE);
        btnCapTaiKhoan = createButton("Cấp tài khoản", COLOR_ACCOUNT);
        btnMatKhauQuyen = createButton("Mật khẩu/Quyền", COLOR_ACCOUNT);
        btnKhoaTaiKhoan = createButton("Khoá tài khoản", COLOR_GRAY);

        pnlRow2.add(btnCapTaiKhoan); 
        pnlRow2.add(btnMatKhauQuyen); 
        pnlRow2.add(btnKhoaTaiKhoan);

        pnlButtons.add(pnlRow1);
        pnlButtons.add(pnlRow2);
        
        pnlTop.add(pnlButtons);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(null, "Tìm kiếm", 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.GRAY));
        
        pnlSearch.add(createLabel("Từ khóa (Mã/Tên/SĐT):"));
        txtTimKiem = new JTextField(20); txtTimKiem.setPreferredSize(new Dimension(200, 30));
        btnTimKiem = createButton("Tìm cơ bản", COLOR_PRIMARY);
        btnTimNangCao = createButton("Tìm nâng cao", COLOR_WARNING);
        btnHuyTim = createButton("Hủy", COLOR_GRAY);

        pnlSearch.add(txtTimKiem); pnlSearch.add(btnTimKiem); pnlSearch.add(btnTimNangCao); pnlSearch.add(btnHuyTim);
        pnlTop.add(pnlSearch);

        add(pnlTop, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE); // Khối nền trắng bóc
        pnlCenter.setBorder(new EmptyBorder(10, 20, 20, 20));

        String[] cols = {"Mã NV", "Họ đệm", "Tên", "Giới tính", "Ngày sinh", "Email", "SĐT", "Chức vụ", "Tài khoản"};
        model = new DefaultTableModel(cols, 0);
        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(28);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblNhanVien.setSelectionBackground(new Color(232, 240, 254));
        tblNhanVien.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblNhanVien.getTableHeader();
        header.setBackground(new Color(240, 240, 240));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 35));

        tblNhanVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    txtMa.setText(getValue(row, 0));
                    txtHo.setText(getValue(row, 1));
                    txtTen.setText(getValue(row, 2));
                    cboGioiTinh.setSelectedItem(getValue(row, 3));
                    txtNgaySinh.setText(getValue(row, 4));
                    txtEmail.setText(getValue(row, 5));
                    txtSDT.setText(getValue(row, 6));
                    cboChucVu.setSelectedItem(getValue(row, 7));
                    txtMa.setEditable(false);
                }
            }
        });

        pnlCenter.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        addEvents();
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setPreferredSize(new Dimension(150, 30));
        return txt;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(135, 35));
        return btn;
    }

    private String getValue(int row, int col) {
        return model.getValueAt(row, col) != null ? model.getValueAt(row, col).toString() : "";
    }

    public void loadDataLenBang(ArrayList<NhanVienDTO> list) {
        if (list == null) return;
        model.setRowCount(0);
        for (NhanVienDTO nv : list) {
            String trangThaiDB = nv.getTrangThaiTaiKhoan();
            String hienThi = "Chưa có";
            if ("1".equals(trangThaiDB)) {
                hienThi = "Đã có";
            } else if ("0".equals(trangThaiDB)) {
                hienThi = "Bị khoá";
            }
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getHoDem(), nv.getTen(), nv.getGioiTinh(), nv.getNgaySinh(),
                nv.getEmail(), nv.getSdt(), nv.getChucVu(), hienThi 
            });
        }
    }

    private void addEvents() {
        btnTimKiem.addActionListener(e -> {
            try {
                String tuKhoa = txtTimKiem.getText().trim();
                loadDataLenBang(nvBUS.timKiem(tuKhoa));
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnTimNangCao.addActionListener(e -> {
            new TimKiemNangCaoDialog(this, nvBUS).setVisible(true);
        });
        
        btnHuyTim.addActionListener(e -> {
            txtTimKiem.setText("");
            nvBUS.docDanhSach(); 
            loadDataLenBang(nvBUS.getList());
        });

        btnThem.addActionListener(e -> {
            NhanVienDTO nv = getNhanVienFromForm();
            if (nv != null) {
                String msg = nvBUS.themNhanVien(nv);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) {
                    nvBUS.docDanhSach(); 
                    loadDataLenBang(nvBUS.getList());
                    lamMoiForm();
                }
            }
        });

        btnSua.addActionListener(e -> {
            NhanVienDTO nv = getNhanVienFromForm();
            if (nv != null) {
                String msg = nvBUS.suaNhanVien(nv);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) {
                    nvBUS.docDanhSach(); 
                    loadDataLenBang(nvBUS.getList());
                    lamMoiForm(); 
                }
            }
        });

        btnXoa.addActionListener(e -> {
            String maNV = txtMa.getText().trim(); 
            if (!maNV.isEmpty()) {
                if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    String msg = nvBUS.xoaNhanVien(maNV);
                    JOptionPane.showMessageDialog(this, msg);
                    if (msg.contains("thành công")) {
                        nvBUS.docDanhSach(); 
                        loadDataLenBang(nvBUS.getList());
                        lamMoiForm();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa trên bảng!");
            }
        });

        btnLamMoi.addActionListener(e -> lamMoiForm());

        btnXuatExcel.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    if (!path.endsWith(".xlsx")) path += ".xlsx";
                    ExcelHelper.exportExcel(tblNhanVien, path);
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + ex.getMessage()); 
                }
            }
        });

        btnNhapExcel.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    ArrayList<NhanVienDTO> listImport = ExcelHelper.importNhanVien(file.getAbsolutePath());
                    int count = 0;
                    for (NhanVienDTO nv : listImport) {
                        if (nvBUS.themNhanVien(nv).contains("thành công")) count++;
                    }
                    JOptionPane.showMessageDialog(this, "Đã xử lý nhập: " + count + " nhân viên mới!");
                    nvBUS.docDanhSach();
                    loadDataLenBang(nvBUS.getList());
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "Lỗi nhập file: " + ex.getMessage()); 
                }
            }
        });

        // ================= XỬ LÝ NÚT TÀI KHOẢN =================
        btnCapTaiKhoan.addActionListener(e -> {
            int row = tblNhanVien.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên trên bảng để cấp tài khoản!");
                return;
            }
            String maNV = getValue(row, 0);
            String tenNV = getValue(row, 1) + " " + getValue(row, 2);
            String trangThai = getValue(row, 8); 
            String chucVu = getValue(row, 7);

            if (!trangThai.equals("Chưa có")) {
                JOptionPane.showMessageDialog(this, "Nhân viên này đã có tài khoản (" + trangThai + ")!");
                return;
            }
            hienThiDialogCapTaiKhoan(maNV, tenNV, chucVu);
        });

        btnMatKhauQuyen.addActionListener(e -> {
            int row = tblNhanVien.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên trên bảng để thao tác!");
                return;
            }
            String maNV = getValue(row, 0);
            String trangThai = getValue(row, 8);
            String chucVu = getValue(row, 7);

            if (trangThai.equals("Chưa có")) {
                JOptionPane.showMessageDialog(this, "Nhân viên này chưa có tài khoản. Vui lòng cấp tài khoản trước!");
                return;
            }
            hienThiDialogDoiMatKhau(maNV, chucVu);
        });

        btnKhoaTaiKhoan.addActionListener(e -> {
            int row = tblNhanVien.getSelectedRow();
            if (row < 0) return;
            String maNV = getValue(row, 0);
            String trangThai = getValue(row, 8); 

            if (trangThai.equals("Chưa có")) return;

            if (trangThai.equals("Bị khoá")) {
                if(JOptionPane.showConfirmDialog(this, "Tài khoản đang bị khóa. Bạn muốn MỞ KHÓA không?", "Mở khóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, tkBUS.thayDoiTrangThai(maNV, "1")); 
                    nvBUS.docDanhSach();
                    loadDataLenBang(nvBUS.getList());
                }
            } else {
                if(JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn KHÓA tài khoản của NV: " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, tkBUS.thayDoiTrangThai(maNV, "0")); 
                    nvBUS.docDanhSach();
                    loadDataLenBang(nvBUS.getList());
                }
            }
        });
    }

    private NhanVienDTO getNhanVienFromForm() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!");
            return null;
        }
        try {
            Date ngaySinh = Date.valueOf(txtNgaySinh.getText().trim()); 
            return new NhanVienDTO(
                txtMa.getText().trim(),    
                txtHo.getText().trim(), 
                txtTen.getText().trim(), 
                ngaySinh,
                cboGioiTinh.getSelectedItem().toString(), 
                txtSDT.getText().trim(), 
                "", 
                txtEmail.getText().trim(), 
                cboChucVu.getSelectedItem().toString()
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi ngày sinh! Nhập đúng định dạng: yyyy-mm-dd (VD: 2000-12-31)");
            return null;
        }
    }

    private void lamMoiForm() {
        txtMa.setText(""); txtMa.setEditable(true);
        txtHo.setText(""); txtTen.setText("");
        txtSDT.setText(""); txtEmail.setText("");
        txtNgaySinh.setText(""); txtTimKiem.setText("");
        cboGioiTinh.setSelectedIndex(0);
        cboChucVu.setSelectedIndex(0);
        tblNhanVien.clearSelection();
    }

    private void hienThiDialogCapTaiKhoan(String maNV, String tenNV, String chucVuHienTai) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cấp tài khoản mới", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));

        pnlForm.add(new JLabel("Mã Nhân Viên:"));
        JTextField txtMaDialog = new JTextField(maNV); txtMaDialog.setEditable(false);
        pnlForm.add(txtMaDialog);

        pnlForm.add(new JLabel("Tên đăng nhập:"));
        JTextField txtUser = new JTextField(); 
        pnlForm.add(txtUser);

        pnlForm.add(new JLabel("Mật khẩu:"));
        JPasswordField txtPass = new JPasswordField();
        pnlForm.add(txtPass);

        pnlForm.add(new JLabel("Quyền hạn:"));
        JComboBox<String> cboQuyen = new JComboBox<>(new String[]{"Thu Thu", "Quan Ly", "Admin", "IT Admin", "Ke Toan", "Quan Ly Kho"});
        cboQuyen.setSelectedItem(chucVuHienTai); 
        pnlForm.add(cboQuyen);

        JButton btnLuu = createButton("Tạo tài khoản", COLOR_SUCCESS);
        btnLuu.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = new String(txtPass.getPassword()).trim();
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập Username và Password!");
                return;
            }

            TaiKhoanDTO tkMoi = new TaiKhoanDTO();
            tkMoi.setTenDangNhap(u);
            tkMoi.setMatKhau(p);
            tkMoi.setMaNV(maNV);
            tkMoi.setQuyenHan(cboQuyen.getSelectedItem().toString());
            tkMoi.setTrangThai("1"); 

            String ketQua = tkBUS.capTaiKhoanMoi(tkMoi);
            JOptionPane.showMessageDialog(dialog, ketQua);
            if(ketQua.contains("thành công")) {
                dialog.dispose();
                nvBUS.docDanhSach(); 
                loadDataLenBang(nvBUS.getList());
            }
        });

        JPanel pnlBot = new JPanel(); pnlBot.add(btnLuu);
        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBot, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void hienThiDialogDoiMatKhau(String maNV, String chucVuHienTai) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật Mật khẩu / Quyền", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));

        TaiKhoanDTO tkHienTai = tkBUS.getTaiKhoanTheoMaNV(maNV);
        String usernameHienTai = (tkHienTai != null) ? tkHienTai.getTenDangNhap() : "";
        JTextField txtUser = new JTextField(usernameHienTai); 
        txtUser.setEditable(false);
        pnlForm.add(txtUser);

        pnlForm.add(new JLabel("Mật khẩu mới:"));
        JPasswordField txtPass = new JPasswordField();
        pnlForm.add(txtPass);

        pnlForm.add(new JLabel("Nhập lại MK:"));
        JPasswordField txtPass2 = new JPasswordField();
        pnlForm.add(txtPass2);

        pnlForm.add(new JLabel("Quyền hạn:"));
        JComboBox<String> cboQuyen = new JComboBox<>(new String[]{"Thu Thu", "Quan Ly", "Admin", "IT Admin", "Ke Toan", "Quan Ly Kho"});
        cboQuyen.setSelectedItem(chucVuHienTai); 
        pnlForm.add(cboQuyen);

        JButton btnLuu = createButton("Cập nhật", COLOR_WARNING);
        btnLuu.addActionListener(e -> {
            String p1 = new String(txtPass.getPassword());
            String p2 = new String(txtPass2.getPassword());
            if (p1.isEmpty() || p2.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập mật khẩu mới!"); return;
            }
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(dialog, "Mật khẩu nhập lại không khớp!"); return;
            }
                
            String ketQua = tkBUS.doiMatKhauVaQuyen(txtUser.getText(), p1, cboQuyen.getSelectedItem().toString());

            JOptionPane.showMessageDialog(dialog, ketQua);
            if(ketQua.contains("thành công")) {
                dialog.dispose();
                nvBUS.docDanhSach();
                loadDataLenBang(nvBUS.getList());
            }
        });

        JPanel pnlBot = new JPanel(); pnlBot.add(btnLuu);
        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBot, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
