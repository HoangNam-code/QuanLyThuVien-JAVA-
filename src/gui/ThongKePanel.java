package gui;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.util.ArrayList;
import bus.ThongKeBUS;
import dto.*;

public class ThongKePanel extends JPanel {
    JPanel wrapTop;
    JScrollPane scrollTop;

    JTable tableKH, tableDG, tableNV;

    ThongKeBUS tkBUS = new ThongKeBUS();

    JLabel lbSach, lbKH, lbNV, lbDoanhThu;
    JTextField txtNam = new JTextField(10);
    JTable table, tableTopSach;

    public ThongKePanel() {
        setLayout(new BorderLayout());

        // ===== TOP: 4 BOX =====
        JPanel panelTop = new JPanel(new GridLayout(2,2,20,20)); 
        panelTop.setPreferredSize(null); 
        panelTop.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));


        panelTop.add(taoBox("Sách", tkBUS.docSach()+"", new Color(255,153,0), "/img/book.png"));
        panelTop.add(taoBox("Độc Giả", tkBUS.docDocGia()+"", new Color(0,204,204), "/img/reader.png"));
        panelTop.add(taoBox("Nhân viên", tkBUS.docNhanVien()+"", new Color(255,102,153), "/img/staff.png"));
        panelTop.add(taoBox("Doanh thu", tkBUS.docDoanhThu()+" VNĐ", new Color(0,200,83), "/img/borrow.png"));

        // ===== CENTER: TABLE =====
        JPanel panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        

        // panel chứa 2 bảng
        JPanel panelMain = new JPanel(new GridLayout(1, 2, 20, 0));
        panelMain.setBackground(Color.WHITE);



                // ==== PANEL BÊN TRÁI (3 bảng mới) ====
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setBackground(Color.WHITE);

        tableKH = new JTable(){ public boolean isCellEditable(int r,int c){ return false; } }; // tạm giữ tên biến, nhưng sẽ dùng cho Sách
        tableDG = new JTable(){ public boolean isCellEditable(int r,int c){ return false; } };
        tableNV = new JTable(){ public boolean isCellEditable(int r,int c){ return false; } };

        int tableWidth = 520;
        int tableHeight = 200;

        // Bảng 1: Tiền Độc Giả (phí + phạt)
        JPanel panelDG = taoPanelBang("TỔNG TIỀN ĐỘC GIẢ ", tableDG, tableWidth, tableHeight);
        panelLeft.add(panelDG);

        // Bảng 2: Nhân Viên
        JPanel panelNV = taoPanelBang("LƯƠNG NHÂN VIÊN", tableNV, tableWidth, tableHeight);
        panelLeft.add(panelNV);

        // Bảng 3: Sách được mượn (thay cho KH cũ)
        JPanel panelSach = taoPanelBang("SÁCH ĐƯỢC MƯỢN NHIỀU", tableKH, tableWidth, tableHeight); // dùng tableKH làm bảng sách
        panelLeft.add(panelSach);






        // === PANEL PHẢI ========
        JPanel panelRight = new JPanel();
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
        panelRight.setBackground(Color.WHITE);

        // ===== TABLE TOP SÁCH =====
        tableTopSach = new JTable(){
        public boolean isCellEditable(int r,int c){
            return false;
        }
    };
        tableTopSach.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        tableTopSach.setPreferredScrollableViewportSize(new Dimension(500, 100));
        

        // ===== TABLE DOANH THU =====
        table = new JTable(){
        public boolean isCellEditable(int r,int c){
            return false;
        }
    };
        table.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        // ==== Load table ====
        loadTopSach();
        txtNam.setText("2024");
        
        

        


        // tiêu đề
        JLabel lblTopSach = new JLabel("TOP SÁCH MƯỢN NHIỀU");
        lblTopSach.setFont(new Font("Arial", Font.BOLD, 16));
        lblTopSach.setAlignmentX(Component.CENTER_ALIGNMENT);

        // bảng top sách được mượn
        scrollTop = new JScrollPane(tableTopSach);
        scrollTop.setPreferredSize(new Dimension(500, 100));

        // wrap căn giữa 
        wrapTop = new JPanel(new GridLayout(1, 2, 10, 10));
        wrapTop.setBackground(Color.WHITE);

        // bảng top
        wrapTop.add(scrollTop);

        // biểu đồ
        ChartPanel chartPanel = taoBieuDo(2024);
        wrapTop.add(chartPanel);


        // bảng thống kê 
        JScrollPane scrollBottom = new JScrollPane(table);
        scrollBottom.setPreferredSize(new Dimension(610,140));

        

        JLabel lblThongKe = new JLabel("BẢNG THỐNG KÊ");
        lblThongKe.setFont(new Font("Arial", Font.BOLD, 16));
        lblThongKe.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel wrapBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapBottom.setBackground(Color.WHITE);

        wrapBottom.add(scrollBottom);


        



        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBottom.setBackground(Color.WHITE);

        // Label
        JLabel lblNam = new JLabel("Năm:");
        lblNam.setFont(new Font("Arial", Font.BOLD, 14));

        // TextField
        txtNam.setPreferredSize(new Dimension(100, 30));

        // Button
        JButton btnThongKe = new JButton("Thống kê");
        btnThongKe.setFocusPainted(false);

        // add vào panel

        // ==== THÊM BOX VÀO ĐÂY ====
        panelRight.add(panelTop);
        panelRight.add(Box.createVerticalStrut(10));

        // nút
        panelRight.add(panelBottom);

        // bảng thống kê
        panelRight.add(lblThongKe);
        panelRight.add(wrapBottom);

        // top sách + biểu đồ
        panelRight.add(lblTopSach);
        panelRight.add(Box.createVerticalStrut(5));
        panelRight.add(wrapTop);

        panelBottom.add(lblNam);
        panelBottom.add(txtNam);
        panelBottom.add(btnThongKe);

        panelRight.add(panelBottom); // nút nằm ở đây
        panelRight.add(lblThongKe);
        panelRight.add(wrapBottom);


        panelRight.add(lblTopSach);
        panelRight.add(Box.createVerticalStrut(5));
        panelRight.add(wrapTop);
        panelRight.add(Box.createVerticalStrut(10));


        

        panelMain.add(panelLeft);
        panelMain.add(panelRight);
        panelCenter.add(panelMain, BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);
        // sự kiện nút
        btnThongKe.addActionListener(e -> loadTableALL());
        loadTableALL();
    }

    private JPanel taoBox(String title, String value, Color color, String iconPath) {
    JPanel panel = new JPanel() {
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
            g2.drawImage(icon.getImage(), getWidth()-90, getHeight()-90, 70, 70, this);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    };

    panel.setLayout(new BorderLayout());
    panel.setBackground(color);
    panel.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));

    JLabel lbValue = new JLabel(value);
    lbValue.setFont(new Font("Arial", Font.BOLD, 28));
    lbValue.setForeground(Color.BLACK);

    JLabel lbTitle = new JLabel(title);
    lbTitle.setForeground(Color.BLACK);
    lbTitle.setFont(new Font("Arial", Font.PLAIN, 14));

    panel.add(lbValue, BorderLayout.CENTER);
    panel.add(lbTitle, BorderLayout.SOUTH);

    return panel;
}

    // load biểu đồ 
         // hàm tạo biểu đồ 
        private ChartPanel taoBieuDo(int nam) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        ArrayList<Double> ds = tkBUS.docTheoQuy(nam);

        // đảm bảo đủ 4 quý
        while (ds.size() < 4) ds.add(0.0);

        dataset.addValue(ds.get(0), "Doanh thu", "Quý 1");
        dataset.addValue(ds.get(1), "Doanh thu", "Quý 2");
        dataset.addValue(ds.get(2), "Doanh thu", "Quý 3");
        dataset.addValue(ds.get(3), "Doanh thu", "Quý 4");

        JFreeChart chart = ChartFactory.createBarChart(
                "Doanh thu năm " + nam,
                "Quý",
                "Doanh thu",
                dataset
        );

        return new ChartPanel(chart);
        }
    // load bảng top sách
    private void loadTopSach() {
    DefaultTableModel model = new DefaultTableModel();

    model.addColumn("STT");
    model.addColumn("Mã sách");
    model.addColumn("Tên sách");
    model.addColumn("Số lần mượn");

    ArrayList<SachMuonDTO> ds = tkBUS.docSachTop(); // hoặc TopSachDTO

    int stt = 1;
    for (SachMuonDTO s : ds) {
        model.addRow(new Object[]{
            stt ++,
            s.getMaSach(),
            s.getTenSach(),
            s.getSoLanMuon()
        });
    }


    //  căn giữa dữ liệu cho hàng
    tableTopSach.setModel(model);
    DefaultTableCellRenderer centerTop = new DefaultTableCellRenderer();
    centerTop.setHorizontalAlignment(JLabel.CENTER);

    for (int i = 0; i < tableTopSach.getColumnCount(); i++) {
        tableTopSach.getColumnModel().getColumn(i).setCellRenderer(centerTop);
    }

    // căn giữa dữ liệu cho tiêu đề
    DefaultTableCellRenderer centerHeaderTop = new DefaultTableCellRenderer();
    centerHeaderTop.setHorizontalAlignment(JLabel.CENTER);
    centerHeaderTop.setBackground(new Color(0, 102, 204));
    centerHeaderTop.setForeground(Color.WHITE);

    for (int i = 0; i < tableTopSach.getColumnModel().getColumnCount(); i++) {
        tableTopSach.getColumnModel().getColumn(i).setHeaderRenderer(centerHeaderTop);
    }





    }
    // ===== LOAD TABLE THEO QUÝ =====
    private void loadTableALL() {
        if(txtNam.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Nhập năm!");
            return;
        }
        int nam = Integer.parseInt(txtNam.getText());

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Quý", "Quý 1","Quý 2","Quý 3"," Quý 4"}, 0
        );

        

        ArrayList<Double> ds = tkBUS.docTheoQuy(nam);

        // đảm bảo đủ 4 quý
        while(ds.size() < 4) ds.add(0.0);

        double tong = 0;
        for(double dt : ds) tong += dt;

        // dòng doanh thu
        model.addRow(new Object[]{
            "Doanh thu",
            String.format("%,.0f", ds.get(0)),
            String.format("%,.0f", ds.get(1)),
            String.format("%,.0f", ds.get(2)),
            String.format("%,.0f", ds.get(3))
        });

        // dòng tổng
        model.addRow(new Object[]{
            "Tổng",
            "",
            "",
            "",
            String.format("%,.0f VNĐ", tong)
        });

    table.setModel(model);


    


        
        
        // ===== STYLE TABLE =====
        table.setFont(new Font("Arial", Font.PLAIN, 14)); // chữ to hơn
        table.setRowHeight(25); // cao dòng

        // căn giữa dữ liệu dòng
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // căn giữa tiêu đề 
        DefaultTableCellRenderer centerHeader = new DefaultTableCellRenderer();
        centerHeader.setHorizontalAlignment(JLabel.CENTER);
        centerHeader.setBackground(new Color(0, 102, 204));
        centerHeader.setForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(centerHeader);
        }

        

        // chỉnh độ rộng cột (nếu muốn nhỏ lại)
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        // style header
        JTableHeader headerTable = table.getTableHeader();
        headerTable.setBackground(new Color(240,240,240));
        headerTable.setForeground(Color.BLACK);
        headerTable.setFont(new Font("Arial", Font.BOLD, 13));

        // không đổi màu khi hover
        headerTable.setReorderingAllowed(false);
        headerTable.setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        
        
        // load biểu đồ 
        wrapTop.removeAll();
        wrapTop.add(scrollTop);
        wrapTop.add(taoBieuDo(nam));

        wrapTop.revalidate();
        wrapTop.repaint();

        // chỉnh  3 bảng 
        loadBangTienDocGia(nam);
        loadBangTienNhanVien(nam);
        loadBangSachMuon(nam);
    }


            private void loadBangTienDocGia(int nam) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã ĐG", "Tên Độc Giả", "Q1", "Q2", "Q3", "Q4", "Tổng"}, 0
        );

        ArrayList<Object[]> ds = tkBUS.docChiTietTienDocGiaTongTheoQuy(nam);
        for (Object[] row : ds) {
            model.addRow(new Object[]{
                row[0], row[1],
                String.format("%,.0f", (Double)row[2]),
                String.format("%,.0f", (Double)row[3]),
                String.format("%,.0f", (Double)row[4]),
                String.format("%,.0f", (Double)row[5]),
                String.format("%,.0f VNĐ", (Double)row[6])
            });
        }
        tableDG.setModel(model);
        styleTableChiTiet(tableDG);
    }

    private void loadBangTienNhanVien(int nam) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã NV", "Tên Nhân Viên", "Q1", "Q2", "Q3", "Q4", "Tổng"}, 0
        );

        ArrayList<Object[]> ds = tkBUS.docChiTietTienNhanVienTheoQuy(nam);
        for (Object[] row : ds) {
            model.addRow(new Object[]{
                row[0], row[1],
                String.format("%,.0f", (Double)row[2]),
                String.format("%,.0f", (Double)row[3]),
                String.format("%,.0f", (Double)row[4]),
                String.format("%,.0f", (Double)row[5]),
                String.format("%,.0f VNĐ", (Double)row[6])
            });
        }
        tableNV.setModel(model);
        styleTableChiTiet(tableNV);
    }

    private void loadBangSachMuon(int nam) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã Sách", "Tên Sách", "Q1", "Q2", "Q3", "Q4", "Tổng"}, 0
        );

        ArrayList<Object[]> ds = tkBUS.docChiTietSachMuonTheoQuy(nam);
        for (Object[] row : ds) {
            model.addRow(new Object[]{
                row[0], row[1],
                row[2], row[3], row[4], row[5],
                row[6] + " lần"
            });
        }
        tableKH.setModel(model);           // dùng tableKH để hiển thị sách
        styleTableChiTiet(tableKH);
    }

        private void styleTableChiTiet(JTable tbl) {
        tbl.setFont(new Font("Arial", Font.PLAIN, 13));
        tbl.setRowHeight(28);
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(180);
        for (int i = 2; i <= 5; i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(80);
        tbl.getColumnModel().getColumn(6).setPreferredWidth(130);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        DefaultTableCellRenderer headerR = new DefaultTableCellRenderer();
        headerR.setHorizontalAlignment(JLabel.CENTER);
        headerR.setBackground(new Color(0, 102, 204));
        headerR.setForeground(Color.WHITE);
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setHeaderRenderer(headerR);
        }
    }


        private JPanel taoPanelBang(String title, JTable table, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        // panel.setBorder(BorderFactory.createLineBorder(new Color(200,200,200))); // nếu muốn có viền

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(width, height));
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

   

}