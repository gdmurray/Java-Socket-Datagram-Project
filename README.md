## CP372 Assignment 2

#### Handshake Model

When the Sender wants to send a file to the receiver, it first sends out a handshake to the receiver to see if it is on, and if its ready to receive a file.

The handshake is a code that fits into a pattern which the receiver only responds to. The pattern is `<transmission MDS={int bytes value}>`, where {} are not used, but the bytes value instead. 
An example handshake would be `<transmission MDS=1024>`.

The receiver would get this handshake, it would then match the regex pattern of `<transmission MDS=(\\d+)>` and then extract the group and set the buffer to 1024.

Once the Buffer is initialized, the reader will send back the string `SEND`, which indicates that the Sender is clear to send the contents of the file.

The Sender is waiting for this command, and will not send anything until it is received.
#### Datagram Format

The format of the datagram is simple, it reserves 4 bytes at the end of the packet for the sequence number.
This means that the content size of any full block is `MAX_SIZE - 4`.
In a typical block, it will contain <=1020 bytes of data and 4 bytes of sequence information.

