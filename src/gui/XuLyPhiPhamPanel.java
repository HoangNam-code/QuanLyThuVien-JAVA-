package gui;

import bus.*;
import dto.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class XuLyPhiPhamPanel extends BackgroundPanel {

    private JTable tblPhieuPhat, tblChiTietPhat;
    private DefaultTableModel modelPhieuPhat, modelChiTietPhat;

    private JTextField txtMaPP, txtMaPM, txtMaDG, txtMaNV, txtNgayGhi, txtTongTienPhieu, txtGhiChu;
    private JTextField txtMaSach, txtNgayHenTra, txtNgayThucTra, txtSoDonVi, txtTienChiTiet, txtTimKiem;
    private JComboBox<String> cboLyDoPhat;

    private JButton btnChonPM, btnChonDG, btnChonNV, btnChonSach;
    private JButton btnThemPhieu, btnThemChiTiet, btnTinhTien, btnLamMoi, btnTimKiem, btnHuyTim;

    private final PhieuPhatBUS phieuPhatBUS = new PhieuPhatBUS();
    private final ChiTietPhieuPhatBUS chiTietPhatBUS = new ChiTietPhieuPhatBUS();
    private final PhieuMuonBus phieuMuonBUS = new PhieuMuonBus();
    private final ChiTietPhieuMuonBus ctpmBUS = new ChiTietPhieuMuonBus();
    private final SachBUS sachBUS = new SachBUS();
    private final NhanVienBUS nhanVienBUS = new NhanVienBUS();
    private final DocGiaBUS docGiaBUS = new DocGiaBUS();
    private final LyDoPhatBUS lyDoPhatBUS = new LyDoPhatBUS();

    private final Map<String, LyDoPhatDTO> mapLyDo = new HashMap<>();
    private long soTienDaTinh = 0;
    private final Color COLOR_PRIMARY = new Color(25, 118, 210);

    public XuLyPhiPhamPanel() {
        initComponents();
        loadLyDoPhat();
        loadDataPhieuPhat();
        taoMaPhieuMoi();
        txtNgayGhi.setText(nowDateTimeText());
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel pnlTop = new JPanel(new BorderLayout(0, 15));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setOpaque(true);
        pnlTop.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("XỬ LÝ PHÍ VI PHẠM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlTop.add(lblTitle, BorderLayout.NORTH);

        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);

        txtMaPP = createTextField(fontInput);
        txtMaPM = createTextField(fontInput); txtMaPM.setEditable(false); txtMaPM.setBackground(new Color(245, 245, 245));
        txtMaDG = createTextField(fontInput); txtMaDG.setEditable(false); txtMaDG.setBackground(new Color(245, 245, 245));
        txtMaNV = createTextField(fontInput); txtMaNV.setEditable(false); txtMaNV.setBackground(new Color(245, 245, 245));
        txtNgayGhi = createTextField(fontInput);
        txtTongTienPhieu = createTextField(fontInput); txtTongTienPhieu.setEditable(false); txtTongTienPhieu.setBackground(new Color(245, 245, 245));
        txtGhiChu = createTextField(fontInput);

        txtMaSach = createTextField(fontInput); txtMaSach.setEditable(false); txtMaSach.setBackground(new Color(245, 245, 245));
        txtNgayHenTra = createTextField(fontInput); txtNgayHenTra.setEditable(false); txtNgayHenTra.setBackground(new Color(245, 245, 245));
        txtNgayThucTra = createTextField(fontInput); txtNgayThucTra.setText(LocalDate.now().toString());
        txtSoDonVi = createTextField(fontInput); txtSoDonVi.setText("1");
        txtTienChiTiet = createTextField(fontInput); txtTienChiTiet.setEditable(false); txtTienChiTiet.setBackground(new Color(245, 245, 245));
        cboLyDoPhat = new JComboBox<>(); cboLyDoPhat.setFont(fontInput); cboLyDoPhat.setBackground(Color.WHITE);

        btnChonPM = new JButton("..."); stylePickerButton(btnChonPM);
        JPanel pnlMaPM = new JPanel(new BorderLayout(5, 0)); pnlMaPM.setOpaque(false); pnlMaPM.add(txtMaPM, BorderLayout.CENTER); pnlMaPM.add(btnChonPM, BorderLayout.EAST);

        btnChonDG = new JButton("..."); stylePickerButton(btnChonDG);
        JPanel pnlMaDG = new JPanel(new BorderLayout(5, 0)); pnlMaDG.setOpaque(false); pnlMaDG.add(txtMaDG, BorderLayout.CENTER); pnlMaDG.add(btnChonDG, BorderLayout.EAST);

        btnChonNV = new JButton("..."); stylePickerButton(btnChonNV);
        JPanel pnlMaNV = new JPanel(new BorderLayout(5, 0)); pnlMaNV.setOpaque(false); pnlMaNV.add(txtMaNV, BorderLayout.CENTER); pnlMaNV.add(btnChonNV, BorderLayout.EAST);

        btnChonSach = new JButton("..."); stylePickerButton(btnChonSach);
        JPanel pnlMaSach = new JPanel(new BorderLayout(5, 0)); pnlMaSach.setOpaque(false); pnlMaSach.add(txtMaSach, BorderLayout.CENTER); pnlMaSach.add(btnChonSach, BorderLayout.EAST);

        // --- CẬP NHẬT LƯỚI GRID ĐỂ THÊM MÃ ĐG ---
        JPanel pnlInput = new JPanel(new GridLayout(3, 6, 15, 12));
        pnlInput.setBackground(Color.WHITE);
        TitledBorder inputBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 1), " Thông tin phiếu phạt ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY);
        pnlInput.setBorder(BorderFactory.createCompoundBorder(inputBorder, BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        pnlInput.add(createLabel("Mã PP:")); pnlInput.add(txtMaPP);
        pnlInput.add(createLabel("Mã Phiếu Mượn:")); pnlInput.add(pnlMaPM);
        pnlInput.add(createLabel("Mã Độc Giả:")); pnlInput.add(pnlMaDG);

        pnlInput.add(createLabel("Mã Nhân Viên:")); pnlInput.add(pnlMaNV);
        pnlInput.add(createLabel("Ngày Ghi:")); pnlInput.add(txtNgayGhi);
        pnlInput.add(createLabel("Tổng Tiền:")); pnlInput.add(txtTongTienPhieu);

        pnlInput.add(createLabel("Ghi Chú Phiếu:")); pnlInput.add(txtGhiChu);
        pnlInput.add(new JLabel()); pnlInput.add(new JLabel());
        pnlInput.add(new JLabel()); pnlInput.add(new JLabel());

        JPanel pnlChiTiet = new JPanel(new GridLayout(2, 6, 15, 10));
        pnlChiTiet.setBackground(Color.WHITE);
        TitledBorder chiTietBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 1), " Chi tiết phiếu phạt ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY);
        pnlChiTiet.setBorder(BorderFactory.createCompoundBorder(chiTietBorder, BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        pnlChiTiet.add(createLabel("Mã Sách Vi Phạm:")); pnlChiTiet.add(pnlMaSach);
        pnlChiTiet.add(createLabel("Lý Do Phạt:")); pnlChiTiet.add(cboLyDoPhat);
        pnlChiTiet.add(createLabel("Số Đơn Vị Vi Phạm:")); pnlChiTiet.add(txtSoDonVi);

        pnlChiTiet.add(createLabel("Ngày Hẹn Trả:")); pnlChiTiet.add(txtNgayHenTra);
        pnlChiTiet.add(createLabel("Ngày Thực Trả:")); pnlChiTiet.add(txtNgayThucTra);
        pnlChiTiet.add(createLabel("Tiền Phạt Chi Tiết:")); pnlChiTiet.add(txtTienChiTiet);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBackground(Color.WHITE);

        btnThemPhieu = new JButton("Thêm Phiếu Phạt"); styleButton(btnThemPhieu, new Color(40, 167, 69));
        btnThemChiTiet = new JButton("Thêm Chi Tiết Vi Phạm"); styleButton(btnThemChiTiet, new Color(25, 118, 210));
        btnTinhTien = new JButton("Tính Tiền Phạt"); styleButton(btnTinhTien, new Color(245, 124, 0));
        btnLamMoi = new JButton("Làm Mới Form"); styleButton(btnLamMoi, new Color(97, 101, 105));

        pnlButtons.add(btnThemPhieu); pnlButtons.add(btnThemChiTiet); pnlButtons.add(btnTinhTien); pnlButtons.add(btnLamMoi);

        JPanel pnlFormAndButtons = new JPanel(new BorderLayout(0, 12)); pnlFormAndButtons.setBackground(Color.WHITE);
        JPanel pnlTopForms = new JPanel(new BorderLayout(0, 12)); pnlTopForms.setBackground(Color.WHITE);
        pnlTopForms.add(pnlInput, BorderLayout.NORTH); pnlTopForms.add(pnlChiTiet, BorderLayout.CENTER);
        pnlFormAndButtons.add(pnlTopForms, BorderLayout.CENTER); pnlFormAndButtons.add(pnlButtons, BorderLayout.SOUTH);
        pnlTop.add(pnlFormAndButtons, BorderLayout.CENTER);

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(Color.WHITE); pnlCenter.setOpaque(true); pnlCenter.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Tìm kiếm phiếu phạt", TitledBorder.LEFT, TitledBorder.TOP));
        txtTimKiem = createTextField(fontInput); txtTimKiem.setPreferredSize(new Dimension(300, 30));
        btnTimKiem = new JButton("Tìm cơ bản"); styleButton(btnTimKiem, COLOR_PRIMARY);
        btnHuyTim = new JButton("Hủy tìm"); styleButton(btnHuyTim, new Color(97, 101, 105));

        pnlSearch.add(createLabel("Từ khóa (Mã PP/PM/ĐG/NV):")); pnlSearch.add(txtTimKiem); pnlSearch.add(btnTimKiem); pnlSearch.add(btnHuyTim);
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // --- ĐÃ THÊM CỘT MÃ ĐG VÀO BẢNG ---
        modelPhieuPhat = new DefaultTableModel(new Object[]{"Mã PP", "Mã PM", "Mã ĐG", "Mã NV", "Ngày Ghi", "Lý Do", "Tổng Tiền"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        modelChiTietPhat = new DefaultTableModel(new Object[]{"Mã PP", "Mã Sách", "Lý Do", "Số Tiền"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblPhieuPhat = new JTable(modelPhieuPhat); tblChiTietPhat = new JTable(modelChiTietPhat);
        styleTable(tblPhieuPhat); styleTable(tblChiTietPhat);

        JScrollPane spPhieu = new JScrollPane(tblPhieuPhat); JScrollPane spChiTiet = new JScrollPane(tblChiTietPhat);
        spPhieu.setBorder(BorderFactory.createTitledBorder("Phiếu phạt (Main)"));
        spChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu phạt (Sub)"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spPhieu, spChiTiet);
        split.setDividerLocation(230); split.setResizeWeight(0.55);
        pnlCenter.add(split, BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH); add(pnlCenter, BorderLayout.CENTER);

        bindEvents();
    }

    private void bindEvents() {
        btnChonPM.addActionListener(e -> showPhieuMuonPickerDialog());
        btnChonDG.addActionListener(e -> showDocGiaPickerDialog());
        btnChonNV.addActionListener(e -> showNhanVienPickerDialog());
        btnChonSach.addActionListener(e -> showSachByPhieuMuonPickerDialog());

        btnThemPhieu.addActionListener(e -> themPhieuPhatMoi());
        btnThemChiTiet.addActionListener(e -> themChiTietPhieuPhat());
        btnTinhTien.addActionListener(e -> tinhTienPhat());
        btnLamMoi.addActionListener(e -> lamMoiForm());

        btnTimKiem.addActionListener(e -> timKiem());
        btnHuyTim.addActionListener(e -> { txtTimKiem.setText(""); loadDataPhieuPhat(); });

        cboLyDoPhat.addActionListener(e -> { capNhatGoiYDonVi(); soTienDaTinh = 0; txtTienChiTiet.setText(""); });

        tblPhieuPhat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhieuPhat.getSelectedRow();
                if (row >= 0) {
                    String maPP = valueAt(modelPhieuPhat, row, 0);
                    txtMaPP.setText(maPP);
                    txtMaPM.setText(valueAt(modelPhieuPhat, row, 1));
                    txtMaDG.setText(valueAt(modelPhieuPhat, row, 2)); // Map Ma_DG
                    txtMaNV.setText(valueAt(modelPhieuPhat, row, 3));
                    txtNgayGhi.setText(valueAt(modelPhieuPhat, row, 4));
                    txtGhiChu.setText(valueAt(modelPhieuPhat, row, 5));
                    txtTongTienPhieu.setText(valueAt(modelPhieuPhat, row, 6));

                    tuDongChonSachDauTienTrongPhieu();
                    loadNgayHenVaNgayTraTuPhieuMuon(txtMaPM.getText().trim());
                    loadChiTietPhatTheoMaPP(maPP);
                }
            }
        });
    }

    private void loadLyDoPhat() {
        cboLyDoPhat.removeAllItems(); mapLyDo.clear();
        ArrayList<LyDoPhatDTO> list = lyDoPhatBUS.getList();
        for (LyDoPhatDTO ld : list) {
            mapLyDo.put(ld.getMaLyDoPhat(), ld);
            cboLyDoPhat.addItem(ld.getMaLyDoPhat() + " - " + ld.getTenLyDoPhat());
        }
        capNhatGoiYDonVi();
    }

    private void loadDataPhieuPhat() { loadDataPhieuPhat(phieuPhatBUS.getList()); }

    private void loadDataPhieuPhat(ArrayList<PhieuPhatDTO> list) {
        modelPhieuPhat.setRowCount(0); modelChiTietPhat.setRowCount(0);
        if (list == null) return;
        for (PhieuPhatDTO pp : list) {
            modelPhieuPhat.addRow(new Object[]{
                pp.getMaPP(), pp.getMaPM(), pp.getMaDG(), pp.getMaNV(), pp.getNgayGhi(), pp.getLyDo(), util.Formatter.FormatVND(pp.getTongTien())
            });
        }
    }

    private void loadChiTietPhatTheoMaPP(String maPP) {
        modelChiTietPhat.setRowCount(0);
        if (maPP == null || maPP.trim().isEmpty()) return;
        ArrayList<ChiTietPhieuPhatDTO> list = chiTietPhatBUS.getByMaPP(maPP);
        for (ChiTietPhieuPhatDTO ct : list) {
            modelChiTietPhat.addRow(new Object[]{
                ct.getMaPP(), ct.getMaSach(), layTenLyDo(ct.getMaLyDoPhat()), util.Formatter.FormatVND(ct.getSoTienPhat())
            });
        }
    }

    private void timKiem() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
        ArrayList<PhieuPhatDTO> ketQua = new ArrayList<>();
        for (PhieuPhatDTO pp : phieuPhatBUS.getList()) {
            if (pp.getMaPP().toLowerCase().contains(tuKhoa) || pp.getMaPM().toLowerCase().contains(tuKhoa) || pp.getMaDG().toLowerCase().contains(tuKhoa) || pp.getMaNV().toLowerCase().contains(tuKhoa)) {
                ketQua.add(pp);
            }
        }
        loadDataPhieuPhat(ketQua);
    }

    private void themPhieuPhatMoi() {
        String maPP = txtMaPP.getText().trim(); String maPM = txtMaPM.getText().trim(); String maDG = txtMaDG.getText().trim(); String maNV = txtMaNV.getText().trim();
        if (maPP.isEmpty() || maPM.isEmpty() || maDG.isEmpty() || maNV.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng nhập/chọn đủ Mã PP, Mã PM, Mã ĐG, Mã NV.", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE); return; }
        for (PhieuPhatDTO pp : phieuPhatBUS.getList()) { if (pp.getMaPP().equalsIgnoreCase(maPP)) { JOptionPane.showMessageDialog(this, "Mã phiếu phạt đã tồn tại.", "Trùng mã", JOptionPane.WARNING_MESSAGE); return; } }

        PhieuPhatDTO phieuPhat = new PhieuPhatDTO();
        phieuPhat.setMaPP(maPP); phieuPhat.setMaPM(maPM); phieuPhat.setMaDG(maDG); phieuPhat.setMaNV(maNV);
        phieuPhat.setNgayGhi(chuanHoaNgayGhi(txtNgayGhi.getText().trim()));
        phieuPhat.setLyDo(txtGhiChu.getText().trim().isEmpty() ? "Phiếu phạt tạo từ giao diện" : txtGhiChu.getText().trim());
        phieuPhat.setTongTien(0);

        if (!phieuPhatBUS.them(phieuPhat)) { JOptionPane.showMessageDialog(this, "Thêm phiếu phạt thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE); return; }

        JOptionPane.showMessageDialog(this, "Thêm phiếu phạt thành công! Bạn có thể thêm chi tiết vi phạm.");
        loadDataPhieuPhat(); chonDongPhieuPhat(maPP); txtTongTienPhieu.setText(util.Formatter.FormatVND(0)); txtTienChiTiet.setText(""); soTienDaTinh = 0;
    }

    private void themChiTietPhieuPhat() {
        String maPP = txtMaPP.getText().trim(); String maPM = txtMaPM.getText().trim(); String maSach = txtMaSach.getText().trim(); String maLyDo = layMaLyDoDangChon();
        if (maPP.isEmpty() || maPM.isEmpty() || maSach.isEmpty() || maLyDo.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng chọn đủ Mã PP, Mã PM, Mã Sách và Lý do phạt.", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE); return; }

        PhieuPhatDTO phieuHienTai = timPhieuPhatTheoMa(maPP);
        if (phieuHienTai == null) { JOptionPane.showMessageDialog(this, "Mã phiếu chưa tồn tại. Hãy bấm 'Thêm Phiếu Phạt' trước.", "Thiếu phiếu", JOptionPane.WARNING_MESSAGE); return; }
        if (soTienDaTinh <= 0) { JOptionPane.showMessageDialog(this, "Vui lòng bấm 'Tính Tiền Phạt' trước khi thêm chi tiết.", "Thiếu bước", JOptionPane.WARNING_MESSAGE); return; }

        ChiTietPhieuPhatDTO ct = new ChiTietPhieuPhatDTO();
        ct.setMaPP(maPP); ct.setMaLyDoPhat(maLyDo); ct.setMaSach(maSach); ct.setSoTienPhat(soTienDaTinh);

        if (!chiTietPhatBUS.them(ct)) { JOptionPane.showMessageDialog(this, "Thêm chi tiết thất bại (có thể trùng Mã PP + Mã sách + Lý do).", "Lỗi", JOptionPane.ERROR_MESSAGE); return; }

        long tongMoi = phieuHienTai.getTongTien() + soTienDaTinh;
        String lyDoMoi = hopNhatLyDo(phieuHienTai.getLyDo(), layTenLyDo(maLyDo));
        if (!phieuPhatBUS.capNhatTongTienVaLyDo(maPP, tongMoi, lyDoMoi)) { JOptionPane.showMessageDialog(this, "Đã thêm chi tiết nhưng cập nhật tổng tiền phiếu thất bại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE); }

        JOptionPane.showMessageDialog(this, "Thêm chi tiết vi phạm thành công!");
        loadDataPhieuPhat(); chonDongPhieuPhat(maPP); loadChiTietPhatTheoMaPP(maPP); txtTongTienPhieu.setText(util.Formatter.FormatVND(tongMoi)); txtTienChiTiet.setText(""); soTienDaTinh = 0;
    }

    private void tinhTienPhat() {
        String maPM = txtMaPM.getText().trim(); String maSach = txtMaSach.getText().trim(); String maLyDo = layMaLyDoDangChon();
        if (maPM.isEmpty() || maSach.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng chọn Mã PM và Mã Sách vi phạm.", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE); return; }
        if (maLyDo.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng chọn lý do phạt.", "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE); return; }

        int soDonVi;
        try { soDonVi = Integer.parseInt(txtSoDonVi.getText().trim()); if (soDonVi <= 0) throw new NumberFormatException(); } 
        catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Số đơn vị vi phạm phải là số nguyên > 0.", "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE); return; }

        SachDTO sach = timSachTheoMa(maSach);
        if (sach == null) { JOptionPane.showMessageDialog(this, "Không tìm thấy sách tương ứng để tính phạt.", "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE); return; }

        ChiTietPhieuMuonDTO ct = timCTPM(maPM, maSach);
        int soLuongMuon = ct == null ? 1 : Math.max(1, ct.getSoLuong());

        long giaSach = Math.round(sach.getDonGia());
        long tienDenBu100 = giaSach * soLuongMuon;
        long soTien; String ghiChuMacDinh;

        switch (maLyDo) {
            case "LDP01": {
                LocalDate hanTra = parseDateOrNull(txtNgayHenTra.getText().trim()); LocalDate ngayTra = parseDateOrNull(txtNgayThucTra.getText().trim());
                if (hanTra == null || ngayTra == null) { JOptionPane.showMessageDialog(this, "Ngày hẹn trả/ngày thực trả không hợp lệ (yyyy-mm-dd).", "Sai định dạng", JOptionPane.ERROR_MESSAGE); return; }
                long soNgayTre = Math.max(0, ChronoUnit.DAYS.between(hanTra, ngayTra)); soTien = soNgayTre * 2000L; ghiChuMacDinh = "Trễ " + soNgayTre + " ngày so với hạn trả."; break;
            }
            case "LDP02": soTien = Math.round(tienDenBu100 * 1.5); ghiChuMacDinh = "Đền bù 150% giá trị sách."; break;
            case "LDP03": soTien = 20000L * soDonVi; ghiChuMacDinh = "Rách bìa mức nhẹ, áp dụng theo số cuốn."; break;
            case "LDP04": soTien = tienDenBu100; ghiChuMacDinh = "Sách ướt/mốc, đền 100% giá trị sách."; break;
            case "LDP05": {
                long tamTinh = 10000L * soDonVi; soTien = Math.min(tamTinh, tienDenBu100); ghiChuMacDinh = "Viết/vẽ bậy " + soDonVi + " trang.";
                if (tamTinh > tienDenBu100) ghiChuMacDinh += " Tổng phạt vượt giá sách nên áp dụng mức 100% giá sách."; break;
            }
            case "LDP06": soTien = tienDenBu100; ghiChuMacDinh = "Mất trang nội dung, đền 100% giá trị sách."; break;
            case "LDP07": soTien = 15000L * soDonVi; ghiChuMacDinh = "Hỏng mã vạch/tem thư viện."; break;
            case "LDP08": soTien = 50000L * soDonVi; ghiChuMacDinh = "Mất thẻ thư viện, thu phí cấp lại thẻ."; break;
            case "LDP09": soTien = tienDenBu100; ghiChuMacDinh = "Tráo đổi ruột sách: đền sách gốc và cấm mượn 6 tháng."; break;
            case "LDP10": soTien = 30000L * soDonVi; ghiChuMacDinh = "Bóc gáy/bung keo sách."; break;
            default: JOptionPane.showMessageDialog(this, "Lý do phạt chưa hỗ trợ tính tự động.", "Thông báo", JOptionPane.WARNING_MESSAGE); return;
        }
        soTienDaTinh = Math.max(0, soTien); txtTienChiTiet.setText(util.Formatter.FormatVND(soTienDaTinh));
        if (txtGhiChu.getText().trim().isEmpty()) txtGhiChu.setText(ghiChuMacDinh);
    }

    private void lamMoiForm() {
        txtMaPP.setText(""); txtMaPM.setText(""); txtMaDG.setText(""); txtMaNV.setText(""); txtNgayGhi.setText(nowDateTimeText()); txtTongTienPhieu.setText(""); txtGhiChu.setText("");
        txtMaSach.setText(""); txtNgayHenTra.setText(""); txtNgayThucTra.setText(LocalDate.now().toString()); txtSoDonVi.setText("1"); txtTienChiTiet.setText(""); soTienDaTinh = 0;
        if (cboLyDoPhat.getItemCount() > 0) cboLyDoPhat.setSelectedIndex(0);
        tblPhieuPhat.clearSelection(); tblChiTietPhat.clearSelection(); taoMaPhieuMoi();
    }

    private void loadNgayHenVaNgayTraTuPhieuMuon(String maPM) {
        txtNgayHenTra.setText(""); txtNgayThucTra.setText(LocalDate.now().toString());
        if (maPM == null || maPM.trim().isEmpty()) return;
        for (PhieuMuonDTO pm : phieuMuonBUS.getList()) {
            if (pm.getMaPM() != null && pm.getMaPM().equalsIgnoreCase(maPM)) {
                txtNgayHenTra.setText(pm.getNgayTraDuKien() == null ? "" : pm.getNgayTraDuKien());
                if (pm.getNgayThucTra() != null && !pm.getNgayThucTra().trim().isEmpty()) txtNgayThucTra.setText(pm.getNgayThucTra().trim());
                return;
            }
        }
    }

    private void taoMaPhieuMoi() {
        int max = 0;
        for (PhieuPhatDTO pp : phieuPhatBUS.getList()) {
            String ma = pp.getMaPP(); if (ma == null) continue;
            String so = ma.replaceAll("[^0-9]", ""); if (so.isEmpty()) continue;
            try { max = Math.max(max, Integer.parseInt(so)); } catch (NumberFormatException ignored) {}
        }
        txtMaPP.setText(String.format("PP%02d", max + 1));
    }

    private void chonDongPhieuPhat(String maPP) {
        for (int i = 0; i < modelPhieuPhat.getRowCount(); i++) {
            if (maPP.equalsIgnoreCase(valueAt(modelPhieuPhat, i, 0))) { tblPhieuPhat.setRowSelectionInterval(i, i); return; }
        }
    }

    private PhieuPhatDTO timPhieuPhatTheoMa(String maPP) {
        for (PhieuPhatDTO pp : phieuPhatBUS.getList()) { if (pp.getMaPP() != null && pp.getMaPP().equalsIgnoreCase(maPP)) return pp; }
        return null;
    }

    private void capNhatGoiYDonVi() {
        String maLyDo = layMaLyDoDangChon();
        if ("LDP01".equals(maLyDo) || "LDP02".equals(maLyDo) || "LDP04".equals(maLyDo) || "LDP06".equals(maLyDo) || "LDP09".equals(maLyDo)) {
            txtSoDonVi.setText("1"); txtSoDonVi.setEnabled(false);
        } else {
            txtSoDonVi.setEnabled(true); if (txtSoDonVi.getText().trim().isEmpty()) txtSoDonVi.setText("1");
        }
    }

    private String layMaLyDoDangChon() { Object selected = cboLyDoPhat.getSelectedItem(); if (selected == null) return ""; String value = selected.toString(); int idx = value.indexOf(" - "); return idx > 0 ? value.substring(0, idx).trim() : value.trim(); }
    private String layTenLyDo(String maLyDo) { LyDoPhatDTO ld = mapLyDo.get(maLyDo); return ld == null ? maLyDo : ld.getTenLyDoPhat(); }
    private String hopNhatLyDo(String hienTai, String moi) { String a = hienTai == null ? "" : hienTai.trim(); String b = moi == null ? "" : moi.trim(); if (b.isEmpty()) return a; if (a.isEmpty()) return b; if (a.toLowerCase().contains(b.toLowerCase())) return a; return a + "; " + b; }
    private String nowDateTimeText() { return LocalDateTime.now().withNano(0).toString().replace('T', ' '); }
    private String chuanHoaNgayGhi(String text) { if (text == null || text.trim().isEmpty()) return nowDateTimeText(); String v = text.trim(); if (v.length() == 10) return v + " 00:00:00"; if (v.length() == 16) return v + ":00"; if (v.length() == 19) return v; return nowDateTimeText(); }
    private ChiTietPhieuMuonDTO timCTPM(String maPM, String maSach) { ArrayList<ChiTietPhieuMuonDTO> list = ctpmBUS.getListByMaPM(maPM); for (ChiTietPhieuMuonDTO ct : list) { if (ct.getMaSach() != null && ct.getMaSach().equalsIgnoreCase(maSach)) return ct; } return null; }
    private SachDTO timSachTheoMa(String maSach) { for (SachDTO s : sachBUS.getList()) { if (s.getMaSach().equalsIgnoreCase(maSach)) return s; } return null; }

    private void showPhieuMuonPickerDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn phiếu mượn", true);
        dialog.setSize(820, 450); dialog.setLocationRelativeTo(this); dialog.setLayout(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã PM", "Mã ĐG", "Mã NV", "Ngày hẹn trả", "Ngày thực trả"}, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } };
        for (PhieuMuonDTO pm : phieuMuonBUS.getList()) { model.addRow(new Object[]{ pm.getMaPM(), pm.getMaDG(), pm.getMaNV(), pm.getNgayTraDuKien(), pm.getNgayThucTra() == null ? "" : pm.getNgayThucTra() }); }
        JTable table = new JTable(model); styleTable(table); dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton btnChon = new JButton("Chọn phiếu này"); styleButton(btnChon, COLOR_PRIMARY);
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER)); pnlBottom.setBackground(Color.WHITE); pnlBottom.add(btnChon); dialog.add(pnlBottom, BorderLayout.SOUTH);

        Runnable xuLy = () -> {
            int row = table.getSelectedRow(); if (row == -1) { JOptionPane.showMessageDialog(dialog, "Vui lòng chọn 1 phiếu mượn.", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
            txtMaPM.setText(String.valueOf(model.getValueAt(row, 0)));
            // TỰ ĐỘNG ĐIỀN MÃ ĐỘC GIẢ
            txtMaDG.setText(String.valueOf(model.getValueAt(row, 1)));
            loadNgayHenVaNgayTraTuPhieuMuon(txtMaPM.getText().trim()); tuDongChonSachDauTienTrongPhieu(); dialog.dispose();
        };
        btnChon.addActionListener(e -> xuLy.run()); table.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) xuLy.run(); } });
        dialog.setVisible(true);
    }
    
    private void showDocGiaPickerDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn độc giả", true);
        dialog.setSize(760, 430); dialog.setLocationRelativeTo(this); dialog.setLayout(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã ĐG", "Họ Đệm", "Tên", "SĐT", "Loại thẻ"}, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } };
        for (DocGiaDTO dg : docGiaBUS.getList()) { model.addRow(new Object[]{dg.getMaDG(), dg.getHoDem(), dg.getTen(), dg.getSdt(), dg.getMaLoaiTV()}); }
        JTable table = new JTable(model); styleTable(table); dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton btnChon = new JButton("Chọn độc giả này"); styleButton(btnChon, COLOR_PRIMARY);
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER)); pnlBottom.setBackground(Color.WHITE); pnlBottom.add(btnChon); dialog.add(pnlBottom, BorderLayout.SOUTH);
        Runnable xuLy = () -> {
            int row = table.getSelectedRow(); if (row == -1) { JOptionPane.showMessageDialog(dialog, "Vui lòng chọn 1 độc giả.", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
            txtMaDG.setText(String.valueOf(model.getValueAt(row, 0))); dialog.dispose();
        };
        btnChon.addActionListener(e -> xuLy.run()); table.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) xuLy.run(); } });
        dialog.setVisible(true);
    }

    private void showNhanVienPickerDialog() {
        nhanVienBUS.docDanhSach();
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn nhân viên", true);
        dialog.setSize(760, 430); dialog.setLocationRelativeTo(this); dialog.setLayout(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã NV", "Họ Đệm", "Tên", "SĐT", "Chức Vụ"}, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } };
        for (NhanVienDTO nv : nhanVienBUS.getList()) { model.addRow(new Object[]{nv.getMaNV(), nv.getHoDem(), nv.getTen(), nv.getSdt(), nv.getChucVu()}); }
        JTable table = new JTable(model); styleTable(table); dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton btnChon = new JButton("Chọn nhân viên này"); styleButton(btnChon, COLOR_PRIMARY);
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER)); pnlBottom.setBackground(Color.WHITE); pnlBottom.add(btnChon); dialog.add(pnlBottom, BorderLayout.SOUTH);
        Runnable xuLy = () -> {
            int row = table.getSelectedRow(); if (row == -1) { JOptionPane.showMessageDialog(dialog, "Vui lòng chọn 1 nhân viên.", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
            txtMaNV.setText(String.valueOf(model.getValueAt(row, 0))); dialog.dispose();
        };
        btnChon.addActionListener(e -> xuLy.run()); table.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) xuLy.run(); } });
        dialog.setVisible(true);
    }

    private void showSachByPhieuMuonPickerDialog() {
        String maPM = txtMaPM.getText().trim();
        if (maPM.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn trước khi chọn sách vi phạm.", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
        ArrayList<ChiTietPhieuMuonDTO> list = ctpmBUS.getListByMaPM(maPM);
        if (list.isEmpty()) { JOptionPane.showMessageDialog(this, "Phiếu mượn này chưa có chi tiết sách.", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chọn sách vi phạm", true);
        dialog.setSize(680, 400); dialog.setLocationRelativeTo(this); dialog.setLayout(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã Sách", "Tình Trạng", "Số Lượng"}, 0) { @Override public boolean isCellEditable(int row, int column) { return false; } };
        for (ChiTietPhieuMuonDTO ct : list) { model.addRow(new Object[]{ct.getMaSach(), ct.getTinhTrangSach(), ct.getSoLuong()}); }
        JTable table = new JTable(model); styleTable(table); dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton btnChon = new JButton("Chọn sách này"); styleButton(btnChon, COLOR_PRIMARY);
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER)); pnlBottom.setBackground(Color.WHITE); pnlBottom.add(btnChon); dialog.add(pnlBottom, BorderLayout.SOUTH);
        Runnable xuLy = () -> {
            int row = table.getSelectedRow(); if (row == -1) { JOptionPane.showMessageDialog(dialog, "Vui lòng chọn 1 sách.", "Thông báo", JOptionPane.WARNING_MESSAGE); return; }
            txtMaSach.setText(String.valueOf(model.getValueAt(row, 0))); soTienDaTinh = 0; txtTienChiTiet.setText(""); dialog.dispose();
        };
        btnChon.addActionListener(e -> xuLy.run()); table.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) xuLy.run(); } });
        dialog.setVisible(true);
    }

    private void tuDongChonSachDauTienTrongPhieu() { txtMaSach.setText(""); String maPM = txtMaPM.getText().trim(); if (maPM.isEmpty()) return; ArrayList<ChiTietPhieuMuonDTO> list = ctpmBUS.getListByMaPM(maPM); if (!list.isEmpty()) txtMaSach.setText(list.get(0).getMaSach()); }
    private LocalDate parseDateOrNull(String dateText) { if (dateText == null || dateText.trim().isEmpty()) return null; try { return LocalDate.parse(dateText.trim()); } catch (DateTimeParseException ex) { return null; } }
    private JLabel createLabel(String text) { JLabel lbl = new JLabel(text); lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl.setForeground(new Color(60, 60, 60)); return lbl; }
    private JTextField createTextField(Font font) { JTextField txt = new JTextField(); txt.setFont(font); txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(5, 5, 5, 5))); return txt; }
    private void styleButton(JButton btn, Color bgColor) { btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); btn.setBackground(bgColor); btn.setForeground(Color.WHITE); btn.setBorderPainted(false); btn.setOpaque(true); btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20)); }
    private void stylePickerButton(JButton btn) { btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); btn.setPreferredSize(new Dimension(45, 30)); btn.setFocusPainted(false); }
    private String valueAt(DefaultTableModel model, int row, int col) { Object value = model.getValueAt(row, col); return value == null ? "" : value.toString(); }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); table.setRowHeight(30); table.setSelectionBackground(new Color(187, 222, 251)); table.setSelectionForeground(Color.BLACK); table.setShowGrid(true); table.setGridColor(new Color(224, 224, 224));
        JTableHeader header = table.getTableHeader(); header.setPreferredSize(new Dimension(100, 40)); header.setReorderingAllowed(false);
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer(); headerRenderer.setBackground(new Color(235, 238, 240)); headerRenderer.setForeground(new Color(30, 30, 30)); headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14)); headerRenderer.setHorizontalAlignment(SwingConstants.CENTER); headerRenderer.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(200, 200, 200)));
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(); centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}