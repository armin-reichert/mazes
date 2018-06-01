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
	private JLabel lblGenerationAlgorithm;
	private JButton btnFindPath;

	public ControlPanel() {
		setPreferredSize(new Dimension(450, 350));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));

		JPanel controlsPanel = new JPanel();
		add(controlsPanel, BorderLayout.NORTH);
		controlsPanel.setLayout(new MigLayout("", "[center][:5px:5px,fill][grow]", "[grow,center][][][]"));

		lblGenerationAlgorithm = new JLabel("Generation Algorithm");
		lblGenerationAlgorithm.setBorder(new EmptyBorder(5, 0, 5, 0));
		lblGenerationAlgorithm.setForeground(Color.BLUE);
		lblGenerationAlgorithm.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblGenerationAlgorithm.setHorizontalAlignment(SwingConstants.CENTER);
		controlsPanel.add(lblGenerationAlgorithm, "cell 0 0 3 1,growx,aligny center");

		JLabel lblGridResolution = new JLabel("Grid Resolution");
		controlsPanel.add(lblGridResolution, "cell 0 1,alignx left,growy");

		comboGridResolution = new JComboBox<>();
		controlsPanel.add(comboGridResolution, "cell 2 1,grow");

		lblPassageWidth = new JLabel("Passage Width");
		controlsPanel.add(lblPassageWidth, "cell 0 2,alignx left,growy");
		lblPassageWidth.setLabelFor(sliderPassageWidth);

		sliderPassageWidth = new JSlider();
		sliderPassageWidth.setPaintLabels(true);
		sliderPassageWidth.setMinimum(1);
		sliderPassageWidth.setToolTipText("Thickness of maze passages");
		sliderPassageWidth.setPaintTicks(true);
		controlsPanel.add(sliderPassageWidth, "cell 2 2,grow");

		lblDelay = new JLabel("Delay");
		controlsPanel.add(lblDelay, "cell 0 3,alignx left,growy");

		sliderDelay = new JSlider();
		sliderDelay.setToolTipText("Rendering Delay");
		sliderDelay.setValue(10);
		sliderDelay.setSnapToTicks(true);
		sliderDelay.setMaximum(50);
		sliderDelay.setMinorTickSpacing(1);
		sliderDelay.setMajorTickSpacing(5);
		controlsPanel.add(sliderDelay, "cell 2 3,grow");

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		textArea.setTabSize(2);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		textArea.setRows(6);

		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnCreateMaze = new JButton("Create Maze");
		buttonPanel.add(btnCreateMaze);

		btnCreateAllMazes = new JButton("Create All Mazes");
		buttonPanel.add(btnCreateAllMazes);
		
		btnFindPath = new JButton("Find Path");
		buttonPanel.add(btnFindPath);

		btnStop = new JButton("Stop");
		buttonPanel.add(btnStop);
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
		return lblGenerationAlgorithm;
	}
	public JButton getBtnFindPath() {
		return btnFindPath;
	}
}
