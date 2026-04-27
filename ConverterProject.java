package expressionconverter;

/**
 *
 * @author ldana
 */
import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class ConverterProject {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainInterface main = new MainInterface();
            main.setVisible(true);
        });
    }
}

class MainInterface extends JFrame {
    public MainInterface() {
        super("Main Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

       
        JLabel titleLabel = new JLabel("Expression converter & evaluator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        
        

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 20, 0));
        JButton infixButton = new JButton("Infix to Prefix");
        

        infixButton.addActionListener(e -> new InfixConverter().setVisible(true));
        

        buttonPanel.add(infixButton);
        
        gbc.gridy = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }
}

class InfixConverter extends JFrame {
    private JTextField inputField;
    private JLabel resultLabel;

    public InfixConverter() {
        super("Infix to Prefix Converter");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputField = new JTextField();
        JButton convertButton = new JButton("Convert to Prefix");
        JButton evaluateButton = new JButton("Evaluate");
        resultLabel = new JLabel("Result will appear here");

        convertButton.addActionListener(e -> convertInfixToPrefix());
        evaluateButton.addActionListener(e -> evaluatePrefix());

        panel.add(new JLabel("Enter Infix Expression:"));
        panel.add(inputField);
        panel.add(convertButton);
        panel.add(evaluateButton);
        panel.add(resultLabel);

        add(panel);
    }

    private void convertInfixToPrefix() {
        try {
            String infix = inputField.getText();
            String prefix = infixToPrefix(infix);
            resultLabel.setText("Prefix: " + prefix);
        } catch (Exception ex) {
            resultLabel.setText("Invalid Expression");
        }
    }

    private void evaluatePrefix() {
        try {
            String prefix = resultLabel.getText().replace("Prefix: ", "");
            double result = evaluatePrefixExpression(prefix);
            resultLabel.setText(resultLabel.getText() + " = " + result);
        } catch (Exception ex) {
            resultLabel.setText("Evaluation Error");
        }
    }

    private String infixToPrefix(String infix) {
        infix = new StringBuilder(infix).reverse().toString();
        infix = infix.replace('(', '@').replace(')', '(').replace('@', ')');

        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (char c : infix.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                postfix.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && precedence(c) < precedence(stack.peek())) {
                    postfix.append(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }

        return new StringBuilder(postfix.toString()).reverse().toString();
    }

    private int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    private double evaluatePrefixExpression(String prefix) {
        Stack<Double> stack = new Stack<>();
        String reversed = new StringBuilder(prefix).reverse().toString();

        for (char c : reversed.toCharArray()) {
            if (Character.isDigit(c)) {
                stack.push((double) (c - '0'));
            } else {
                double a = stack.pop();
                double b = stack.pop();
                switch (c) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/': stack.push(a / b); break;
                }
            }
        }
        return stack.pop();
    }
}
