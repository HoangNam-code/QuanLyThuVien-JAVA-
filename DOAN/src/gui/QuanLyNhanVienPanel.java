package gui;

import bus.NhanVienBUS;
import dto.NhanVienDTO;
import java.awt.*; // Import file tiện ích vừa tạo
import java.awt.event.*;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import util.ExcelHelper;

public class QuanLyNhanVienPanel extends JPanel {

    private NhanVienBUS nvBUS = new NhanVienBUS();
    private JTable tblNhanVien;
    private DefaultTableModel model;
    
    // Nhập liệu
    private JTextField txtMa, txtHo, txtTen, txtSDT, txtEmail, txtNgaySinh;
    private JComboBox<String> cboGioiTinh, cboChucVu;
    
    // Nút chức năng
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    
    // --- MỚI: Phần Tìm kiếm & Excel ---
    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnNhapExcel, btnXuatExcel;

    public QuanLyNhanVienPanel() {
        initComponents();
        loadDataLenBang(nvBUS.getList()); // Load dữ liệu ban đầu
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        // ===================================================================
        // 2. THANH CÔNG CỤ (TÌM KIẾM + EXCEL) - MỚI
        // ===================================================================
        JPanel pnlToolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlToolBar.setBorder(new TitledBorder("Chức năng mở rộng"));

        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tìm kiếm");
        btnNhapExcel = new JButton("Nhập Excel");
        btnXuatExcel = new JButton("Xuất Excel");
        
        styleButton(btnTimKiem); styleButton(btnNhapExcel); styleButton(btnXuatExcel);
        // Đổi màu nút Excel cho khác biệt
        btnNhapExcel.setBackground(new Color(46, 125, 50)); // Màu xanh lá Excel
        btnXuatExcel.setBackground(new Color(46, 125, 50));

        pnlToolBar.add(new JLabel("Từ khóa:"));
        pnlToolBar.add(txtTimKiem);
        pnlToolBar.add(btnTimKiem);
        pnlToolBar.add(Box.createHorizontalStrut(20)); // Khoảng cách
        pnlToolBar.add(btnNhapExcel);
        pnlToolBar.add(btnXuatExcel);

        // ===================================================================
        // 3. BẢNG DỮ LIỆU
        // ===================================================================
        String[] columns = {"Mã NV", "Họ đệm", "Tên", "Ngày sinh", "Giới tính", "SĐT", "Email", "Chức vụ"};
        model = new DefaultTableModel(columns, 0);
        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(25);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Click bảng đổ dữ liệu xuống form
        tblNhanVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    txtMa.setText(getValue(row, 0));
                    txtHo.setText(getValue(row, 1));
                    txtTen.setText(getValue(row, 2));
                    txtNgaySinh.setText(getValue(row, 3));
                    cboGioiTinh.setSelectedItem(getValue(row, 4));
                    txtSDT.setText(getValue(row, 5));
                    txtEmail.setText(getValue(row, 6));
                    cboChucVu.setSelectedItem(getValue(row, 7));
                    txtMa.setEditable(false);
                }
            }
        });

        // ===================================================================
        // 4. FORM NHẬP LIỆU & NÚT CRUD
        // ===================================================================
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlToolBar, BorderLayout.NORTH); // Thêm thanh tìm kiếm lên trên bảng
        pnlCenter.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);
        
        add(pnlCenter, BorderLayout.CENTER);

        // Panel dưới cùng (Form + Nút Thêm/Xóa/Sửa)
        JPanel pnlSouth = new JPanel(new BorderLayout());
        
        JPanel pnlInput = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlInput.setBorder(new TitledBorder("Thông tin chi tiết"));
        
        // Khởi tạo các ô nhập
        txtMa = new JTextField(); txtHo = new JTextField(); txtTen = new JTextField();
        txtSDT = new JTextField(); txtEmail = new JTextField(); txtNgaySinh = new JTextField();
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboChucVu = new JComboBox<>(new String[]{"Thu Thu", "Quan Ly", "Bao Ve"});

        pnlInput.add(new JLabel("Mã NV:")); pnlInput.add(txtMa);
        pnlInput.add(new JLabel("Họ đệm:")); pnlInput.add(txtHo);
        pnlInput.add(new JLabel("Tên:")); pnlInput.add(txtTen);
        pnlInput.add(new JLabel("Ngày sinh:")); pnlInput.add(txtNgaySinh);
        pnlInput.add(new JLabel("Giới tính:")); pnlInput.add(cboGioiTinh);
        pnlInput.add(new JLabel("SĐT:")); pnlInput.add(txtSDT);
        pnlInput.add(new JLabel("Email:")); pnlInput.add(txtEmail);
        pnlInput.add(new JLabel("Chức vụ:")); pnlInput.add(cboChucVu);

        JPanel pnlCRUDButtons = new JPanel();
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        
        styleButton(btnThem); styleButton(btnSua); styleButton(btnXoa); styleButton(btnLamMoi);
        pnlCRUDButtons.add(btnThem); pnlCRUDButtons.add(btnSua);
        pnlCRUDButtons.add(btnXoa); pnlCRUDButtons.add(btnLamMoi);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);
        pnlSouth.add(pnlCRUDButtons, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        // ===================================================================
        // 5. XỬ LÝ SỰ KIỆN (EVENTS)
        // ===================================================================
        
        // --- TÌM KIẾM ---
        btnTimKiem.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText();
            loadDataLenBang(nvBUS.timKiem(tuKhoa));
        });

        // --- NHẬP EXCEL ---
        btnNhapExcel.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    ArrayList<NhanVienDTO> listImport = ExcelHelper.importNhanVien(file.getAbsolutePath());
                    
                    // Lưu vào DB
                    int count = 0;
                    for (NhanVienDTO nv : listImport) {
                        if (nvBUS.themNhanVien(nv).contains("thành công")) count++;
                    }
                    JOptionPane.showMessageDialog(this, "Đã nhập thành công " + count + " nhân viên!");
                    loadDataLenBang(nvBUS.getList());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi nhập file: " + ex.getMessage());
                }
            }
        });

        // --- XUẤT EXCEL ---
        btnXuatExcel.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    if (!path.endsWith(".xlsx")) path += ".xlsx";
                    ExcelHelper.exportExcel(tblNhanVien, path);
                    JOptionPane.showMessageDialog(this, "Xuất file thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + ex.getMessage());
                }
            }
        });

        // --- CÁC NÚT CRUD (NHƯ CŨ) ---
        btnThem.addActionListener(e -> {
            try {
                NhanVienDTO nv = getNhanVienFromForm();
                JOptionPane.showMessageDialog(this, nvBUS.themNhanVien(nv));
                loadDataLenBang(nvBUS.getList());
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        btnSua.addActionListener(e -> {
            try {
                NhanVienDTO nv = getNhanVienFromForm();
                JOptionPane.showMessageDialog(this, nvBUS.suaNhanVien(nv));
                loadDataLenBang(nvBUS.getList());
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        btnXoa.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên này?") == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, nvBUS.xoaNhanVien(txtMa.getText()));
                loadDataLenBang(nvBUS.getList());
                lamMoiForm();
            }
        });

        btnLamMoi.addActionListener(e -> {
            lamMoiForm();
            loadDataLenBang(nvBUS.getList()); // Load lại toàn bộ (bỏ tìm kiếm)
        });
    }

    // --- HÀM HỖ TRỢ ---
    
    // Load dữ liệu linh hoạt (cho cả lúc đầu và lúc tìm kiếm)
    private void loadDataLenBang(ArrayList<NhanVienDTO> list) {
        model.setRowCount(0);
        for (NhanVienDTO nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getHoDem(), nv.getTen(), nv.getNgaySinh(),
                nv.getGioiTinh(), nv.getSdt(), nv.getEmail(), nv.getChucVu()
            });
        }
    }

    private NhanVienDTO getNhanVienFromForm() {
        return new NhanVienDTO(
            txtMa.getText(), txtHo.getText(), txtTen.getText(),
            Date.valueOf(txtNgaySinh.getText()), 
            cboGioiTinh.getSelectedItem().toString(),
            txtSDT.getText(), "", txtEmail.getText(),
            cboChucVu.getSelectedItem().toString()
        );
    }

    private String getValue(int row, int col) {
        return model.getValueAt(row, col).toString();
    }

    private void lamMoiForm() {
        txtMa.setText(""); txtMa.setEditable(true);
        txtHo.setText(""); txtTen.setText("");
        txtSDT.setText(""); txtEmail.setText("");
        txtNgaySinh.setText(""); txtTimKiem.setText("");
    }
    
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(25, 118, 210));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
