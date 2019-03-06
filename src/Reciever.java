import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JSeparator;

public class Reciever extends JFrame {

	private JPanel contentPane;
	private JTextField txtEnterIpAddress;
	private JTextField txtEnterPort;
	private JTextField txtEnterPort_1;
	private JTextField txtEnterNameOf;
	private JTextField textField;

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

	/**
	 * Create the frame.
	 */
	public Reciever() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 547, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		GridBagConstraints gbc_lblIpAddress = new GridBagConstraints();
		gbc_lblIpAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblIpAddress.anchor = GridBagConstraints.WEST;
		gbc_lblIpAddress.gridx = 0;
		gbc_lblIpAddress.gridy = 0;
		contentPane.add(lblIpAddress, gbc_lblIpAddress);
		
		txtEnterIpAddress = new JTextField();
		txtEnterIpAddress.setText("Enter IP Address");
		GridBagConstraints gbc_txtEnterIpAddress = new GridBagConstraints();
		gbc_txtEnterIpAddress.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterIpAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterIpAddress.gridx = 1;
		gbc_txtEnterIpAddress.gridy = 0;
		contentPane.add(txtEnterIpAddress, gbc_txtEnterIpAddress);
		txtEnterIpAddress.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 2;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 1;
		contentPane.add(separator_1, gbc_separator_1);
		
		JLabel lblUdpReceive = new JLabel("Sender Port Number\r\n");
		GridBagConstraints gbc_lblUdpReceive = new GridBagConstraints();
		gbc_lblUdpReceive.anchor = GridBagConstraints.WEST;
		gbc_lblUdpReceive.insets = new Insets(0, 0, 5, 5);
		gbc_lblUdpReceive.gridx = 0;
		gbc_lblUdpReceive.gridy = 2;
		contentPane.add(lblUdpReceive, gbc_lblUdpReceive);
		
		txtEnterPort = new JTextField();
		txtEnterPort.setText("Enter Port # Used by Sender");
		GridBagConstraints gbc_txtEnterPort = new GridBagConstraints();
		gbc_txtEnterPort.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterPort.gridx = 1;
		gbc_txtEnterPort.gridy = 2;
		contentPane.add(txtEnterPort, gbc_txtEnterPort);
		txtEnterPort.setColumns(10);
		
		JLabel lblReceiverPortNumber = new JLabel("Receiver Port Number");
		GridBagConstraints gbc_lblReceiverPortNumber = new GridBagConstraints();
		gbc_lblReceiverPortNumber.anchor = GridBagConstraints.WEST;
		gbc_lblReceiverPortNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblReceiverPortNumber.gridx = 0;
		gbc_lblReceiverPortNumber.gridy = 3;
		contentPane.add(lblReceiverPortNumber, gbc_lblReceiverPortNumber);
		
		txtEnterPort_1 = new JTextField();
		txtEnterPort_1.setText("Enter Port # Used by Receiver");
		GridBagConstraints gbc_txtEnterPort_1 = new GridBagConstraints();
		gbc_txtEnterPort_1.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterPort_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterPort_1.gridx = 1;
		gbc_txtEnterPort_1.gridy = 3;
		contentPane.add(txtEnterPort_1, gbc_txtEnterPort_1);
		txtEnterPort_1.setColumns(10);
		
		JLabel lblTransferFileName = new JLabel("File Name");
		GridBagConstraints gbc_lblTransferFileName = new GridBagConstraints();
		gbc_lblTransferFileName.anchor = GridBagConstraints.WEST;
		gbc_lblTransferFileName.insets = new Insets(0, 0, 5, 5);
		gbc_lblTransferFileName.gridx = 0;
		gbc_lblTransferFileName.gridy = 5;
		contentPane.add(lblTransferFileName, gbc_lblTransferFileName);
		
		txtEnterNameOf = new JTextField();
		txtEnterNameOf.setText("Enter Name of File to Write Received Data\r\n");
		GridBagConstraints gbc_txtEnterNameOf = new GridBagConstraints();
		gbc_txtEnterNameOf.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterNameOf.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterNameOf.gridx = 1;
		gbc_txtEnterNameOf.gridy = 5;
		contentPane.add(txtEnterNameOf, gbc_txtEnterNameOf);
		txtEnterNameOf.setColumns(10);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 2;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 6;
		contentPane.add(separator, gbc_separator);
		
		JLabel lblReceivedInorderPackets = new JLabel("Received In-order Packets");
		GridBagConstraints gbc_lblReceivedInorderPackets = new GridBagConstraints();
		gbc_lblReceivedInorderPackets.anchor = GridBagConstraints.EAST;
		gbc_lblReceivedInorderPackets.insets = new Insets(0, 0, 0, 5);
		gbc_lblReceivedInorderPackets.gridx = 0;
		gbc_lblReceivedInorderPackets.gridy = 7;
		contentPane.add(lblReceivedInorderPackets, gbc_lblReceivedInorderPackets);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 7;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
	}

}
