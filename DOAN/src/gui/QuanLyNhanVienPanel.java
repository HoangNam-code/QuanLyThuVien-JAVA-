package gui;

import bus.NhanVienBUS;
import dto.NhanVienDTO;
import java.awt.*; // Đã mở khóa dòng này
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
import javax.swing.table.JTableHeader; // Đã thêm import thiếu lúc trước
import util.ExcelHelper;

public class QuanLyNhanVienPanel extends JPanel {

    private NhanVienBUS nvBUS = new NhanVienBUS();
    private JTable tblNhanVien;
    private DefaultTableModel model;
    
    // Components nhập liệu
    private JTextField txtMa, txtHo, txtTen, txtSDT, txtEmail, txtNgaySinh, txtTimKiem;
    private JComboBox<String> cboGioiTinh, cboChucVu;
    
    // Các nút chức năng
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JButton btnNhapExcel, btnXuatExcel;
    private JButton btnTimKiem, btnTimNangCao, btnHuyTim;

    // --- BẢNG MÀU CHUẨN ---
    private final Color COLOR_PRIMARY = new Color(0, 102, 204);   
    private final Color COLOR_WARNING = new Color(255, 153, 0);   
    private final Color COLOR_DANGER = new Color(204, 0, 0);      
    private final Color COLOR_SUCCESS = new Color(40, 167, 69);   
    private final Color COLOR_GRAY = new Color(108, 117, 125);    
    private final Color COLOR_TEXT_BLUE = new Color(0, 102, 204); 

    public QuanLyNhanVienPanel() {
        initComponents();
        // Load dữ liệu an toàn
        if (nvBUS.getList() != null) {
            loadDataLenBang(nvBUS.getList());
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);

        // ===================================================================
        // PHẦN TOP
        // ===================================================================
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BoxLayout(pnlTop, BoxLayout.Y_AXIS));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(10, 20, 10, 20));

        // 1. TIÊU ĐỀ
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_TEXT_BLUE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.setBackground(Color.WHITE);
        pnlTitle.add(lblTitle);
        pnlTop.add(pnlTitle);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 10)));

        // 2. KHUNG THÔNG TIN
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                new LineBorder(COLOR_TEXT_BLUE, 1), " Thông tin chi tiết ", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
                new Font("Segoe UI", Font.BOLD, 14), COLOR_TEXT_BLUE
        );
        pnlInput.setBorder(titledBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Hàng 1
        gbc.gridx = 0; gbc.gridy = 0; pnlInput.add(createLabel("Mã NV:"), gbc);
        txtMa = createTextField(); gbc.gridx = 1; pnlInput.add(txtMa, gbc);

        gbc.gridx = 2; pnlInput.add(createLabel("Họ đệm:"), gbc);
        txtHo = createTextField(); gbc.gridx = 3; pnlInput.add(txtHo, gbc);

        gbc.gridx = 4; pnlInput.add(createLabel("Tên:"), gbc);
        txtTen = createTextField(); gbc.gridx = 5; pnlInput.add(txtTen, gbc);

        // Hàng 2
        gbc.gridx = 0; gbc.gridy = 1; pnlInput.add(createLabel("Giới tính:"), gbc);
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setBackground(Color.WHITE);
        gbc.gridx = 1; pnlInput.add(cboGioiTinh, gbc);

        gbc.gridx = 2; pnlInput.add(createLabel("Ngày sinh (yyyy-mm-dd):"), gbc);
        txtNgaySinh = createTextField(); 
        txtNgaySinh.setToolTipText("Ví dụ: 2000-12-31"); 
        gbc.gridx = 3; pnlInput.add(txtNgaySinh, gbc);

        gbc.gridx = 4; pnlInput.add(createLabel("SĐT:"), gbc);
        txtSDT = createTextField(); gbc.gridx = 5; pnlInput.add(txtSDT, gbc);

        // Hàng 3
        gbc.gridx = 0; gbc.gridy = 2; pnlInput.add(createLabel("Email:"), gbc);
        txtEmail = createTextField(); gbc.gridx = 1; pnlInput.add(txtEmail, gbc);

        gbc.gridx = 2; pnlInput.add(createLabel("Chức vụ:"), gbc);
        cboChucVu = new JComboBox<>(new String[]{"Thu Thu", "Quan Ly", "Bao Ve", "Tap Vu"});
        cboChucVu.setBackground(Color.WHITE);
        gbc.gridx = 3; pnlInput.add(cboChucVu, gbc);

        // Ô trống
        gbc.gridx = 4; pnlInput.add(new JLabel(""), gbc);
        gbc.gridx = 5; pnlInput.add(new JLabel(""), gbc);

        pnlTop.add(pnlInput);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 15)));

        // 3. NÚT CHỨC NĂNG
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlButtons.setBackground(Color.WHITE);

        btnThem = createButton("Thêm Mới", COLOR_PRIMARY);
        btnSua = createButton("Cập Nhật", COLOR_WARNING);
        btnXoa = createButton("Xóa", COLOR_DANGER);
        btnLamMoi = createButton("Làm Mới Form", COLOR_GRAY);
        btnNhapExcel = createButton("Nhập Excel", COLOR_SUCCESS);
        btnXuatExcel = createButton("Xuất Excel", COLOR_SUCCESS);

        pnlButtons.add(btnThem); pnlButtons.add(btnSua); pnlButtons.add(btnXoa); pnlButtons.add(btnLamMoi);
        pnlButtons.add(Box.createHorizontalStrut(20));
        pnlButtons.add(btnNhapExcel); pnlButtons.add(btnXuatExcel);

        pnlTop.add(pnlButtons);
        pnlTop.add(Box.createRigidArea(new Dimension(0, 15)));

        // 4. TÌM KIẾM
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setBackground(Color.WHITE);
        
        JLabel lblTimKiem = new JLabel("Từ khóa (Mã/Tên/SĐT):");
        lblTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        txtTimKiem = new JTextField(25);
        txtTimKiem.setPreferredSize(new Dimension(300, 35));
        
        btnTimKiem = createButton("Tìm cơ bản", COLOR_PRIMARY);
        btnTimNangCao = createButton("Tìm nâng cao", COLOR_WARNING);
        btnHuyTim = createButton("Hủy tìm", COLOR_GRAY);

        pnlSearch.add(lblTimKiem); pnlSearch.add(txtTimKiem); pnlSearch.add(btnTimKiem); pnlSearch.add(btnTimNangCao); pnlSearch.add(btnHuyTim);
        
        JPanel pnlSearchWrapper = new JPanel(new BorderLayout());
        pnlSearchWrapper.setBackground(Color.WHITE);
        pnlSearchWrapper.setBorder(BorderFactory.createTitledBorder(null, "Tìm kiếm", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.PLAIN, 12), Color.GRAY));
        pnlSearchWrapper.add(pnlSearch, BorderLayout.CENTER);

        pnlTop.add(pnlSearchWrapper);
        add(pnlTop, BorderLayout.NORTH);

        // ===================================================================
        // PHẦN CENTER: BẢNG
        // ===================================================================
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(new EmptyBorder(10, 20, 20, 20));

        String[] columns = {"Mã NV", "Họ đệm", "Tên", "Giới tính", "Ngày sinh", "Email", "SĐT", "Chức vụ"};
        model = new DefaultTableModel(columns, 0);
        tblNhanVien = new JTable(model);
        
        tblNhanVien.setRowHeight(30);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblNhanVien.setSelectionBackground(new Color(232, 240, 254));
        tblNhanVien.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblNhanVien.getTableHeader();
        header.setBackground(new Color(240, 240, 240)); 
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));

        // Click bảng
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

    // --- HÀM HỖ TRỢ ---
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
        btn.setPreferredSize(new Dimension(130, 35));
        return btn;
    }

    private void addEvents() {
        // TÌM KIẾM
        btnTimKiem.addActionListener(e -> {
            try {
                String tuKhoa = txtTimKiem.getText();
                loadDataLenBang(nvBUS.timKiem(tuKhoa));
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        
        btnHuyTim.addActionListener(e -> {
            txtTimKiem.setText("");
            loadDataLenBang(nvBUS.getList());
        });

        // THÊM / SỬA / XÓA
        btnThem.addActionListener(e -> {
            NhanVienDTO nv = getNhanVienFromForm();
            if (nv != null) {
                JOptionPane.showMessageDialog(this, nvBUS.themNhanVien(nv));
                loadDataLenBang(nvBUS.getList());
                lamMoiForm();
            }
        });

        btnSua.addActionListener(e -> {
            NhanVienDTO nv = getNhanVienFromForm();
            if (nv != null) {
                JOptionPane.showMessageDialog(this, nvBUS.suaNhanVien(nv));
                loadDataLenBang(nvBUS.getList());
            }
        });

        btnXoa.addActionListener(e -> {
            if (!txtMa.getText().isEmpty()) {
                if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + txtMa.getText() + "?") == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this, nvBUS.xoaNhanVien(txtMa.getText()));
                    loadDataLenBang(nvBUS.getList());
                    lamMoiForm();
                }
            }
        });

        btnLamMoi.addActionListener(e -> lamMoiForm());

        // --- XỬ LÝ NÚT XUẤT EXCEL (ĐÃ SỬA) ---
        btnXuatExcel.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    if (!path.endsWith(".xlsx")) path += ".xlsx";
                    
                    // Gọi hàm export từ ExcelHelper
                    ExcelHelper.exportExcel(tblNhanVien, path);
                    
                    // Nếu dùng bản giả lập, thông báo sẽ nằm trong hàm exportExcel
                    // Nếu dùng bản thật, bạn có thể thêm JOptionPane ở đây
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + ex.getMessage()); 
                }
            }
        });

        // --- XỬ LÝ NÚT NHẬP EXCEL (ĐÃ SỬA) ---
        btnNhapExcel.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    
                    // Gọi hàm import và nhận danh sách
                    ArrayList<NhanVienDTO> listImport = ExcelHelper.importNhanVien(file.getAbsolutePath());
                    
                    int count = 0;
                    for (NhanVienDTO nv : listImport) {
                        if (nvBUS.themNhanVien(nv).contains("thành công")) {
                            count++;
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Đã xử lý nhập: " + count + " nhân viên mới!");
                    loadDataLenBang(nvBUS.getList());
                    
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "Lỗi nhập file: " + ex.getMessage()); 
                }
            }
        });
        
        // MỞ TÌM KIẾM NÂNG CAO
        btnTimNangCao.addActionListener(e -> {
            // Truyền this (Panel hiện tại) và nvBUS sang Dialog
            TimKiemNangCaoDialog dialog = new TimKiemNangCaoDialog(this, nvBUS);
            dialog.setVisible(true);
        });
    }

    public void loadDataLenBang(ArrayList<NhanVienDTO> list) {
        if (list == null) return;
        model.setRowCount(0);
        for (NhanVienDTO nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getHoDem(), nv.getTen(), nv.getGioiTinh(), nv.getNgaySinh(),
                nv.getEmail(), nv.getSdt(), nv.getChucVu()
            });
        }
    }

    private NhanVienDTO getNhanVienFromForm() {
        if (txtMa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!");
            return null;
        }
        try {
            Date ngaySinh = Date.valueOf(txtNgaySinh.getText()); 
            return new NhanVienDTO(
                txtMa.getText(), txtHo.getText(), txtTen.getText(), ngaySinh,
                cboGioiTinh.getSelectedItem().toString(), txtSDT.getText(), "", 
                txtEmail.getText(), cboChucVu.getSelectedItem().toString()
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi ngày sinh! Nhập đúng định dạng: yyyy-mm-dd (VD: 2000-12-31)");
            return null;
        }
    }

    private String getValue(int row, int col) {
        Object val = model.getValueAt(row, col);
        return (val == null) ? "" : val.toString();
    }

    private void lamMoiForm() {
        txtMa.setText(""); txtMa.setEditable(true);
        txtHo.setText(""); txtTen.setText("");
        txtSDT.setText(""); txtEmail.setText("");
        txtNgaySinh.setText(""); txtTimKiem.setText("");
        cboGioiTinh.setSelectedIndex(0);
        cboChucVu.setSelectedIndex(0);
    }
}
