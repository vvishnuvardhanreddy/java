import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.security.SecureRandom;

public class PasswordGenerator extends JFrame {
    private JTextField lengthField;
    private JCheckBox upperCaseCheck, lowerCaseCheck, numbersCheck, specialCheck;
    private JTextField passwordField;
    private JButton generateButton, copyButton;

    private final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private final String NUMBERS = "0123456789";
    private final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?/`~";

    public PasswordGenerator() {
        setTitle("Strong Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lengthLabel = new JLabel("Password Length:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lengthLabel, gbc);

        lengthField = new JTextField("16");
        gbc.gridx = 1;
        add(lengthField, gbc);

        upperCaseCheck = new JCheckBox("Include Uppercase (A-Z)", true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(upperCaseCheck, gbc);

        lowerCaseCheck = new JCheckBox("Include Lowercase (a-z)", true);
        gbc.gridy = 2;
        add(lowerCaseCheck, gbc);

        numbersCheck = new JCheckBox("Include Numbers (0-9)", true);
        gbc.gridy = 3;
        add(numbersCheck, gbc);

        specialCheck = new JCheckBox("Include Special Characters (!@#...)", true);
        gbc.gridy = 4;
        add(specialCheck, gbc);

        generateButton = new JButton("Generate Password");
        gbc.gridy = 5;
        add(generateButton, gbc);

        passwordField = new JTextField("Your generated password will appear here");
        passwordField.setEditable(false);
        gbc.gridy = 6;
        add(passwordField, gbc);

        copyButton = new JButton("Copy Password");
        gbc.gridy = 7;
        add(copyButton, gbc);

        // Event Handlers
        generateButton.addActionListener(this::handleGenerate);
        copyButton.addActionListener(e -> {
            String pwd = passwordField.getText();
            if (!pwd.isBlank() && !pwd.equals("Your generated password will appear here")) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(pwd), null);
                JOptionPane.showMessageDialog(this, "Password copied to clipboard!");
            }
        });

        setVisible(true);
    }

    private void handleGenerate(ActionEvent e) {
        try {
            int length = Integer.parseInt(lengthField.getText());
            if (length < 4 || length > 64) {
                JOptionPane.showMessageDialog(this, "Password length must be between 4 and 64.");
                return;
            }

            boolean hasUpper = upperCaseCheck.isSelected();
            boolean hasLower = lowerCaseCheck.isSelected();
            boolean hasNumber = numbersCheck.isSelected();
            boolean hasSpecial = specialCheck.isSelected();

            if (!hasUpper && !hasLower && !hasNumber && !hasSpecial) {
                JOptionPane.showMessageDialog(this, "Please select at least one character type.");
                return;
            }

            String password = generatePassword(length, hasUpper, hasLower, hasNumber, hasSpecial);
            passwordField.setText(password);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private String generatePassword(int length, boolean upper, boolean lower, boolean number, boolean special) {
        StringBuilder characterPool = new StringBuilder();
        SecureRandom random = new SecureRandom();

        StringBuilder mandatory = new StringBuilder();
        if (upper) {
            characterPool.append(UPPER);
            mandatory.append(UPPER.charAt(random.nextInt(UPPER.length())));
        }
        if (lower) {
            characterPool.append(LOWER);
            mandatory.append(LOWER.charAt(random.nextInt(LOWER.length())));
        }
        if (number) {
            characterPool.append(NUMBERS);
            mandatory.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }
        if (special) {
            characterPool.append(SPECIAL);
            mandatory.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        }

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length - mandatory.length(); i++) {
            int index = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }

        // Combine and shuffle
        password.append(mandatory);
        return shuffle(password.toString());
    }

    private String shuffle(String input) {
        char[] characters = input.toCharArray();
        SecureRandom rnd = new SecureRandom();
        for (int i = characters.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordGenerator::new);
    }
}
