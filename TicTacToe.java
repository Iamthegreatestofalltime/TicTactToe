import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private boolean isSinglePlayer = false;

    public TicTacToe() {
        super("Tic Tac Toe");
        chooseMode();
        initializeBoard();
        setGameProperties();
    }

    private void chooseMode() {
        String[] options = {"AI", "Two Players"};
        int mode = JOptionPane.showOptionDialog(null, "Choose the game mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        isSinglePlayer = (mode == 0);
    }

    private void initializeBoard() {
        setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].addActionListener(this::buttonClicked);
                add(buttons[i][j]);
            }
        }
    }

    private void setGameProperties() {
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buttonClicked(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        button.setText(String.valueOf(currentPlayer));
        button.setEnabled(false);

        if (checkForWin()) {
            JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " wins!");
            resetGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(null, "Draw!");
            resetGame();
        } else {
            switchPlayer();
            if (isSinglePlayer && currentPlayer == 'O') {
                aiMove();
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private boolean checkForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkLine(buttons[i][0], buttons[i][1], buttons[i][2]) ||
                checkLine(buttons[0][i], buttons[1][i], buttons[2][i]))
                return true;
        }
        return checkLine(buttons[0][0], buttons[1][1], buttons[2][2]) ||
               checkLine(buttons[0][2], buttons[1][1], buttons[2][0]);
    }

    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        return b1.getText().equals(b2.getText()) &&
               b2.getText().equals(b3.getText()) &&
               !b1.getText().isEmpty();
    }

    private boolean isBoardFull() {
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                if (button.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private JButton findCriticalMove(char playerSymbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (checkPotentialWin(buttons[i][0], buttons[i][1], buttons[i][2], playerSymbol))
                    return getEmptyButton(buttons[i][0], buttons[i][1], buttons[i][2]);
                if (checkPotentialWin(buttons[0][j], buttons[1][j], buttons[2][j], playerSymbol))
                    return getEmptyButton(buttons[0][j], buttons[1][j], buttons[2][j]);
            }
        }

        if (checkPotentialWin(buttons[0][0], buttons[1][1], buttons[2][2], playerSymbol))
            return getEmptyButton(buttons[0][0], buttons[1][1], buttons[2][2]);
        if (checkPotentialWin(buttons[0][2], buttons[1][1], buttons[2][0], playerSymbol))
            return getEmptyButton(buttons[0][2], buttons[1][1], buttons[2][0]);


        return null;
    }


    private boolean checkPotentialWin(JButton b1, JButton b2, JButton b3, char symbol) {
        return ((b1.getText().equals(String.valueOf(symbol)) && b2.getText().equals(String.valueOf(symbol)) && b3.getText().isEmpty()) ||
                (b1.getText().equals(String.valueOf(symbol)) && b3.getText().equals(String.valueOf(symbol)) && b2.getText().isEmpty()) ||
                (b2.getText().equals(String.valueOf(symbol)) && b3.getText().equals(String.valueOf(symbol)) && b1.getText().isEmpty()));
    }


    private JButton getEmptyButton(JButton b1, JButton b2, JButton b3) {
        if (b1.getText().isEmpty()) return b1;
        if (b2.getText().isEmpty()) return b2;
        if (b3.getText().isEmpty()) return b3;
        return null;
    }

    private void aiMove() {
        Random random = new Random();
        JButton moveButton = null;

        List<JButton> emptyButtons = new ArrayList<>();

        for (JButton[] row : buttons) {
            for (JButton button : row) {
                if (button.getText().isEmpty()) {
                    emptyButtons.add(button);
                }
            }
        }

        if (random.nextFloat() < 0.75) {
            moveButton = findCriticalMove('O');
            if (moveButton != null) {
                performMove(moveButton, 'O');
                return;
            }
        }

        if (random.nextFloat() < 0.6) {
            moveButton = findCriticalMove('X');
            if (moveButton != null) {
                performMove(moveButton, 'O');
                return;
            }
        }

        if (!emptyButtons.isEmpty()) {
            moveButton = emptyButtons.get(random.nextInt(emptyButtons.size()));
            if (moveButton != null) {
                performMove(moveButton, 'O');
            }
        }
    }


    private void performMove(JButton button, char symbol) {
        button.setText(String.valueOf(symbol));
        button.setEnabled(false);


        if (checkForWin()) {
            JOptionPane.showMessageDialog(null, "AI wins!");
            resetGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(null, "Draw!");
            resetGame();
        } else {
            switchPlayer();
        }
    }

    private void resetGame() {
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setText("");
                button.setEnabled(true);
            }
        }
        Random random = new Random();
        currentPlayer = random.nextBoolean() ? 'X' : 'O';
        
        if(isSinglePlayer && currentPlayer == 'O') {
        	aiMove();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}