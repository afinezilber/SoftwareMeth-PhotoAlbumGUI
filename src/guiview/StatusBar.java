package guiview;

import java.awt.Dimension;

import javax.swing.JLabel;

/**
 * Statusbar for showing results of interactions
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
 public class StatusBar extends JLabel {
    
    public StatusBar() {
        super();
        this.setPreferredSize(new Dimension(1000, 16));
        setMessage("Ready");
    }
    
    public void setMessage(String message) {
        setText(" "+message);        
    }        
}