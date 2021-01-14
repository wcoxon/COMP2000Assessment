import javax.swing.*;

public class AdminLoginPage extends Page {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    @Override
    public void newPage(KioskApp app) {
        pagePanel = new JPanel();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton();

        pagePanel.setLayout(null);
        pagePanel.add(usernameField);
        pagePanel.add(passwordField);
        pagePanel.add(loginButton);

        usernameField.setLocation(10,10);
        usernameField.setSize(100,25);

        passwordField.setLocation(10,70);
        passwordField.setSize(100,25);

        loginButton.setLocation(10,130);
        loginButton.setSize(100,25);
        loginButton.setText("log in");

        loginButton.addActionListener(actionEvent ->{

            app.authenticate(usernameField.getText(),app.hashSHA256(passwordField.getPassword(),app.getSalt(usernameField.getText())));

        });

    }
}
