package de.amr.demos.maze.swingapp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ImageIcon;

/**
 * Panel for setting parameters and running maze generator and path finder.
 * 
 * @author Armin Reichert
 */
public class ControlPanel extends JPanel {

	private JButton btnCreateMaze;
	private JComboBox<?> comboGridResolution;
	private JSlider sliderDelay;
	private JTextArea textArea;
	private JLabel lblPassageWidth;
	private JSlider sliderPassageWidth;
	private JLabel lblDelay;
	private JButton btnCreateAllMazes;
	private JButton btnStop;
	private JLabel lblGeneratorName;
	private JButton btnSolve;
	private JLabel lblSolverName;
	private JLabel lblGenerator;
	private JLabel lblSolver;
	private JScrollPane scrollPane;
	private JPanel controls;
	private JButton btnToggleMinMax;

	public ControlPanel() {
		setPreferredSize(new Dimension(497, 433));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));

		controls = new JPanel();
		add(controls, BorderLayout.CENTER);
		controls.setLayout(new MigLayout("", "[][grow,fill]", "[][][][][][]"));

		lblGenerator = new JLabel("Generator");
		controls.add(lblGenerator, "cell 0 0,growx");

		lblGeneratorName = new JLabel("Generator Algorithm");
		lblGenerator.setLabelFor(lblGeneratorName);
		lblGeneratorName.setBorder(new EmptyBorder(5, 0, 5, 0));
		lblGeneratorName.setForeground(Color.BLUE);
		lblGeneratorName.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblGeneratorName.setHorizontalAlignment(SwingConstants.LEFT);
		controls.add(lblGeneratorName, "flowy,cell 1 0,alignx left");

		lblSolver = new JLabel("Solver");
		controls.add(lblSolver, "flowx,cell 0 1,growx");

		lblSolverName = new JLabel("Solver Algorithm");
		lblSolver.setLabelFor(lblSolverName);
		controls.add(lblSolverName, "cell 1 1");
		lblSolverName.setHorizontalAlignment(SwingConstants.LEFT);
		lblSolverName.setForeground(Color.BLUE);
		lblSolverName.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblSolverName.setBorder(new EmptyBorder(5, 0, 5, 0));

		JLabel lblGridResolution = new JLabel("Grid Resolution");
		controls.add(lblGridResolution, "cell 0 2,growx");

		comboGridResolution = new JComboBox<>();
		lblGridResolution.setLabelFor(comboGridResolution);
		controls.add(comboGridResolution, "cell 1 2,growx");

		lblPassageWidth = new JLabel("Passage Width (%)");
		controls.add(lblPassageWidth, "cell 0 3,growx");

		sliderPassageWidth = new JSlider();
		lblPassageWidth.setLabelFor(sliderPassageWidth);
		sliderPassageWidth.setMinorTickSpacing(10);
		sliderPassageWidth.setMajorTickSpacing(50);
		sliderPassageWidth.setPaintLabels(true);
		sliderPassageWidth.setToolTipText("Passage Width (%)");
		sliderPassageWidth.setPaintTicks(true);
		controls.add(sliderPassageWidth, "cell 1 3,growx");

		lblDelay = new JLabel("Delay");
		controls.add(lblDelay, "cell 0 4,growx");

		sliderDelay = new JSlider();
		lblDelay.setLabelFor(sliderDelay);
		sliderDelay.setToolTipText("Rendering Delay");
		sliderDelay.setValue(10);
		sliderDelay.setSnapToTicks(true);
		sliderDelay.setMaximum(50);
		sliderDelay.setMinorTickSpacing(1);
		sliderDelay.setMajorTickSpacing(5);
		controls.add(sliderDelay, "cell 1 4,growx");

		scrollPane = new JScrollPane();
		controls.add(scrollPane, "cell 0 5 2 1,grow");

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setTabSize(2);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setRows(100);

		JPanel buttons = new JPanel();
		add(buttons, BorderLayout.SOUTH);
		buttons.setLayout(new MigLayout("", "[][][][][grow,right]", "[28px]"));

		btnCreateMaze = new JButton("Create");
		buttons.add(btnCreateMaze, "cell 0 0,alignx left,aligny top");

		btnSolve = new JButton("Solve");
		buttons.add(btnSolve, "cell 1 0,alignx left,aligny top");

		btnCreateAllMazes = new JButton("All Mazes");
		buttons.add(btnCreateAllMazes, "cell 2 0,alignx left,aligny top");

		btnStop = new JButton("Stop");
		buttons.add(btnStop, "cell 3 0,alignx left,aligny top");
		
		btnToggleMinMax = new JButton("");
		btnToggleMinMax.setIcon(new ImageIcon(ControlPanel.class.getResource("/com/sun/java/swing/plaf/windows/icons/DetailsView.gif")));
		buttons.add(btnToggleMinMax, "cell 4 0,aligny top");
	}

	public void showMessage(String msg) {
		textArea.append(msg);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	public JButton getBtnCreateMaze() {
		return btnCreateMaze;
	}

	@SuppressWarnings("unchecked")
	public JComboBox<String> getComboGridResolution() {
		return (JComboBox<String>) comboGridResolution;
	}

	public JSlider getSliderDelay() {
		return sliderDelay;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public JSlider getSliderPassageWidth() {
		return sliderPassageWidth;
	}

	public JButton getBtnCreateAllMazes() {
		return btnCreateAllMazes;
	}

	public JButton getBtnStop() {
		return btnStop;
	}

	public JLabel getLblGenerationAlgorithm() {
		return lblGeneratorName;
	}

	public JButton getBtnFindPath() {
		return btnSolve;
	}

	public JLabel getLblSolver() {
		return lblSolverName;
	}

	public JPanel getControls() {
		return controls;
	}
	public JButton getBtnToggleMinMax() {
		return btnToggleMinMax;
	}
}
