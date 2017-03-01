package org.usfirst.frc.team4729.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.usfirst.frc.team4729.robot.Robot;

import java.io.*;
import java.net.*;

/**
 *
 */
public class TCPSubsystem extends Subsystem {
	ServerSocket serverSocket;
	Socket connectionSocket;
	public TCPSubsystem(int port) {
		if (Robot.nucConnected){
			SmartDashboard.putString("connection error", "None");
			// Create a socket to listen on the port.
			try {
				serverSocket = new ServerSocket(port);
				connectionSocket = serverSocket.accept();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				SmartDashboard.putString("connection error", "Socket creation failed");
			}
		}
	}

	public String receivePacket() {
		if (Robot.nucConnected) {
			try {
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
				String clientSentence = inFromClient.readLine();
				return clientSentence;

			} catch (IOException e) {
				SmartDashboard.putString("connection error", "error reicing packet");
				return null;
			}
		} else {
			SmartDashboard.putString("connection error", "def not using nuc");
			return null;
		}
	}

	public void sendPacket(String msg) {
		SmartDashboard.putString("blah", "blah");

		if (Robot.nucConnected) {
			try {
				DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
				outToClient.writeBytes(msg);
			} catch (IOException e) {
				SmartDashboard.putString("connection error", "error sending packet");
			}
		} else {
			SmartDashboard.putString("connection error", "not using nuc");
		}
	}

	public float[] requestImageData() {
		Robot.tcpSubsystem.sendPacket("ball\n");
    	String message = Robot.tcpSubsystem.receivePacket();
    	if (message.contains(":")) {
	    	String[] stringData = message.split(":");
	    	float[] data = new float[2];
				//data[0] is the distance, data[1] is the x position
	    	data[0] = Float.parseFloat(stringData[0]);
	    	data[1] = Float.parseFloat(stringData[1]);
	    	return data;
    	} else {
    		return null;
    	}
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
