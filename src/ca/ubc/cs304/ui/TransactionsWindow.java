package ca.ubc.cs304.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ca.ubc.cs304.controller.Bank;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;

/**
 * The class is only responsible for displaying and handling the login GUI. 
 */
public class TransactionsWindow extends JFrame implements ActionListener {

	private static final int TEXT_FIELD_WIDTH = 10;
    private TerminalTransactionsDelegate delegate = null;
    
    //textbox components
    private JTextField input; 


    public TransactionsWindow(){
        super("transactions");
    }

    public void showFrame(TerminalTransactionsDelegate delegate){
        this.delegate = delegate;

        JLabel query1 = new JLabel("1. Insert branch");
		JLabel query2 = new JLabel("2. Delete branch");
		JLabel query3 = new JLabel("3. Update branch name");
		JLabel query4 = new JLabel("4. Show branch");
	    JLabel query5 = new JLabel("5. Quit");
        JLabel queryLabel = new JLabel("Please choose one of the above 5 options: ");

        JTextField queryField = new JTextField(TEXT_FIELD_WIDTH);

        JButton queryButton = new JButton("Go");



        JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);

		// layout components using the GridBag layout manager
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// place the query textfield label 
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(queryLabel, c);
		contentPane.add(queryLabel);

        // place the query field
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(queryField, c);
		contentPane.add(queryField);

        // place the go button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(queryButton, c);
		contentPane.add(queryButton);

        // idk 
        queryButton.addActionListener(this);

        // anonymous inner class for closing the window
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

        // size the window to obtain a best fit for the components
		this.pack();

        // center the frame
		Dimension d = this.getToolkit().getScreenSize();
		Rectangle r = this.getBounds();
		this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		 this.setVisible(true);

		// place the cursor in the text field for the username
		queryField.requestFocus();



	}


	@Override
	public void actionPerformed(ActionEvent actionEvent) {

	}
}
