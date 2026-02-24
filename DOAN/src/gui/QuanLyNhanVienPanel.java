package gui;

import bus.NhanVienBUS;
import dto.NhanVienDTO;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;

public class QuanLyNhanVienPanel extends JPanel {

    private NhanVienBUS nvBUS = new NhanVienBUS();
    private JTable tblNhanVien;
    private DefaultTableModel model;
    
    // Các ô nhập liệu
    private JTextField txtMa, txtHo, txtTen, txtSDT, txtEmail, txtDiaChi, txtNgaySinh;
    private JComboBox<String> cboGioiTinh, cboChucVu;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public QuanLyNhanVienPanel() {
        initComponents();
        loadDataLenBang();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Bảng danh sách (Ở giữa)
        String[] columns = {"Mã NV", "Họ đệm", "Tên", "Ngày sinh", "Giới tính", "SĐT", "Email", "Chức vụ"};
        model = new DefaultTableModel(columns, 0);
        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(30);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Sự kiện click bảng
        tblNhanVien.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    txtMa.setText(model.getValueAt(row, 0).toString());
                    txtHo.setText(model.getValueAt(row, 1).toString());
                    txtTen.setText(model.getValueAt(row, 2).toString());
                    txtNgaySinh.setText(model.getValueAt(row, 3).toString());
                    cboGioiTinh.setSelectedItem(model.getValueAt(row, 4).toString());
                    txtSDT.setText(model.getValueAt(row, 5).toString());
                    txtEmail.setText(model.getValueAt(row, 6).toString());
                    cboChucVu.setSelectedItem(model.getValueAt(row, 7).toString());
                    txtMa.setEditable(false); // Không cho sửa mã
                }
            }
        });
        
        JScrollPane sc = new JScrollPane(tblNhanVien);
        add(sc, BorderLayout.CENTER);

        // 3. Form nhập liệu (Bên dưới)
        JPanel pnlInput = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlInput.setBorder(new TitledBorder("Thông tin nhân viên"));
        pnlInput.setPreferredSize(new Dimension(0, 200));

        pnlInput.add(new JLabel("Mã NV:"));
        txtMa = new JTextField(); pnlInput.add(txtMa);
        
        pnlInput.add(new JLabel("Ngày sinh (yyyy-mm-dd):"));
        txtNgaySinh = new JTextField(); pnlInput.add(txtNgaySinh);

        pnlInput.add(new JLabel("Họ đệm:"));
        txtHo = new JTextField(); pnlInput.add(txtHo);

        pnlInput.add(new JLabel("Giới tính:"));
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"}); pnlInput.add(cboGioiTinh);

        pnlInput.add(new JLabel("Tên:"));
        txtTen = new JTextField(); pnlInput.add(txtTen);

        pnlInput.add(new JLabel("SĐT:"));
        txtSDT = new JTextField(); pnlInput.add(txtSDT);
        
        pnlInput.add(new JLabel("Email:"));
        txtEmail = new JTextField(); pnlInput.add(txtEmail);
        
        pnlInput.add(new JLabel("Chức vụ:"));
        cboChucVu = new JComboBox<>(new String[]{"Thu Thu", "Quan Ly", "Bao Ve", "Tap Vu"}); pnlInput.add(cboChucVu);

        // 4. Các nút chức năng
        JPanel pnlButtons = new JPanel();
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        
        styleButton(btnThem); styleButton(btnSua); styleButton(btnXoa); styleButton(btnLamMoi);

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        
        // Sự kiện nút Thêm
        btnThem.addActionListener(e -> {
            try {
                NhanVienDTO nv = new NhanVienDTO(
                    txtMa.getText(), txtHo.getText(), txtTen.getText(),
                    Date.valueOf(txtNgaySinh.getText()), 
                    cboGioiTinh.getSelectedItem().toString(),
                    txtSDT.getText(), "", txtEmail.getText(),
                    cboChucVu.getSelectedItem().toString()
                );
                JOptionPane.showMessageDialog(this, nvBUS.themNhanVien(nv));
                loadDataLenBang();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage());
            }
        });
        
        // Sự kiện nút Xóa
        btnXoa.addActionListener(e -> {
            String ma = txtMa.getText();
            if(JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + ma + "?") == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, nvBUS.xoaNhanVien(ma));
                loadDataLenBang();
                lamMoiForm();
            }
        });
        
        // Sự kiện Làm mới
        btnLamMoi.addActionListener(e -> lamMoiForm());

        // Gom Form và Nút vào Panel phía Nam
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.add(pnlInput, BorderLayout.CENTER);
        pnlSouth.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);
    }
    
    private void loadDataLenBang() {
        nvBUS.docDanhSach();
        ArrayList<NhanVienDTO> list = nvBUS.getList();
        model.setRowCount(0);
        for (NhanVienDTO nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getHoDem(), nv.getTen(), nv.getNgaySinh(),
                nv.getGioiTinh(), nv.getSdt(), nv.getEmail(), nv.getChucVu()
            });
        }
    }
    
    private void lamMoiForm() {
        txtMa.setText(""); txtMa.setEditable(true);
        txtHo.setText(""); txtTen.setText("");
        txtSDT.setText(""); txtEmail.setText("");
        txtNgaySinh.setText("");
    }
    
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(25, 118, 210));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}