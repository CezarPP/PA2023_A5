package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static java.lang.System.exit;

public class Window extends JFrame {

    static final int CANVAS_WIDTH = 650;
    static final int CANVAS_HEIGHT = 650;
    static Canvas canvas = null;

    static final String SAVE_FILE = "graph.bin";
    JButton load, save, reset, exit, createNewGameButton, saveAsPNG;
    JPanel topPanel, bottomPanel;
    JLabel numberOfDotsLabel, sliderLabel;
    JSpinner numberOfDotsButton;
    JComboBox<String> probabilityBox;

    void createBottomPanel() {
        save = new JButton("Save");
        load = new JButton("Load");
        reset = new JButton("Reset");
        exit = new JButton("Exit");
        saveAsPNG = new JButton("Save as PNG");

        save.addActionListener(e -> {
            if (canvas != null) {
                try (FileWriter fileWriter = new FileWriter(SAVE_FILE)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    CanvasData canvasData = new CanvasData(canvas.numberOfDots, canvas.width, canvas.height,
                            canvas.lineProbability, canvas.edges);
                    objectMapper.writeValue(fileWriter, canvasData);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(this,
                            "An error occurred while saving the file: " + exception.getMessage(),
                            "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        load.addActionListener(e -> {
            try (FileReader fileReader = new FileReader(SAVE_FILE)) {
                ObjectMapper objectMapper = new ObjectMapper();
                CanvasData canvasData = objectMapper.readValue(fileReader, CanvasData.class);

                if (canvas != null) {
                    remove(canvas);
                }
                canvas = new Canvas(canvasData.numberOfDots, canvasData.width, canvasData.height,
                        canvasData.lineProbability, canvasData.edges);
                numberOfDotsButton.setValue(canvasData.numberOfDots);
                probabilityBox.setSelectedIndex((int) (canvasData.lineProbability / 0.1 - 1));
                add(canvas, BorderLayout.CENTER);
                setVisible(true);
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
                JOptionPane.showMessageDialog(this,
                        "An error occurred while loading the file: " + exception.getMessage(),
                        "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reset.addActionListener(e -> {
            numberOfDotsButton.setValue(0);
            probabilityBox.setSelectedIndex(0);
            createNewGameButton.doClick();
        });
        exit.addActionListener(e -> exit(0));

        saveAsPNG.addActionListener(e -> {
            if (canvas != null) {
                String filename = "game_board.png";
                canvas.saveToPNG(filename);
                JOptionPane.showMessageDialog(this,
                        "Game board saved as " + filename,
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(load);
        bottomPanel.add(save);
        bottomPanel.add(reset);
        bottomPanel.add(exit);
        bottomPanel.add(saveAsPNG);
    }

    void createTopPanel() {
        topPanel = new JPanel(new FlowLayout());
        numberOfDotsLabel = new JLabel("Number of dots:");
        topPanel.add(numberOfDotsLabel);
        numberOfDotsButton = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        numberOfDotsButton.setPreferredSize(new Dimension(100, 30));
        topPanel.add(numberOfDotsButton);


        sliderLabel = new JLabel("Line probability:");
        topPanel.add(sliderLabel);

        String[] probabilities = new String[10];
        for (int i = 1; i <= 10; i++) {
            double probability = i * 0.1;
            probabilities[i - 1] = String.format("%.1f", probability);
        }

        probabilityBox = new JComboBox<>(probabilities);
        probabilityBox.setSelectedIndex(0); // set default selection to 0.1
        probabilityBox.setPreferredSize(new Dimension(100, 30));
        topPanel.add(probabilityBox);
        createNewGameButton = new JButton("Create new game");

        createNewGameButton.addActionListener(e -> {
            if (canvas != null)
                remove(canvas);
            canvas = new Canvas((int) numberOfDotsButton.getValue(), CANVAS_WIDTH, CANVAS_HEIGHT,
                    ((probabilityBox.getSelectedIndex() + 1) * 0.1f));
            add(canvas, BorderLayout.CENTER);
            setVisible(true);
        });

        topPanel.add(createNewGameButton);
    }

    Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(800, 800);

        createTopPanel();
        createBottomPanel();

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
