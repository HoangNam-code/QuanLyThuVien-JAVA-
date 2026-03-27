package gui;

import bus.DocGiaBUS;
import dto.DocGiaDTO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import util.ExcelHelper;
import com.toedter.calendar.JDateChooser;

public class QuanLyDocGiaPanel extends JPanel {

    private DocGiaBUS dgBUS = new DocGiaBUS();
    private JTable tblDocGia;
    private DefaultTableModel model;

    // Khai báo thêm các trường mới
    private JDateChooser jdNgayDangKy;
    private JTextField txtNgayHetHan;

    // Nhập liệu
    private JTextField txtMa, txtHoDem, txtTen, txtNgaySinh, txtEmail, txtSDT, txtDiaChi, txtPhiTV, txtLoaiTV;
    private JComboBox<String> cboGioiTinh;

    // Tìm kiếm
    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnTimNangCao, btnHuyTim;

    // Nút chức năng
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnNhapExcel, btnXuatExcel;
    private JButton btnInDanhSach, btnInThe;

    // Màu chủ đạo
    private final Color COLOR_PRIMARY = new Color(25, 118, 210);

    public QuanLyDocGiaPanel() {
        initComponents();
        loadTable(dgBUS.getList());
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =========================================================
        // 1. TIÊU ĐỀ (NORTH)
        // =========================================================
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐỘC GIẢ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // =========================================================
        // 2. TÌM KIẾM VÀ BẢNG DỮ LIỆU (CENTER)
        // =========================================================
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setOpaque(true);

        // --- Thanh Tìm Kiếm ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setBackground(Color.WHITE);

        JLabel lblTimKiem = new JLabel("Tìm nhanh (Mã/Tên/SĐT):");
        lblTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlSearch.add(lblTimKiem);

        txtTimKiem = new JTextField(25);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        pnlSearch.add(txtTimKiem);

        btnTimKiem = new JButton("Tìm");
        styleSearchButton(btnTimKiem, COLOR_PRIMARY);

        btnTimNangCao = new JButton("Tìm nâng cao");
        styleSearchButton(btnTimNangCao, COLOR_PRIMARY);

        btnHuyTim = new JButton("Hủy");
        styleSearchButton(btnHuyTim, new Color(231, 76, 60)); // Đỏ

        pnlSearch.add(btnTimKiem);
        pnlSearch.add(btnTimNangCao);
        pnlSearch.add(btnHuyTim);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // --- Bảng dữ liệu ---
        String[] cols = { "Mã DG", "Họ Đệm", "Tên", "Giới Tính", "Ngày Sinh", "Email", "SĐT", "Địa Chỉ", "Đã Mượn",
                "Ngày Đăng Ký", "Ngày Hết Hạn", "Phí TV", "Mã LTV" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDocGia = new JTable(model);
        tblDocGia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblDocGia.setRowHeight(30);
        tblDocGia.setSelectionBackground(new Color(187, 222, 251));
        tblDocGia.setSelectionForeground(Color.BLACK);
        tblDocGia.setShowGrid(true);
        tblDocGia.setGridColor(new Color(224, 224, 224));
        tblDocGia.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Chỉnh độ rộng cột
        tblDocGia.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblDocGia.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblDocGia.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblDocGia.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblDocGia.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblDocGia.getColumnModel().getColumn(5).setPreferredWidth(160);
        tblDocGia.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblDocGia.getColumnModel().getColumn(7).setPreferredWidth(200);
        tblDocGia.getColumnModel().getColumn(8).setPreferredWidth(70);
        tblDocGia.getColumnModel().getColumn(9).setPreferredWidth(100);
        tblDocGia.getColumnModel().getColumn(10).setPreferredWidth(100);
        tblDocGia.getColumnModel().getColumn(11).setPreferredWidth(90);
        tblDocGia.getColumnModel().getColumn(12).setPreferredWidth(70);

        JTableHeader header = tblDocGia.getTableHeader();
        header.setPreferredSize(new Dimension(100, 40));
        header.setReorderingAllowed(false);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(240, 240, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblDocGia.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblDocGia.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblDocGia.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblDocGia.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tblDocGia);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // =========================================================
        // 3. FORM NHẬP LIỆU VÀ NÚT CHỨC NĂNG (SOUTH)
        // =========================================================
        JPanel pnlSouth = new JPanel(new BorderLayout(0, 15));
        pnlSouth.setBackground(Color.WHITE);

        // --- Form Nhập Liệu ---
        JPanel pnlInput = new JPanel(new GridLayout(4, 3, 20, 15));
        pnlInput.setBackground(Color.WHITE);
        TitledBorder borderInput = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin chi tiết độc giả");
        borderInput.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        borderInput.setTitleColor(COLOR_PRIMARY);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(borderInput, new EmptyBorder(15, 15, 15, 15)));

        // Khởi tạo các trường nhập liệu
        txtMa = new JTextField();
        txtHoDem = new JTextField();
        txtTen = new JTextField();
        cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
        txtNgaySinh = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();
        txtDiaChi = new JTextField();
        txtPhiTV = new JTextField();
        txtLoaiTV = new JTextField();

        jdNgayDangKy = new JDateChooser();
        jdNgayDangKy.setDateFormatString("yyyy-MM-dd");
        jdNgayDangKy.setDate(new java.util.Date());

        txtNgayHetHan = new JTextField();
        txtNgayHetHan.setEditable(false);
        txtNgayHetHan.setBackground(new Color(240, 240, 240));
        txtNgayHetHan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtNgayHetHan.setForeground(new Color(211, 47, 47));

        // Logic tự động điền ngày hết hạn
        jdNgayDangKy.addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                tinhToanNgayHetHan();
            }
        });

        txtPhiTV.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                tinhToanNgayHetHan();
            }

            public void removeUpdate(DocumentEvent e) {
                tinhToanNgayHetHan();
            }

            public void insertUpdate(DocumentEvent e) {
                tinhToanNgayHetHan();
            }
        });

        // Add form items theo thứ tự Lưới 4x3
        pnlInput.add(createFormItem("Mã ĐG:", txtMa));
        pnlInput.add(createFormItem("Họ đệm:", txtHoDem));
        pnlInput.add(createFormItem("Tên:", txtTen));

        pnlInput.add(createFormItem("Giới tính:", cboGioiTinh));
        pnlInput.add(createFormItem("Ngày sinh:", txtNgaySinh));
        pnlInput.add(createFormItem("SĐT:", txtSDT));

        pnlInput.add(createFormItem("Email:", txtEmail));
        pnlInput.add(createFormItem("Địa chỉ:", txtDiaChi));
        pnlInput.add(createFormItem("Loại TV:", txtLoaiTV));

        pnlInput.add(createFormItem("Ngày ĐK:", jdNgayDangKy));
        pnlInput.add(createFormItem("Ngày HH:", txtNgayHetHan));
        pnlInput.add(createFormItem("Phí TV:", txtPhiTV));

        // --- Nhóm Nút Bấm ---
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlBtn.setBackground(Color.WHITE);

        btnThem = createButton("Thêm", "add.png", new Color(46, 204, 113));
        btnSua = createButton("Cập Nhật", "sua.png", new Color(241, 196, 15));
        btnXoa = createButton("Xóa", "xoa.png", new Color(231, 76, 60));
        btnLamMoi = createButton("Làm Mới", "lammoi.png", new Color(149, 165, 166));
        btnNhapExcel = createButton("Nhập Excel", "import.png", new Color(39, 174, 96));
        btnXuatExcel = createButton("Xuất Excel", "export.png", new Color(41, 128, 185));
        btnInDanhSach = createButton("In DS PDF", "pdf.png", new Color(155, 89, 182));
        btnInThe = createButton("In Thẻ PDF", "pdf.png", new Color(142, 68, 173));

        pnlBtn.add(btnThem);
        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);
        pnlBtn.add(btnLamMoi);
        pnlBtn.add(btnNhapExcel);
        pnlBtn.add(btnXuatExcel);
        pnlBtn.add(btnInDanhSach);
        pnlBtn.add(btnInThe);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);
        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);

        add(pnlSouth, BorderLayout.SOUTH);

        // =========================================================
        // 4. SỰ KIỆN LẮNG NGHE (EVENTS)
        // =========================================================

        tblDocGia.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblDocGia.getSelectedRow();
                if (row >= 0) {
                    txtMa.setText(model.getValueAt(row, 0) != null ? model.getValueAt(row, 0).toString() : "");
                    txtMa.setEditable(false);
                    txtHoDem.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");
                    txtTen.setText(model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "");
                    cboGioiTinh.setSelectedItem(
                            model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "Nam");
                    txtNgaySinh.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
                    txtEmail.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
                    txtSDT.setText(model.getValueAt(row, 6) != null ? model.getValueAt(row, 6).toString() : "");
                    txtDiaChi.setText(model.getValueAt(row, 7) != null ? model.getValueAt(row, 7).toString() : "");

                    if (model.getValueAt(row, 9) != null && !model.getValueAt(row, 9).toString().isEmpty()) {
                        try {
                            java.util.Date dateDK = new java.text.SimpleDateFormat("yyyy-MM-dd")
                                    .parse(model.getValueAt(row, 9).toString());
                            jdNgayDangKy.setDate(dateDK);
                        } catch (Exception ex) {
                            jdNgayDangKy.setDate(null);
                        }
                    } else {
                        jdNgayDangKy.setDate(null);
                    }

                    txtNgayHetHan
                            .setText(model.getValueAt(row, 10) != null ? model.getValueAt(row, 10).toString() : "");
                    txtPhiTV.setText(model.getValueAt(row, 11) != null ? model.getValueAt(row, 11).toString() : "");
                    txtLoaiTV.setText(model.getValueAt(row, 12) != null ? model.getValueAt(row, 12).toString() : "");
                }
            }
        });

        tblDocGia.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    // Gọi sự kiện click để điền form (tái sử dụng code)
                    MouseEvent me = new MouseEvent(tblDocGia, 0, 0, 0, 0, 0, 1, false);
                    for (MouseListener ml : tblDocGia.getMouseListeners()) {
                        ml.mouseClicked(me);
                    }
                }
            }
        });

        btnTimKiem.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText();
            loadTable(dgBUS.timKiem(tuKhoa));
        });

        // Tìm kiếm realtime trên RAM giống Kho Sách (Optional)
        txtTimKiem.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                loadTable(dgBUS.timKiem(txtTimKiem.getText()));
            }
        });

        btnTimNangCao.addActionListener(e -> showAdvancedSearchDialog());

        btnHuyTim.addActionListener(e -> {
            txtTimKiem.setText("");
            loadTable(dgBUS.getList());
        });

        btnThem.addActionListener(e -> {
            try {
                java.sql.Date sqlNgayDK = null;
                if (jdNgayDangKy.getDate() != null) {
                    sqlNgayDK = new java.sql.Date(jdNgayDangKy.getDate().getTime());
                }

                java.sql.Date sqlNgayHH = null;
                if (!txtNgayHetHan.getText().trim().isEmpty()) {
                    sqlNgayHH = Date.valueOf(txtNgayHetHan.getText().trim());
                }

                DocGiaDTO dg = new DocGiaDTO(
                        txtMa.getText().trim(), txtHoDem.getText().trim(), txtTen.getText().trim(),
                        cboGioiTinh.getSelectedItem().toString(), Date.valueOf(txtNgaySinh.getText().trim()),
                        txtEmail.getText().trim(), txtSDT.getText().trim(), txtDiaChi.getText().trim(),
                        0, sqlNgayDK, sqlNgayHH, Double.parseDouble(txtPhiTV.getText().trim()),
                        txtLoaiTV.getText().trim());

                String msg = dgBUS.add(dg);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) {
                    loadTable(dgBUS.getList());
                    lamMoi();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi nhập liệu! Vui lòng kiểm tra định dạng Ngày Sinh (yyyy-mm-dd) và Phí TV (số).", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSua.addActionListener(e -> {
            String maDG = txtMa.getText().trim();
            if (maDG.isEmpty() || txtMa.isEditable()) {
                JOptionPane.showMessageDialog(this, "Vui lòng click chọn một độc giả trên bảng để cập nhật!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                java.sql.Date sqlNgayDK = null;
                if (jdNgayDangKy.getDate() != null) {
                    sqlNgayDK = new java.sql.Date(jdNgayDangKy.getDate().getTime());
                }

                java.sql.Date sqlNgayHH = null;
                if (!txtNgayHetHan.getText().trim().isEmpty()) {
                    sqlNgayHH = Date.valueOf(txtNgayHetHan.getText().trim());
                }

                DocGiaDTO dg = new DocGiaDTO(
                        maDG, txtHoDem.getText().trim(), txtTen.getText().trim(),
                        cboGioiTinh.getSelectedItem().toString(), Date.valueOf(txtNgaySinh.getText().trim()),
                        txtEmail.getText().trim(), txtSDT.getText().trim(), txtDiaChi.getText().trim(),
                        0, sqlNgayDK, sqlNgayHH, Double.parseDouble(txtPhiTV.getText().trim()),
                        txtLoaiTV.getText().trim());

                String msg = dgBUS.update(dg);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) {
                    loadTable(dgBUS.getList());
                    lamMoi();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi nhập liệu! Vui lòng kiểm tra định dạng.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnXoa.addActionListener(e -> {
            String maDG = txtMa.getText().trim();
            if (maDG.isEmpty())
                return;
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa độc giả mã: " + maDG + " không?", "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                String msg = dgBUS.delete(maDG);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) {
                    loadTable(dgBUS.getList());
                    lamMoi();
                }
            }
        });

        btnLamMoi.addActionListener(e -> lamMoi());

        btnXuatExcel.addActionListener(e -> {
            if (tblDocGia.getRowCount() == 0)
                return;
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chọn vị trí lưu file Excel");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
            chooser.setFileFilter(filter);
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".xlsx"))
                        filePath += ".xlsx";
                    ExcelHelper.exportExcel(tblDocGia, filePath);
                    JOptionPane.showMessageDialog(this, "Xuất dữ liệu thành công!\nĐã lưu tại: " + filePath);
                } catch (Exception ex) {
                }
            }
        });

        btnInDanhSach.addActionListener(e -> {
            if (tblDocGia.getRowCount() == 0)
                return;
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Lưu Danh Sách PDF");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf");
            chooser.setFileFilter(filter);
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                if (!path.toLowerCase().endsWith(".pdf"))
                    path += ".pdf";
                util.PDFHelper.exportDanhSach(tblDocGia, path);
                JOptionPane.showMessageDialog(this, "Xuất PDF danh sách thành công!");
            }
        });

        btnInThe.addActionListener(e -> {
            int row = tblDocGia.getSelectedRow();
            if (row == -1)
                return;
            try {
                DocGiaDTO dg = new DocGiaDTO();
                dg.setMaDG(tblDocGia.getValueAt(row, 0).toString());
                dg.setHoDem(tblDocGia.getValueAt(row, 1).toString());
                dg.setTen(tblDocGia.getValueAt(row, 2).toString());
                dg.setGioiTinh(tblDocGia.getValueAt(row, 3).toString());
                dg.setNgaySinh(java.sql.Date.valueOf(tblDocGia.getValueAt(row, 4).toString()));
                dg.setSdt(tblDocGia.getValueAt(row, 6).toString());
                if (tblDocGia.getValueAt(row, 10) != null && !tblDocGia.getValueAt(row, 10).toString().isEmpty()) {
                    dg.setNgayHetHan(java.sql.Date.valueOf(tblDocGia.getValueAt(row, 10).toString()));
                }
                dg.setMaLoaiTV(tblDocGia.getValueAt(row, 12).toString());

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Lưu Thẻ Độc Giả PDF");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf");
                chooser.setFileFilter(filter);

                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    if (!path.toLowerCase().endsWith(".pdf"))
                        path += ".pdf";
                    util.PDFHelper.exportTheDocGia(dg, path);
                    JOptionPane.showMessageDialog(this, "In thẻ thành công!");
                }
            } catch (Exception ex) {
            }
        });
    }

    // =========================================================
    // CÁC HÀM HỖ TRỢ VẼ GIAO DIỆN MỚI (Từ Form Quản Lý Sách)
    // =========================================================

    private JPanel createFormItem(String labelText, JComponent comp) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setPreferredSize(new Dimension(85, 30)); // 85px để fit với text "Ngày Sinh:"

        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setPreferredSize(new Dimension(0, 30));

        panel.add(lbl, BorderLayout.WEST);
        panel.add(comp, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, String iconName, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 35));
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
            } catch (Exception e) {
            }
        }
        return btn;
    }

    private void styleSearchButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // =========================================================
    // CÁC HÀM XỬ LÝ LOGIC HIỆN TẠI ĐƯỢC GIỮ NGUYÊN
    // =========================================================

    private void tinhToanNgayHetHan() {
        java.util.Date ngayDK = jdNgayDangKy.getDate();
        String phiStr = txtPhiTV.getText().trim().replace(",", "").replace(".", "");

        if (ngayDK != null && !phiStr.isEmpty()) {
            try {
                double phi = Double.parseDouble(phiStr);
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(ngayDK);

                if (phi >= 0) {
                    if (phi == 0) {
                        cal.add(java.util.Calendar.YEAR, 4);
                    } else {
                        cal.add(java.util.Calendar.YEAR, 1);
                    }
                    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    txtNgayHetHan.setText(df.format(cal.getTime()));
                } else {
                    txtNgayHetHan.setText("Phí không hợp lệ");
                }

            } catch (NumberFormatException e) {
                txtNgayHetHan.setText("Phí phải là số");
            }
        } else {
            txtNgayHetHan.setText("");
        }
    }

    private void showAdvancedSearchDialog() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Tìm kiếm nâng cao Độc Giả", true);
        dialog.setSize(500, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("BỘ LỌC TÌM KIẾM ĐỘC GIẢ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        dialog.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 15, 15));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        Font fontForm = new Font("Segoe UI", Font.PLAIN, 14);

        pnlForm.add(new JLabel("Ngày hết hạn (Từ):"));
        JDateChooser tuNgayChooser = new JDateChooser();
        tuNgayChooser.setDateFormatString("yyyy-MM-dd");
        ((JTextField) tuNgayChooser.getDateEditor().getUiComponent()).setEditable(false);
        pnlForm.add(tuNgayChooser);

        pnlForm.add(new JLabel("Ngày hết hạn (Đến):"));
        JDateChooser denNgayChooser = new JDateChooser();
        denNgayChooser.setDateFormatString("yyyy-MM-dd");
        ((JTextField) denNgayChooser.getDateEditor().getUiComponent()).setEditable(false);
        pnlForm.add(denNgayChooser);

        pnlForm.add(new JLabel("Giới tính:"));
        JComboBox<String> cboGioiTinhLoc = new JComboBox<>(new String[] { "Tất cả", "Nam", "Nữ" });
        cboGioiTinhLoc.setFont(fontForm);
        cboGioiTinhLoc.setBackground(Color.WHITE);
        pnlForm.add(cboGioiTinhLoc);

        pnlForm.add(new JLabel("Loại thành viên:"));
        JComboBox<String> cboLoaiTV = new JComboBox<>();
        cboLoaiTV.setFont(fontForm);
        cboLoaiTV.setBackground(Color.WHITE);
        cboLoaiTV.addItem("Tất cả");

        java.util.Set<String> setLoaiTV = new java.util.HashSet<>();
        for (dto.DocGiaDTO dg : dgBUS.getList()) {
            if (dg.getMaLoaiTV() != null && !dg.getMaLoaiTV().trim().isEmpty()) {
                setLoaiTV.add(dg.getMaLoaiTV());
            }
        }
        for (String loai : setLoaiTV) {
            cboLoaiTV.addItem(loai);
        }
        pnlForm.add(cboLoaiTV);

        pnlForm.add(new JLabel("Phí TV (Từ mức):"));
        JTextField txtTuPhi = new JTextField();
        txtTuPhi.setFont(fontForm);
        pnlForm.add(txtTuPhi);

        pnlForm.add(new JLabel("Phí TV (Đến mức):"));
        JTextField txtDenPhi = new JTextField();
        txtDenPhi.setFont(fontForm);
        pnlForm.add(txtDenPhi);

        dialog.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnSearch = new JButton("Áp dụng bộ lọc");
        styleSearchButton(btnSearch, COLOR_PRIMARY);

        JButton btnCancel = new JButton("Hủy bỏ");
        styleSearchButton(btnCancel, new Color(97, 101, 105));

        pnlButtons.add(btnSearch);
        pnlButtons.add(btnCancel);
        dialog.add(pnlButtons, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> {
            java.util.Date tuNgay = tuNgayChooser.getDate();
            java.util.Date denNgay = denNgayChooser.getDate();
            String gioiTinh = cboGioiTinhLoc.getSelectedItem().toString();
            String loaiTV = cboLoaiTV.getSelectedItem().toString();

            Double tuPhi = null;
            Double denPhi = null;

            try {
                if (!txtTuPhi.getText().trim().isEmpty()) {
                    tuPhi = Double.parseDouble(txtTuPhi.getText().trim());
                }
                if (!txtDenPhi.getText().trim().isEmpty()) {
                    denPhi = Double.parseDouble(txtDenPhi.getText().trim());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: Phí thành viên phải là con số hợp lệ!", "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tuNgay != null && denNgay != null && tuNgay.after(denNgay)) {
                JOptionPane.showMessageDialog(dialog, "'Từ ngày' không được lớn hơn 'Đến ngày'!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tuPhi != null && denPhi != null && tuPhi > denPhi) {
                JOptionPane.showMessageDialog(dialog, "Phí 'Từ mức' không được lớn hơn phí 'Đến mức'!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            loadTable(dgBUS.timKiemNangCao(tuNgay, denNgay, gioiTinh, loaiTV, tuPhi, denPhi));
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void lamMoi() {
        txtMa.setText("");
        txtMa.setEditable(true);
        txtHoDem.setText("");
        txtTen.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtNgaySinh.setText("");
        txtEmail.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtPhiTV.setText("");
        txtLoaiTV.setText("");

        jdNgayDangKy.setDate(new java.util.Date());
        txtNgayHetHan.setText("");

        tblDocGia.clearSelection();
    }

    private void loadTable(ArrayList<DocGiaDTO> list) {
        model.setRowCount(0);
        if (list == null)
            return;
        for (DocGiaDTO d : list) {
            model.addRow(new Object[] {
                    d.getMaDG(), d.getHoDem(), d.getTen(), d.getGioiTinh(), d.getNgaySinh(),
                    d.getEmail(), d.getSdt(), d.getDiaChi(), d.getSoSachMuon(),
                    d.getNgayDangKy(), d.getNgayHetHan(), d.getTienPhiThanhVien(), d.getMaLoaiTV()
            });
        }
    }
}