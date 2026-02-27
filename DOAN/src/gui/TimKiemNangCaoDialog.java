package gui;

import dao.TacGiaDAO;
import dao.TheLoaiDAO;
import dto.TacGiaDTO;
import dto.TheLoaiDTO;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TimKiemNangCaoDialog extends JDialog {

    private JTextField txtTenSach, txtGiaTu, txtGiaDen;
    private JComboBox<String> cboTacGia, cboTheLoai;
    private JButton btnTimKiem, btnHuy;
    
    private boolean isConfirm = false; 

    public TimKiemNangCaoDialog(Window parent) {
        super(parent, "Tìm Kiếm Nâng Cao", Dialog.ModalityType.APPLICATION_MODAL);
        initComponents();
        loadComboBoxData();
        pack(); 
        setLocationRelativeTo(parent); 
    }

    private void initComponents() {
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlMain.setBackground(Color.WHITE);

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("TÌM KIẾM SÁCH NÂNG CAO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // --- Khung nhập liệu ---
        JPanel pnlInput = new JPanel(new GridLayout(5, 2, 10, 15));
        pnlInput.setBackground(Color.WHITE);

        txtTenSach = new JTextField();
        cboTacGia = new JComboBox<>();
        cboTheLoai = new JComboBox<>();
        txtGiaTu = new JTextField();
        txtGiaDen = new JTextField();

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);

        addFormItem(pnlInput, "Tên Sách (có thể bỏ trống ):", txtTenSach, fontLabel, fontInput);
        addFormItem(pnlInput, "Tác Giả:", cboTacGia, fontLabel, fontInput);
        addFormItem(pnlInput, "Thể Loại:", cboTheLoai, fontLabel, fontInput);
        addFormItem(pnlInput, "Giá từ (VNĐ):", txtGiaTu, fontLabel, fontInput);
        addFormItem(pnlInput, "Giá đến (VNĐ):", txtGiaDen, fontLabel, fontInput);

        pnlMain.add(pnlInput, BorderLayout.CENTER);

        // --- Nút bấm ---
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlBtn.setBackground(Color.WHITE);
        pnlBtn.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnTimKiem = new JButton("Tìm Kiếm");
        btnTimKiem.setBackground(new Color(46, 204, 113));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTimKiem.setPreferredSize(new Dimension(150, 35));
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setOpaque(true); btnTimKiem.setBorderPainted(false);

        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(231, 76, 60));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(100, 35));
        btnHuy.setFocusPainted(false);
        btnHuy.setOpaque(true); btnHuy.setBorderPainted(false);

        pnlBtn.add(btnTimKiem);
        pnlBtn.add(btnHuy);
        pnlMain.add(pnlBtn, BorderLayout.SOUTH);

        add(pnlMain);

        // =========================================================
        // TÍNH NĂNG UX/UI MỚI: BÀN PHÍM
        // =========================================================
        
        // 1. Nhấn nút Enter ở bất kỳ đâu cũng tự động kích hoạt nút Tìm Kiếm
        getRootPane().setDefaultButton(btnTimKiem);

        // 2. Dùng mũi tên Lên/Xuống để chuyển đổi giữa các ô
        setupArrowKeyNavigation(txtTenSach);
        setupArrowKeyNavigation(txtGiaTu);
        setupArrowKeyNavigation(txtGiaDen);

        // --- Sự kiện click chuột ---
        btnTimKiem.addActionListener(e -> {
            isConfirm = true;
            dispose(); 
        });

        btnHuy.addActionListener(e -> {
            isConfirm = false;
            dispose(); 
        });
    }

    // Hàm hỗ trợ: Bắt sự kiện mũi tên Lên/Xuống để nhảy focus
    private void setupArrowKeyNavigation(JTextField txt) {
        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    txt.transferFocus(); // Nhảy xuống ô tiếp theo
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    txt.transferFocusBackward(); // Nhảy lên ô trước đó
                }
            }
        });
    }

    private void addFormItem(JPanel parent, String label, JComponent input, Font fLabel, Font fInput) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(fLabel);
        input.setFont(fInput);
        parent.add(lbl);
        parent.add(input);
    }

    private void loadComboBoxData() {
        cboTacGia.addItem("Tất cả");
        ArrayList<TacGiaDTO> listTG = new TacGiaDAO().selectAll();
        for (TacGiaDTO tg : listTG) cboTacGia.addItem(tg.getMaTG() + " - " + tg.getHoTen());

        cboTheLoai.addItem("Tất cả");
        ArrayList<TheLoaiDTO> listTL = new TheLoaiDAO().selectAll();
        for (TheLoaiDTO tl : listTL) cboTheLoai.addItem(tl.getMaTL() + " - " + tl.getTenTL());
    }

    // --- Các hàm để bên ngoài lấy dữ liệu ---
    public boolean isConfirm() { return isConfirm; }
    public String getTenSach() { return txtTenSach.getText().trim(); }
    public String getMaTG() {
        if (cboTacGia.getSelectedIndex() == 0) return "Tất cả";
        return cboTacGia.getSelectedItem().toString().split(" - ")[0]; 
    }
    public String getMaTL() {
        if (cboTheLoai.getSelectedIndex() == 0) return "Tất cả";
        return cboTheLoai.getSelectedItem().toString().split(" - ")[0];
    }
    public double getGiaTu() {
        try { return Double.parseDouble(txtGiaTu.getText().trim()); } catch (Exception e) { return 0; }
    }
    public double getGiaDen() {
        try { return Double.parseDouble(txtGiaDen.getText().trim()); } catch (Exception e) { return 0; }
    }
}