import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JSeparator;
import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.sql.Time;

public class Sender extends JFrame {

    private JTextField IPAddress;
    private JTextField ReceiverPort;
    private JTextField SenderPort;
    private JTextField FileName;
    private JTextField MaxFileSize;
    private JTextField Timeout;
    private JTextField TotalTransmissionTime;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Sender frame = new Sender();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */


    public Sender() {

        this.initGUI();
    }

    private void sendFile() throws Exception {
        byte b[] = new byte[1024];
        FileInputStream f = new FileInputStream(FileName.getText());

    }

    private void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = 100.0;
        gc.weighty = 100.0;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = align;
        gc.fill = GridBagConstraints.NONE;
        p.add(c, gc);
    }

    private void initGUI() {
        IPAddress = new JTextField();
        SenderPort = new JTextField();
        ReceiverPort = new JTextField();
        FileName = new JTextField();
        Timeout = new JTextField();
        TotalTransmissionTime = new JTextField();

        JButton btnTransfer;


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        JLabel ipLabel = new JLabel("IP Address");
        addItem(contentPane, ipLabel, 0, 0, 1, 1, GridBagConstraints.WEST);


        IPAddress.setText("Enter IP Address");
        IPAddress.setColumns(20);
        addItem(contentPane, IPAddress, 1, 0, 2, 1, GridBagConstraints.WEST);

        JLabel lblUdpReceive = new JLabel("Receiver Port");
        addItem(contentPane, lblUdpReceive, 3, 0, 1, 1, GridBagConstraints.WEST);

        //ReceiverPort.setText("Enter Port # of the Receiver");
        ReceiverPort.setColumns(10);
        addItem(contentPane, ReceiverPort, 4, 0, 1, 1, GridBagConstraints.WEST);


        //SenderPort.setText("Enter Port # of the Sender");
        //SenderPort.setColumns(10);

        JLabel lblFileName = new JLabel("File Name");
        addItem(contentPane, lblFileName, 0, 1, 1, 1, GridBagConstraints.WEST);

        FileName.setText("File Name");
        FileName.setColumns(20);
        addItem(contentPane, FileName, 1, 1, 2, 1, GridBagConstraints.WEST);

        JLabel lblUdpSend = new JLabel("Sender Port");
        addItem(contentPane, lblUdpSend, 3, 1, 1, 1, GridBagConstraints.WEST);

        SenderPort.setColumns(10);
        addItem(contentPane, SenderPort, 4, 1, 1, 1, GridBagConstraints.WEST);

        JLabel lblDatagramMaxSize = new JLabel("Datagram Max Size");
        addItem(contentPane, lblDatagramMaxSize, 0, 2, 1, 1, GridBagConstraints.WEST);

        MaxFileSize = new JTextField();
        //MaxFileSize.setText("Max Size");
        MaxFileSize.setColumns(10);
        addItem(contentPane, MaxFileSize, 1, 2, 1, 1, GridBagConstraints.WEST);

        JLabel lblTimeout = new JLabel("Timeout");
        addItem(contentPane, lblTimeout, 3, 2, 1, 1, GridBagConstraints.WEST);

        Timeout.setColumns(10);
        addItem(contentPane, Timeout, 4, 2, 1, 1, GridBagConstraints.WEST);

        JLabel lblTotalTransmissionTime = new JLabel("Total Transmission Time");
        addItem(contentPane, lblTotalTransmissionTime, 0, 3, 1, 1, GridBagConstraints.WEST);

        TotalTransmissionTime.setColumns(10);
        addItem(contentPane, TotalTransmissionTime, 1, 3, 1, 1, GridBagConstraints.WEST);

        btnTransfer = new JButton("Transfer");
        btnTransfer.setForeground(Color.DARK_GRAY);
        btnTransfer.setFont(new Font("Tahoma", Font.BOLD, 21));
        addItem(contentPane, btnTransfer, 2, 4, 1, 1, GridBagConstraints.WEST);

        btnTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        this.add(contentPane);
        this.pack();
        this.setVisible(true);
    }

}
