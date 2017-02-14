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
			return null;
		}
	}
	
	public void sendPacket(String msg) {
		if (Robot.nucConnected) {
			try {
				DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
				outToClient.writeBytes(msg);
			} catch (IOException e) {
				SmartDashboard.putString("connection error", "error sending packet");
			}
		}
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

