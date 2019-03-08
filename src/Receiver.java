import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Receiver extends JFrame {
    //private static ReceiverThread activeThread;
    private JTextField IPAddress;
    private JTextField SenderPort;
    private JTextField ReceiverPort;
    private JTextField FileName;
    private JTextField ReceivedPackets;
    private JToggleButton isReliable;

    //Map of the default values if no GUI value provided
    private final Map<JComponent, Integer> DefaultValueMap = new HashMap<JComponent, Integer>() {{
        put(ReceiverPort, 8080);
        put(SenderPort, 8081);
    }};

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            Receiver receiver = new Receiver();
            ReceiverThread activeThread = new ReceiverThread(receiver);
            activeThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Receiver() {
        initGUI();
        initValues();
    }

    private int getValue(JTextField component) {
        /*
        Fetches the value for the given field.
        If GUI is empty or cant be parsed, uses default value
         */
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
        /*
        Fetches IP Address from Client.
        Gives the default localhost if none provided
         */
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

    private boolean isReliable() {
        /*
        Whether the Receiver is in Reliable Mode or Not
         */
        return (this.isReliable.isSelected());
    }

    private void setReceivedPackets(int packets) {
        this.ReceivedPackets.setText(Integer.toString(packets));
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

    public static class ReceiverThread extends Thread {
        int receiverPort, senderPort;
        int inOrderPackets;
        int bufferSize = 1024; //default buffer size
        int activeAck = 0;

        Receiver receiver;
        DatagramSocket receive;

        // Regex Pattern for Matching in receiver
        String handshakePattern = "<transmission MDS=(\\d+)>";
        Pattern p = Pattern.compile(handshakePattern);

        private ReceiverThread(Receiver r) {
            /*
            Constructor of Thread, takes receiver parameters and binds to thread instance
             */
            this.receiver = r;
            this.receiverPort = r.getValue(r.ReceiverPort);
            this.senderPort = r.getValue(r.SenderPort);
        }

        public void run() {
            /*
            Main Event Loop for Receiver.
            Listens for a handshake value matching defined pattern
            Extracts the buffer size from the handshake
            Then Listens for file packets until end of file transmission is received
             */
            try {
                System.out.println("Opening Datagram Socket to receive Files.");
                // Start Receiver thread and create socket

                this.receive = new DatagramSocket(receiverPort);

                System.out.println("Listening on port: " + receiverPort);

                byte[] listenerBuf = new byte[1024];
                DatagramPacket pkg = new DatagramPacket(listenerBuf, listenerBuf.length);

                while (true) {
                    try {
                        //Listen for Handshake
                        receive.receive(pkg);
                        String recv = new String(pkg.getData(), 0, pkg.getLength());
                        System.out.println("Received Packet: " + recv);

                        Map<Integer, byte[]> FileMap = new HashMap<Integer, byte[]>();

                        //If handshake matches handshake pattern with the byte size, then start
                        if (recv.matches(handshakePattern)) {
                            System.out.println("Regex Matches");
                            Matcher m = p.matcher(recv);
                            if (m.find()) {
                                String mbs = m.group(1);
                                this.bufferSize = Integer.parseInt(mbs);
                                System.out.println("Pattern Matches and found buffer: " + this.bufferSize);
                            }
                            // Send handshake
                            System.out.println("Sending Handshake");
                            this.handshake();

                            //New file
                            File newfile = new File(this.receiver.FileName.getText());
                            FileOutputStream bos = new FileOutputStream(newfile);

                            //Set buffer to size defined in handshake
                            byte[] buf = new byte[this.bufferSize];
                            DatagramPacket packet = new DatagramPacket(buf, buf.length);

                            receive.receive(packet);
                            int packetsReceived = 0, previous_seq = 0, current_seq;
                            this.inOrderPackets = 0;

                            //Loop until End Transmission is Received
                            while (!(new String(packet.getData(), 0, packet.getLength()).equals("END"))) {

                                //Extract content from payload, last 4-bytes are always sequence number
                                byte[] content = Arrays.copyOfRange(packet.getData(), 0, packet.getLength() - 4);
                                byte[] seq = Arrays.copyOfRange(packet.getData(), packet.getLength() - 4, packet.getLength());
                                current_seq = fromByteArray(seq);

                                //Check if chunk has been received or not
                                if (!(FileMap.containsKey(current_seq))) {
                                    FileMap.put(current_seq, content);
                                    packetsReceived++;

                                    // If reliable mode is off, and packets received is every 10th, drop it.
                                    if (!(this.receiver.isReliable()) && (packetsReceived % 10 == 0)) {
                                        System.out.println("In Unreliable Mode, dropping this packet: " + current_seq);
                                        System.out.println("Content of Dropped Packet");
                                        System.out.println(new String(content, 0, content.length));
                                        current_seq = previous_seq;
                                    } else {
                                        System.out.println("Sending ack: " + (this.activeAck));
                                        sendAck();

                                    }
                                } else {
                                    // Duplicate, send Ack to acknowledge we have it
                                    sendAck();
                                }

                                if ((current_seq - previous_seq) == 1) {
                                    inOrderPackets++;
                                }

                                System.out.println("Sequence #" + fromByteArray(seq) + " Length: " + content.length);
                                previous_seq = current_seq;

                                //Get Next Packet
                                receive.receive(packet);
                            }

                            buildFile(FileMap, bos);
                            System.out.println("End of Transmission Received");
                            this.receiver.setReceivedPackets(inOrderPackets);

                        } else {
                            System.out.println("Request does not match the pattern");
                        }

                    } catch (IOException e) {
                        System.out.println("IOException: " + e.toString());
                    }
                }

            } catch (SocketException e) {
                System.out.println("Socket Exception: " + e.toString());
            }
        }

        private void sendAck() {
            /*
            Sends ack to server
             */
            if (this.activeAck == 0) {
                this.activeAck = 1;
            } else {
                this.activeAck = 0;
            }
            byte[] ack = intToBytes(this.activeAck);

            DatagramPacket pkg = new DatagramPacket(ack, ack.length, this.receiver.getIPAddress(), this.senderPort);

            try {
                this.receive.send(pkg);
            } catch (IOException e) {
                System.out.println("Experienced an IOException sending Ack: " + e.toString());
            }

        }

        private void buildFile(Map<Integer, byte[]> map, FileOutputStream bos) {
            /*
            Builds the output file
             */
            System.out.println("Writing to File");
            List sortedKeys = new ArrayList(map.keySet());
            Collections.sort(sortedKeys);
            try {
                for (int l = 0; l < sortedKeys.size(); l++) {
                    bos.write(map.get(sortedKeys.get(l)));
                }
            } catch (IOException e) {
                System.out.println("IO Exception when writing to file");
            }

        }

        private void handshake() {
            /*
            Sends a handshake back to the server to initiate file transfer
             */
            byte[] confirm = "SEND".getBytes();
            DatagramPacket responsePkg = new DatagramPacket(confirm, confirm.length, this.receiver.getIPAddress(), this.senderPort);

            try {
                this.receive.send(responsePkg);
                System.out.println("Sent Handshake");
            } catch (IOException e) {
                System.out.println("Error Handshaking");
            }
        }
    }

    private void initGUI() {
        IPAddress = new JTextField();
        SenderPort = new JTextField();
        ReceiverPort = new JTextField();
        FileName = new JTextField();
        ReceivedPackets = new JTextField();
        isReliable = new JCheckBox("Reliable", true);

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

        addItem(contentPane, isReliable, 3, 1, 1, 1, GridBagConstraints.WEST);

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

    // Used for testing purposes to insert values into the fields
    private void initValues() {
        try {
            IPAddress.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Unknown host in initValues: " + e.toString());
        }
        ReceiverPort.setText("8080");
        SenderPort.setText("8081");
        FileName.setText("newfile.txt");

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

}
