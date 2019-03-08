import javax.swing.JFrame;
import javax.swing.JPanel;
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
import javax.swing.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Sender extends JFrame {
    private JTextField ReceiverPort = new JTextField();
    private JTextField SenderPort = new JTextField();
    private JTextField MaxDataGramSize = new JTextField();
    private JTextField Timeout = new JTextField();
    private JTextField IPAddress;
    private JTextField TotalTransmissionTime;
    private JTextField FileName;
    private static TransferThread activeThread;

    //Default Values when gui not filled
    private final Map<JTextField, Integer> DefaultValueMap = new HashMap<JTextField, Integer>() {{
        put(ReceiverPort, 8080);
        put(SenderPort, 8081);
        put(MaxDataGramSize, 1024);
        put(Timeout, 1000);
    }};

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            Sender sender = new Sender();
            activeThread = new TransferThread(sender);
            activeThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Sender() {
        this.initGUI();

        //Comment this out if you don't want default values
        this.initValues();
    }

    /* a getter for the fields that returns default if not set*/
    private int getValue(JTextField component) {
        if (!(component.getText().isEmpty())) {
            try {
                return Integer.parseInt(component.getText());
            } catch (NumberFormatException e) {
                return DefaultValueMap.get(component);
            }
        } else {
            return DefaultValueMap.get(component);
        }
    }

    private InetAddress getIPAddress() {
        if (!(this.IPAddress.getText().isEmpty())) {
            try {
                return InetAddress.getByName(this.IPAddress.getText());
            } catch (UnknownHostException e) {
                System.out.println("Unknown Host Exception: " + e.toString());
            }
        }

        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Cant get localhost, something is broken");
        }
        return null;
    }

    private void setTotalTime(long time) {
        this.TotalTransmissionTime.setText(Long.toString(time));
    }

    private static byte[] intToBytes(final int a) {
        /*
        Takes an integer and converts to a 4-byte byte array
         */
        return new byte[]{
                (byte) ((a >> 24) & 0xff),
                (byte) ((a >> 16) & 0xff),
                (byte) ((a >> 8) & 0xff),
                (byte) ((a) & 0xff),
        };
    }

    private static int fromByteArray(byte[] bytes) {
        /*
        Takes a 4-byte byte array and converts it into an integer
         */
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    private static byte[] addAll(final byte[] array1, byte[] array2) {
        /*
        Combines two byte arrays to make 1
         */
        byte[] joinedArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }


    private static class TransferThread extends Thread {
        /*
        Thread which facilitates the transfer, long running task could interrupt GUI
         */
        DatagramSocket send;
        Sender sender;
        FileInputStream fis;
        InetAddress address;
        byte[] buf;
        int avail, prev_avail, packet;
        int receiverPort, senderPort, timeout;
        long start, finish;

        private TransferThread(Sender s) {
            /*
            Constructor of Thread, takes sender parameters and binds to thread instance
             */
            this.sender = s;
            this.senderPort = s.getValue(s.SenderPort);
            this.address = sender.getIPAddress();
            this.receiverPort = this.sender.getValue(this.sender.ReceiverPort);
            this.timeout = this.sender.getValue(this.sender.Timeout);

        }

        public void run() {
            /*
            Starts Thread and opens socket for sending
             */
            try {
                System.out.println("Initializing Transfer thread");
                this.send = new DatagramSocket(this.senderPort);
                System.out.println("Transfer Thread Initialized on Port: " + senderPort);
            } catch (SocketException e) {
                System.out.println("Socket Exception: " + e.toString());
            }

        }

        private void transferFile() {
            /*
            This method initiates the transfer of the file
             */
            System.out.println("Transferring File");
            try {

                // Fetches Max Datagram Size to instantiate buffer
                this.buf = new byte[this.sender.getValue(this.sender.MaxDataGramSize)];

                this.fis = new FileInputStream(this.sender.FileName.getText());

                // Create handshake to send to server and get echo response
                String connectString = "<transmission MDS=" + this.sender.getValue(this.sender.MaxDataGramSize) + ">";
                System.out.println("Connect String: " + connectString);

                byte connect[] = connectString.getBytes();

                //Creates connection handshake packet and sends to receiver
                DatagramPacket conn = new DatagramPacket(connect, connect.length, address, receiverPort);
                System.out.println("Sending Handshake");
                this.send.send(conn);

                byte confirm[] = new byte[64];
                DatagramPacket packet = new DatagramPacket(confirm, confirm.length);
                this.send.receive(packet);

                //Check if server Response with SEND, handshake confirmed and can initiate file transmission
                if (new String(
                        packet.getData(), 0, packet.getLength()).equals("SEND")) {
                    System.out.println("HandShake Received, Transmitting File");
                    transmitFile();
                }


            } catch (FileNotFoundException e) {
                System.out.println("File not Found: " + e.toString());
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.toString());
            }
        }

        private void transmitFile() {
            /*
            This method facilitates the sending of the active file
             */
            try {
                System.out.println("Attempting to Transmit File");

                this.start = System.currentTimeMillis();

                //Send First Chunk
                this.packet = 0;
                int activePacket = 0;
                this.prev_avail = this.fis.available();
                int activeAck;

                sendChunk();

                //Loop from the top
                while (true) {
                    try {
                        //Receive Ack
                        activeAck = receiveAck();

                        System.out.println("Active Returned Ack :" + activeAck);

                        //If Ack is current packet;
                        // Send the chunk again and wait until activeAck
                        activePacket = this.packet;
                        sendChunk();

                    } catch (SocketTimeoutException st) {
                        System.out.println("Socket Timed out at Frame: " + activePacket);
                        reTransmitChunk(activePacket);
                    } catch (EOFTransmission eof) {
                        System.out.println("Eof from within While Loop");
                        endTransmission();
                        break;
                    }
                }

            } catch (SocketException e) {
                System.out.println("Socket Exception?: " + e.toString());
                endTransmission();
            } catch (IOException e) {
                System.out.println("IO Exception in Transmit: " + e.toString());
                endTransmission();
            } catch (EOFTransmission eof) {
                endTransmission();
            }
        }

        private int receiveAck() throws SocketTimeoutException, IOException {
            /*
            This function receives the Ack and throws a timeout
             */
            // Set timeout before receiving ack
            this.send.setSoTimeout(this.timeout);

            byte ack_buffer[] = new byte[4];
            DatagramPacket ack_packet = new DatagramPacket(ack_buffer, ack_buffer.length);

            this.send.receive(ack_packet);

            // Once ack received, set timeout to 0
            this.send.setSoTimeout(0);

            return fromByteArray(ack_buffer);
        }

        private DatagramPacket createChunk(byte[] seq, byte[] buffer, int av, int pre_av) {
            /*
            Builds the datagram chunk from the buffer and sequence number
             */
            DatagramPacket pkg;
            if (av == 0) {
                // Combines the bytes into one array consisting of content + sequence
                byte eo_buf[] = addAll(Arrays.copyOfRange(buffer, 0, pre_av), seq);
                pkg = new DatagramPacket(eo_buf, eo_buf.length, address, receiverPort);
            } else {
                pkg = new DatagramPacket(buffer, buffer.length, address, receiverPort);
                for (int j = 0; j < 4; j++) {
                    buffer[buffer.length - (4 - j)] = seq[j];
                }
            }
            return pkg;
        }

        private void reTransmitChunk(int i) {
            /*
            When a transfer timed out, retransmit that chunk from the given sequence.
             */
            byte[] tmpbuf = new byte[this.sender.getValue(this.sender.MaxDataGramSize)];
            try {
                // Since the file input stream reads the file in a forward motion
                // we need to re-open the file and skip to that section in the file
                System.out.println("Retransmitting Chunk: " + i);
                FileInputStream bis = new FileInputStream(this.sender.FileName.getText());

                System.out.println("Offset: " + (i * (tmpbuf.length - 4)));

                // Skip to part in file that is where the data segment would begin
                bis.skip((long) (i * (tmpbuf.length - 4)));
                int len = bis.read(tmpbuf, 0, tmpbuf.length - 4);
                byte[] seq = (intToBytes(i));

                //System.out.println("Retransmitted Data for Chunk: " + i);
                //System.out.println(new String(tmpbuf, 0, (tmpbuf.length - 4)));
                //If this isnt end of file
                if (len != -1) {
                    DatagramPacket pkg = createChunk(seq, tmpbuf, this.avail, this.prev_avail);
                    this.send.send(pkg);
                    System.out.println("Retransmitted chunk");
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found exception: " + e.toString());
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.toString());
            }

        }

        private void sendChunk() throws IOException, EOFTransmission {
            /*
            Transmits the next chunk in the file sending process
             */
            int len = this.fis.read(this.buf, 0, this.buf.length - 4);

            //How many bytes are available in the file
            this.avail = fis.available();

            System.out.println("SendChunk: Len: " + len);
            byte[] seq = (intToBytes(this.packet));

            //System.out.println("Current vs Previous Avail: " + this.avail + " , " + this.prev_avail);
            //If it isnt the end of the file
            if (len != -1) {
                DatagramPacket pkg = createChunk(seq, this.buf, this.avail, this.prev_avail);
                this.send.send(pkg);
                this.prev_avail = fis.available();
                this.packet++;
            } else {
                throw new EOFTransmission("End of File Reached");
            }
        }

        private void endTransmission() {
            /*
            Ends the transmission with an EOF request
             */

            byte[] endbuff = "END".getBytes();
            DatagramPacket endpkg = new DatagramPacket(endbuff, endbuff.length, this.address, this.receiverPort);
            try {
                this.send.send(endpkg);
                //Confirm end of processing and report time
                this.finish = System.currentTimeMillis();
                this.sender.setTotalTime((finish - start));
            } catch (IOException e) {
                System.out.println("End Transmission IO Exception: " + e.toString());
            }
        }


        public class EOFTransmission extends Exception {
            /*
            Custom Exception class to alert the event loop that the end of the file has been reached
             */
            private EOFTransmission(String message) {
                super(message);
            }

            public EOFTransmission(String message, Throwable throwable) {
                super(message, throwable);
            }

        }

    }

    private void addItem(JPanel p, JComponent c, int x, int y, int width, int height, int align) {
        /*
        Condense arbitrary elements
         */
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


    private void transfer() {
        activeThread.transferFile();
    }

    /**
     * Create the frame.
     */
    private void initGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.TotalTransmissionTime = new JTextField();
        this.IPAddress = new JTextField();
        this.FileName = new JTextField();
        JButton btnTransfer;

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        JLabel ipLabel = new JLabel("IP Address");
        addItem(contentPane, ipLabel, 0, 0, 1, 1, GridBagConstraints.WEST);

        IPAddress.setText("Enter IP Address");
        IPAddress.setColumns(20);
        addItem(contentPane, IPAddress, 1, 0, 2, 1, GridBagConstraints.WEST);

        JLabel lblUdpReceive = new JLabel("Receiver Port");
        addItem(contentPane, lblUdpReceive, 3, 0, 1, 1, GridBagConstraints.WEST);

        ReceiverPort.setColumns(10);
        addItem(contentPane, ReceiverPort, 4, 0, 1, 1, GridBagConstraints.WEST);

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

        MaxDataGramSize.setColumns(10);
        addItem(contentPane, MaxDataGramSize, 1, 2, 1, 1, GridBagConstraints.WEST);

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
                transfer();
            }
        });
        this.add(contentPane);
        this.pack();
        this.setVisible(true);
    }

    private void initValues() {
        /*
        For testing purposes to fill default values
         */
        try {
            IPAddress.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        ReceiverPort.setText(Integer.toString(this.getValue(this.ReceiverPort)));
        SenderPort.setText(Integer.toString(this.getValue(this.SenderPort)));
        MaxDataGramSize.setText(Integer.toString(this.getValue(this.MaxDataGramSize)));
        Timeout.setText(Integer.toString(this.getValue(this.Timeout)));
        FileName.setText("file.txt");
    }


}
