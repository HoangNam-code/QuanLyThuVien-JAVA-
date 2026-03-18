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

public class QuanLyDocGiaPanel extends BackgroundPanel {

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

    // Màu chủ đạo SGU
    private final Color COLOR_PRIMARY = new Color(25, 118, 210);

    public QuanLyDocGiaPanel() {
        initComponents();
        loadTable(dgBUS.getList());
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // =========================================================
        // 1. PANEL PHÍA TRÊN
        // =========================================================
        JPanel pnlTop = new JPanel(new BorderLayout(0, 15));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setOpaque(true); 
        pnlTop.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐỘC GIẢ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTop.add(lblTitle, BorderLayout.NORTH);

        // Form Nhập liệu
        JPanel pnlInput = new JPanel(new GridLayout(4, 6, 15, 15));
        pnlInput.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
                " Thông tin chi tiết ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);
        txtMa = createTextField(fontInput);
        txtHoDem = createTextField(fontInput);
        txtTen = createTextField(fontInput);
        cboGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ" });
        cboGioiTinh.setFont(fontInput);
        cboGioiTinh.setBackground(Color.WHITE);
        txtNgaySinh = createTextField(fontInput);
        txtSDT = createTextField(fontInput);
        txtEmail = createTextField(fontInput);
        txtDiaChi = createTextField(fontInput);
        txtPhiTV = createTextField(fontInput);
        txtLoaiTV = createTextField(fontInput);
        
        // Khởi tạo các trường liên quan đến Ngày đăng ký & Hết hạn
        jdNgayDangKy = new JDateChooser();
        jdNgayDangKy.setDateFormatString("yyyy-MM-dd");
        jdNgayDangKy.setDate(new java.util.Date()); // Mặc định là ngày hôm nay

        txtNgayHetHan = createTextField(fontInput);
        txtNgayHetHan.setEditable(false); // Không cho phép chỉnh sửa
        txtNgayHetHan.setBackground(new Color(240, 240, 240)); // Tô xám để biểu thị chỉ đọc
        txtNgayHetHan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtNgayHetHan.setForeground(new Color(211, 47, 47)); // Màu chữ đỏ cho nổi bật
        
        // --- XỬ LÝ LOGIC TỰ ĐỘNG ĐIỀN NGÀY HẾT HẠN ---
        // Lắng nghe thay đổi khi chọn ngày đăng ký
        jdNgayDangKy.addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                tinhToanNgayHetHan();
            }
        });
        
        // Lắng nghe thay đổi khi gõ tiền phí thành viên
        txtPhiTV.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { tinhToanNgayHetHan(); }
            public void removeUpdate(DocumentEvent e) { tinhToanNgayHetHan(); }
            public void insertUpdate(DocumentEvent e) { tinhToanNgayHetHan(); }
        });

        // Row 1
        pnlInput.add(createLabel("Mã ĐG:"));
        pnlInput.add(txtMa);
        pnlInput.add(createLabel("Họ đệm:"));
        pnlInput.add(txtHoDem);
        pnlInput.add(createLabel("Tên:"));
        pnlInput.add(txtTen);
        // Row 2
        pnlInput.add(createLabel("Giới tính:"));
        pnlInput.add(cboGioiTinh);
        pnlInput.add(createLabel("Ngày sinh (yyyy-mm-dd):"));
        pnlInput.add(txtNgaySinh);
        pnlInput.add(createLabel("SĐT:"));
        pnlInput.add(txtSDT);
        // Row 3
        pnlInput.add(createLabel("Email:"));
        pnlInput.add(txtEmail);
        pnlInput.add(createLabel("Địa chỉ:"));
        pnlInput.add(txtDiaChi);
        pnlInput.add(createLabel("Phí thành viên:"));
        pnlInput.add(txtPhiTV);
        // Row 4 - Chứa thông tin đăng ký và hết hạn
        pnlInput.add(createLabel("Loại TV:"));
        pnlInput.add(txtLoaiTV);
        pnlInput.add(createLabel("Ngày Đăng Ký:"));
        pnlInput.add(jdNgayDangKy);
        pnlInput.add(createLabel("Ngày Hết Hạn:"));
        pnlInput.add(txtNgayHetHan);

        JPanel pnlFormAndButtons = new JPanel(new BorderLayout(0, 15));
        pnlFormAndButtons.setBackground(Color.WHITE);
        pnlFormAndButtons.add(pnlInput, BorderLayout.CENTER);

        // Nhóm nút bấm thao tác
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBackground(Color.WHITE);

        btnThem = new JButton("Thêm Mới");
        styleButton(btnThem, new Color(25, 118, 210));
        btnSua = new JButton("Cập Nhật");
        styleButton(btnSua, new Color(245, 124, 0));
        btnXoa = new JButton("Xóa");
        styleButton(btnXoa, new Color(211, 47, 47));
        btnLamMoi = new JButton("Làm Mới Form");
        styleButton(btnLamMoi, new Color(97, 101, 105));
        btnNhapExcel = new JButton("Nhập Excel");
        styleButton(btnNhapExcel, new Color(46, 125, 50));
        btnXuatExcel = new JButton("Xuất Excel");
        styleButton(btnXuatExcel, new Color(46, 125, 50));
        btnInDanhSach = new JButton("In Danh Sách PDF");
        styleButton(btnInDanhSach, new Color(156, 39, 176));
        btnInThe = new JButton("In Thẻ PDF");
        styleButton(btnInThe, new Color(156, 39, 176));

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnNhapExcel);
        pnlButtons.add(btnXuatExcel);
        pnlButtons.add(btnInDanhSach);
        pnlButtons.add(btnInThe);

        pnlFormAndButtons.add(pnlButtons, BorderLayout.SOUTH);
        pnlTop.add(pnlFormAndButtons, BorderLayout.CENTER);

        // =========================================================
        // 2. KHU VỰC TÌM KIẾM & BẢNG DỮ LIỆU
        // =========================================================
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setOpaque(true); 
        pnlCenter.setBorder(new EmptyBorder(15, 20, 15, 20)); 

        // Thanh Tìm Kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));

        txtTimKiem = createTextField(fontInput);
        txtTimKiem.setPreferredSize(new Dimension(300, 30));

        btnTimKiem = new JButton("Tìm cơ bản");
        styleButton(btnTimKiem, COLOR_PRIMARY);
        btnTimNangCao = new JButton("Tìm nâng cao");
        styleButton(btnTimNangCao, new Color(245, 124, 0));
        btnHuyTim = new JButton("Hủy tìm");
        styleButton(btnHuyTim, new Color(97, 101, 105));

        pnlSearch.add(createLabel("Từ khóa (Mã/Tên/SĐT):"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);
        pnlSearch.add(btnTimNangCao);
        pnlSearch.add(btnHuyTim);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // Bảng dữ liệu (Đã chèn thêm cột Ngày Đăng Ký)
        String[] cols = { "Mã DG", "Họ Đệm", "Tên", "Giới Tính", "Ngày Sinh", "Email", "SĐT", "Địa Chỉ", "Đã Mượn",
                "Ngày Đăng Ký", "Ngày Hết Hạn", "Phí TV", "Mã LTV" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDocGia = new JTable(model);
        tblDocGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDocGia.setRowHeight(30);
        tblDocGia.setSelectionBackground(new Color(187, 222, 251));
        tblDocGia.setSelectionForeground(Color.BLACK);
        tblDocGia.setShowGrid(true);
        tblDocGia.setGridColor(new Color(224, 224, 224));
        tblDocGia.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tblDocGia.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblDocGia.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblDocGia.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblDocGia.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblDocGia.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblDocGia.getColumnModel().getColumn(5).setPreferredWidth(160);
        tblDocGia.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblDocGia.getColumnModel().getColumn(7).setPreferredWidth(200);
        tblDocGia.getColumnModel().getColumn(8).setPreferredWidth(70);
        tblDocGia.getColumnModel().getColumn(9).setPreferredWidth(100);  // Ngày Đăng Ký
        tblDocGia.getColumnModel().getColumn(10).setPreferredWidth(100); // Ngày Hết Hạn
        tblDocGia.getColumnModel().getColumn(11).setPreferredWidth(90);
        tblDocGia.getColumnModel().getColumn(12).setPreferredWidth(70);

        JTableHeader header = tblDocGia.getTableHeader();
        header.setPreferredSize(new Dimension(100, 40));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(235, 238, 240));
        headerRenderer.setForeground(new Color(30, 30, 30));
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(200, 200, 200)));

        for (int i = 0; i < tblDocGia.getColumnModel().getColumnCount(); i++) {
            tblDocGia.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

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

        add(pnlTop, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        // =========================================================
        // 3. SỰ KIỆN LẮNG NGHE (EVENTS)
        // =========================================================

        // Sự kiện Bảng
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
                    
                    // Nạp dữ liệu vào ô JDateChooser Ngày đăng ký
                    if (model.getValueAt(row, 9) != null && !model.getValueAt(row, 9).toString().isEmpty()) {
                        try {
                            java.util.Date dateDK = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(row, 9).toString());
                            jdNgayDangKy.setDate(dateDK);
                        } catch(Exception ex) {
                            jdNgayDangKy.setDate(null);
                        }
                    } else {
                        jdNgayDangKy.setDate(null);
                    }
                    
                    txtNgayHetHan.setText(model.getValueAt(row, 10) != null ? model.getValueAt(row, 10).toString() : "");
                    txtPhiTV.setText(model.getValueAt(row, 11) != null ? model.getValueAt(row, 11).toString() : "");
                    txtLoaiTV.setText(model.getValueAt(row, 12) != null ? model.getValueAt(row, 12).toString() : "");
                }
            }
        });

        // --- SỰ KIỆN TÌM KIẾM CƠ BẢN ---
        btnTimKiem.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText();
            loadTable(dgBUS.timKiem(tuKhoa));
        });

        // --- SỰ KIỆN MỞ FORM TÌM KIẾM NÂNG CAO ---
        btnTimNangCao.addActionListener(e -> showAdvancedSearchDialog());

        // --- SỰ KIỆN HỦY TÌM KIẾM ---
        btnHuyTim.addActionListener(e -> {
            txtTimKiem.setText("");
            loadTable(dgBUS.getList());
        });

        // Nút Thêm
// --- Nút Thêm ---
        btnThem.addActionListener(e -> {
            try {
                java.sql.Date sqlNgayDK = null;
                if(jdNgayDangKy.getDate() != null) {
                    sqlNgayDK = new java.sql.Date(jdNgayDangKy.getDate().getTime());
                }
                
                java.sql.Date sqlNgayHH = null;
                if(!txtNgayHetHan.getText().trim().isEmpty()) {
                    sqlNgayHH = Date.valueOf(txtNgayHetHan.getText().trim());
                }

                // Truyền đủ 13 tham số vào Constructor
                DocGiaDTO dg = new DocGiaDTO(
                        txtMa.getText().trim(), txtHoDem.getText().trim(), txtTen.getText().trim(),
                        cboGioiTinh.getSelectedItem().toString(), Date.valueOf(txtNgaySinh.getText().trim()),
                        txtEmail.getText().trim(), txtSDT.getText().trim(), txtDiaChi.getText().trim(),
                        0, sqlNgayDK, sqlNgayHH, Double.parseDouble(txtPhiTV.getText().trim()), txtLoaiTV.getText().trim());

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

        // --- Nút Cập Nhật (Sửa) ---
        btnSua.addActionListener(e -> {
            String maDG = txtMa.getText().trim();
            if (maDG.isEmpty() || txtMa.isEditable()) {
                JOptionPane.showMessageDialog(this, "Vui lòng click chọn một độc giả trên bảng để cập nhật!",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                java.sql.Date sqlNgayDK = null;
                if(jdNgayDangKy.getDate() != null) {
                    sqlNgayDK = new java.sql.Date(jdNgayDangKy.getDate().getTime());
                }
                
                java.sql.Date sqlNgayHH = null;
                if(!txtNgayHetHan.getText().trim().isEmpty()) {
                    sqlNgayHH = Date.valueOf(txtNgayHetHan.getText().trim());
                }

                // Truyền đủ 13 tham số vào Constructor
                DocGiaDTO dg = new DocGiaDTO(
                        maDG, txtHoDem.getText().trim(), txtTen.getText().trim(),
                        cboGioiTinh.getSelectedItem().toString(), Date.valueOf(txtNgaySinh.getText().trim()),
                        txtEmail.getText().trim(), txtSDT.getText().trim(), txtDiaChi.getText().trim(),
                        0, sqlNgayDK, sqlNgayHH, Double.parseDouble(txtPhiTV.getText().trim()), txtLoaiTV.getText().trim());

                String msg = dgBUS.update(dg);
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
    // HÀM XỬ LÝ LOGIC TỰ ĐỘNG TÍNH NGÀY HẾT HẠN
    // =========================================================
    private void tinhToanNgayHetHan() {
        java.util.Date ngayDK = jdNgayDangKy.getDate();
        
        // Lấy chuỗi tiền và tự động bỏ dấu phẩy/chấm để tránh lỗi parse số
        String phiStr = txtPhiTV.getText().trim().replace(",", "").replace(".", "");

        if (ngayDK != null && !phiStr.isEmpty()) {
            try {
                // Ép kiểu phí thành viên sang double
                double phi = Double.parseDouble(phiStr);
                
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(ngayDK);
                
                // LƯU Ý: Số tiền không được âm, nếu người dùng nhập số âm thì có thể báo lỗi hoặc bỏ qua
                if (phi >= 0) {
                    if (phi == 0) {
                        // Nếu phí = 0: Hết hạn sau 4 năm
                        cal.add(java.util.Calendar.YEAR, 4);
                    } else {
                        // Nếu phí > 0: Hết hạn sau 1 năm
                        cal.add(java.util.Calendar.YEAR, 1);
                    }
                    
                    // Format và set lại text cho ô Ngày hết hạn
                    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    txtNgayHetHan.setText(df.format(cal.getTime()));
                } else {
                    txtNgayHetHan.setText("Phí không hợp lệ");
                }
                
            } catch (NumberFormatException e) {
                txtNgayHetHan.setText("Phí phải là số"); // Báo lỗi nếu nhập chữ
            }
        } else {
            txtNgayHetHan.setText(""); // Xóa rỗng nếu 1 trong 2 ô (Ngày ĐK hoặc Phí) bị trống
        }
    }

    // =========================================================
    // HÀM HIỂN THỊ HỘP THOẠI TÌM KIẾM NÂNG CAO
    // =========================================================
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

        pnlForm.add(createLabel("Ngày hết hạn (Từ):"));
        JDateChooser tuNgayChooser = new JDateChooser();
        tuNgayChooser.setDateFormatString("yyyy-MM-dd");
        ((JTextField) tuNgayChooser.getDateEditor().getUiComponent()).setEditable(false);

        JButton btnTuNgay = tuNgayChooser.getCalendarButton();
        btnTuNgay.setPreferredSize(new Dimension(40, 30));
        tuNgayChooser.removeAll();
        tuNgayChooser.setLayout(new BorderLayout());
        tuNgayChooser.add(btnTuNgay, BorderLayout.WEST);
        tuNgayChooser.add(tuNgayChooser.getDateEditor().getUiComponent(), BorderLayout.CENTER);

        pnlForm.add(tuNgayChooser);

        pnlForm.add(createLabel("Ngày hết hạn (Đến):"));
        JDateChooser denNgayChooser = new JDateChooser();
        denNgayChooser.setDateFormatString("yyyy-MM-dd");
        ((JTextField) denNgayChooser.getDateEditor().getUiComponent()).setEditable(false);

        JButton btnDenNgay = denNgayChooser.getCalendarButton();
        btnDenNgay.setPreferredSize(new Dimension(40, 30));
        denNgayChooser.removeAll();
        denNgayChooser.setLayout(new BorderLayout());
        denNgayChooser.add(btnDenNgay, BorderLayout.WEST);
        denNgayChooser.add(denNgayChooser.getDateEditor().getUiComponent(), BorderLayout.CENTER);

        pnlForm.add(denNgayChooser);

        pnlForm.add(createLabel("Giới tính:"));
        JComboBox<String> cboGioiTinh = new JComboBox<>(new String[] { "Tất cả", "Nam", "Nữ" });
        cboGioiTinh.setFont(fontForm);
        cboGioiTinh.setBackground(Color.WHITE);
        pnlForm.add(cboGioiTinh);

        pnlForm.add(createLabel("Loại thành viên:"));
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

        pnlForm.add(createLabel("Phí TV (Từ mức):"));
        JTextField txtTuPhi = createTextField(fontForm);
        pnlForm.add(txtTuPhi);

        pnlForm.add(createLabel("Phí TV (Đến mức):"));
        JTextField txtDenPhi = createTextField(fontForm);
        pnlForm.add(txtDenPhi);

        dialog.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);

        JButton btnSearch = new JButton("Áp dụng bộ lọc");
        styleButton(btnSearch, COLOR_PRIMARY);

        JButton btnCancel = new JButton("Hủy bỏ");
        styleButton(btnCancel, new Color(97, 101, 105));

        pnlButtons.add(btnSearch);
        pnlButtons.add(btnCancel);
        dialog.add(pnlButtons, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> {
            java.util.Date tuNgay = tuNgayChooser.getDate();
            java.util.Date denNgay = denNgayChooser.getDate();
            String gioiTinh = cboGioiTinh.getSelectedItem().toString();
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

    // --- CÁC HÀM HỖ TRỢ GIAO DIỆN ---

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }

    private JTextField createTextField(Font font) {
        JTextField txt = new JTextField();
        txt.setFont(font);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return txt;
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
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