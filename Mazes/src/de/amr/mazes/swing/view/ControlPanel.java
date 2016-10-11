package de.amr.mazes.swing.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

public class ControlPanel extends JPanel {

	private JButton btnCreateMaze;
	private JComboBox<?> resolutionSelector;
	private JSlider delaySlider;
	private JTextArea textArea;
	private JLabel lblPassageThickness;
	private JSlider passageThicknessSlider;
	private JLabel lblDelay;
	private JButton btnCreateAllMazes;
	private JButton btnStop;
	private JLabel algorithmLabel;

	public ControlPanel() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));

		JPanel controls = new JPanel();
		add(controls, BorderLayout.NORTH);
		GridBagLayout gbl_controls = new GridBagLayout();
		gbl_controls.columnWeights = new double[] { 0.0, 1.0 };
		gbl_controls.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		controls.setLayout(gbl_controls);
		
		algorithmLabel = new JLabel("Generation Algorithm");
		algorithmLabel.setForeground(Color.BLUE);
		algorithmLabel.setFont(new Font("Arial Black", Font.PLAIN, 14));
		algorithmLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_algorithmLabel = new GridBagConstraints();
		gbc_algorithmLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_algorithmLabel.gridwidth = 2;
		gbc_algorithmLabel.insets = new Insets(0, 0, 5, 0);
		gbc_algorithmLabel.gridx = 0;
		gbc_algorithmLabel.gridy = 0;
		controls.add(algorithmLabel, gbc_algorithmLabel);

		JLabel lblGridResolution = new JLabel("Grid Resolution");
		GridBagConstraints gbc_lblGridResolution = new GridBagConstraints();
		gbc_lblGridResolution.fill = GridBagConstraints.BOTH;
		gbc_lblGridResolution.insets = new Insets(0, 0, 5, 5);
		gbc_lblGridResolution.gridx = 0;
		gbc_lblGridResolution.gridy = 1;
		controls.add(lblGridResolution, gbc_lblGridResolution);

		resolutionSelector = new JComboBox<>();
		GridBagConstraints gbc_resolutionSelector = new GridBagConstraints();
		gbc_resolutionSelector.weightx = 1.0;
		gbc_resolutionSelector.fill = GridBagConstraints.BOTH;
		gbc_resolutionSelector.insets = new Insets(0, 0, 5, 0);
		gbc_resolutionSelector.gridx = 1;
		gbc_resolutionSelector.gridy = 1;
		controls.add(resolutionSelector, gbc_resolutionSelector);

		lblPassageThickness = new JLabel("Passage Thickness");
		GridBagConstraints gbc_lblPassageThickness = new GridBagConstraints();
		gbc_lblPassageThickness.fill = GridBagConstraints.BOTH;
		gbc_lblPassageThickness.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassageThickness.gridx = 0;
		gbc_lblPassageThickness.gridy = 2;
		controls.add(lblPassageThickness, gbc_lblPassageThickness);
		lblPassageThickness.setLabelFor(passageThicknessSlider);

		passageThicknessSlider = new JSlider();
		passageThicknessSlider.setPaintLabels(true);
		passageThicknessSlider.setMinimum(1);
		passageThicknessSlider.setToolTipText("Thickness of maze passages");
		passageThicknessSlider.setPaintTicks(true);
		GridBagConstraints gbc_passageThicknessSlider = new GridBagConstraints();
		gbc_passageThicknessSlider.weightx = 1.0;
		gbc_passageThicknessSlider.fill = GridBagConstraints.BOTH;
		gbc_passageThicknessSlider.insets = new Insets(0, 0, 5, 0);
		gbc_passageThicknessSlider.gridx = 1;
		gbc_passageThicknessSlider.gridy = 2;
		controls.add(passageThicknessSlider, gbc_passageThicknessSlider);

		lblDelay = new JLabel("Delay");
		GridBagConstraints gbc_lblDelay = new GridBagConstraints();
		gbc_lblDelay.fill = GridBagConstraints.BOTH;
		gbc_lblDelay.insets = new Insets(0, 0, 0, 5);
		gbc_lblDelay.gridx = 0;
		gbc_lblDelay.gridy = 3;
		controls.add(lblDelay, gbc_lblDelay);

		delaySlider = new JSlider();
		delaySlider.setToolTipText("Rendering Delay");
		delaySlider.setValue(10);
		delaySlider.setSnapToTicks(true);
		delaySlider.setMaximum(50);
		delaySlider.setMinorTickSpacing(1);
		delaySlider.setMajorTickSpacing(5);
		GridBagConstraints gbc_delaySlider = new GridBagConstraints();
		gbc_delaySlider.weightx = 1.0;
		gbc_delaySlider.fill = GridBagConstraints.BOTH;
		gbc_delaySlider.gridx = 1;
		gbc_delaySlider.gridy = 3;
		controls.add(delaySlider, gbc_delaySlider);

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

		btnStop = new JButton("Stop");
		buttonPanel.add(btnStop);
	}

	public JButton getBtnCreateMaze() {
		return btnCreateMaze;
	}

	@SuppressWarnings("unchecked")
	public JComboBox<String> getResolutionSelector() {
		return (JComboBox<String>) resolutionSelector;
	}

	public JSlider getDelaySlider() {
		return delaySlider;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void showMessage(String msg) {
		textArea.append(msg);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	public JSlider getPassageThicknessSlider() {
		return passageThicknessSlider;
	}

	public JButton getBtnCreateAllMazes() {
		return btnCreateAllMazes;
	}

	public JButton getBtnStop() {
		return btnStop;
	}
	public JLabel getAlgorithmLabel() {
		return algorithmLabel;
	}
}
