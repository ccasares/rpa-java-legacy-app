package com.mulesoft.rpa.workshop;

import org.apache.commons.cli.*;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class UserLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;

    public UserLogin(Object[] params) {
        super("NTO Login");
        setIconImage(getToolkit().getImage(getClass().getResource("nto_icon.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(423, 13, 273, 93);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(481, 170, 281, 68);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(545, 392, 162, 73);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserMenu um = new UserMenu(params);
                setVisible(false);
                um.setSize(1100, 800);
                um.setLocationRelativeTo(null);
                um.setResizable(false);
                um.setVisible(true);
            }
        });

        contentPane.add(btnNewButton);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);
    }

    public static void main(String[] args) {

        Options options = new Options();

        Option dnhost = new Option("h", "dbhost", true, "DB host:port");
        dnhost.setRequired(true);
        options.addOption(dnhost);

        Option dbname = new Option("d", "dbname", true, "DB name");
        dbname.setRequired(true);
        options.addOption(dbname);

        Option dbuser = new Option("u", "dbuser", true, "DB username");
        dbuser.setRequired(true);
        options.addOption(dbuser);

        Option dbpassword = new Option("p", "dbpassword", true, "DB password");
        dbpassword.setRequired(true);
        options.addOption(dbpassword);

        Option mode = new Option("m", "mode", true, "Execution mode (normal|extended)");
        mode.setRequired(true);
        options.addOption(mode);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null; //not a good practice, it serves its purpose

        try {
            cmd = parser.parse(options, args);
            if (cmd.getOptionValue("mode").toLowerCase().compareToIgnoreCase("normal") != 0 &&
                cmd.getOptionValue("mode").toLowerCase().compareToIgnoreCase("extended") != 0
                ) {
                throw new ParseException("Invalid mode value. Valid ones are 'normal' and 'extended'");
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(" ", options);
            System.exit(1);
        }

        Object[] params = {cmd.getOptionValue("dbhost"), cmd.getOptionValue("dbname"), cmd.getOptionValue("dbuser"), cmd.getOptionValue("dbpassword"), cmd.getOptionValue("mode") };

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserLogin frame = new UserLogin(params);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}