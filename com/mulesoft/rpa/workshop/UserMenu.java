package com.mulesoft.rpa.workshop;

import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

class UserMenu extends JFrame {
    JTabbedPane tabs;
    HomePanel home;
    InvoiceListing invoiceListing;
    NewInvoice newInvoice;

    UserMenu(Object params[]) {
        super("NTO Financial Module");
        setIconImage(getToolkit().getImage(getClass().getResource("nto_icon.png")));
        Connection conn;
        try {
            String cnx = "jdbc:mysql://" + params[0] + "/" + params[1];
            conn = (Connection) DriverManager.getConnection(cnx, (String) params[2], (String) params[3]);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
            home = new HomePanel();
            invoiceListing = new InvoiceListing(conn);
            newInvoice = new NewInvoice(conn);
            tabs.addTab("Home", home);
            tabs.addTab("Invoices", invoiceListing);
            tabs.addTab("New Invoice", newInvoice);
            getContentPane().add(tabs);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Unexpected error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }
}

class HomePanel extends JPanel {
    Color c = new Color(0, 0x66, 0x99);

    HomePanel() {
        BufferedImage wPic;
        try {
            Box box = Box.createVerticalBox();
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
            setBackground(c);
            wPic = ImageIO.read(this.getClass().getResource("nto.png"));
            JLabel wIcon = new JLabel(new ImageIcon(wPic));
            box.add(wIcon);
            JPanel empty = new JPanel();
            empty.setBackground(c);
            empty.setPreferredSize(new Dimension(0, 30));
            box.add(empty);
            JPanel title = new JPanel();
            title.setBackground(c);
            JLabel l = new JLabel("FINANCIAL");
            l.setFont(new Font("Arial", Font.PLAIN, 30));
            l.setForeground(Color.white);
            title.add(l);
            box.add(title);
            JPanel title2 = new JPanel();
            title2.setBackground(c);
            JLabel l2 = new JLabel("MODULE");
            l2.setFont(new Font("Arial", Font.PLAIN, 30));
            l2.setForeground(Color.white);
            title2.add(l2);
            box.add(title2);
            add(box);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Unexpected error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }
}

class InvoiceListing extends JPanel {
    Connection connection = null;
    JButton refresh, clear;
    final Color c = new Color(0, 0x66, 0x99);
    final String[] columns = new String[] {
           "Invoice Id", "Invoice Date", "Invoice Due  Date", "Purchase Order",  "Customer Name", "Bill to Street", "Bill to City", "Bill To State", "Bill To Zipcode", "Invoice Total"
    };

    private void ClearData(JTable table) {
        Statement st;
        try {
            st = connection.createStatement();
            st.executeUpdate("delete from invoice_totals");
            ClearTable(table);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Unexpected error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    private void ClearTable(JTable table) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setRowCount(0);
    }

    private void ReadData(JTable table) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        dtm.setDataVector(FetchData(), columns);
        table.getColumnModel().getColumn(0).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(80);
        table.getColumnModel().getColumn(2).setMinWidth(110);
        table.getColumnModel().getColumn(3).setMinWidth(110);
        table.getColumnModel().getColumn(4).setMinWidth(110);
        table.getColumnModel().getColumn(5).setMinWidth(140);
        table.getColumnModel().getColumn(6).setMinWidth(120);
        table.getColumnModel().getColumn(7).setMinWidth(80);
        table.getColumnModel().getColumn(8).setMinWidth(100);
        table.getColumnModel().getColumn(9).setMinWidth(100);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private Object[][] FetchData() {
        Object[][] data = new Object[][] {};
        try {
            PreparedStatement st = (PreparedStatement) connection.prepareStatement("Select * from invoice_totals",
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery();
            int rowCount = 0;
            if (rs.last()) {// make cursor to point to the last row in the ResultSet object
                rowCount = rs.getRow();
                rs.beforeFirst(); // make cursor to point to the front of the ResultSet object, just before the
                                  // first row.
            }
            data = new Object[rowCount][columns.length];
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getObject(1, String.class);
                data[i][1] = rs.getObject(2, String.class);
                data[i][2] = rs.getObject(3, String.class);
                data[i][3] = rs.getObject(4, String.class);
                data[i][4] = rs.getObject(5, String.class);
                data[i][5] = rs.getObject(6, String.class);
                data[i][6] = rs.getObject(7, String.class);
                data[i][7] = rs.getObject(8, String.class);
                data[i][8] = rs.getObject(9, String.class);
                data[i][9] = rs.getObject(10, double.class);
                i++;
            }
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Unexpected error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        return data;
    }

    InvoiceListing(Connection conn) {
        this.connection = conn;
        setBackground(c);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        setPreferredSize(new Dimension(525, 40));
        Box box = Box.createVerticalBox();
        JPanel menuButton = new JPanel();
        menuButton.setPreferredSize(new Dimension(525, 40));
        menuButton.setBackground(c);
        DefaultTableModel dtm = new DefaultTableModel(FetchData(), columns);
        JTable table = new JTable(dtm);
        table.getColumnModel().getColumn(0).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(80);
        table.getColumnModel().getColumn(2).setMinWidth(110);
        table.getColumnModel().getColumn(3).setMinWidth(110);
        table.getColumnModel().getColumn(4).setMinWidth(110);
        table.getColumnModel().getColumn(5).setMinWidth(140);
        table.getColumnModel().getColumn(6).setMinWidth(120);
        table.getColumnModel().getColumn(7).setMinWidth(80);
        table.getColumnModel().getColumn(8).setMinWidth(100);
        table.getColumnModel().getColumn(9).setMinWidth(100);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClearTable(table);
                ReadData(table);
            }
        });
        clear = new JButton("Delete All Data");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int input = JOptionPane.showConfirmDialog(null, "This will delete all invoice entries. Are you sure?",
                        "Please, confirm", JOptionPane.YES_NO_OPTION);
                if (input == 0) {
                    ClearData(table);
                }
            }
        });
        menuButton.add(refresh);
        menuButton.add(clear);
        box.add(menuButton);
        JScrollPane jsp = new JScrollPane(table);
        jsp.setPreferredSize(new Dimension(1053, 500));
        box.add(jsp);
        add(box);
    }
}

class NewInvoice extends JPanel {
    Connection connection;
    final Color c = new Color(0, 0x66, 0x99);

    private void InsertInvoice(Object[] data) {
        try {
            String sql = "insert into invoice_totals values(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, (String) data[0]);
            stmt.setString(2, (String) data[1]);
            stmt.setString(3, (String) data[2]);
            stmt.setString(4, (String) data[3]);
            stmt.setString(5, (String) data[4]);
            stmt.setString(6, (String) data[5]);
            stmt.setString(7, (String) data[6]);
            stmt.setString(8, (String) data[7]);
            stmt.setString(9, (String) data[8]);
            stmt.setDouble(10, (double) data[9]);
            stmt.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Unexpected error", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    private JPanel BuildInputBox(String label, JTextField jtf) {
        JPanel j = new JPanel();
        j.setLayout(new GridLayout(1, 2));
        j.setBackground(c);
        j.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel lblName = new JLabel(label);
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblName.setForeground(Color.white);
        j.add(lblName);
        jtf.setFont(new Font("Tahoma", Font.PLAIN, 20));
        jtf.setColumns(15);
        j.add(jtf);
        return j;
    }

    private void RenderForm() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        setBackground(c);
        Box box = Box.createVerticalBox();

        JPanel jNewInvoice = new JPanel();
        jNewInvoice.setBackground(c);
        jNewInvoice.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel lblNewInvoice = new JLabel("Register New Invoice");
        lblNewInvoice.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        lblNewInvoice.setForeground(Color.white);
        jNewInvoice.add(lblNewInvoice);

        box.add(jNewInvoice);

        JTextField jtfInvoiceID = new JTextField();
        box.add(BuildInputBox("Invoice Number*", jtfInvoiceID));
        JTextField jtfInvoiceDate = new JTextField();
        box.add(BuildInputBox("Invoice Date*", jtfInvoiceDate));
        JTextField jtfInvoiceDuedate = new JTextField();
        box.add(BuildInputBox("Invoice Due Date", jtfInvoiceDuedate));
        JTextField jtfPurchaseOrder = new JTextField();
        box.add(BuildInputBox("Purchase Order", jtfPurchaseOrder));
        JTextField jtfCustomerName = new JTextField();
        box.add(BuildInputBox("Customer Name*", jtfCustomerName));
        JTextField jtfAddressStreet  = new JTextField();
        box.add(BuildInputBox("Bill To Address", jtfAddressStreet ));
        JTextField jtfAddressCity  = new JTextField();
        box.add(BuildInputBox("Bill To City", jtfAddressCity ));
        JTextField jtfAddressState  = new JTextField();
        box.add(BuildInputBox("Bill To State", jtfAddressState ));
        JTextField jtfAddressZipcode  = new JTextField();
        box.add(BuildInputBox("Bill To Zipcode", jtfAddressZipcode ));
        JTextField jtfInvoiceTotal  = new JTextField();
        box.add(BuildInputBox("Invoice Total*", jtfInvoiceTotal ));

        JPanel j = new JPanel();
        j.setLayout(new GridLayout(1, 2));
        j.setBackground(c);
        j.setBorder(new EmptyBorder(30, 25, 15, 25));
        JButton btnNewButton = new JButton("Create");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    InsertInvoice(new Object[] {
                        jtfInvoiceID.getText(),
                        jtfInvoiceDate.getText(),
                        jtfInvoiceDuedate.getText(),
                        jtfPurchaseOrder.getText(),
                        jtfCustomerName.getText(),
                        jtfAddressStreet.getText(),
                        jtfAddressCity.getText(),
                        jtfAddressState.getText(),
                        jtfAddressZipcode.getText(),                        
                        new Double(jtfInvoiceTotal.getText()).doubleValue()
                    });
                    JOptionPane.showMessageDialog(null, "Invoice registered successfully", "Invoice Registered", JOptionPane.INFORMATION_MESSAGE);
                    jtfCustomerName.setText("");
                    jtfInvoiceID.setText("");
                    jtfInvoiceDate.setText("");
                    jtfInvoiceDuedate.setText("");
                    jtfPurchaseOrder.setText("");
                    jtfAddressStreet.setText("");
                    jtfAddressCity.setText("");
                    jtfAddressState.setText("");
                    jtfAddressZipcode.setText("");
                    jtfInvoiceTotal.setText("");                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Unexpected error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
        btnNewButton.setBounds(399, 447, 259, 74);
        j.add(btnNewButton);
        box.add(j);

        add(box);
    }

    NewInvoice(Connection conn) {
        this.connection = conn;
        RenderForm();
    }
}
