package gui;

import bus.*;
import dto.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class QuanLyNhapHangPanel extends JPanel {

    CardLayout cardLayout;
    JPanel panelMain;
    

    public QuanLyNhapHangPanel() {

        setLayout(new BorderLayout());

        // ======= MENU CHUYỂN CARD =======
        JPanel panelTop = new JPanel();

        JButton btnNhapHang = new JButton("Nhập hàng");
        JButton btnXemPN = new JButton("Xem lại phiếu nhập");

        panelTop.add(btnNhapHang);
        panelTop.add(btnXemPN);

        add(panelTop, BorderLayout.NORTH);

        // ======= CARD LAYOUT =======
        cardLayout = new CardLayout();
        panelMain = new JPanel(cardLayout);

        panelMain.add(panelNhapHang(), "nhaphang");
        panelMain.add(panelXemPhieuNhap(), "xemphieu");

        add(panelMain, BorderLayout.CENTER);

        // ======= SỰ KIỆN =======
        btnNhapHang.addActionListener(e -> {
            cardLayout.show(panelMain, "nhaphang");
        });

        btnXemPN.addActionListener(e -> {
            cardLayout.show(panelMain, "xemphieu");
        });


        


        


        

    }

    // =========================
    // CARD 1 : NHẬP HÀNG
    // =========================
    private JPanel panelNhapHang() {
        
        JPanel panel = new JPanel(new BorderLayout());


        PhieuNhapBUS pnBUS = new PhieuNhapBUS();
        NhanVienBUS nvBUS = new NhanVienBUS();
        ArrayList<PhieuNhapDTO> DSpn = new ArrayList<> ();
        ArrayList<ChiTietPhieuNhapDTO> DSctpn = new ArrayList<> ();
        ArrayList<SachDTO> DSsach = new ArrayList<> ();

        JLabel title = new JLabel("Kho hàng", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));


        // panel bên phải CARD1
        panel.add(title, BorderLayout.NORTH);


        JTable tableKho = new JTable(){
        public boolean isCellEditable(int r,int c){
            return false;
        }
    };
    //tableCTPN.getTableHeader().setDefaultRenderer(headerRenderer);
        JTable tableChoNhap = new JTable(){
        public boolean isCellEditable(int r,int c){
            return false;
        }
    };
    //tableCTPN.getTableHeader().setDefaultRenderer(headerRenderer);

        // màu cho phần tiêu đề của bảng

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(0,102,204));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setFont(new Font("Arial", Font.BOLD, 14));

        tableKho.getTableHeader().setDefaultRenderer(headerRenderer);
        tableChoNhap.getTableHeader().setDefaultRenderer(headerRenderer);


        tableKho.setRowHeight(25);
        tableChoNhap.setRowHeight(25);


        // đặt tiêu dều chờ nhập
        DefaultTableModel modelChoNhap = new DefaultTableModel();
        modelChoNhap.setColumnIdentifiers(new String[]{
                                                "Mã sách",
                                                "Tên sách",
                                                "Số lượng",
                                                "Đơn giá"
        });
        tableChoNhap.setModel(modelChoNhap);

    // đổ dữ liệu tabel kho CARD1
            DSsach = pnBUS.docKho();

        DefaultTableModel modelKho = new DefaultTableModel();
        modelKho.setColumnIdentifiers(new String[]{
                                        "Mã sách",
                                        "Tên sách",
                                        "Số lượng"
                                        });

        for(SachDTO sach: DSsach) {
            Vector row = new Vector<>();
            row.add(sach.getMaSach());
            row.add(sach.getTenSach());
            row.add(sach.getSoLuong());
            modelKho.addRow(row);
        }
        tableKho.setModel(modelKho);

        JScrollPane sp1 = new JScrollPane(tableKho);
        JScrollPane sp2 = new JScrollPane(tableChoNhap);

        JPanel center = new JPanel(new BorderLayout());

       

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp1, sp2);
        split.setDividerLocation(300);
        center.add(split, BorderLayout.CENTER);




        // panel bên trái CARD1
        panel.add(center, BorderLayout.CENTER);
        JPanel panelRight = new JPanel();
        panelRight.setPreferredSize(new Dimension(450,0));
        panelRight.setLayout(new BoxLayout(panelRight,BoxLayout.Y_AXIS));






        // thông tin sản phẩm
        JLabel tieuDe = new JLabel("Thông tin sản phẩm",JLabel.CENTER);
        tieuDe.setFont(new Font("Aria",Font.BOLD,20));
        tieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtMaSach = new JTextField();
        JLabel maSach = new JLabel("Mã sách");


        JTextField txtTenSach = new JTextField();
        JLabel  tenSach = new JLabel("Tên sách");

        JTextField txtSoLuong = new JTextField();
        JLabel soLuong = new JLabel("Số lượng");

        JTextField txtDonGia = new JTextField();
        JLabel donGia = new JLabel("Đơn giá");

        JButton btnChonNhap = new JButton("Chọn Nhập");
        btnChonNhap.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChonNhap.setBackground(new Color(0,153,76));
        btnChonNhap.setForeground(Color.WHITE);
        btnChonNhap.setFocusPainted(false);
        btnChonNhap.setBorderPainted(false);
        btnChonNhap.setOpaque(true);
        


        txtMaSach.setMaximumSize(new Dimension(200,25));
        txtTenSach.setMaximumSize(new Dimension(200,25));
        txtSoLuong.setMaximumSize(new Dimension(200,25));
        txtDonGia.setMaximumSize(new Dimension(200,25));
        
        panelRight.add(tieuDe);
        panelRight.add(maSach);
        panelRight.add(txtMaSach);
        panelRight.add(tenSach);
        panelRight.add(txtTenSach);
        panelRight.add(soLuong);
        panelRight.add(txtSoLuong);
        panelRight.add(donGia);
        panelRight.add(txtDonGia);
        panelRight.add(btnChonNhap);
        
        panelRight.add(Box.createVerticalStrut(20));


        // thông tin phiếu nhập CARD1

        JLabel tieuDe2 = new JLabel("Thông tin phiếu nhập",JLabel.CENTER);
        tieuDe2.setFont(new Font("Arial",Font.BOLD,20));
        tieuDe2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nhaCungCap = new JLabel("Nhà cung cấp");
        JComboBox<String> cbNhaCungCap = new JComboBox<> ();
        cbNhaCungCap.setMaximumSize(new Dimension(200,25));

        for(NhaCungCapDTO ncc: pnBUS.docNCC()) {
            String Item = ncc.getMaNCC();
            cbNhaCungCap.addItem(Item);
        }
        

        JLabel nhanVien = new JLabel("Nhân viên");
        JComboBox<String> cbNhanVien = new JComboBox<> ();
        cbNhanVien.setMaximumSize(new Dimension(200,25));
        

        for(NhanVienDTO nv : nvBUS.getList()) {
            String Item = nv.getMaNV();
            cbNhanVien.addItem(Item);
        }

        JTextArea txtGhiChu = new JTextArea(5,20);
        JScrollPane spGhiChu = new JScrollPane(txtGhiChu);
        JLabel ghiChu = new JLabel("Ghi Chú: ");
        spGhiChu.setMaximumSize(new Dimension(200,60));

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(204,0,0) );
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setBorderPainted(false);
        btnXoa.setOpaque(true);

        JButton btnXacNhan = new JButton("Xác Nhận");
        btnXacNhan.setBackground(new Color(0,153,76));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setBorderPainted(false);
        btnXacNhan.setOpaque(true);

        JPanel btnPanel = new JPanel(new GridLayout(1,2,10,0));
        btnXoa.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnXacNhan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPanel.add(btnXoa);
        btnPanel.add(btnXacNhan);
        btnPanel.setMaximumSize(new Dimension(200,30));





        panelRight.add(tieuDe2);
        panelRight.add(nhaCungCap);
        panelRight.add(cbNhaCungCap);
        panelRight.add(nhanVien);
        panelRight.add(cbNhanVien);
        panelRight.add(ghiChu);
        panelRight.add(spGhiChu);
        panelRight.add(Box.createVerticalStrut(20));
        panelRight.add(btnPanel);



        // đổ dữ liệu khi click kho 
        tableKho.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableKho.getSelectedRow();
                txtMaSach.setText(tableKho.getValueAt(row,0).toString());
                txtTenSach.setText(tableKho.getValueAt(row,1).toString());
            }
        });

        // Nút chọn nhập CARD1
        btnChonNhap.addActionListener(e -> {

            if(txtMaSach.getText().isEmpty() ||
                    txtSoLuong.getText().isEmpty() ||
                    txtDonGia.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Nhập đầy đủ thông tin");
                    return;
                }

            boolean tonTai = false;
                for(int i = 0; i < modelChoNhap.getRowCount(); i++){
                    if(modelChoNhap.getValueAt(i,0).toString().equals(txtMaSach.getText())){
                    int slCu = Integer.parseInt(modelChoNhap.getValueAt(i,2).toString());
                    int soLuongMoi = Integer.parseInt(txtSoLuong.getText());
                    modelChoNhap.setValueAt(slCu + soLuongMoi, i, 2);
                    tonTai = true;
                break;
                    }
                }
            if(!tonTai) {
            Vector row = new Vector<>();            
                row.add(txtMaSach.getText());
                row.add(txtTenSach.getText());
                row.add(txtSoLuong.getText());
                row.add(txtDonGia.getText());
                modelChoNhap.addRow(row);
            }
        });

        // nút Xóa dòng chờ nhập CARD1
        btnXoa.addActionListener(e -> {
            int row = tableChoNhap.getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(null,"Chọn dòng cần xóa");
            }
            else {
            DefaultTableModel model = (DefaultTableModel) tableChoNhap.getModel();
            model.removeRow(row);
            }
        });



        // nút xác nhận thông tin sản phẩm CARD1
        btnXacNhan.addActionListener(e -> {
            btnXacNhan.setEnabled(false);
            DefaultTableModel model = (DefaultTableModel) tableChoNhap.getModel();
            if(model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null,"Chưa có dữ liệu");
            }
            else {

            try {
                PhieuNhapDTO pn = new PhieuNhapDTO();
                ArrayList<ChiTietPhieuNhapDTO> ctpn = new ArrayList<> ();
                
                String maPN = "PN" + System.currentTimeMillis();
                pn.setMaPN(maPN);
                pn.setMaNCC(cbNhaCungCap.getSelectedItem().toString());
                pn.setMaNV(cbNhanVien.getSelectedItem().toString());
                pn.setNgayNhap(new Date());
                pn.setGhiChu(txtGhiChu.getText());
                Double tongTien = 0.0;
                for(int i = 0;i < model.getRowCount();i ++) {
                    ChiTietPhieuNhapDTO ct = new ChiTietPhieuNhapDTO();
                    ct.setMaPN(maPN);
                    ct.setMaSach(model.getValueAt(i,0).toString());
                    ct.setSoLuong(Integer.parseInt(model.getValueAt(i,2).toString()));
                    ct.setDonGiaNhap(Double.parseDouble(model.getValueAt(i,3).toString()));

                    int so = Integer.parseInt(model.getValueAt(i, 2).toString());
                    double gia = Double.parseDouble(model.getValueAt(i,3).toString());
                    tongTien += so*gia;
                    ctpn.add(ct);
                }
                pn.setTongTien(tongTien);


                // thêm phiếu nhập 
                boolean kq = pnBUS.themPhieuNhap(pn, ctpn);
                if(kq){
                    JOptionPane.showMessageDialog(null,"Nhập hàng thành công");
                    model.setRowCount(0);
                    modelKho.setRowCount(0);
                    for(SachDTO sach : pnBUS.docKho()){
                        Vector row = new Vector();
                        row.add(sach.getMaSach());
                        row.add(sach.getTenSach());
                        row.add(sach.getSoLuong());
                        modelKho.addRow(row);
                    }
                    txtMaSach.setText("");
                    txtGhiChu.setText("");
                    txtDonGia.setText("");
                    txtTenSach.setText("");
                    txtSoLuong.setText("");
                    cbNhaCungCap.setSelectedIndex(-1);
                    cbNhanVien.setSelectedIndex(-1);
                    DSctpn.clear();
                    DSpn.clear();
                }else{
                JOptionPane.showMessageDialog(null,"Nhập thất bại");
                }


                
            }
            catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "thêm lỗi");
            }
        }
        btnXacNhan.setEnabled(true);
    });

        panel.add(panelRight,BorderLayout.EAST);
        return panel;
    }

    // =========================
    // CARD 2 : XEM PHIẾU NHẬP
    // =========================
    private JPanel panelXemPhieuNhap() {

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

    PhieuNhapBUS pnBUS = new PhieuNhapBUS();

    // ===== PANEL CENTER =====
    JPanel center = new JPanel(new GridLayout(1,2,10,0));

    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(0,102,204));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setFont(new Font("Arial", Font.BOLD, 14));

        // bảng chi tiết phiếu nhập
    JTable tableCTPN = new JTable(){
        public boolean isCellEditable(int r,int c){
            return false;
        }
    };
    tableCTPN.getTableHeader().setDefaultRenderer(headerRenderer);


        // bảng phiếu nhập
    JTable tablePN = new JTable(){
        public boolean isCellEditable(int row,int column){
            return false;
        }
    };
    tablePN.getTableHeader().setDefaultRenderer(headerRenderer);

    // =========================
    // PANEL TRÁI - PHIẾU NHẬP
    // =========================
    JPanel panelPN = new JPanel();
    panelPN.setPreferredSize(new Dimension(300,0));
    panelPN.setLayout(new BoxLayout(panelPN,BoxLayout.Y_AXIS));
    


    JLabel titlePN = new JLabel("Phiếu nhập",JLabel.CENTER);
    titlePN.setFont(new Font("Arial",Font.BOLD,18));
    titlePN.setAlignmentX(Component.CENTER_ALIGNMENT);

    JTextField txtMaPN = new JTextField();
    JTextField txtMaNCC = new JTextField();
    JTextField txtMaNV = new JTextField();
    JTextField txtNgayLap = new JTextField();
    JTextField txtTongTien = new JTextField();

    txtMaPN.setEditable(false);
    txtMaNCC.setEditable(false);
    txtMaNV.setEditable(false);
    txtNgayLap.setEditable(false);
    txtTongTien.setEditable(false);


    JLabel maPN = new JLabel("Mã PN");
    JLabel maNCC = new JLabel("Mã NCC");
    JLabel maNV = new JLabel("Mã NV");
    JLabel ngayLap = new JLabel("Ngày lập");
    JLabel tongTien = new JLabel("Tổng tiền");

    Dimension size1 =  new Dimension(350,25);
    txtMaPN.setPreferredSize(size1);
    txtMaNCC.setPreferredSize(size1);
    txtMaNV.setPreferredSize(size1);
    txtNgayLap.setPreferredSize(size1);
    txtTongTien.setPreferredSize(size1);

    panelPN.add(titlePN);
    
    JPanel panelTT = new JPanel(new GridLayout(5,2,1,1));
    panelTT.setMaximumSize(new Dimension(280,120));
    panelTT.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
        "Thông Tin Phiếu nhập"
        )
    );
    panelTT.add(maPN);
    panelTT.add(txtMaPN);

    panelTT.add(maNCC);
    panelTT.add(txtMaNCC);

    panelTT.add(maNV);
    panelTT.add(txtMaNV);

    panelTT.add(ngayLap);
    panelTT.add(txtNgayLap);
    
    
    panelTT.add(tongTien);
    panelTT.add(txtTongTien);

    panelPN.add(panelTT);




    



    // ===== TÌM KIẾM =====
    JLabel timKiem = new JLabel("Tìm kiếm");
    timKiem.setFont(new Font("Arial",Font.BOLD,18));
    timKiem.setAlignmentX(Component.CENTER_ALIGNMENT);

    JTextField txtGiaTu = new JTextField();
    JTextField txtGiaToi = new JTextField();

    JLabel giaTu = new JLabel("Giá Từ: ");
    JLabel giaToi = new JLabel("Giá Tới: ");

    JTextField txtTuNgay = new JTextField();
    JTextField txtDenNgay = new JTextField();

    JLabel tuNgay = new JLabel("Từ ngày: ");
    JLabel denNgay = new JLabel("Đến ngày: ");

    Dimension size = new Dimension(550,25);
    txtGiaTu.setMaximumSize(size1);
    txtGiaToi.setMaximumSize(size1);
    txtTuNgay.setMaximumSize(size1);
    txtDenNgay.setMaximumSize(size1);


    
    

    panelPN.add(timKiem);

    JPanel panelTK = new JPanel(new GridLayout(4,2,1,1));
    panelTK.setMaximumSize(new Dimension(380,150));
    panelTK.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
        "Tìm kiếm Phiếu nhập"
        )
    );
    panelTK.add(giaTu);
    panelTK.add(txtGiaTu);

    panelTK.add(giaToi);
    panelTK.add(txtGiaToi);


    panelTK.add(tuNgay);
    panelTK.add(txtTuNgay);

    panelTK.add(denNgay);
    panelTK.add(txtDenNgay);

    panelPN.add(panelTK);

    JButton btnTimKiem = new JButton("Tìm kiếm");
    btnTimKiem.setBackground(new Color(0,153,76));
    btnTimKiem.setForeground(Color.WHITE);
    btnTimKiem.setFocusPainted(false);
    btnTimKiem.setBorderPainted(false);
    btnTimKiem.setOpaque(true);
    btnTimKiem.setMaximumSize(new Dimension(50,25));

    JButton btnHuy = new JButton("Hủy");
    btnHuy.setBackground(new Color(204,0,0));
    btnHuy.setForeground(Color.WHITE);
    btnHuy.setFocusPainted(false);
    btnHuy.setBorderPainted(false);
    btnHuy.setOpaque(true);
    btnHuy.setMaximumSize(new Dimension(50,25));
    JPanel panelBTN = new JPanel(new GridLayout(1,2));
    panelBTN.add(btnTimKiem);
    panelBTN.add(btnHuy);
    panelBTN.setMaximumSize(new Dimension(200,50));
    panelPN.add(panelBTN);

    
    

    DefaultTableModel modelPN = new DefaultTableModel();
    modelPN.addColumn("Mã PN");
    modelPN.addColumn("Ngày lập");
    modelPN.addColumn("Mã NCC");
    modelPN.addColumn("Mã NV");
    modelPN.addColumn("Ghi chú");
    modelPN.addColumn("Tổng tiền");

    for(PhieuNhapDTO pn : pnBUS.docPhieuNhap()){
        Vector row = new Vector();
        row.add(pn.getMaPN());
        row.add(pn.getNgayNhap());
        row.add(pn.getMaNCC());
        row.add(pn.getMaNV());
        row.add(pn.getGhiChu());
        row.add(String.format("%.0f VNĐ", pn.getTongTien()));
        modelPN.addRow(row);
    }

    tablePN.setModel(modelPN);

    JScrollPane spPN = new JScrollPane(tablePN);
    spPN.setMaximumSize(new Dimension(700,120));


    
    // =========================
    // PANEL PHẢI - CHI TIẾT
    // =========================
    JPanel panelCT = new JPanel();
    panelCT.setLayout(new BoxLayout(panelCT,BoxLayout.Y_AXIS));

    JLabel titleCT = new JLabel("Chi tiết phiếu nhập",JLabel.CENTER);
    titleCT.setFont(new Font("Arial",Font.BOLD,18));
    titleCT.setAlignmentX(Component.CENTER_ALIGNMENT);

    JTextField txtSanPham = new JTextField();
    JTextField txtSoLuong = new JTextField();
    JTextField txtDonGia = new JTextField();
    JTextField txtThanhTien = new JTextField();

    txtSanPham.setEditable(false);
    txtSoLuong.setEditable(false);
    txtDonGia.setEditable(false);
    txtThanhTien.setEditable(false);

    txtSanPham.setMaximumSize(new Dimension(200,25));
    txtSoLuong.setMaximumSize(new Dimension(200,25));
    txtDonGia.setMaximumSize(new Dimension(200,25));
    txtThanhTien.setMaximumSize(new Dimension(200,25));

    JPanel panelTTCT = new JPanel(new GridLayout(4,2));
    panelTTCT.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
        "Thông Tin Phiếu nhập"
        )
    );
    panelTTCT.setMaximumSize(new Dimension(380,120));

    panelCT.add(titleCT);
    panelTTCT.add(new JLabel("Sản phẩm"));
    panelTTCT.add(txtSanPham);

    panelTTCT.add(new JLabel("Số lượng"));
    panelTTCT.add(txtSoLuong);

    panelTTCT.add(new JLabel("Đơn giá"));
    panelTTCT.add(txtDonGia);

    panelTTCT.add(new JLabel("Thành tiền"));
    panelTTCT.add(txtThanhTien);

    panelCT.add(panelTTCT);
    panelCT.add(Box.createVerticalStrut(20));

    

    DefaultTableModel modelCT = new DefaultTableModel();
    modelCT.addColumn("Mã SP");
    modelCT.addColumn("Số lượng");
    modelCT.addColumn("Đơn giá");
    modelCT.addColumn("Thành tiền");

    tableCTPN.setModel(modelCT);

    JScrollPane spCT = new JScrollPane(tableCTPN);
    spCT.setMaximumSize(new Dimension(600,500));

    // ===== CLICK PHIẾU NHẬP =====
    tablePN.addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent e){

            int row = tablePN.getSelectedRow();

            String maPN = tablePN.getValueAt(row,0).toString();

            modelCT.setRowCount(0);

            for(ChiTietPhieuNhapDTO ct : pnBUS.docChiTietPhieuNhap(maPN)){

                Vector r = new Vector();
                r.add(ct.getMaSach());
                r.add(ct.getSoLuong());
                r.add(ct.getDonGiaNhap());
                r.add(String.format("%,.0f VNĐ",ct.getThanhTien()));

                modelCT.addRow(r);
            }

                txtMaPN.setText(tablePN.getValueAt(row,0).toString());
                txtNgayLap.setText(tablePN.getValueAt(row,1).toString());
                txtMaNCC.setText(tablePN.getValueAt(row,2).toString());
                txtMaNV.setText(tablePN.getValueAt(row,3).toString());
                txtTongTien.setText(tablePN.getValueAt(row,5).toString());
        }
    });



    // click bảng chi tiết phiếu nhập
    tableCTPN.addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent e){

        int row = tableCTPN.getSelectedRow();

        txtSanPham.setText(tableCTPN.getValueAt(row,0).toString());
        txtSoLuong.setText(tableCTPN.getValueAt(row,1).toString());
        txtDonGia.setText(tableCTPN.getValueAt(row,2).toString());
        txtThanhTien.setText(tableCTPN.getValueAt(row,3).toString());
        }
    });

    // sự kiện nút nhập 
    btnTimKiem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String tuNgay = txtTuNgay.getText();
            String denNgay = txtDenNgay.getText();
            String giaTu = txtGiaTu.getText();
            String giaToi = txtGiaToi.getText();
            ArrayList<PhieuNhapDTO> dspn = new ArrayList<> ();
            dspn = pnBUS.timPhieuNhap(tuNgay,denNgay,giaTu,giaToi);
            DefaultTableModel model = (DefaultTableModel) tablePN.getModel();
            model.setRowCount(0);

            for(PhieuNhapDTO pn: dspn) {
                Vector row = new Vector<> ();
                row.add(pn.getMaPN());
                row.add(pn.getNgayNhap());
                row.add(pn.getMaNCC());
                row.add(pn.getMaNV());
                row.add(pn.getGhiChu());
                row.add(String.format("%,.0f VNĐ",pn.getTongTien()));
                model.addRow(row);
            }
        }
    });

    // sự kiện nút hủy 

    btnHuy.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            txtGiaTu.setText("");
            txtGiaToi.setText("");
            txtTuNgay.setText("");
            txtDenNgay.setText("");

            DefaultTableModel model = (DefaultTableModel) tablePN.getModel();
            model.setRowCount(0);
            for(PhieuNhapDTO pn: pnBUS.docPhieuNhap()) {
                Vector row = new Vector<> ();
                row.add(pn.getMaPN());
                row.add(pn.getNgayNhap());
                row.add(pn.getMaNCC());
                row.add(pn.getMaNV());
                row.add(pn.getGhiChu());
                row.add(String.format("%,.0f VNĐ",pn.getTongTien()));
                model.addRow(row);
            }

            DefaultTableModel modelCT = (DefaultTableModel) tableCTPN.getModel();
            modelCT.setRowCount(0);


            txtMaPN.setText("");
            txtMaNCC.setText("");
            txtMaNV.setText("");
            txtNgayLap.setText("");
            txtTongTien.setText("");

            txtSanPham.setText("");
            txtSoLuong.setText("");
            txtDonGia.setText("");
            txtThanhTien.setText("");
        }
    });




    // ===== ADD TABLE =====
    panelPN.add(spPN);
    panelCT.add(spCT);

    center.add(panelPN);
    center.add(panelCT);

    panel.add(center,BorderLayout.CENTER);

    return panel;
}
}