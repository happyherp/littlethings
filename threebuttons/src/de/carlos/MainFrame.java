package de.carlos;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame implements ActionListener {
    
    private JTextField field1 = new JTextField("0.0",5);
    private JTextField field2 = new JTextField("0.0",5);
    private JTextField field3 = new JTextField("0.0",5);
    
    private JButton button = new JButton("Calculate!");

    private JLabel resultlabel = new JLabel("No result yet.");
    
    
    public MainFrame(){
	
	this.setSize(300,400);
	
	JPanel panel = new JPanel() ;
	panel.setLayout(new GridLayout(4, 2));
	
	panel.add(new JLabel("Feld1"));
	panel.add(field1);
	
	panel.add(new JLabel("Feld2"));
	panel.add(field2);
	
	panel.add(new JLabel("Feld3"));
	panel.add(field3);	
	
	panel.add(button);
	panel.add(resultlabel);
	
	
	this.getContentPane().add(panel);
	
	this.pack();
	
	
	button.addActionListener(this);

    }
    


    /**
     * Diese methode wird bei klick des Buttons aufgerufen.
     * 
     */
    @Override
    public void actionPerformed(ActionEvent event) {
	
	try{
	double val1 = Double.valueOf(field1.getText());
	double val2 = Double.valueOf(field2.getText());
	double val3 = Double.valueOf(field3.getText());
	
	double result = val1 + val2 + val3;
	
	this.resultlabel.setText("Result: "+result);
	
	ResultWriter.writeToFile(val1, val2, val3, result);
	
	}catch (Exception e){
	    JOptionPane.showMessageDialog(this, "An error Occurred: "+e);
	}
	
    };
    
    
    /**
     * Hier startet das programm.
     * 
     * @param args
     */
    public static void main(String[] args){
	
	MainFrame frame = new MainFrame();
	frame.setVisible(true);
	
    }

    

}
