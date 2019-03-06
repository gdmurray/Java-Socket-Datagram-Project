import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Reciever extends JFrame {

    private JTextField IPAddress;
    private JTextField SenderPort;
    private JTextField ReceiverPort;
    private JTextField FileName;
    private JTextField ReceivedPackets;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Reciever frame = new Reciever();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    /**
     * Create the frame.
     */
    private void initGUI() {
        IPAddress = new JTextField();
        SenderPort = new JTextField();
        ReceiverPort = new JTextField();
        FileName = new JTextField();
        ReceivedPackets = new JTextField();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        JLabel lblIpAddress = new JLabel("IP Address");
        addItem(contentPane, lblIpAddress, 0, 0, 1, 1, GridBagConstraints.WEST);

        IPAddress.setText("Enter IP Address");
        IPAddress.setColumns(20);
        addItem(contentPane, IPAddress, 1, 0, 2, 1, GridBagConstraints.WEST);

        JLabel lblUdpReceive = new JLabel("Sender Port Number");
        addItem(contentPane, lblUdpReceive, 3, 0, 1, 1, GridBagConstraints.WEST);

        SenderPort.setColumns(10);
        addItem(contentPane, SenderPort, 4, 0, 1, 1, GridBagConstraints.WEST);

        JLabel lblReceiverPortNumber = new JLabel("Receiver Port Number");
        addItem(contentPane, lblReceiverPortNumber, 0, 1, 1, 1, GridBagConstraints.WEST);

        ReceiverPort.setColumns(10);
        addItem(contentPane, ReceiverPort, 1, 1, 1, 1, GridBagConstraints.WEST);

        JLabel lblTransferFileName = new JLabel("File Name");
        addItem(contentPane, lblTransferFileName, 0, 2, 1, 1, GridBagConstraints.WEST);

        FileName.setColumns(20);
        addItem(contentPane, FileName, 1, 2, 2, 1, GridBagConstraints.WEST);

        JLabel lblReceivedInorderPackets = new JLabel("Received In-order Packets");
        addItem(contentPane, lblReceivedInorderPackets, 0, 3, 1, 1, GridBagConstraints.WEST);

        ReceivedPackets.setColumns(10);
        addItem(contentPane, ReceivedPackets, 1, 3, 1, 1, GridBagConstraints.WEST);


        this.add(contentPane);
        this.pack();
        this.setVisible(true);
    }

    public Reciever() {
        initGUI();
    }

}
