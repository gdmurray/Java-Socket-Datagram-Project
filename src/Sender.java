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

public class Sender extends JFrame {

	private JPanel contentPane;
	private JTextField ipTxtField;
	private JLabel lblUdpReceive;
	private JLabel lblUdpSend;
	private JLabel lblFileName;
	private JTextField txtEnterPort;
	private JTextField txtEnterPort_1;
	private JTextField txtEnterTransferFile;
	private JLabel lblDatagramMaxSize;
	private JTextField txtEnterMaxSize;
	private JTextField textField;
	private JLabel lblTotalTransmissionTime;
	private JButton btnTransfer;
	private JLabel lblTimeout;
	private JTextField textField_1;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sender frame = new Sender();
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
	public Sender() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 577, 515);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel ipLabel = new JLabel("IP Address");
		GridBagConstraints gbc_ipLabel = new GridBagConstraints();
		gbc_ipLabel.insets = new Insets(0, 0, 5, 5);
		gbc_ipLabel.anchor = GridBagConstraints.WEST;
		gbc_ipLabel.gridx = 0;
		gbc_ipLabel.gridy = 1;
		contentPane.add(ipLabel, gbc_ipLabel);
		
		ipTxtField = new JTextField();
		ipTxtField.setText("Enter IP Address");
		GridBagConstraints gbc_ipTxtField = new GridBagConstraints();
		gbc_ipTxtField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ipTxtField.insets = new Insets(0, 0, 5, 0);
		gbc_ipTxtField.gridx = 1;
		gbc_ipTxtField.gridy = 1;
		contentPane.add(ipTxtField, gbc_ipTxtField);
		ipTxtField.setColumns(10);
		
		separator_2 = new JSeparator();
		GridBagConstraints gbc_separator_2 = new GridBagConstraints();
		gbc_separator_2.insets = new Insets(0, 0, 5, 5);
		gbc_separator_2.gridx = 0;
		gbc_separator_2.gridy = 2;
		contentPane.add(separator_2, gbc_separator_2);
		
		lblUdpReceive = new JLabel("Receiver Port Number");
		GridBagConstraints gbc_lblUdpReceive = new GridBagConstraints();
		gbc_lblUdpReceive.anchor = GridBagConstraints.WEST;
		gbc_lblUdpReceive.insets = new Insets(0, 0, 5, 5);
		gbc_lblUdpReceive.gridx = 0;
		gbc_lblUdpReceive.gridy = 3;
		contentPane.add(lblUdpReceive, gbc_lblUdpReceive);
		
		txtEnterPort = new JTextField();
		txtEnterPort.setText("Enter Port # of the Receiver");
		GridBagConstraints gbc_txtEnterPort = new GridBagConstraints();
		gbc_txtEnterPort.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterPort.gridx = 1;
		gbc_txtEnterPort.gridy = 3;
		contentPane.add(txtEnterPort, gbc_txtEnterPort);
		txtEnterPort.setColumns(10);
		
		lblUdpSend = new JLabel("Sender Port Number\r\n");
		GridBagConstraints gbc_lblUdpSend = new GridBagConstraints();
		gbc_lblUdpSend.anchor = GridBagConstraints.WEST;
		gbc_lblUdpSend.insets = new Insets(0, 0, 5, 5);
		gbc_lblUdpSend.gridx = 0;
		gbc_lblUdpSend.gridy = 4;
		contentPane.add(lblUdpSend, gbc_lblUdpSend);
		
		txtEnterPort_1 = new JTextField();
		txtEnterPort_1.setText("Enter Port # of the Sender");
		GridBagConstraints gbc_txtEnterPort_1 = new GridBagConstraints();
		gbc_txtEnterPort_1.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterPort_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterPort_1.gridx = 1;
		gbc_txtEnterPort_1.gridy = 4;
		contentPane.add(txtEnterPort_1, gbc_txtEnterPort_1);
		txtEnterPort_1.setColumns(10);
		
		lblFileName = new JLabel("File Name");
		GridBagConstraints gbc_lblFileName = new GridBagConstraints();
		gbc_lblFileName.anchor = GridBagConstraints.WEST;
		gbc_lblFileName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFileName.gridx = 0;
		gbc_lblFileName.gridy = 5;
		contentPane.add(lblFileName, gbc_lblFileName);
		
		txtEnterTransferFile = new JTextField();
		txtEnterTransferFile.setText("Enter Transfer File Name");
		GridBagConstraints gbc_txtEnterTransferFile = new GridBagConstraints();
		gbc_txtEnterTransferFile.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterTransferFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterTransferFile.gridx = 1;
		gbc_txtEnterTransferFile.gridy = 5;
		contentPane.add(txtEnterTransferFile, gbc_txtEnterTransferFile);
		txtEnterTransferFile.setColumns(10);
		
		lblDatagramMaxSize = new JLabel("Datagram Max Size");
		GridBagConstraints gbc_lblDatagramMaxSize = new GridBagConstraints();
		gbc_lblDatagramMaxSize.anchor = GridBagConstraints.WEST;
		gbc_lblDatagramMaxSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatagramMaxSize.gridx = 0;
		gbc_lblDatagramMaxSize.gridy = 6;
		contentPane.add(lblDatagramMaxSize, gbc_lblDatagramMaxSize);
		
		txtEnterMaxSize = new JTextField();
		txtEnterMaxSize.setText("Enter Max Size of Datagram");
		GridBagConstraints gbc_txtEnterMaxSize = new GridBagConstraints();
		gbc_txtEnterMaxSize.insets = new Insets(0, 0, 5, 0);
		gbc_txtEnterMaxSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterMaxSize.gridx = 1;
		gbc_txtEnterMaxSize.gridy = 6;
		contentPane.add(txtEnterMaxSize, gbc_txtEnterMaxSize);
		txtEnterMaxSize.setColumns(10);
		
		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridheight = 2;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 7;
		contentPane.add(separator, gbc_separator);
		
		separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 9;
		contentPane.add(separator_1, gbc_separator_1);
		
		lblTimeout = new JLabel("Timeout");
		GridBagConstraints gbc_lblTimeout = new GridBagConstraints();
		gbc_lblTimeout.anchor = GridBagConstraints.WEST;
		gbc_lblTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_lblTimeout.gridx = 0;
		gbc_lblTimeout.gridy = 10;
		contentPane.add(lblTimeout, gbc_lblTimeout);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 10;
		contentPane.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		lblTotalTransmissionTime = new JLabel("Total Transmission Time");
		GridBagConstraints gbc_lblTotalTransmissionTime = new GridBagConstraints();
		gbc_lblTotalTransmissionTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalTransmissionTime.anchor = GridBagConstraints.EAST;
		gbc_lblTotalTransmissionTime.gridx = 0;
		gbc_lblTotalTransmissionTime.gridy = 11;
		contentPane.add(lblTotalTransmissionTime, gbc_lblTotalTransmissionTime);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 11;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		
		btnTransfer = new JButton("Transfer");
		btnTransfer.setForeground(Color.DARK_GRAY);
		btnTransfer.setFont(new Font("Tahoma", Font.BOLD, 21));
		GridBagConstraints gbc_btnTransfer = new GridBagConstraints();
		gbc_btnTransfer.anchor = GridBagConstraints.WEST;
		gbc_btnTransfer.gridx = 1;
		gbc_btnTransfer.gridy = 12;
		contentPane.add(btnTransfer, gbc_btnTransfer);
	}

}
