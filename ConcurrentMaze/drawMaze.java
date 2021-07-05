package ConcurrentMaze;

import java.awt.*;
import java.util.Scanner;

import javax.swing.*;

import ConcurrentMaze.Maze;

public class drawMaze {
	public static void main(String args[]) {
		JFrame frame = new JFrame();
        Maze m;
        Scanner scan = new Scanner(System.in);
        System.out.print("Filename: ");
        String filename = scan.nextLine();
        scan.close();
        try {
            m = new Maze(filename);
        } catch (Exception e) {
            System.out.println("Catch");
            m = new Maze(10,10,0,0,9,9);
        }
		frame.add(m);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.white);
		frame.setSize(1000, 1025);
        frame.setVisible(true);
        // m.repaint();
	}
}