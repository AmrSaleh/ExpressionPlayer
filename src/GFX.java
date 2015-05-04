import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JRadioButtonMenuItem;
import java.awt.Font;

public class GFX {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton btnCalculate, btnSaveTruthtable, btnCompare, btnEvaluateForA, btnEvaluate,
			buttonSimplify;
	private JTextArea textArea;
	private JTextField lblExpressionTypeWill;
	private JTextField ComparisonResultWill;
	Mastermind currentExp;
	private JTextField textField_3;
	private JRadioButtonMenuItem[] variables;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GFX window = new GFX();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GFX() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 699, 615);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setTitle("Expression Player by Amr & Khatib");
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		JLabel lblEnterYourExpression = new JLabel("Enter your expression");

		textField = new JTextField();
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				btnEvaluateForA.setEnabled(false);
				btnEvaluate.setEnabled(false);
				btnCompare.setEnabled(false);
				buttonSimplify.setEnabled(false);
				btnSaveTruthtable.setEnabled(false);
			}
		});
		textField.setToolTipText("separate every thing with spaces");
		textField.setColumns(10);

		lblExpressionTypeWill = new JTextField("Expression type will be shown after you click calculate");
		lblExpressionTypeWill.setEnabled(false);

		final JPopupMenu popupMenu = new JPopupMenu();

		btnCalculate = new JButton("Calculate");
		btnCalculate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String trimmed = ExpressionEvaluation.trimAndRemoveInterspaces(textField.getText());
				if (!(trimmed = ExpressionEvaluation.isValidated(trimmed)).equals("-1")) {
					// initializing text boxes
					textField_3.setText("Simplified equation");
					textField_2.setText("Evaluation answer");
					ComparisonResultWill.setText("Comparison result will be shown here");
					textArea.setText("");
					//long start = System.currentTimeMillis();
					currentExp = ExpressionEvaluation.validateAndParse(trimmed);
					//long time = System.currentTimeMillis() - start;
					//System.out.print(time + " - ");
					//start = System.currentTimeMillis();
					textArea.setText(currentExp.drawTable());
					//time = System.currentTimeMillis() - start;
					//System.out.println(time);
					if (currentExp.checkTautology()) {
						lblExpressionTypeWill.setText("This is a Tautology");
					} else if (currentExp.checkContradiction()) {
						lblExpressionTypeWill.setText("This is a Contradiction");
					} else {
						lblExpressionTypeWill.setText("This is not a Tautology nor a Contradiction");
					}
					btnEvaluateForA.setEnabled(true);
					btnEvaluate.setEnabled(true);
					btnCompare.setEnabled(true);
					buttonSimplify.setEnabled(true);
					btnSaveTruthtable.setEnabled(true);
					popupMenu.removeAll();
					variables = new JRadioButtonMenuItem[currentExp.getTruthTable().getNumOfVariables()];
					for (int i = 0; i < variables.length; i++) {
						variables[i] = new JRadioButtonMenuItem(currentExp.getTruthTable().getMapTags().get(i));
						variables[i].setName("variable" + i);
						variables[i].addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {

								popupMenu.setVisible(true);
							}

						});
						popupMenu.add(variables[i]);
					}
				} else {
					lblExpressionTypeWill.setText("Expression not valid");
					textField_3.setText("Simplified equation");
					textField_2.setText("Evaluation answer");
					ComparisonResultWill.setText("Comparison result will be shown here");
					textArea.setText("");

					btnEvaluateForA.setEnabled(false);
					btnEvaluate.setEnabled(false);
					btnCompare.setEnabled(false);
					buttonSimplify.setEnabled(false);
					btnSaveTruthtable.setEnabled(false);
				}

			}
		});

		btnSaveTruthtable = new JButton("Save TruthTable");
		btnSaveTruthtable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String userhome = System.getProperty("user.home");
				JFileChooser chooser = new JFileChooser(userhome + "\\Desktop");

				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt Files", "txt");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose to save this file: " + chooser.getSelectedFile().getName());

					java.io.File file = chooser.getSelectedFile();
					String file_name = file.toString();

					if (!file_name.endsWith(".txt")) {
						file_name = file_name + ".txt";
					}

					try {
						// create a file named "testfile.txt" in the current
						// working directory
						File myFile = new File(file_name);
						if (myFile.createNewFile()) {
							System.out.println("Success! " + file_name);
						} else {
							System.out.println("Failure! " + file_name);
						}
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					
					String table = textArea.getText();
					
					 String newLine = System.getProperty("line.separator");
					table=table.replaceAll("\n", newLine);
					
					try
					{
					   
					    FileWriter fw = new FileWriter(file_name,false); //the true will append the new data
					    fw.write(table);//appends the string to the file
					    fw.close();
					}
					catch(IOException ioe)
					{
					    System.err.println("IOException: " + ioe.getMessage());
					}

				}
			}
		});
		btnSaveTruthtable.setEnabled(false);

		JLabel lblCompareToAnother = new JLabel("Compare to another expression");

		textField_1 = new JTextField();
		textField_1.setToolTipText("separate every thing with spaces");
		textField_1.setColumns(10);

		btnCompare = new JButton("Compare");
		btnCompare.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String trimmed = ExpressionEvaluation.trimAndRemoveInterspaces(textField_1.getText());
				if (!(trimmed = ExpressionEvaluation.isValidated(trimmed)).equals("-1")) {
					if (currentExp.isEquivalent(trimmed)) {
						ComparisonResultWill.setText("The two expressions are Equivalent");
					} else {
						ComparisonResultWill.setText("The two expressions are NOT Equivalent");
					}
				} else {
					ComparisonResultWill.setText("Expression not valid");
				}

			}
		});
		btnCompare.setEnabled(false);

		ComparisonResultWill = new JTextField("Comparison result will be shown here");
		ComparisonResultWill.setEnabled(false);

		btnEvaluateForA = new JButton("Select variables values");
		btnEvaluateForA.setEnabled(false);

		btnEvaluate = new JButton("Evaluate");
		btnEvaluate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ArrayList<Boolean> values = new ArrayList<Boolean>();

				for (int i = 0; i < variables.length; i++) {
					values.add(variables[i].isSelected());
				}

				textField_2.setText("The answer is : " + currentExp.evaluate(values));

			}
		});
		btnEvaluate.setEnabled(false);

		textField_2 = new JTextField("Evaluation answer");
		textField_2.setEnabled(false);

		textField_3 = new JTextField("Simplified equation");
		textField_3.setEnabled(false);

		buttonSimplify = new JButton("Simplify");
		buttonSimplify.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				

				textField_3.setText("The answer is : " + currentExp.simplify());

			}
		});
		buttonSimplify.setEnabled(false);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
						.addComponent(textField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(lblExpressionTypeWill, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 303, Short.MAX_VALUE)
							.addComponent(btnCalculate))
						.addComponent(textField_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(ComparisonResultWill, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 306, Short.MAX_VALUE)
							.addComponent(btnCompare))
						.addComponent(btnSaveTruthtable, Alignment.TRAILING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(textField_3, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonSimplify, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
								.addComponent(btnEvaluateForA))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEvaluate))
						.addComponent(lblCompareToAnother)
						.addComponent(lblEnterYourExpression, GroupLayout.PREFERRED_SIZE, 331, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEnterYourExpression)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnCalculate)
						.addComponent(lblExpressionTypeWill, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonSimplify))
					.addGap(16)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnEvaluate)
								.addComponent(btnEvaluateForA)))
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addComponent(lblCompareToAnother)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCompare)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSaveTruthtable))
						.addComponent(ComparisonResultWill, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
					.addContainerGap())
		);

		addPopup(btnEvaluateForA, popupMenu);

		// JRadioButtonMenuItem rdbtnmntmA = new
		// JRadioButtonMenuItem("Aywaaaaa");
		// rdbtnmntmA.setName("");
		// popupMenu.add("Select the true variables");

		textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.PLAIN, 17));
		textArea.setEditable(false);
		textArea.setBorder(null);
		scrollPane.setViewportView(textArea);
		frame.getContentPane().setLayout(groupLayout);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// if (e.isPopupTrigger()) {
				showMenu(e);
				// }
			}

			public void mouseReleased(MouseEvent e) {
				// if (e.isPopupTrigger()) {
				// showMenu(e);
				// }
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
