package gui;

import bus.NhanVienBUS;
import dto.NhanVienDTO;
import dto.TaiKhoanDTO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import util.ExcelHelper;

public class QuanLyNhanVienPanel extends BackgroundPanel {

    private NhanVienBUS nvBUS = new NhanVienBUS();
    private bus.TaiKhoanBUS tkBUS = new bus.TaiKhoanBUS();
    private JTable tblNhanVien;
    private DefaultTableModel model;

    private JTextField txtMa, txtHo, txtTen, txtSDT, txtEmail, txtNgaySinh, txtTimKiem;
    private JTextField txtLuong, txtNgayVaoLam, txtNgayNghiViec;
    private JComboBox<String> cboGioiTinh, cboChucVu;

    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnNhapExcel, btnXuatExcel;
    private JButton btnTimKiem, btnTimNangCao, btnHuyTim;
    private JButton btnCapTaiKhoan, btnMatKhauQuyen, btnKhoaTaiKhoan;

    private final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private DecimalFormat df = new DecimalFormat("#,###");

    public QuanLyNhanVienPanel() {
        initComponents();
        if (nvBUS.getList() != null) {
            loadDataLenBang(nvBUS.getList());
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // 1. TIÊU ĐỀ
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTop.add(lblTitle, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // 2. FORM NHẬP LIỆU & NÚT
        JPanel pnlSouth = new JPanel(new BorderLayout(0, 15));
        pnlSouth.setBackground(Color.WHITE);
        pnlSouth.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pnlInput = new JPanel(new GridLayout(4, 3, 20, 15));
        pnlInput.setBackground(Color.WHITE);
        TitledBorder borderInput = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY), "Thông tin chi tiết nhân viên");
        borderInput.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        borderInput.setTitleColor(COLOR_PRIMARY);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(borderInput, new EmptyBorder(15, 15, 15, 15)));

        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);
        txtMa = createTextField(fontInput); txtHo = createTextField(fontInput); txtTen = createTextField(fontInput);
        cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" }); cboGioiTinh.setFont(fontInput); cboGioiTinh.setBackground(Color.WHITE);
        txtNgaySinh = createTextField(fontInput); txtSDT = createTextField(fontInput); txtEmail = createTextField(fontInput);
        cboChucVu = new JComboBox<>(new String[] { "Thu Thu", "Quan Ly", "Bao Ve", "Tap Vu", "IT Admin", "Ke Toan", "Quan Ly Kho" }); cboChucVu.setFont(fontInput); cboChucVu.setBackground(Color.WHITE);
        txtLuong = createTextField(fontInput); txtNgayVaoLam = createTextField(fontInput); txtNgayNghiViec = createTextField(fontInput);

        pnlInput.add(createFormItem("Mã NV:", txtMa)); pnlInput.add(createFormItem("Họ đệm:", txtHo)); pnlInput.add(createFormItem("Tên:", txtTen));
        pnlInput.add(createFormItem("Giới tính:", cboGioiTinh)); pnlInput.add(createFormItem("Ngày sinh:", txtNgaySinh)); pnlInput.add(createFormItem("SĐT:", txtSDT));
        pnlInput.add(createFormItem("Email:", txtEmail)); pnlInput.add(createFormItem("Chức vụ:", cboChucVu)); pnlInput.add(createFormItem("Lương (VNĐ):", txtLuong));
        pnlInput.add(createFormItem("Ngày vào:", txtNgayVaoLam)); pnlInput.add(createFormItem("Ngày nghỉ:", txtNgayNghiViec));
        JPanel pnlEmpty = new JPanel(); pnlEmpty.setBackground(Color.WHITE); pnlInput.add(pnlEmpty);

        JPanel pnlButtonsWrapper = new JPanel(new GridLayout(2, 1, 0, 10)); 
        pnlButtonsWrapper.setBackground(Color.WHITE);

        // --- ĐÃ CẬP NHẬT ICON CHO TẤT CẢ CÁC NÚT ---
        JPanel pnlBtnRow1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); pnlBtnRow1.setBackground(Color.WHITE);
        btnThem = createActionBtn("Thêm", "add.png", new Color(46, 204, 113)); 
        btnSua = createActionBtn("Cập Nhật", "sua.png", new Color(241, 196, 15));
        btnXoa = createActionBtn("Xóa", "xoa.png", new Color(231, 76, 60)); 
        btnLamMoi = createActionBtn("Làm Mới", "lammoi.png", new Color(149, 165, 166));
        btnNhapExcel = createActionBtn("Nhập Excel", "import.png", new Color(39, 174, 96)); 
        btnXuatExcel = createActionBtn("Xuất Excel", "export.png", new Color(41, 128, 185));
        pnlBtnRow1.add(btnThem); pnlBtnRow1.add(btnSua); pnlBtnRow1.add(btnXoa); pnlBtnRow1.add(btnLamMoi); pnlBtnRow1.add(btnNhapExcel); pnlBtnRow1.add(btnXuatExcel);

        JPanel pnlBtnRow2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); pnlBtnRow2.setBackground(Color.WHITE);
        btnCapTaiKhoan = createActionBtn("Cấp Tài Khoản", "account.png", new Color(156, 39, 176)); 
        btnMatKhauQuyen = createActionBtn("Mật Khẩu/Quyền", "key.png", new Color(156, 39, 176)); 
        btnKhoaTaiKhoan = createActionBtn("Khóa Tài Khoản", "lock.png", new Color(108, 117, 125));
        pnlBtnRow2.add(btnCapTaiKhoan); pnlBtnRow2.add(btnMatKhauQuyen); pnlBtnRow2.add(btnKhoaTaiKhoan);

        pnlButtonsWrapper.add(pnlBtnRow1); pnlButtonsWrapper.add(pnlBtnRow2);
        pnlSouth.add(pnlInput, BorderLayout.CENTER); pnlSouth.add(pnlButtonsWrapper, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        // 3. KHU VỰC TÌM KIẾM & BẢNG
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Tìm kiếm nhân viên"));

        txtTimKiem = createTextField(fontInput); txtTimKiem.setPreferredSize(new Dimension(300, 35));
        
        // --- ĐÃ CẬP NHẬT ICON CHO NÚT TÌM KIẾM ---
        btnTimKiem = createActionBtn("Tìm", "search.png", COLOR_PRIMARY); btnTimKiem.setPreferredSize(new Dimension(100, 35));
        btnTimNangCao = createActionBtn("Tìm nâng cao", "search.png", new Color(245, 124, 0)); btnTimNangCao.setPreferredSize(new Dimension(160, 35));
        btnHuyTim = createActionBtn("Hủy", "cancel.png", new Color(231, 76, 60)); btnHuyTim.setPreferredSize(new Dimension(100, 35));

        pnlSearch.add(createLabel("Từ khóa (Mã/Tên/SĐT):")); pnlSearch.add(txtTimKiem); pnlSearch.add(btnTimKiem); pnlSearch.add(btnTimNangCao); pnlSearch.add(btnHuyTim);
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        String[] cols = { "Mã NV", "Họ đệm", "Tên", "Giới tính", "Ngày sinh", "Email", "SĐT", "Chức vụ", "Lương", "Ngày Vào", "Ngày Nghỉ", "Tài khoản" };
        model = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } };
        tblNhanVien = new JTable(model); 
        styleTable(tblNhanVien); 

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // Sự kiện...
        tblNhanVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    txtMa.setText(getValue(row, 0)); txtHo.setText(getValue(row, 1)); txtTen.setText(getValue(row, 2));
                    cboGioiTinh.setSelectedItem(getValue(row, 3)); txtNgaySinh.setText(getValue(row, 4)); txtEmail.setText(getValue(row, 5));
                    txtSDT.setText(getValue(row, 6)); cboChucVu.setSelectedItem(getValue(row, 7));
                    txtLuong.setText(getValue(row, 8).replaceAll("[^0-9]", "")); txtNgayVaoLam.setText(getValue(row, 9)); txtNgayNghiViec.setText(getValue(row, 10));
                    txtMa.setEditable(false);
                }
            }
        });

        btnTimKiem.addActionListener(e -> { try { loadDataLenBang(nvBUS.timKiem(txtTimKiem.getText().trim())); } catch (Exception ex) { ex.printStackTrace(); } });
        btnHuyTim.addActionListener(e -> { txtTimKiem.setText(""); nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); });
        btnTimNangCao.addActionListener(e -> { new TimKiemNangCaoDialog(this, nvBUS).setVisible(true); });

        addCRUDAndAccountEvents();
    }

    private JPanel createFormItem(String labelText, JComponent comp) {
        JPanel panel = new JPanel(new BorderLayout(10, 0)); panel.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(labelText); lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl.setPreferredSize(new Dimension(85, 30));
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14)); comp.setPreferredSize(new Dimension(0, 30));
        panel.add(lbl, BorderLayout.WEST); panel.add(comp, BorderLayout.CENTER); return panel;
    }
    private JLabel createLabel(String text) { JLabel lbl = new JLabel(text); lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl.setForeground(new Color(60, 60, 60)); return lbl; }
    private JTextField createTextField(Font font) { JTextField txt = new JTextField(); txt.setFont(font); txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 5, 5, 5))); return txt; }
    
    // --- HÀM TẠO NÚT CÓ ICON ---
    private JButton createActionBtn(String text, String iconName, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 35)); // Nới rộng chút để nhét vừa icon
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        if (iconName != null && !iconName.isEmpty()) {
            try {
                java.net.URL resource = getClass().getResource("/img/" + iconName);
                if (resource != null) {
                    ImageIcon icon = new ImageIcon(resource);
                    Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(img));
                    btn.setIconTextGap(8);
                }
            } catch (Exception e) {}
        }
        return btn;
    }

    private String getValue(int row, int col) { return model.getValueAt(row, col) != null ? model.getValueAt(row, col).toString() : ""; }

    public void loadDataLenBang(ArrayList<NhanVienDTO> list) {
        if (list == null) return; model.setRowCount(0);
        for (NhanVienDTO nv : list) {
            String trangThaiDB = nv.getTrangThaiTaiKhoan(); String hienThi = "Chưa có";
            if ("1".equals(trangThaiDB)) hienThi = "Đã có"; else if ("0".equals(trangThaiDB)) hienThi = "Bị khoá";
            model.addRow(new Object[] { nv.getMaNV(), nv.getHoDem(), nv.getTen(), nv.getGioiTinh(), nv.getNgaySinh(), nv.getEmail(), nv.getSdt(), nv.getChucVu(), df.format(nv.getLuong()), nv.getNgayVaoLam(), nv.getNgayNghiViec() != null ? nv.getNgayNghiViec() : "", hienThi });
        }
    }

    private NhanVienDTO getNhanVienFromForm() {
        if (txtMa.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!"); return null; }
        try {
            Date ngaySinh = Date.valueOf(txtNgaySinh.getText().trim()); double luong = Double.parseDouble(txtLuong.getText().trim());
            Date ngayVaoLam = Date.valueOf(txtNgayVaoLam.getText().trim()); Date ngayNghiViec = txtNgayNghiViec.getText().trim().isEmpty() ? null : Date.valueOf(txtNgayNghiViec.getText().trim());
            return new NhanVienDTO(txtMa.getText().trim(), txtHo.getText().trim(), txtTen.getText().trim(), ngaySinh, cboGioiTinh.getSelectedItem().toString(), txtSDT.getText().trim(), "", txtEmail.getText().trim(), cboChucVu.getSelectedItem().toString(), luong, ngayVaoLam, ngayNghiViec);
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Lỗi nhập liệu! Vui lòng kiểm tra lại định dạng."); return null; }
    }

    private void lamMoiForm() { txtMa.setText(""); txtMa.setEditable(true); txtHo.setText(""); txtTen.setText(""); txtSDT.setText(""); txtEmail.setText(""); txtNgaySinh.setText(""); txtTimKiem.setText(""); txtLuong.setText(""); txtNgayVaoLam.setText(""); txtNgayNghiViec.setText(""); cboGioiTinh.setSelectedIndex(0); cboChucVu.setSelectedIndex(0); tblNhanVien.clearSelection(); }

    private void addCRUDAndAccountEvents() {
        btnThem.addActionListener(e -> { NhanVienDTO nv = getNhanVienFromForm(); if (nv != null) { String msg = nvBUS.themNhanVien(nv); JOptionPane.showMessageDialog(this, msg); if (msg.contains("thành công")) { nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); lamMoiForm(); } } });
        btnSua.addActionListener(e -> { NhanVienDTO nv = getNhanVienFromForm(); if (nv != null) { String msg = nvBUS.suaNhanVien(nv); JOptionPane.showMessageDialog(this, msg); if (msg.contains("thành công")) { nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); lamMoiForm(); } } });
        btnXoa.addActionListener(e -> { String maNV = txtMa.getText().trim(); if (!maNV.isEmpty()) { if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { String msg = nvBUS.xoaNhanVien(maNV); JOptionPane.showMessageDialog(this, msg); if (msg.contains("thành công")) { nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); lamMoiForm(); } } } });
        btnLamMoi.addActionListener(e -> lamMoiForm());

        btnXuatExcel.addActionListener(e -> { JFileChooser chooser = new JFileChooser(); chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx")); if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { try { String path = chooser.getSelectedFile().getAbsolutePath(); if (!path.endsWith(".xlsx")) path += ".xlsx"; ExcelHelper.exportExcel(tblNhanVien, path); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + ex.getMessage()); } } });
        btnNhapExcel.addActionListener(e -> { JFileChooser chooser = new JFileChooser(); chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx")); if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { try { File file = chooser.getSelectedFile(); ArrayList<NhanVienDTO> listImport = ExcelHelper.importNhanVien(file.getAbsolutePath()); int count = 0; for (NhanVienDTO nv : listImport) { if (nvBUS.themNhanVien(nv).contains("thành công")) count++; } JOptionPane.showMessageDialog(this, "Đã xử lý nhập: " + count + " nhân viên mới!"); nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi nhập file: " + ex.getMessage()); } } });

        btnCapTaiKhoan.addActionListener(e -> { int row = tblNhanVien.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên trên bảng để cấp tài khoản!"); return; } String maNV = getValue(row, 0); String tenNV = getValue(row, 1) + " " + getValue(row, 2); String trangThai = getValue(row, 11); String chucVu = getValue(row, 7); if (!trangThai.equals("Chưa có")) { JOptionPane.showMessageDialog(this, "Nhân viên này đã có tài khoản (" + trangThai + ")!"); return; } hienThiDialogCapTaiKhoan(maNV, tenNV, chucVu); });
        btnMatKhauQuyen.addActionListener(e -> { int row = tblNhanVien.getSelectedRow(); if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 nhân viên trên bảng để thao tác!"); return; } String maNV = getValue(row, 0); String trangThai = getValue(row, 11); String chucVu = getValue(row, 7); if (trangThai.equals("Chưa có")) { JOptionPane.showMessageDialog(this, "Nhân viên này chưa có tài khoản. Vui lòng cấp tài khoản trước!"); return; } hienThiDialogDoiMatKhau(getValue(row, 0), getValue(row, 7)); });
        btnKhoaTaiKhoan.addActionListener(e -> { int row = tblNhanVien.getSelectedRow(); if (row < 0) return; String maNV = getValue(row, 0); String trangThai = getValue(row, 11); if (trangThai.equals("Chưa có")) return; if (trangThai.equals("Bị khoá")) { if (JOptionPane.showConfirmDialog(this, "Tài khoản đang bị khóa. Bạn muốn MỞ KHÓA không?", "Mở khóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { JOptionPane.showMessageDialog(this, tkBUS.thayDoiTrangThai(maNV, "1")); nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); } } else { if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn KHÓA tài khoản của NV: " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { JOptionPane.showMessageDialog(this, tkBUS.thayDoiTrangThai(maNV, "0")); nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); } } });
    }

    private void hienThiDialogCapTaiKhoan(String maNV, String tenNV, String chucVuHienTai) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cấp tài khoản mới", true);
        dialog.setSize(400, 300); dialog.setLocationRelativeTo(this); dialog.setLayout(new BorderLayout());
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 20)); pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlForm.add(new JLabel("Mã Nhân Viên:")); JTextField txtMaDialog = new JTextField(maNV); txtMaDialog.setEditable(false); pnlForm.add(txtMaDialog);
        pnlForm.add(new JLabel("Tên đăng nhập:")); JTextField txtUser = new JTextField(); pnlForm.add(txtUser);
        pnlForm.add(new JLabel("Mật khẩu:")); JPasswordField txtPass = new JPasswordField(); pnlForm.add(txtPass);
        pnlForm.add(new JLabel("Quyền hạn:")); JComboBox<String> cboQuyen = new JComboBox<>(new String[] { "Thu Thu", "Quan Ly", "Admin", "IT Admin", "Ke Toan", "Quan Ly Kho" }); cboQuyen.setSelectedItem(chucVuHienTai); pnlForm.add(cboQuyen);
        JButton btnLuu = createActionBtn("Tạo tài khoản", null, new Color(40, 167, 69));
        btnLuu.addActionListener(e -> { String u = txtUser.getText().trim(); String p = new String(txtPass.getPassword()).trim(); if (u.isEmpty() || p.isEmpty()) { JOptionPane.showMessageDialog(dialog, "Vui lòng nhập Username và Password!"); return; } TaiKhoanDTO tkMoi = new TaiKhoanDTO(); tkMoi.setTenDangNhap(u); tkMoi.setMatKhau(p); tkMoi.setMaNV(maNV); tkMoi.setQuyenHan(cboQuyen.getSelectedItem().toString()); tkMoi.setTrangThai("1"); String ketQua = tkBUS.capTaiKhoanMoi(tkMoi); JOptionPane.showMessageDialog(dialog, ketQua); if (ketQua.contains("thành công")) { dialog.dispose(); nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); } });
        JPanel pnlBot = new JPanel(); pnlBot.add(btnLuu); dialog.add(pnlForm, BorderLayout.CENTER); dialog.add(pnlBot, BorderLayout.SOUTH); dialog.setVisible(true);
    }

    private void hienThiDialogDoiMatKhau(String maNV, String chucVuHienTai) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật Mật khẩu / Quyền", true);
        dialog.setSize(400, 300); dialog.setLocationRelativeTo(this); dialog.setLayout(new BorderLayout());
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 20)); pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        TaiKhoanDTO tkHienTai = tkBUS.getTaiKhoanTheoMaNV(maNV); JTextField txtUser = new JTextField((tkHienTai != null) ? tkHienTai.getTenDangNhap() : ""); txtUser.setEditable(false);
        pnlForm.add(new JLabel("Tên đăng nhập:")); pnlForm.add(txtUser); pnlForm.add(new JLabel("Mật khẩu mới:")); JPasswordField txtPass = new JPasswordField(); pnlForm.add(txtPass);
        pnlForm.add(new JLabel("Nhập lại MK:")); JPasswordField txtPass2 = new JPasswordField(); pnlForm.add(txtPass2);
        pnlForm.add(new JLabel("Quyền hạn:")); JComboBox<String> cboQuyen = new JComboBox<>(new String[] { "Thu Thu", "Quan Ly", "Admin", "IT Admin", "Ke Toan", "Quan Ly Kho" }); cboQuyen.setSelectedItem(chucVuHienTai); pnlForm.add(cboQuyen);
        JButton btnLuu = createActionBtn("Cập nhật", null, new Color(255, 153, 0));
        btnLuu.addActionListener(e -> { String p1 = new String(txtPass.getPassword()); String p2 = new String(txtPass2.getPassword()); if (p1.isEmpty() || p2.isEmpty()) { JOptionPane.showMessageDialog(dialog, "Vui lòng nhập mật khẩu mới!"); return; } if (!p1.equals(p2)) { JOptionPane.showMessageDialog(dialog, "Mật khẩu nhập lại không khớp!"); return; } String ketQua = tkBUS.doiMatKhauVaQuyen(txtUser.getText(), p1, cboQuyen.getSelectedItem().toString()); JOptionPane.showMessageDialog(dialog, ketQua); if (ketQua.contains("thành công")) { dialog.dispose(); nvBUS.docDanhSach(); loadDataLenBang(nvBUS.getList()); } });
        JPanel pnlBot = new JPanel(); pnlBot.add(btnLuu); dialog.add(pnlForm, BorderLayout.CENTER); dialog.add(pnlBot, BorderLayout.SOUTH); dialog.setVisible(true);
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); table.setRowHeight(30); table.setSelectionBackground(new Color(187, 222, 251)); table.setGridColor(new Color(224, 224, 224));
        JTableHeader header = table.getTableHeader(); header.setPreferredSize(new Dimension(100, 35));
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer(); headerRenderer.setBackground(new Color(235, 238, 240)); headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14)); headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(); centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN); 
        if(table.getColumnCount() >= 12) {
            table.getColumnModel().getColumn(0).setPreferredWidth(60); table.getColumnModel().getColumn(1).setPreferredWidth(120); table.getColumnModel().getColumn(2).setPreferredWidth(60); table.getColumnModel().getColumn(3).setPreferredWidth(60); table.getColumnModel().getColumn(4).setPreferredWidth(90); table.getColumnModel().getColumn(5).setPreferredWidth(170); table.getColumnModel().getColumn(6).setPreferredWidth(90); table.getColumnModel().getColumn(7).setPreferredWidth(90); table.getColumnModel().getColumn(8).setPreferredWidth(100); table.getColumnModel().getColumn(9).setPreferredWidth(90); table.getColumnModel().getColumn(10).setPreferredWidth(90); table.getColumnModel().getColumn(11).setMinWidth(90);  
        }
    }
}