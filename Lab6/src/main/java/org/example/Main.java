package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

import static java.lang.System.exit;

public class Main {
    static final int CANVAS_WIDTH = 600;
    static final int CANVAS_HEIGHT = 600;
    static Canvas canvas = null;

    static final String SAVE_FILE = "graph.bin";

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add bottom buttons
        JButton load = new JButton("Load");
        JButton save = new JButton("Save");
        JButton reset = new JButton("Reset");
        JButton exit = new JButton("Exit");

        frame.setSize(800, 800);

        JPanel topPanel = new JPanel(new FlowLayout());

        JLabel numberOfDotsLabel = new JLabel("Number of dots:");
        topPanel.add(numberOfDotsLabel);

        JSpinner numberOfDotsButton = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        numberOfDotsButton.setPreferredSize(new Dimension(100, 30));
        topPanel.add(numberOfDotsButton);

        JLabel sliderLabel = new JLabel("Line probability:");
        topPanel.add(sliderLabel);

        String[] probabilities = new String[10];
        for (int i = 1; i <= 10; i++) {
            double probability = i * 0.1;
            probabilities[i - 1] = String.format("%.1f", probability);
        }

        JComboBox<String> probabilityBox = new JComboBox<>(probabilities);
        probabilityBox.setSelectedIndex(0); // set default selection to 0.1
        probabilityBox.setPreferredSize(new Dimension(100, 30));
        topPanel.add(probabilityBox);

        JButton createNewGameButton = new JButton("Create new game");

        createNewGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas != null)
                    frame.remove(canvas);
                canvas = new Canvas((int) numberOfDotsButton.getValue(), CANVAS_WIDTH, CANVAS_HEIGHT,
                        ((probabilityBox.getSelectedIndex() + 1) * 0.1f));
                frame.add(canvas, BorderLayout.CENTER);
                frame.setVisible(true);
            }
        });

        save.addActionListener(e -> {
            // only save the canvas
            try (DataOutputStream f = new DataOutputStream(new FileOutputStream(SAVE_FILE, false))) {
                if (canvas != null) {
                    f.writeInt(canvas.numberOfDots);
                    f.writeInt((int) (canvas.lineProbability * 100));
                    f.writeInt(canvas.edges.size());
                    for (Canvas.Pair edge : canvas.edges) {
                        f.writeInt(edge.x.x);
                        f.writeInt(edge.x.y);
                        f.writeInt(edge.y.x);
                        f.writeInt(edge.y.y);
                    }
                }
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(frame,
                        "An error occurred while saving the file: " + exception.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
                // throw new RuntimeException(exception);
            }
        });

        load.addActionListener(e -> {
            try (DataInputStream f = new DataInputStream(new FileInputStream(SAVE_FILE))) {
                if (canvas != null)
                    frame.remove(canvas);
                int N = f.readInt();
                float probability = f.readInt() / 100.0f;
                int cntEdges = f.readInt();
                ArrayList<Canvas.Pair> edges = new ArrayList<>(cntEdges);
                for (int i = 0; i < cntEdges; i++) {
                    Canvas.Pair edge = new Canvas.Pair();
                    edge.x.x = f.readInt();
                    edge.x.y = f.readInt();
                    edge.y.x = f.readInt();
                    edge.y.y = f.readInt();
                    System.out.println("Reading the edge" + edge);
                    edges.add(edge);
                }
                canvas = new Canvas(N, CANVAS_WIDTH, CANVAS_HEIGHT, probability, edges);
                numberOfDotsButton.setValue(N);
                probabilityBox.setSelectedIndex((int) (probability / 0.1 - 1));
                frame.add(canvas, BorderLayout.CENTER);
                frame.setVisible(true);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(frame,
                        "An error occurred while loading the file: " + exception.getMessage(),
                        "Load Error", JOptionPane.ERROR_MESSAGE);
                // throw new RuntimeException(exception);
            }
        });
        reset.addActionListener(e -> {
            numberOfDotsButton.setValue(0);
            probabilityBox.setSelectedIndex(0);
            createNewGameButton.doClick();
        });
        exit.addActionListener(e -> exit(0));

        topPanel.add(createNewGameButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(load);
        bottomPanel.add(save);
        bottomPanel.add(reset);
        bottomPanel.add(exit);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}