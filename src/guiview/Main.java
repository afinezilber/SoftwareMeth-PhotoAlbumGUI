package guiview;

import javax.swing.UIManager.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

import javax.swing.*;

import cs213.photoAlbum.control.*;

/**
 * Main entry point for application
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class Main {
	
	public static MainApp app;
		
	/**
	 * Constructs GUI and creates pertinent child elements
	 */
	public static void createAndShowGUI() {
		app = new MainApp();
    }
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

            	//Try to use a better look and feel because the default is boring :)
            	try {
            	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            	        if ("Nimbus".equals(info.getName())) {
            	            UIManager.setLookAndFeel(info.getClassName());
            	            break;
            	        }
            	    }
            	} catch (Exception e) {
            	    // Goes to default look and feel
            	}
                createAndShowGUI();
            }
        });
	}

}
