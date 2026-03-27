package gui;

import bus.*;
import dto.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MuonTraSachPanel extends BackgroundPanel {

    private JTable tblPhieu, tblChiTiet;
    private DefaultTableModel modelPhieu, modelChiTiet;

    private JTextField txtMaPM, txtMaDG, txtMaNV, txtNgayMuon, txtNgayTraDuKien;
    private JTextField txtMaSach, txtSoLuong, txtTinhTrang, txtTongSoLuong, txtTimKiem;
    private JComboBox<String> cboTrangThai;

    private JButton btnThemPhieu, btnThemSach, btnTraSach, btnLamMoi, btnChonSach, btnChonDocGia, btnChonNhanVien;
    private JButton btnTimKiem, btnTimNangCao, btnHuyTim;
    private JButton btnNhapExcel, btnXuatExcel, btnInPDF;

    private PhieuMuonBus pmBUS = new PhieuMuonBus();
    private ChiTietPhieuMuonBus ctBUS = new ChiTietPhieuMuonBus();
    private SachBUS sachBUS = new SachBUS();
    private DocGiaBUS dgBUS = new DocGiaBUS();
    private NhanVienBUS nvBUS = new NhanVienBUS();

    private final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private String maPhieuVuaTao = null;  // Theo dõi phiếu mới tạo

    public MuonTraSachPanel() {
        initComponents();
        loadDataPhieu();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================== 1) KHỐI TIÊU ĐỀ VỚI GRADIENT ==================
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Header với gradient background
        JPanel pnlHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 118, 210),
                    getWidth(), getHeight(), new Color(13, 71, 161)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlHeader.setPreferredSize(new Dimension(0, 80));
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("📚 QUẢN LÝ MƯỢN - TRẢ SÁCH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        pnlTop.add(pnlHeader, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // ================== 2) KHỐI TRÁI: FORM INPUT VỚI MÀU ==================
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 10));
        pnlLeft.setBackground(new Color(245, 248, 250));
        pnlLeft.setOpaque(true);
        pnlLeft.setBorder(new EmptyBorder(15, 15, 15, 10));

        JPanel pnlInput = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setOpaque(true);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(25, 118, 210), 2),
                " 📋 Thông tin phiếu mượn ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(25, 118, 210)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        Font fontInput = new Font("Segoe UI", Font.PLAIN, 12);
        txtMaPM = createTextField(fontInput);
        
        txtMaDG = createTextField(fontInput);
        txtMaDG.setEditable(false);
        txtMaDG.setBackground(new Color(245, 245, 245));
        btnChonDocGia = new JButton("...");
        btnChonDocGia.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnChonDocGia.setPreferredSize(new Dimension(40, 28));
        btnChonDocGia.setFocusPainted(false);

        JPanel pnlMaDG = new JPanel(new BorderLayout(3, 0));
        pnlMaDG.setOpaque(false);
        pnlMaDG.add(txtMaDG, BorderLayout.CENTER);
        pnlMaDG.add(btnChonDocGia, BorderLayout.EAST);

        txtMaNV = createTextField(fontInput);
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(245, 245, 245));
        btnChonNhanVien = new JButton("...");
        btnChonNhanVien.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnChonNhanVien.setPreferredSize(new Dimension(40, 28));
        btnChonNhanVien.setFocusPainted(false);

        JPanel pnlMaNV = new JPanel(new BorderLayout(3, 0));
        pnlMaNV.setOpaque(false);
        pnlMaNV.add(txtMaNV, BorderLayout.CENTER);
        pnlMaNV.add(btnChonNhanVien, BorderLayout.EAST);

        txtNgayMuon = createTextField(fontInput);
        txtNgayTraDuKien = createTextField(fontInput);
        txtNgayTraDuKien.setEditable(true);
        txtNgayTraDuKien.setBackground(Color.WHITE);

        // 5x2 grid
        pnlInput.add(createLabel("Mã Phiếu:")); 
        pnlInput.add(txtMaPM);
        pnlInput.add(createLabel("Mã Độc Giả:")); 
        pnlInput.add(pnlMaDG);
        pnlInput.add(createLabel("Mã NV:")); 
        pnlInput.add(pnlMaNV);
        pnlInput.add(createLabel("Ngày Mượn:")); 
        pnlInput.add(txtNgayMuon);
        pnlInput.add(createLabel("Hạn Trả:")); 
        pnlInput.add(txtNgayTraDuKien);

        JPanel pnlButtonsTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pnlButtonsTop.setBackground(Color.WHITE);
        btnThemPhieu = new JButton("➕ Phiếu"); styleButton(btnThemPhieu, new Color(76, 175, 80));
        btnLamMoi = new JButton("↻ Làm Mới"); styleButton(btnLamMoi, new Color(158, 158, 158));
        btnTimNangCao = new JButton("🔍 Nâng Cao"); styleButton(btnTimNangCao, new Color(255, 152, 0));
        btnThemPhieu.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnTimNangCao.setFont(new Font("Segoe UI", Font.BOLD, 10));
        pnlButtonsTop.add(btnThemPhieu);
        pnlButtonsTop.add(btnLamMoi);
        pnlButtonsTop.add(btnTimNangCao);

        // Chi tiết mượn (2x2 grid)
        JPanel pnlChiTiet = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlChiTiet.setBackground(Color.WHITE);
        pnlChiTiet.setOpaque(true);
        pnlChiTiet.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 125, 50), 2),
                " 📖 Chi tiết mượn ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(46, 125, 50)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        txtMaSach = createTextField(fontInput);
        txtMaSach.setEditable(false);
        txtMaSach.setBackground(new Color(245, 245, 245));
        btnChonSach = new JButton("...");
        btnChonSach.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnChonSach.setPreferredSize(new Dimension(40, 28));
        btnChonSach.setFocusPainted(false);

        JPanel pnlMaSach = new JPanel(new BorderLayout(3, 0));
        pnlMaSach.setOpaque(false);
        pnlMaSach.add(txtMaSach, BorderLayout.CENTER);
        pnlMaSach.add(btnChonSach, BorderLayout.EAST);

        txtSoLuong = createTextField(fontInput);
        txtTinhTrang = createTextField(fontInput);
        txtTinhTrang.setText("Bình thường");

        pnlChiTiet.add(createLabel("Mã Sách:"));
        pnlChiTiet.add(pnlMaSach);
        pnlChiTiet.add(createLabel("Số Lượng:"));
        pnlChiTiet.add(txtSoLuong);

        JPanel pnlButtonsChiTiet = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pnlButtonsChiTiet.setBackground(Color.WHITE);
        btnThemSach = new JButton("➕ Sách"); styleButton(btnThemSach, new Color(33, 150, 243));
        btnTraSach = new JButton("↩️ Trả"); styleButton(btnTraSach, new Color(244, 81, 30));
        btnThemSach.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnTraSach.setFont(new Font("Segoe UI", Font.BOLD, 10));
        pnlButtonsChiTiet.add(btnThemSach);
        pnlButtonsChiTiet.add(btnTraSach);

        // Tổng hợp
        JPanel pnlInfo = new JPanel(new GridLayout(2, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(230, 242, 200));
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2d.setColor(new Color(130, 177, 60));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };
        pnlInfo.setOpaque(false);
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        txtTongSoLuong = createTextField(fontInput);
        txtTongSoLuong.setEditable(false);
        txtTongSoLuong.setBackground(new Color(245, 245, 245));

        cboTrangThai = new JComboBox<>(new String[]{"Chưa trả", "Đã trả"});
        cboTrangThai.setFont(fontInput);
        cboTrangThai.setBackground(Color.WHITE);
        cboTrangThai.setEnabled(false);

        pnlInfo.add(createLabel("Tổng SL:"));
        pnlInfo.add(txtTongSoLuong);
        pnlInfo.add(createLabel("Trạng thái:"));
        pnlInfo.add(cboTrangThai);

        // Export/Import buttons
        JPanel pnlButtonsBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pnlButtonsBottom.setBackground(Color.WHITE);
        btnNhapExcel = new JButton("📥 Nhập Excel"); styleButton(btnNhapExcel, new Color(76, 175, 80));
        btnXuatExcel = new JButton("📤 Xuất Excel"); styleButton(btnXuatExcel, new Color(76, 175, 80));
        btnInPDF = new JButton("📄 In PDF"); styleButton(btnInPDF, new Color(156, 39, 176));
        btnNhapExcel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnXuatExcel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnInPDF.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnNhapExcel.setToolTipText("Nhập dữ liệu từ file Excel");
        btnXuatExcel.setToolTipText("Xuất dữ liệu ra file Excel");
        btnInPDF.setToolTipText("In hoặc xuất phiếu mượn ra PDF");
        pnlButtonsBottom.add(btnNhapExcel);
        pnlButtonsBottom.add(btnXuatExcel);
        pnlButtonsBottom.add(btnInPDF);

        // Stack left panel
        pnlLeft.add(pnlInput, BorderLayout.NORTH);
        JPanel pnlLeftScroll = new JPanel(new BorderLayout(0, 8));
        pnlLeftScroll.setOpaque(false);
        pnlLeftScroll.add(pnlButtonsTop, BorderLayout.NORTH);
        pnlLeftScroll.add(pnlChiTiet, BorderLayout.CENTER);
        pnlLeftScroll.add(pnlButtonsChiTiet, BorderLayout.SOUTH);
        
        JScrollPane scrollLeft = new JScrollPane(pnlLeftScroll);
        scrollLeft.setOpaque(false);
        scrollLeft.getViewport().setOpaque(false);
        scrollLeft.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel pnlLeftBottom = new JPanel(new BorderLayout(0, 8));
        pnlLeftBottom.setBackground(Color.WHITE);
        pnlLeftBottom.add(pnlInfo, BorderLayout.NORTH);
        pnlLeftBottom.add(pnlButtonsBottom, BorderLayout.SOUTH);
        
        pnlLeft.add(scrollLeft, BorderLayout.CENTER);
        pnlLeft.add(pnlLeftBottom, BorderLayout.SOUTH);

        // ================== 3) KHỐI PHẢI: SEARCH + TABLES ==================
        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setBackground(new Color(245, 248, 250));
        pnlRight.setOpaque(true);
        pnlRight.setBorder(new EmptyBorder(15, 10, 15, 15));

        JPanel pnlSearch = new JPanel(new BorderLayout(5, 5));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setOpaque(true);
        pnlSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
                " 🔍 Tìm kiếm ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 11), new Color(33, 150, 243)
            ),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        Font fontSearch = new Font("Segoe UI", Font.PLAIN, 12);
        txtTimKiem = createTextField(fontSearch);
        txtTimKiem.setPreferredSize(new Dimension(150, 28));

        btnTimKiem = new JButton("Tìm"); styleButton(btnTimKiem, new Color(33, 150, 243));
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnHuyTim = new JButton("Hủy"); styleButton(btnHuyTim, new Color(189, 189, 189));
        btnHuyTim.setFont(new Font("Segoe UI", Font.BOLD, 11));

        JPanel pnlSearchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pnlSearchBar.setBackground(Color.WHITE);
        pnlSearchBar.add(new JLabel("Khóa:"));
        pnlSearchBar.add(txtTimKiem);
        pnlSearchBar.add(btnTimKiem);
        pnlSearchBar.add(btnHuyTim);

        pnlSearch.add(pnlSearchBar, BorderLayout.CENTER);

        modelPhieu = new DefaultTableModel(
                new Object[]{"Mã PM", "ĐG", "NV", "Ngày Mượn", "Hạn Trả"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelChiTiet = new DefaultTableModel(
            new Object[]{"Sách", "SL", "Tình Trạng", "Đã Trả", "Trạng Thái"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPhieu = new JTable(modelPhieu);
        tblChiTiet = new JTable(modelChiTiet);

        styleTable(tblPhieu);
        styleTable(tblChiTiet);
        
        // Set column widths
        if (tblChiTiet.getColumnModel().getColumnCount() >= 5) {
            tblChiTiet.getColumnModel().getColumn(0).setPreferredWidth(90);  // Sách
            tblChiTiet.getColumnModel().getColumn(1).setPreferredWidth(50);  // SL
            tblChiTiet.getColumnModel().getColumn(2).setPreferredWidth(90);  // Tình Trạng
            tblChiTiet.getColumnModel().getColumn(3).setPreferredWidth(60);  // Đã Trả
            tblChiTiet.getColumnModel().getColumn(4).setPreferredWidth(80);  // Trạng Thái
        }

        JScrollPane spPhieu = new JScrollPane(tblPhieu);
        JScrollPane spChiTiet = new JScrollPane(tblChiTiet);
        spPhieu.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 1),
            "📋 Danh sách phiếu mượn"
        ));
        spChiTiet.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(76, 175, 80), 1),
            "📚 Chi tiết phiếu"
        ));

        JSplitPane splitTable = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spPhieu, spChiTiet);
        splitTable.setDividerLocation(150);
        splitTable.setResizeWeight(0.4);

        pnlRight.add(pnlSearch, BorderLayout.NORTH);
        pnlRight.add(splitTable, BorderLayout.CENTER);

        // ================== 4) MAIN LAYOUT: LEFT-RIGHT SPLIT ==================
        JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlLeft, pnlRight);
        splitMain.setDividerLocation(300);
        splitMain.setResizeWeight(0.35);

        add(splitMain, BorderLayout.CENTER);

        // ================== 3) EVENTS ==================

        tblPhieu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieu.getSelectedRow();
                if (row >= 0) {
                    String maPM = valueAt(modelPhieu, row, 0);
                    txtMaPM.setText(maPM);
                    txtMaDG.setText(valueAt(modelPhieu, row, 1));
                    txtMaNV.setText(valueAt(modelPhieu, row, 2));
                    txtNgayMuon.setText(valueAt(modelPhieu, row, 3));
                    txtNgayTraDuKien.setText(valueAt(modelPhieu, row, 4));
                    lamMoiChiTiet();
                    loadDataChiTiet(maPM);
                    // Nếu chọn phiếu khác, reset maPhieuVuaTao
                    if (maPhieuVuaTao != null && !maPhieuVuaTao.equalsIgnoreCase(maPM)) {
                        maPhieuVuaTao = null;
                    }
                }
            }
        });

        tblChiTiet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblChiTiet.getSelectedRow();
                if (row >= 0) {
                    txtMaSach.setText(valueAt(modelChiTiet, row, 0));
                    txtSoLuong.setText(valueAt(modelChiTiet, row, 1));
                    txtTinhTrang.setText(valueAt(modelChiTiet, row, 2));

                    String maPM = txtMaPM.getText().trim();
                    String maSach = valueAt(modelChiTiet, row, 0);
                    ChiTietPhieuMuonDTO chiTiet = timChiTietByMaPMVaMaSach(maPM, maSach);
                    cboTrangThai.setSelectedItem(
                            chiTiet != null && chiTiet.getDaTra() == 1 ? "Đã trả" : "Chưa trả"
                    );
                }
            }
        });

        btnThemPhieu.addActionListener(e -> themPhieuMuonMoi());
        btnThemSach.addActionListener(e -> themSach());
        btnTraSach.addActionListener(e -> traSach());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnChonSach.addActionListener(e -> showSachPickerDialog());
        btnChonDocGia.addActionListener(e -> showDocGiaPickerDialog());
        btnChonNhanVien.addActionListener(e -> showNhanVienPickerDialog());

        btnTimKiem.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
            ArrayList<PhieuMuonDTO> ketQua = new ArrayList<>();
            for (PhieuMuonDTO pm : pmBUS.getList()) {
                if (pm.getMaPM().toLowerCase().contains(tuKhoa)
                        || pm.getMaDG().toLowerCase().contains(tuKhoa)
                        || pm.getMaNV().toLowerCase().contains(tuKhoa)) {
                    ketQua.add(pm);
                }
            }
            loadDataPhieu(ketQua);
        });

        btnTimNangCao.addActionListener(e -> showAdvancedSearchDialog());

        btnHuyTim.addActionListener(e -> {
            txtTimKiem.setText("");
            loadDataPhieu();
        });

        btnXuatExcel.addActionListener(e -> xuatExcel());
        btnNhapExcel.addActionListener(e -> nhapExcelChiTiet());
        btnInPDF.addActionListener(e -> inPDF());
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(187, 222, 251));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(224, 224, 224));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(100, 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(235, 238, 240));
        headerRenderer.setForeground(new Color(30, 30, 30));
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(200, 200, 200)));

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        // Custom renderer cho cột "Trạng Thái" (cột cuối cùng)
        if (table.getColumnModel().getColumnCount() >= 5) {
            DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(SwingConstants.CENTER);
                    
                    if (!isSelected) {
                        if (value != null && value.toString().equals("Đã trả")) {
                            c.setBackground(new Color(200, 230, 201)); // Xanh lá nhạt
                            c.setForeground(new Color(27, 94, 32)); // Xanh lá đậm
                        } else if (value != null && value.toString().equals("Đã mượn")) {
                            c.setBackground(new Color(255, 224, 178)); // Cam nhạt
                            c.setForeground(new Color(230, 124, 15)); // Cam đậm
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    }
                    return c;
                }
            };
            table.getColumnModel().getColumn(table.getColumnModel().getColumnCount() - 1).setCellRenderer(statusRenderer);
        }
    }

    private void themPhieuMuonMoi() {
        String maPM = txtMaPM.getText().trim();
        String maDG = txtMaDG.getText().trim();
        String maNV = txtMaNV.getText().trim();
        String ngayMuon = txtNgayMuon.getText().trim();
        String hanTra = txtNgayTraDuKien.getText().trim();

        if (maPM.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã phiếu mượn!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (maDG.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Mã độc giả!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Mã nhân viên!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ngayMuon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Ngày mượn (yyyy-mm-dd)!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (hanTra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Hạn trả (yyyy-mm-dd)!", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate dateMuon = parseDateOrNull(ngayMuon);
        LocalDate dateHanTra = parseDateOrNull(hanTra);

        if (dateMuon == null) {
            JOptionPane.showMessageDialog(this, "Ngày mượn phải đúng định dạng yyyy-mm-dd.", "Sai định dạng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dateHanTra == null) {
            JOptionPane.showMessageDialog(this, "Hạn trả phải đúng định dạng yyyy-mm-dd.", "Sai định dạng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (dateHanTra.isBefore(dateMuon)) {
            JOptionPane.showMessageDialog(this, "Hạn trả không thể trước ngày mượn!", "Lỗi logic", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (PhieuMuonDTO pm : pmBUS.getList()) {
            if (pm.getMaPM().equalsIgnoreCase(maPM)) {
                JOptionPane.showMessageDialog(this, "Mã phiếu đã tồn tại, vui lòng nhập mã khác.", "Trùng mã", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        PhieuMuonDTO pmMoi = new PhieuMuonDTO();
        pmMoi.setMaPM(maPM);
        pmMoi.setMaDG(maDG);
        pmMoi.setMaNV(maNV);
        pmMoi.setNgayMuon(ngayMuon);
        pmMoi.setNgayTraDuKien(hanTra);
        pmMoi.setNgayThucTra(null);
        pmMoi.setGhiChu("Tạo từ giao diện mượn sách");

        if (pmBUS.themPhieuMuon(pmMoi)) {
            maPhieuVuaTao = maPM;  // Lưu mã phiếu vừa tạo
            JOptionPane.showMessageDialog(this, "✓ Thêm phiếu mượn mới thành công!\nBây giờ bạn có thể thêm chi tiết sách.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadDataPhieu();
            for (int i = 0; i < modelPhieu.getRowCount(); i++) {
                if (maPM.equalsIgnoreCase(valueAt(modelPhieu, i, 0))) {
                    tblPhieu.setRowSelectionInterval(i, i);
                    break;
                }
            }
            loadDataChiTiet(maPM);
        } else {
            JOptionPane.showMessageDialog(this, "✗ Thêm phiếu mượn thất bại. Vui lòng kiểm tra lại dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSachPickerDialog() {
        sachBUS.docDanhSach();
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn mã sách", true);
        dialog.setSize(700, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        DefaultTableModel modelSach = new DefaultTableModel(new Object[]{"Mã Sách", "Tên Sách", "Số Lượng Tồn"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (SachDTO s : sachBUS.getList()) {
            if (s.getSoLuong() > 0) {
                modelSach.addRow(new Object[]{s.getMaSach(), s.getTenSach(), s.getSoLuong()});
            }
        }

        JTable tblSach = new JTable(modelSach);
        styleTable(tblSach);
        JScrollPane scroll = new JScrollPane(tblSach);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(scroll, BorderLayout.CENTER);

        JButton btnChon = new JButton("Chọn sách này");
        styleButton(btnChon, new Color(25, 118, 210));
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(btnChon);
        dialog.add(pnlBottom, BorderLayout.SOUTH);

        Runnable xuLyChon = () -> {
            int row = tblSach.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn 1 sách.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            txtMaSach.setText(String.valueOf(modelSach.getValueAt(row, 0)));
            dialog.dispose();
        };

        btnChon.addActionListener(e -> xuLyChon.run());
        tblSach.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    xuLyChon.run();
                }
            }
        });

        dialog.setVisible(true);
    }

    private void loadDataPhieu() {
        loadDataPhieu(pmBUS.getList());
    }

    private void loadDataPhieu(ArrayList<PhieuMuonDTO> list) {
        modelPhieu.setRowCount(0);
        if (list == null) return;

        for (PhieuMuonDTO pm : list) {
            modelPhieu.addRow(new Object[]{
                    pm.getMaPM(),
                    pm.getMaDG(),
                    pm.getMaNV(),
                    pm.getNgayMuon(),
                    pm.getNgayTraDuKien()
            });
        }
    }

    private void loadDataChiTiet(String maPM) {
        modelChiTiet.setRowCount(0);
        if (maPM == null || maPM.trim().isEmpty()) return;

        ArrayList<ChiTietPhieuMuonDTO> ds = ctBUS.getListByMaPM(maPM);
        int tongSoLuong = 0;
        for (ChiTietPhieuMuonDTO ct : ds) {
            tongSoLuong += ct.getSoLuong();
            int soLuongDaTra = ct.getDaTra() == 1 ? ct.getSoLuong() : 0;
            String trangThaiSach = ct.getDaTra() == 1 ? "Đã trả" : "Đã mượn";
            modelChiTiet.addRow(new Object[]{
                    ct.getMaSach(),
                    ct.getSoLuong(),
                    ct.getTinhTrangSach(),
                    soLuongDaTra,
                    trangThaiSach
            });
        }
        txtTongSoLuong.setText(String.valueOf(tongSoLuong));
    }

    private void themSach() {
        String maPM = txtMaPM.getText().trim();
        String maSach = txtMaSach.getText().trim();

        if (maPM.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn trước khi thêm sách!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (maSach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Sách!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên > 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!kiemTraTonKho(maSach, soLuong)) {
            JOptionPane.showMessageDialog(this, "Số lượng tồn kho không đủ để mượn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ChiTietPhieuMuonDTO ct = new ChiTietPhieuMuonDTO();
        ct.setMaPM(maPM);
        ct.setMaSach(maSach);
        ct.setSoLuong(soLuong);
        ct.setTinhTrangSach(txtTinhTrang.getText().trim().isEmpty() ? "Bình thường" : txtTinhTrang.getText().trim());
        // Mượn mới luôn là chưa trả.
        ct.setDaTra(0);

        if (ctBUS.them(ct)) {
            capNhatTonKho(maSach, -soLuong);
            JOptionPane.showMessageDialog(this, "Thêm chi tiết phiếu mượn thành công!");
            loadDataChiTiet(maPM);
            sachBUS.docDanhSach();
            lamMoiChiTiet();
            // Phiếu đã có chi tiết, không còn rỗng nữa
            if (maPhieuVuaTao != null && maPhieuVuaTao.equalsIgnoreCase(maPM)) {
                maPhieuVuaTao = null;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại! Có thể sách đã tồn tại trong phiếu này.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void traSach() {
        int row = tblChiTiet.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng sách cần trả!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maPM = txtMaPM.getText().trim();
        if (maPM.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn trước khi trả sách!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maSach = valueAt(modelChiTiet, row, 0);
        ChiTietPhieuMuonDTO chiTiet = timChiTietByMaPMVaMaSach(maPM, maSach);
        if (chiTiet == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết mượn tương ứng. Vui lòng tải lại dữ liệu.", "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            loadDataChiTiet(maPM);
            return;
        }

        int daTra = chiTiet.getDaTra();

        if (daTra == 1) {
            JOptionPane.showMessageDialog(this, "Sách này đã được trả trước đó.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (ctBUS.traSach(maPM, maSach)) {
            int soLuong = chiTiet.getSoLuong();
            capNhatTonKho(maSach, soLuong);
            JOptionPane.showMessageDialog(this, "Cập nhật trả sách thành công!");
            loadDataChiTiet(maPM);
            sachBUS.docDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật trả sách thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ChiTietPhieuMuonDTO timChiTietByMaPMVaMaSach(String maPM, String maSach) {
        ArrayList<ChiTietPhieuMuonDTO> ds = ctBUS.getListByMaPM(maPM);
        for (ChiTietPhieuMuonDTO ct : ds) {
            if (ct.getMaSach() != null && ct.getMaSach().equalsIgnoreCase(maSach)) {
                return ct;
            }
        }
        return null;
    }

    private boolean kiemTraTonKho(String maSach, int soLuongMuon) {
        for (SachDTO s : sachBUS.getList()) {
            if (s.getMaSach().equalsIgnoreCase(maSach)) {
                return s.getSoLuong() >= soLuongMuon;
            }
        }
        return false;
    }

    private void capNhatTonKho(String maSach, int soLuongThayDoi) {
        for (SachDTO s : sachBUS.getList()) {
            if (s.getMaSach().equalsIgnoreCase(maSach)) {
                SachDTO capNhat = new SachDTO(
                        s.getMaSach(),
                        s.getTenSach(),
                        s.getNamXB(),
                        s.getMaTL(),
                        s.getDonGia(),
                        Math.max(0, s.getSoLuong() + soLuongThayDoi),
                        s.getMaTG(),
                        s.getMaNXB(),
                        s.getSoTrang()
                );
                sachBUS.suaSach(capNhat);
                return;
            }
        }
    }

    private void nhapExcelChiTiet() {
        String maPM = txtMaPM.getText().trim();
        if (maPM.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn trước khi nhập Excel!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn file Excel chi tiết mượn");
        chooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            try {
                ArrayList<ChiTietPhieuMuonDTO> list = util.ExcelHelper.importChiTietPhieuMuon(selectedFile.getAbsolutePath(), maPM);

                int success = 0;
                for (ChiTietPhieuMuonDTO ct : list) {
                    if (!kiemTraTonKho(ct.getMaSach(), ct.getSoLuong())) {
                        continue;
                    }

                    if (ctBUS.them(ct)) {
                        capNhatTonKho(ct.getMaSach(), -ct.getSoLuong());
                        success++;
                    }
                }

                sachBUS.docDanhSach();
                loadDataChiTiet(maPM);
                JOptionPane.showMessageDialog(this, "Đã nhập thành công " + success + "/" + list.size() + " dòng từ Excel.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi nhập Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xuatExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn vị trí lưu file Excel");
        chooser.setSelectedFile(new File("BaoCao_MuonTraSach.xlsx"));
        chooser.setFileFilter(new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".xlsx")) {
                path += ".xlsx";
            }

            try {
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "Xuất bảng chi tiết? (No = xuất bảng phiếu mượn)",
                        "Chọn dữ liệu xuất",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                    return;
                }

                JTable target = (option == JOptionPane.YES_OPTION) ? tblChiTiet : tblPhieu;
                util.ExcelHelper.exportExcel(target, path);
                JOptionPane.showMessageDialog(this, "Xuất Excel thành công!\nĐã lưu tại: " + path);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void inPDF() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu PDF");
        chooser.setSelectedFile(new File("BaoCao_MuonTraSach.pdf"));
        chooser.setFileFilter(new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".pdf")) {
                path += ".pdf";
            }

            // Nếu có phiếu được chọn, cho phép in từng phiếu
            int selectedRow = tblPhieu.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "In phiếu mượn riêng lẻ? (Yes = phiếu được chọn, No = toàn bộ danh sách)",
                        "Chọn dữ liệu in",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                
                if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                    return;
                }

                // In phiếu riêng lẻ
                if (option == JOptionPane.YES_OPTION) {
                    exportPhieuMuonToPDF(selectedRow, path);
                    return;
                }
            }

            // In danh sách (mặc định)
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "In bảng chi tiết? (No = in bảng phiếu mượn)",
                    "Chọn dữ liệu in",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return;
            }

            JTable target = (option == JOptionPane.YES_OPTION) ? tblChiTiet : tblPhieu;
            util.PDFHelper.exportDanhSach(target, path);
            JOptionPane.showMessageDialog(this, "In PDF thành công!");
        }
    }

    // Hàm hỗ trợ: Xuất phiếu mượn riêng lẻ ra PDF
    private void exportPhieuMuonToPDF(int selectedRow, String filePath) {
        try {
            String maPM = tblPhieu.getValueAt(selectedRow, 0).toString();

            // Lấy thông tin phiếu mượn
            PhieuMuonDTO pm = new PhieuMuonDTO();
            pm.setMaPM(maPM);
            pm.setMaDG(tblPhieu.getValueAt(selectedRow, 1).toString());
            pm.setMaNV(tblPhieu.getValueAt(selectedRow, 2).toString());
            pm.setNgayMuon(tblPhieu.getValueAt(selectedRow, 3).toString());
            pm.setNgayTraDuKien(tblPhieu.getValueAt(selectedRow, 4).toString());

            // Lấy chi tiết phiếu mượn từ BUS (để đảm bảo đầy đủ)
            ArrayList<ChiTietPhieuMuonDTO> chiTietList = ctBUS.getListByMaPM(maPM);
            if (chiTietList == null) {
                chiTietList = new ArrayList<>();
            }

            // Xuất PDF
            util.PDFHelper.exportPhieuMuon(pm, chiTietList, filePath);
            JOptionPane.showMessageDialog(this, "Xuất phiếu mượn PDF thành công!\nĐã lưu tại: " + filePath);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất phiếu mượn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showAdvancedSearchDialog() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Tìm kiếm nâng cao phiếu mượn", true);
        dialog.setSize(500, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.getContentPane().setBackground(Color.WHITE);

        Font fontForm = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 12));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField txtAdvKeyword = createTextField(fontForm);
        txtAdvKeyword.setText(txtTimKiem.getText().trim());
        JTextField txtFromDate = createTextField(fontForm);
        JTextField txtToDate = createTextField(fontForm);
        JComboBox<String> cboAdvStatus = new JComboBox<>(new String[]{"Tất cả", "Đã trả", "Chưa trả"});
        cboAdvStatus.setFont(fontForm);
        cboAdvStatus.setBackground(Color.WHITE);

        pnlForm.add(createLabel("Từ khóa (PM/DG/NV):")); pnlForm.add(txtAdvKeyword);
        pnlForm.add(createLabel("Từ ngày mượn (yyyy-mm-dd):")); pnlForm.add(txtFromDate);
        pnlForm.add(createLabel("Đến ngày mượn (yyyy-mm-dd):")); pnlForm.add(txtToDate);
        pnlForm.add(createLabel("Trạng thái trả:")); pnlForm.add(cboAdvStatus);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(Color.WHITE);
        JButton btnSearch = new JButton("Tìm kiếm"); styleButton(btnSearch, COLOR_PRIMARY);
        JButton btnCancel = new JButton("Hủy bỏ"); styleButton(btnCancel, new Color(97, 101, 105));
        pnlButtons.add(btnSearch);
        pnlButtons.add(btnCancel);

        btnSearch.addActionListener(e -> {
            String keyword = txtAdvKeyword.getText().trim().toLowerCase();
            LocalDate fromDate = parseDateOrNull(txtFromDate.getText().trim());
            LocalDate toDate = parseDateOrNull(txtToDate.getText().trim());

            if ((!txtFromDate.getText().trim().isEmpty() && fromDate == null)
                    || (!txtToDate.getText().trim().isEmpty() && toDate == null)) {
                JOptionPane.showMessageDialog(dialog, "Ngày không đúng định dạng yyyy-mm-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String status = cboAdvStatus.getSelectedItem().toString();
            ArrayList<PhieuMuonDTO> result = new ArrayList<>();

            for (PhieuMuonDTO pm : pmBUS.getList()) {
                boolean matchKeyword = keyword.isEmpty()
                        || pm.getMaPM().toLowerCase().contains(keyword)
                        || pm.getMaDG().toLowerCase().contains(keyword)
                        || pm.getMaNV().toLowerCase().contains(keyword);

                LocalDate ngayMuon = parseDateOrNull(pm.getNgayMuon());
                boolean matchDate = true;
                if (ngayMuon != null) {
                    if (fromDate != null && ngayMuon.isBefore(fromDate)) matchDate = false;
                    if (toDate != null && ngayMuon.isAfter(toDate)) matchDate = false;
                }

                boolean isReturned = daTraHet(pm.getMaPM());
                boolean matchStatus = "Tất cả".equals(status)
                        || ("Đã trả".equals(status) && isReturned)
                        || ("Chưa trả".equals(status) && !isReturned);

                if (matchKeyword && matchDate && matchStatus) {
                    result.add(pm);
                }
            }

            txtTimKiem.setText(txtAdvKeyword.getText().trim());
            loadDataPhieu(result);
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlButtons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private boolean daTraHet(String maPM) {
        ArrayList<ChiTietPhieuMuonDTO> list = ctBUS.getListByMaPM(maPM);
        if (list.isEmpty()) return false;
        for (ChiTietPhieuMuonDTO ct : list) {
            if (ct.getDaTra() == 0) {
                return false;
            }
        }
        return true;
    }

    private void lamMoi() {
        String maPMHienTai = txtMaPM.getText().trim();
        
        // Nếu vừa tạo phiếu và chưa thêm chi tiết, hỏi có xóa không
        if (maPhieuVuaTao != null && !maPhieuVuaTao.isEmpty() && 
            maPhieuVuaTao.equalsIgnoreCase(maPMHienTai)) {
            
            int result = JOptionPane.showConfirmDialog(
                this,
                "Bạn vừa tạo phiếu \"" + maPMHienTai + "\" nhưng chưa thêm chi tiết sách.\n" +
                "Bạn có muốn xóa phiếu này không?",
                "Xác nhận xóa phiếu",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                try {
                    if (pmBUS.xoaPhieuMuon(maPMHienTai)) {
                        JOptionPane.showMessageDialog(
                            this,
                            "✓ Đã xóa phiếu mượn \"" + maPMHienTai + "\" và hoàn lại tồn kho.",
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        sachBUS.docDanhSach();
                        pmBUS.docDanhSach();  // Refresh dữ liệu phiếu mượn từ database
                        loadDataPhieu();
                        maPhieuVuaTao = null;
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "✗ Lỗi khi xóa phiếu mượn! Hãy xem console để biết chi tiết.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "✗ Exception khi xóa phiếu: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }
            // Nếu user chọn NO, tiếp tục làm mới form bình thường
        }
        
        // Reset các trường input
        txtMaPM.setText("");
        txtMaDG.setText("");
        txtMaNV.setText("");
        txtNgayMuon.setText("");
        txtNgayTraDuKien.setText("");
        txtTongSoLuong.setText("0");
        lamMoiChiTiet();
        tblPhieu.clearSelection();
        tblChiTiet.clearSelection();
        maPhieuVuaTao = null;
    }

    private void lamMoiChiTiet() {
        txtMaSach.setText("");
        txtSoLuong.setText("");
        txtTinhTrang.setText("Bình thường");
        cboTrangThai.setSelectedIndex(0);
    }

    private void showDocGiaPickerDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn mã độc giả", true);
        dialog.setSize(760, 430);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        DefaultTableModel modelDG = new DefaultTableModel(new Object[]{"Mã ĐG", "Họ Đệm", "Tên", "SĐT"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (DocGiaDTO dg : dgBUS.getList()) {
            modelDG.addRow(new Object[]{dg.getMaDG(), dg.getHoDem(), dg.getTen(), dg.getSdt()});
        }

        JTable tblDG = new JTable(modelDG);
        styleTable(tblDG);
        JScrollPane scroll = new JScrollPane(tblDG);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(scroll, BorderLayout.CENTER);

        JButton btnChon = new JButton("Chọn độc giả này");
        styleButton(btnChon, new Color(25, 118, 210));
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(btnChon);
        dialog.add(pnlBottom, BorderLayout.SOUTH);

        Runnable xuLyChon = () -> {
            int row = tblDG.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn 1 độc giả.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            txtMaDG.setText(String.valueOf(modelDG.getValueAt(row, 0)));
            dialog.dispose();
        };

        btnChon.addActionListener(e -> xuLyChon.run());
        tblDG.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    xuLyChon.run();
                }
            }
        });

        dialog.setVisible(true);
    }

    private void showNhanVienPickerDialog() {
        nvBUS.docDanhSach();
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn mã nhân viên", true);
        dialog.setSize(760, 430);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        DefaultTableModel modelNV = new DefaultTableModel(new Object[]{"Mã NV", "Họ Đệm", "Tên", "SĐT", "Chức Vụ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (NhanVienDTO nv : nvBUS.getList()) {
            modelNV.addRow(new Object[]{nv.getMaNV(), nv.getHoDem(), nv.getTen(), nv.getSdt(), nv.getChucVu()});
        }

        JTable tblNV = new JTable(modelNV);
        styleTable(tblNV);
        JScrollPane scroll = new JScrollPane(tblNV);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(scroll, BorderLayout.CENTER);

        JButton btnChon = new JButton("Chọn nhân viên này");
        styleButton(btnChon, new Color(25, 118, 210));
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBackground(Color.WHITE);
        pnlBottom.add(btnChon);
        dialog.add(pnlBottom, BorderLayout.SOUTH);

        Runnable xuLyChon = () -> {
            int row = tblNV.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn 1 nhân viên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            txtMaNV.setText(String.valueOf(modelNV.getValueAt(row, 0)));
            dialog.dispose();
        };

        btnChon.addActionListener(e -> xuLyChon.run());
        tblNV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    xuLyChon.run();
                }
            }
        });

        dialog.setVisible(true);
    }

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
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
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

    private LocalDate parseDateOrNull(String dateText) {
        if (dateText == null || dateText.trim().isEmpty()) {
            return null;
        }
        try {
            String text = dateText.trim();
            // Chấp nhận cả format yyyy-mm-dd và yyyy/mm/dd
            if (text.contains("/")) {
                text = text.replace("/", "-");
            }
            return LocalDate.parse(text);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private String valueAt(DefaultTableModel model, int row, int col) {
        Object value = model.getValueAt(row, col);
        return value == null ? "" : value.toString();
    }

}