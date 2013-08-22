package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class Minesweeper extends JFrame {
    private int amountOfRows = 20;
    private int amountOfColumns = 20;
    private int sizeOfTiles = 20;
    private int[][] grid = new int[amountOfRows][amountOfColumns];
    private MyPanel panel = new MyPanel();
    private int tilesTillWin;
    private int bombs = 70;
    private boolean firstClick = true;

    public Minesweeper() {
        super("Minesweeper");
        createGui();
        newGame();
    }

    private void newGame() {
        //Create Map
        tilesTillWin = amountOfRows * amountOfColumns - bombs;
        grid = new int[amountOfRows][amountOfColumns];
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                grid[row][column] = 0;
            }
        }
        firstClick = true;
    }

    /*


        Alle Felder um Mausklick aufdecken -> dürfen keine Bombe enthalten!!!



    */
    private void placeBombs(int currRow, int currCol) {
        for (int i = 0; i < bombs; i++) {
            int row = (int) (Math.random() * amountOfRows);
            int column = (int) (Math.random() * amountOfColumns);
            if (getNumber(grid[row][column]) < 9 && !(currRow == row && currCol == column)) {
                grid[row][column] = 9;

                //oben links
                if (row > 0 && column > 0 && grid[row - 1][column - 1] < 9) {
                    grid[row - 1][column - 1] += 1;
                }

                //oben mitte
                if (row > 0 && grid[row - 1][column] < 9) {
                    grid[row - 1][column] += 1;
                }

                //oben rechts
                if (row > 0 && column < amountOfColumns - 1 && grid[row - 1][column + 1] < 9) {
                    grid[row - 1][column + 1] += 1;
                }

                //links
                if (column > 0 && grid[row][column - 1] < 9) {
                    grid[row][column - 1] += 1;
                }

                //rechts
                if (column < amountOfColumns - 1 && grid[row][column + 1] < 9) {
                    grid[row][column + 1] += 1;
                }

                //unten links
                if (row < amountOfRows - 1 && column > 0 && grid[row + 1][column - 1] < 9) {
                    grid[row + 1][column - 1] += 1;
                }

                //unten mitte
                if (row < amountOfRows - 1 && grid[row + 1][column] < 9) {
                    grid[row + 1][column] += 1;
                }

                //unten rechts
                if (row < amountOfRows - 1 && column < amountOfColumns - 1 && grid[row + 1][column + 1] < 9) {
                    grid[row + 1][column + 1] += 1;
                }
            } else {
                i--;
            }
        }
        firstClick = false;
        //Map ausgeben
        for (int[] aGrid : grid) {
            for (int anAGrid : aGrid) {
                System.out.print(anAGrid + " ");
            }
            System.out.println("");
        }

    }


    private int getStatus(int value) {
        value = (value - getNumber(value)) / 10;
        return value;
    }

    private int getNumber(int value) {
        return value % 10;
    }

    private void setStatus(int status, int row, int column) {
        grid[row][column] = getNumber(grid[row][column]) + status * 10;
    }

    private void uncoverTile(int row, int column) {

        int value = getNumber(grid[row][column]);
        if (value == 0 && getStatus(grid[row][column]) == 0) {
            //leeres feld aufdecken, alle drum herum aufdecken
            setStatus(1, row, column);
            tilesTillWin--;
            //oben links
            if (row > 0 && column > 0) {
                uncoverTile(row - 1, column - 1);
            }
            //oben mitte
            if (row > 0) {
                uncoverTile(row - 1, column);
            }
            //oben rechts
            if (row > 0 && column < amountOfColumns - 1) {
                uncoverTile(row - 1, column + 1);
            }
            //links
            if (column > 0) {
                uncoverTile(row, column - 1);
            }
            //rechts
            if (column < amountOfColumns - 1) {
                uncoverTile(row, column + 1);
            }
            //unten links
            if (row < amountOfRows - 1 && column > 0) {
                uncoverTile(row + 1, column - 1);
            }
            //unten mitte
            if (row < amountOfRows - 1) {
                uncoverTile(row + 1, column);
            }
            //unten rechts
            if (row < amountOfRows - 1 && column < amountOfColumns - 1) {
                uncoverTile(row + 1, column + 1);
            }

        }
        if (value == 9) {
            //Bombe -> Ende
            setStatus(1, row, column);
            System.out.println("BOMBE!");

            //Alle Bomben aufdecken
            for (row = 0; row < grid.length; row++) {
                for (column = 0; column < grid[row].length; column++) {
                    setStatus(1, row, column);
                }
            }
            repaint();
            JOptionPane.showMessageDialog(this, "Bombe! Du hast verloren!");


        }
        if (value > 0 && value < 9) {
            //Nummer
            if (getStatus(grid[row][column]) == 0) {
                tilesTillWin--;
            }
            setStatus(1, row, column);

        }
        checkWin();

    }

    private void checkWin() {
        if (tilesTillWin == 0) {
            JOptionPane.showMessageDialog(this, "DU hast Gewonnen!");
        }
    }

    private void createGui() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        menuBar.add(menuGame);
        final JMenuItem menuItemNewGame = new JMenuItem("New Game");
        menuGame.add(menuItemNewGame);
        final JMenuItem menuItemSettings = new JMenuItem("Settings");
        menuGame.add(menuItemSettings);
        final JMenuItem menuItemQuit = new JMenuItem("Quit");
        menuGame.add(menuItemQuit);
        setJMenuBar(menuBar);
        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == menuItemNewGame) {
                    newGame();
                    repaint();
                }
                if (e.getSource() == menuItemSettings) {
                    //Open settings dialog
                    new SettingsDialog();


                }
                if (e.getSource() == menuItemQuit) {
                    System.exit(0);
                }
            }
        };
        menuItemNewGame.addActionListener(menuListener);
        menuItemSettings.addActionListener(menuListener);
        menuItemQuit.addActionListener(menuListener);
        add(panel);
        pack();
        setLocationRelativeTo(null);
        panel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int column = e.getX() / 20;
                int row = e.getY() / 20;
                if (firstClick) {
                    placeBombs(row, column);
                }
                //Linke Maustaste
                if (e.getButton() == 1) {
                    if (getStatus(grid[row][column]) == 0) {
                        uncoverTile(row, column);
                    }
                    repaint();
                }

                //RechteMaustaste
                if (e.getButton() == 3) {
                    if (getStatus(grid[row][column]) == 0) {
                        setStatus(2, row, column);
                    } else {
                        if (getStatus(grid[row][column]) == 2) {
                            setStatus(0, row, column);
                        }
                    }
                    repaint();
                }
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });

    }

    public static void main(String[] arg) {
        new Minesweeper();
    }

    private class MyPanel extends JPanel {
        public Dimension getPreferredSize() {
            return new Dimension(amountOfColumns * sizeOfTiles, amountOfRows * sizeOfTiles);
        }

        @Override
        public void paintComponent(Graphics g) {
            for (int row = 0; row < grid.length; row++) {
                for (int column = 0; column < grid[row].length; column++) {
                    g.drawRect(column * sizeOfTiles, row * sizeOfTiles, sizeOfTiles, sizeOfTiles);
                    int status = getStatus(grid[row][column]);
                    //System.out.println(status);
                    switch (status) {
                        case 0: //closed
                            g.setColor(Color.gray);
                            g.fillRect(column * sizeOfTiles, row * sizeOfTiles, sizeOfTiles, sizeOfTiles);
                            g.setColor(Color.black);
                            g.drawRect(column * sizeOfTiles, row * sizeOfTiles, sizeOfTiles, sizeOfTiles);
                            break;
                        case 1:
                            int value = getNumber(grid[row][column]);
                            if (value > 8) {
                                g.setColor(Color.red);
                                g.drawString("B", column * sizeOfTiles + 6, row * sizeOfTiles + 15);
                                g.setColor(Color.black);
                            } else {
                                if (value != 0) {
                                    g.drawString(Integer.toString(value), column * sizeOfTiles + 6, row * sizeOfTiles + 16);
                                }
                            }
                            break;
                        case 2:
                            g.setColor(Color.red);
                            g.fillRect(column * sizeOfTiles, row * sizeOfTiles, sizeOfTiles, sizeOfTiles);
                            g.setColor(Color.black);
                            g.drawRect(column * sizeOfTiles, row * sizeOfTiles, sizeOfTiles, sizeOfTiles);
                            break;

                    }

                }
            }

        }
    }

    private void changeSettings(int rows, int cols, int bombs) {
        amountOfColumns = cols;
        amountOfRows = rows;
        if (bombs < (rows * cols)) {
            this.bombs = bombs;
        }
        newGame();
        pack();
    }

    private class SettingsDialog extends JDialog {
        public SettingsDialog() {

            setLocationRelativeTo(null);
            setVisible(true);
            setLayout(new BorderLayout());

            final JRadioButton easy = new JRadioButton("Easy");
            final JRadioButton medium = new JRadioButton("Medium", true);
            final JRadioButton hard = new JRadioButton("Hard");
            final JRadioButton custom = new JRadioButton("Custom");
            ButtonGroup difficulty = new ButtonGroup();
            difficulty.add(easy);
            difficulty.add(medium);
            difficulty.add(hard);
            difficulty.add(custom);

            Box verticalBoxDifficulty = Box.createVerticalBox();
            add(verticalBoxDifficulty, BorderLayout.WEST);
            verticalBoxDifficulty.add(Box.createVerticalGlue());
            verticalBoxDifficulty.add(easy);
            verticalBoxDifficulty.add(medium);
            verticalBoxDifficulty.add(hard);
            verticalBoxDifficulty.add(custom);
            verticalBoxDifficulty.add(Box.createVerticalGlue());

            JLabel lblFields = new JLabel("Size:");
            JLabel lblMines = new JLabel("Amount of mines:");
            final JTextField txtFieldR = new JTextField("20");
            txtFieldR.setPreferredSize(new Dimension(40, 20));
            final JTextField txtFieldC = new JTextField("20");
            txtFieldC.setPreferredSize(new Dimension(40, 20));
            final JTextField txtMines = new JTextField("30");
            txtMines.setPreferredSize(new Dimension(40, 20));

            Box centerBox = Box.createVerticalBox();
            add(centerBox, BorderLayout.CENTER);
            centerBox.add(Box.createVerticalGlue());
            centerBox.add(lblFields);
            JPanel panelFields = new JPanel();
            panelFields.add(new JLabel("X:"));
            panelFields.add(txtFieldC);
            panelFields.add(new JLabel("Y:"));
            panelFields.add(txtFieldR);
            centerBox.add(panelFields);
            centerBox.add(lblMines);
            JPanel panelMines = new JPanel();
            panelMines.add(txtMines);
            centerBox.add(panelMines);
            txtFieldC.setEditable(false);
            txtFieldR.setEditable(false);
            txtMines.setEditable(false);
            centerBox.add(Box.createVerticalGlue());


            JPanel panelSouth = new JPanel();
            final JButton save = new JButton("Save");
            final JButton cancel = new JButton("Cancel");
            panelSouth.add(save);
            panelSouth.add(cancel);
            add(panelSouth, BorderLayout.SOUTH);

            pack();

            ActionListener radioButtonListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (easy.isSelected()) {
                        txtFieldC.setText("10");
                        txtFieldR.setText("10");
                        txtMines.setText("3");
                        txtFieldC.setEditable(false);
                        txtFieldR.setEditable(false);
                        txtMines.setEditable(false);
                    }
                    if (medium.isSelected()) {
                        txtFieldC.setText("20");
                        txtFieldR.setText("20");
                        txtMines.setText("30");
                        txtFieldC.setEditable(false);
                        txtFieldR.setEditable(false);
                        txtMines.setEditable(false);
                    }
                    if (hard.isSelected()) {
                        txtFieldC.setText("30");
                        txtFieldR.setText("30");
                        txtMines.setText("50");
                        txtFieldC.setEditable(false);
                        txtFieldR.setEditable(false);
                        txtMines.setEditable(false);
                    }
                    if (custom.isSelected()) {
                        txtFieldC.setEditable(true);
                        txtFieldR.setEditable(true);
                        txtMines.setEditable(true);
                    }
                }
            };


            easy.addActionListener(radioButtonListener);
            medium.addActionListener(radioButtonListener);
            hard.addActionListener(radioButtonListener);
            custom.addActionListener(radioButtonListener);

            ActionListener buttonListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == save) {
                        //save
                        System.out.println(Integer.parseInt(txtFieldR.getText()) + "" + Integer.parseInt(txtFieldC.getText()) + "" + Integer.parseInt(txtMines.getText()));
                        changeSettings(Integer.parseInt(txtFieldR.getText()), Integer.parseInt(txtFieldC.getText()), Integer.parseInt(txtMines.getText()));
                        dispose();
                    }
                    if (e.getSource() == cancel) {
                        //cancel
                        dispose();
                    }
                }
            };
            save.addActionListener(buttonListener);
            cancel.addActionListener(buttonListener);
        }
    }

}
 

