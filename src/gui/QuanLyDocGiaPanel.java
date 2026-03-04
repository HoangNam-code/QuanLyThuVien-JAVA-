package gui;

import bus.DocGiaBUS;
import dto.DocGiaDTO;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import util.ExcelHelper;
import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

public class QuanLyDocGiaPanel extends JPanel {

    private DocGiaBUS dgBUS = new DocGiaBUS();
    private JTable tblDocGia;
    private DefaultTableModel model;
    
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
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
        setBackground(Color.WHITE);

        // =========================================================
        // 1. PANEL PHÍA TRÊN (TIÊU ĐỀ + FORM NHẬP LIỆU + NÚT BẤM)
        // =========================================================
        JPanel pnlTop = new JPanel(new BorderLayout(0, 15));
        pnlTop.setBackground(Color.WHITE);

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
        txtMa = createTextField(fontInput); txtHoDem = createTextField(fontInput); txtTen = createTextField(fontInput);
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"}); cboGioiTinh.setFont(fontInput);
        txtNgaySinh = createTextField(fontInput); txtSDT = createTextField(fontInput);
        txtEmail = createTextField(fontInput); txtDiaChi = createTextField(fontInput);
        txtPhiTV = createTextField(fontInput); txtLoaiTV = createTextField(fontInput);

        // Row 1
        pnlInput.add(createLabel("Mã ĐG:")); pnlInput.add(txtMa);
        pnlInput.add(createLabel("Họ đệm:")); pnlInput.add(txtHoDem);
        pnlInput.add(createLabel("Tên:")); pnlInput.add(txtTen);
        // Row 2
        pnlInput.add(createLabel("Giới tính:")); pnlInput.add(cboGioiTinh);
        pnlInput.add(createLabel("Ngày sinh (yyyy-mm-dd):")); pnlInput.add(txtNgaySinh);
        pnlInput.add(createLabel("SĐT:")); pnlInput.add(txtSDT);
        // Row 3
        pnlInput.add(createLabel("Email:")); pnlInput.add(txtEmail);
        pnlInput.add(createLabel("Địa chỉ:")); pnlInput.add(txtDiaChi);
        pnlInput.add(createLabel("Phí thành viên:")); pnlInput.add(txtPhiTV);
        // Row 4
        pnlInput.add(createLabel("Loại TV:")); pnlInput.add(txtLoaiTV);
        pnlInput.add(new JLabel("")); pnlInput.add(new JLabel("")); 
        pnlInput.add(new JLabel("")); pnlInput.add(new JLabel("")); 

        JPanel pnlFormAndButtons = new JPanel(new BorderLayout(0, 15));
        pnlFormAndButtons.setBackground(Color.WHITE);
        pnlFormAndButtons.add(pnlInput, BorderLayout.CENTER);

        // Nhóm nút bấm thao tác
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlButtons.setBackground(Color.WHITE);
        
        btnThem = new JButton("Thêm Mới"); styleButton(btnThem, new Color(25, 118, 210)); 
        btnSua = new JButton("Cập Nhật"); styleButton(btnSua, new Color(245, 124, 0)); 
        btnXoa = new JButton("Xóa"); styleButton(btnXoa, new Color(211, 47, 47)); 
        btnLamMoi = new JButton("Làm Mới Form"); styleButton(btnLamMoi, new Color(97, 101, 105)); 
        btnNhapExcel = new JButton("Nhập Excel"); styleButton(btnNhapExcel, new Color(46, 125, 50)); 
        btnXuatExcel = new JButton("Xuất Excel"); styleButton(btnXuatExcel, new Color(46, 125, 50)); 
        btnInDanhSach = new JButton("In Danh Sách PDF"); styleButton(btnInDanhSach, new Color(156, 39, 176));
        btnInThe = new JButton("In Thẻ PDF"); styleButton(btnInThe, new Color(156, 39, 176));

        pnlButtons.add(btnThem); pnlButtons.add(btnSua); pnlButtons.add(btnXoa); pnlButtons.add(btnLamMoi); 
        pnlButtons.add(btnNhapExcel); pnlButtons.add(btnXuatExcel); pnlButtons.add(btnInDanhSach); pnlButtons.add(btnInThe);

        pnlFormAndButtons.add(pnlButtons, BorderLayout.SOUTH);
        pnlTop.add(pnlFormAndButtons, BorderLayout.CENTER);

        // =========================================================
        // 2. KHU VỰC TÌM KIẾM & BẢNG DỮ LIỆU
        // =========================================================
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(Color.WHITE);

        // Thanh Tìm Kiếm (Đã được thu gọn)
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));

        txtTimKiem = createTextField(fontInput); 
        txtTimKiem.setPreferredSize(new Dimension(300, 30));

        btnTimKiem = new JButton("Tìm cơ bản"); styleButton(btnTimKiem, COLOR_PRIMARY);
        btnTimNangCao = new JButton("Tìm nâng cao"); styleButton(btnTimNangCao, new Color(245, 124, 0));
        btnHuyTim = new JButton("Hủy tìm"); styleButton(btnHuyTim, new Color(97, 101, 105));

        pnlSearch.add(createLabel("Từ khóa (Mã/Tên/SĐT):")); 
        pnlSearch.add(txtTimKiem); 
        pnlSearch.add(btnTimKiem);
        pnlSearch.add(btnTimNangCao); 
        pnlSearch.add(btnHuyTim);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"Mã DG", "Họ Đệm", "Tên", "Giới Tính", "Ngày Sinh", "Email", "SĐT", "Địa Chỉ", "Đã Mượn", "Ngày Hết Hạn", "Phí TV", "Mã LTV"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; } 
        };
        tblDocGia = new JTable(model);
        tblDocGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDocGia.setRowHeight(30); 
        tblDocGia.setSelectionBackground(new Color(187, 222, 251)); 
        tblDocGia.setSelectionForeground(Color.BLACK);
        tblDocGia.setShowGrid(true);
        tblDocGia.setGridColor(new Color(224, 224, 224));
        tblDocGia.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        
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
        tblDocGia.getColumnModel().getColumn(10).setPreferredWidth(90); 
        tblDocGia.getColumnModel().getColumn(11).setPreferredWidth(70); 

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
                    cboGioiTinh.setSelectedItem(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "Nam");
                    txtNgaySinh.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
                    txtEmail.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
                    txtSDT.setText(model.getValueAt(row, 6) != null ? model.getValueAt(row, 6).toString() : "");
                    txtDiaChi.setText(model.getValueAt(row, 7) != null ? model.getValueAt(row, 7).toString() : "");
                    txtPhiTV.setText(model.getValueAt(row, 10) != null ? model.getValueAt(row, 10).toString() : "");
                    txtLoaiTV.setText(model.getValueAt(row, 11) != null ? model.getValueAt(row, 11).toString() : "");
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

        // Nút Thêm, Sửa, Xóa, Làm Mới... (Giữ nguyên như cũ)
        btnThem.addActionListener(e -> {
            try {
                DocGiaDTO dg = new DocGiaDTO(
                    txtMa.getText().trim(), txtHoDem.getText().trim(), txtTen.getText().trim(),
                    cboGioiTinh.getSelectedItem().toString(), Date.valueOf(txtNgaySinh.getText().trim()),
                    txtEmail.getText().trim(), txtSDT.getText().trim(), txtDiaChi.getText().trim(),
                    0, null, Double.parseDouble(txtPhiTV.getText().trim()), txtLoaiTV.getText().trim()
                );
                String msg = dgBUS.add(dg);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) {
                    loadTable(dgBUS.getList());
                    lamMoi();
                }
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu! Vui lòng kiểm tra định dạng Ngày Sinh (yyyy-mm-dd) và Phí TV (số).", "Lỗi", JOptionPane.ERROR_MESSAGE); 
            }
        });
        
        btnSua.addActionListener(e -> {
            String maDG = txtMa.getText().trim();
            if (maDG.isEmpty() || txtMa.isEditable()) {
                JOptionPane.showMessageDialog(this, "Vui lòng click chọn một độc giả trên bảng để cập nhật!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                DocGiaDTO dg = new DocGiaDTO(
                    maDG, txtHoDem.getText().trim(), txtTen.getText().trim(),
                    cboGioiTinh.getSelectedItem().toString(), Date.valueOf(txtNgaySinh.getText().trim()),
                    txtEmail.getText().trim(), txtSDT.getText().trim(), txtDiaChi.getText().trim(),
                    0, null, Double.parseDouble(txtPhiTV.getText().trim()), txtLoaiTV.getText().trim()
                );
                String msg = dgBUS.update(dg);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) {
                    loadTable(dgBUS.getList());
                    lamMoi();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu! Vui lòng kiểm tra định dạng Ngày Sinh (yyyy-mm-dd) và Phí TV (số).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnXoa.addActionListener(e -> {
            String maDG = txtMa.getText().trim();
            if (maDG.isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa độc giả mã: " + maDG + " không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
            if (tblDocGia.getRowCount() == 0) return;
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chọn vị trí lưu file Excel");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx");
            chooser.setFileFilter(filter);
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".xlsx")) filePath += ".xlsx";
                    ExcelHelper.exportExcel(tblDocGia, filePath);
                    JOptionPane.showMessageDialog(this, "Xuất dữ liệu thành công!\nĐã lưu tại: " + filePath);
                } catch (Exception ex) {}
            }
        });       

        btnInDanhSach.addActionListener(e -> {
            if (tblDocGia.getRowCount() == 0) return;
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Lưu Danh Sách PDF");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf");
            chooser.setFileFilter(filter);
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                if(!path.toLowerCase().endsWith(".pdf")) path += ".pdf";
                util.PDFHelper.exportDanhSach(tblDocGia, path);
                JOptionPane.showMessageDialog(this, "Xuất PDF danh sách thành công!");
            }
        });

        btnInThe.addActionListener(e -> {
            int row = tblDocGia.getSelectedRow();
            if (row == -1) return;
            try {
                DocGiaDTO dg = new DocGiaDTO();
                dg.setMaDG(tblDocGia.getValueAt(row, 0).toString());
                dg.setHoDem(tblDocGia.getValueAt(row, 1).toString());
                dg.setTen(tblDocGia.getValueAt(row, 2).toString());
                dg.setGioiTinh(tblDocGia.getValueAt(row, 3).toString());
                dg.setNgaySinh(java.sql.Date.valueOf(tblDocGia.getValueAt(row, 4).toString()));
                dg.setSdt(tblDocGia.getValueAt(row, 6).toString());
                if(tblDocGia.getValueAt(row, 9) != null && !tblDocGia.getValueAt(row, 9).toString().isEmpty()){
                     dg.setNgayHetHan(java.sql.Date.valueOf(tblDocGia.getValueAt(row, 9).toString()));
                }
                dg.setMaLoaiTV(tblDocGia.getValueAt(row, 11).toString());

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Lưu Thẻ Độc Giả PDF");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf");
                chooser.setFileFilter(filter);

                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    if(!path.toLowerCase().endsWith(".pdf")) path += ".pdf";
                    util.PDFHelper.exportTheDocGia(dg, path);
                    JOptionPane.showMessageDialog(this, "In thẻ thành công!");
                }
            } catch(Exception ex) {}
        });
    }
    
    // =========================================================
    // HÀM HIỂN THỊ HỘP THOẠI TÌM KIẾM NÂNG CAO (POPUP FORM)
    // =========================================================
    private void showAdvancedSearchDialog() {
        // Lấy cửa sổ chính đang chứa Panel này để làm cha cho Dialog
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Tìm kiếm nâng cao", true);
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 10, 15));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        Font fontForm = new Font("Segoe UI", Font.PLAIN, 14);

        // Trường nhập liệu trên Dialog
        pnlForm.add(createLabel("Từ khóa (Mã/Tên/SĐT):"));
        JTextField txtAdvTimKiem = createTextField(fontForm);
        txtAdvTimKiem.setText(txtTimKiem.getText()); // Kế thừa từ khóa đang gõ ở ngoài (nếu có)
        pnlForm.add(txtAdvTimKiem);

        pnlForm.add(createLabel("Giới tính:"));
        JComboBox<String> cboAdvGioiTinh = new JComboBox<>(new String[]{"Tất cả", "Nam", "Nữ"});
        cboAdvGioiTinh.setFont(fontForm);
        pnlForm.add(cboAdvGioiTinh);

        pnlForm.add(createLabel("Mã Loại TV:"));
        JTextField txtAdvLoaiTV = createTextField(fontForm);
        pnlForm.add(txtAdvLoaiTV);

        // Nút chức năng trên Dialog
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButtons.setBackground(Color.WHITE);
        
        JButton btnSearch = new JButton("Tìm kiếm"); 
        styleButton(btnSearch, COLOR_PRIMARY);
        
        JButton btnCancel = new JButton("Hủy bỏ"); 
        styleButton(btnCancel, new Color(97, 101, 105));

        pnlButtons.add(btnSearch);
        pnlButtons.add(btnCancel);

        // Sự kiện khi bấm "Tìm kiếm" trong Form nâng cao
        btnSearch.addActionListener(e -> {
            String tuKhoa = txtAdvTimKiem.getText().trim();
            String gioiTinh = cboAdvGioiTinh.getSelectedItem().toString();
            String loaiTV = txtAdvLoaiTV.getText().trim();
            
            // Cập nhật lại thanh tìm kiếm bên ngoài cho đồng bộ giao diện
            txtTimKiem.setText(tuKhoa);
            
            // Gọi phương thức lọc từ BUS và tải lên bảng
            loadTable(dgBUS.timKiemNangCao(tuKhoa, gioiTinh, loaiTV));
            
            dialog.dispose(); // Tắt hộp thoại
        });

        // Sự kiện Hủy bỏ
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlButtons, BorderLayout.SOUTH);
        dialog.setVisible(true); // Hiển thị Dialog
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

    private void lamMoi() {
        txtMa.setText(""); txtMa.setEditable(true); 
        txtHoDem.setText(""); txtTen.setText(""); 
        cboGioiTinh.setSelectedIndex(0);
        txtNgaySinh.setText(""); txtEmail.setText(""); txtSDT.setText(""); 
        txtDiaChi.setText(""); txtPhiTV.setText(""); txtLoaiTV.setText("");
        tblDocGia.clearSelection();
    }

    private void loadTable(ArrayList<DocGiaDTO> list) {
        model.setRowCount(0);
        for (DocGiaDTO d : list) {
            model.addRow(new Object[]{
                d.getMaDG(), d.getHoDem(), d.getTen(), d.getGioiTinh(), d.getNgaySinh(), 
                d.getEmail(), d.getSdt(), d.getDiaChi(), d.getSoSachMuon(), 
                d.getNgayHetHan(), d.getTienPhiThanhVien(), d.getMaLoaiTV()
            });
        }
    }
}