package org.usfirst.frc.team4729.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 */
public class UDPSubsystem extends Subsystem {
	byte[] buffer;
	DatagramSocket dsocket;
	DatagramPacket packet;
	public UDPSubsystem(int port) {
		SmartDashboard.putString("connection error", "None");
      // Create a socket to listen on the port.
      try {
		dsocket = new DatagramSocket(port);
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		SmartDashboard.putString("connection error", "Socket creation failed");
	}

      // Create a buffer to read datagrams into. If a
      // packet is larger than this buffer, the
      // excess will simply be discarded!
      buffer = new byte[2048];

      // Create a packet to receive data into the buffer
      packet = new DatagramPacket(buffer, buffer.length);
	}
	
	public void receivePacket() {
		try {
			dsocket.receive(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			SmartDashboard.putString("connection error", "Receive packet failed");
		}

        // Convert the contents to a string, and display them
        String msg = new String(buffer, 0, packet.getLength());
        SmartDashboard.putString("packet", packet.getAddress().getHostName() + ": "
            + msg);

        // Reset the length of the packet before reusing it.
        packet.setLength(buffer.length);
	}
	
	public void sendPacket(String msg, String address) {
		try {
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName(address), 1927);
			dsocket.send(sendPacket);
		} catch (IOException e) {
			SmartDashboard.putString("connection error", "sending packet failed");
		}
		
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

