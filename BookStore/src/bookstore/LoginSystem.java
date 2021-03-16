package bookstore;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LoginSystem implements ActionListener {

    static void main(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Panel loginPanel;
    private Frame frame;
    private TextField userTextField, passwordTextField, confirmPasswordTextField, emailTextField,
            firstNameTextField, lastNameTextField, addressTextField;
    private Button loginButton, signInButton, registerButton, backButton;
    private final Label errorLabel;

    private final UserDatabase userDatabase;
    private final GridBagConstraints gbc = new GridBagConstraints();
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        LoginSystem loginSystem = new LoginSystem();
    }

    /**
     * Create the application.
     */
    private LoginSystem() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = (int) screenSize.getWidth();
        SCREEN_HEIGHT = (int) screenSize.getHeight();

        userDatabase = new UserDatabase();
        errorLabel = new Label("");

        makeFrameOne();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void makeFrameOne() {

        frame = new Frame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("MainIcon.png"));
        frame.setTitle("Book Store: Login Screen");
        frame.setSize(400, 300);
        frame.setLocation(SCREEN_WIDTH / 2 - 200, SCREEN_HEIGHT / 2 - 150);
        frame.setVisible(true);
        frame.setResizable(false);

        loginPanel = new Panel();
        loginPanel.setLayout(new GridLayout(4, 1, 10, 10));

        //Title Label
        Label titleLabel = new Label("Login System");
        titleLabel.setBackground(Color.ORANGE);
        titleLabel.setAlignment(Label.CENTER);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 25));

        //UserName & Password Layout
        Panel middlePanel = new Panel();
        middlePanel.setLayout(new GridBagLayout());

        gbc.insets = new Insets(4, 4, 4, 4);
        //UserName Label
        Label userLabel = new Label("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        middlePanel.add(userLabel, gbc);

        //UserName TextFiled
        userTextField = new TextField();
        userTextField.setColumns(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        middlePanel.add(userTextField, gbc);

        //Password Label
        Label passwordLabel = new Label("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        middlePanel.add(passwordLabel, gbc);

        //Password TextFiled
        passwordTextField = new TextField();
        passwordTextField.setColumns(15);
        passwordTextField.setEchoChar('*');
        gbc.gridx = 1;
        gbc.gridy = 1;
        middlePanel.add(passwordTextField, gbc);

        //Button Layout
        Panel buttonLayout = new Panel();
        buttonLayout.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 0));

        //Login Button
        loginButton = new Button("Login");
        loginButton.addActionListener(this);
        buttonLayout.add(loginButton);

        //Sign In Button
        signInButton = new Button("Sign In");
        signInButton.addActionListener(this);
        buttonLayout.add(signInButton);

        //Error Label
        errorLabel.setAlignment(Label.CENTER);

        //Add to Login Panel
        loginPanel.add(titleLabel);
        loginPanel.add(middlePanel);
        loginPanel.add(buttonLayout);
        loginPanel.add(errorLabel);

        //Add to Frame
        frame.add(loginPanel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                userDatabase.Upload();
                System.exit(0);
            }
        });
    }

    private void makeFrameTwo() {
        frame = new Frame();
        frame.setTitle("Book Store: Register Screen");
        frame.setSize(400, 450);
        frame.setLocation(SCREEN_WIDTH / 2 - 200, SCREEN_HEIGHT / 2 - 225);
        frame.setVisible(true);
        frame.setResizable(false);

        loginPanel = new Panel();
        loginPanel.setLayout(new BorderLayout());

        //Back button
        Panel panelOne = new Panel(new FlowLayout(FlowLayout.LEFT));
        panelOne.setBackground(Color.ORANGE);
        backButton = new Button("Back");
        backButton.addActionListener(this);
        panelOne.add(backButton);

        //Title Label
        Label titleLabel = new Label("Register");
        titleLabel.setBackground(Color.ORANGE);
        titleLabel.setAlignment(Label.CENTER);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 25));
        panelOne.add(titleLabel);

        //Center Panel
        Panel centerPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 50, 0));

        //UserName & Password Layout
        Panel middlePanel = new Panel();
        middlePanel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(4, 4, 4, 4);

        //UserName Label
        Label userLabel = new Label("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        middlePanel.add(userLabel, gbc);

        //UserName TextFiled
        userTextField = new TextField();
        userTextField.setColumns(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        middlePanel.add(userTextField, gbc);

        //Password Label
        Label passwordLabel = new Label("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        middlePanel.add(passwordLabel, gbc);

        //Password TextFiled
        passwordTextField = new TextField();
        passwordTextField.setColumns(15);
        passwordTextField.setEchoChar('*');
        gbc.gridx = 1;
        gbc.gridy = 1;
        middlePanel.add(passwordTextField, gbc);

        //Confirm Password Label
        Label confirmPasswordLabel = new Label("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        middlePanel.add(confirmPasswordLabel, gbc);

        //Confirm Password TextField
        confirmPasswordTextField = new TextField();
        confirmPasswordTextField.setColumns(15);
        confirmPasswordTextField.setEchoChar('*');
        gbc.gridx = 1;
        gbc.gridy = 2;
        middlePanel.add(confirmPasswordTextField, gbc);

        //Email Label
        Label emailLabel = new Label("Email Address:", Font.BOLD);
        gbc.gridx = 0;
        gbc.gridy = 3;
        middlePanel.add(emailLabel, gbc);

        //Email TextField
        emailTextField = new TextField();
        emailTextField.setColumns(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        middlePanel.add(emailTextField, gbc);

        //First Name Label
        Label firstNameLabel = new Label("First Name:", Font.BOLD);
        gbc.gridx = 0;
        gbc.gridy = 4;
        middlePanel.add(firstNameLabel, gbc);

        //First Name TextField
        firstNameTextField = new TextField();
        firstNameTextField.setColumns(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        middlePanel.add(firstNameTextField, gbc);

        //Last Name Label
        Label lastNameLabel = new Label("Last Name:", Font.BOLD);
        gbc.gridx = 0;
        gbc.gridy = 5;
        middlePanel.add(lastNameLabel, gbc);

        //Last Name TextField
        lastNameTextField = new TextField();
        lastNameTextField.setColumns(15);
        gbc.gridx = 1;
        gbc.gridy = 5;
        middlePanel.add(lastNameTextField, gbc);

        //Address Label
        Label addressLabel = new Label("Address:", Font.BOLD);
        gbc.gridx = 0;
        gbc.gridy = 6;
        middlePanel.add(addressLabel, gbc);

        //Address TextField
        addressTextField = new TextField();
        addressTextField.setColumns(15);
        gbc.gridx = 1;
        gbc.gridy = 6;
        middlePanel.add(addressTextField, gbc);

        //Button Layout
        Panel buttonLayout = new Panel();
        buttonLayout.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 0));

        //Register Button
        registerButton = new Button("Register");
        registerButton.addActionListener(this);
        buttonLayout.add(registerButton);

        //Error Label
        errorLabel.setAlignment(Label.CENTER);

        //Add to Center Panel
        centerPanel.add(middlePanel);
        centerPanel.add(buttonLayout);

        //Add to Login Panel
        loginPanel.add(panelOne, BorderLayout.NORTH);
        loginPanel.add(centerPanel, BorderLayout.CENTER);
        loginPanel.add(errorLabel, BorderLayout.SOUTH);

        //Add to Frame
        frame.add(loginPanel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                userDatabase.Upload();
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object id = e.getSource();
        if (id == loginButton) {
            String user = userTextField.getText();
            String password = passwordTextField.getText();
            this.SearchUser(user, password);
            return;
        }
        if (id == signInButton) {
            frame.dispose();
            makeFrameTwo();
        }
        if (id == backButton) {
            frame.dispose();
            makeFrameOne();
        }
        if (id == registerButton) {
            RegisterAttempt(userTextField.getText(),
                    passwordTextField.getText(),
                    confirmPasswordTextField.getText(),
                    emailTextField.getText(),
                    firstNameTextField.getText(),
                    lastNameTextField.getText(),
                    addressTextField.getText());
        }
    }

    private void RegisterAttempt(String user, String password, String confirmPassword,
            String email, String firstName, String lastName, String address) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{8,20}$");
        Matcher matcher = pattern.matcher(user);

//        if (!matcher.find()) {
//            errorLabel.setText("User name must contain 8-20 letters and/or digits");
//            userTextField.setText("");
//            return;
//        }
//        for (User i : userDatabase.getUserDatabase()) {
//            if (i.isThisTheUser(user)) {
//                errorLabel.setText("Username already exists");
//                userTextField.setText("");
//                return;
//            }
//        }
//        pattern = Pattern.compile("^[a-zA-Z0-9]{8,20}$");
//        matcher = pattern.matcher(password);
//        if (!matcher.find()) {
//            errorLabel.setText("Password must contain 8-20 letters and/or digits");
//            passwordTextField.setText("");
//            confirmPasswordTextField.setText("");
//            return;
//        }
//        if (confirmPassword.equals("")) {
//            errorLabel.setText("You must confirm the password");
//            return;
//        }
//        if (!confirmPassword.equals(password)) {
//            errorLabel.setText("Password confirmed incorrectly");
//            confirmPasswordTextField.setText("");
//            return;
//        }
//        pattern = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
//        matcher = pattern.matcher(email);
//        if (!matcher.find()) {
//            errorLabel.setText("Invalid email");
//            emailTextField.setText("");
//            return;
//        }
//        pattern = Pattern.compile("^([(A-Z][a-z]*(\\s)?)+");
//        matcher = pattern.matcher(firstName);
//        if (!matcher.find()) {
//            errorLabel.setText("Invalid First name");
//            firstNameTextField.setText("");
//            return;
//        }
//        pattern = Pattern.compile("^([A-Z][a-z]*)");
//        matcher = pattern.matcher(lastName);
//        if (!matcher.find()) {
//            errorLabel.setText("Invalid Last Name");
//            lastNameTextField.setText("");
//            return;
//        }
//        if (address.equals("")) {
//            errorLabel.setText("You must enter a address");
//            return;
//        }
        userDatabase.addToUserDatabase(new User(user, password, email, firstName, lastName, address));
        frame.dispose();
        errorLabel.setText("Register Successful!");
        makeFrameOne();
    }

    private void SearchUser(String user, String password) {
        boolean userError = true;
        for (User i : userDatabase.getUserDatabase()) {
            if (i.isThisTheUser(user)) {
                userError = false;
                if (i.isThisThePassword(password)) {
                    userDatabase.Upload();
                    frame.dispose();
                    StoreHome.main(i);
                    return;
                }
            }
        }
        passwordTextField.setText("");
        if (userError) {
            userTextField.setText("");
            errorLabel.setText("Incorrect Username");
            return;
        }
        errorLabel.setText("Incorrect Password");
    }
}
