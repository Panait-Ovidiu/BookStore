package bookstore;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StoreHome implements ActionListener, MouseListener, ItemListener {

    private final Frame frame;
    private final Panel storeMainPanel;
    private Button logOutButton;
    private ImageComponent shoppingCartIcon, searchIcon;
    private Label userLabel;
    private final Label errorLabel;
    private final Label errorLabelTwo;
    private ArrayList<Panel> bookPanelList;
    private final BookDatabase bookDatabase;
    private final User user;

    //Book Details Frame
    private TextArea reviewTextArea;
    private Button addReviewButton, lowerButton, higherButton, addToCartButton;
    private Label totalLabel, quantityLabel, priceValue;
    private int selectedBook;

    //Shopping Cart
    private final ShoppingCart shoppingCart;
    private Button purchaseButton;
    private final ArrayList<Button> itemRemoveButtons;

    //Purchase Frame
    private Checkbox noDeliveryCheckbox, postMailCheckbox, courierCheckbox,
            bankAccountCheckbox, cardCheckbox, paySafeCheckbox;
    private boolean bankAccountSelected, cardSelected, paySafeSelected, emptyChoiceSelected, finishPurchase;
    private TextField pinTextField, expireDateTextField, codeTextField;
    private Choice bankChoice, cardChoice;
    private Label completePurchaseLabel;
    private Button confirmPurchase;
    private int extraCost;
    private String deliveryMethod, paymentMethod;

    //Admin
    private Button addBookButtonAdmin, backButton, addButtonAdmin;
    private TextField bookTitleTextField, authorTextField, yearTextField, pagesTextField, priceTextField;
    private TextArea descriptionTextArea;

    /**
     * Launch the application.
     */
    public static void main(User user) {
        StoreHome StoreHome = new StoreHome(user);
    }

    private StoreHome(User user) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        this.user = user;

        purchaseInitialization();
        shoppingCart = new ShoppingCart();
        itemRemoveButtons = new ArrayList<>();
        bookDatabase = new BookDatabase();
        errorLabel = new Label("");
        errorLabelTwo = new Label("");

        frame = new Frame();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("MainIcon.png"));
        frame.setSize(screenWidth, screenHeight);
        storeMainPanel = new Panel();

        storeHomeFrame();
    }

    private void purchaseInitialization() {
        //Purchase: Delivery Checkboxes
        noDeliveryCheckbox = new Checkbox();
        noDeliveryCheckbox.addItemListener(this);
        noDeliveryCheckbox.setState(true);
        postMailCheckbox = new Checkbox();
        postMailCheckbox.addItemListener(this);
        courierCheckbox = new Checkbox();
        courierCheckbox.addItemListener(this);

        //Purchase: Payment Method Checkboxes
        bankAccountCheckbox = new Checkbox();
        bankAccountCheckbox.addItemListener(this);
        cardCheckbox = new Checkbox();
        cardCheckbox.setState(true);
        cardSelected = true;
        cardCheckbox.addItemListener(this);
        paySafeCheckbox = new Checkbox();
        paySafeCheckbox.addItemListener(this);

        //Purchase: Bank Choices
        emptyChoiceSelected = true;
        bankChoice = new Choice();
        bankChoice.add("-");
        bankChoice.add("BCR");
        bankChoice.add("BRD");
        bankChoice.add("ING Bank");
        bankChoice.add("Transilvania Bank");
        bankChoice.add("Raiffeisen Bank");
        bankChoice.addItemListener(this);

        //Purchase: Card Choices
        cardChoice = new Choice();
        cardChoice.add("-");
        cardChoice.add("Mastercard");
        cardChoice.add("Visa");
        cardChoice.addItemListener(this);

        //Purchase: Confirm button
        confirmPurchase = new Button("Confirm Purchase");
        confirmPurchase.addActionListener(this);

        //Other
        deliveryMethod = "Delivery: Pick-up in Store x " + extraCost + " $";
        paymentMethod = "Payment by: Credit Cart";
        finishPurchase = false;
    }

    private void storeHomeFrame() {
        frame.setTitle("Store Home: Welcome !");
        storeMainPanel.setLayout(new BorderLayout());

        //Add Top Panel
        storeMainPanel.add(topPanel(true, false), BorderLayout.NORTH);

        //Add Center Panel
        storeMainPanel.add(makeBookPanelList(), BorderLayout.CENTER);

        storeMainPanel.validate();
        frame.add(storeMainPanel);
        frame.validate();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        frame.setResizable(true);
    }

    //Used by storeHomeFrame(), bookDetailsFrame(int i), shoppingCartFrame()
    private Panel topPanel(boolean searchPanelBoolean, boolean backButtonPanelBoolean) {
        //Top Panel Layout
        Panel topPanel = new Panel();
        topPanel.setBackground(Color.orange);
        topPanel.setLayout(new GridLayout(2, 2));

        //GridLayout 1 : Title
        Label title = new Label("Store Title");
        title.setAlignment(Label.RIGHT);
        title.setFont(new Font("", Font.BOLD, 25));
        topPanel.add(title);

        //GridLayout 1 : Shopping Cart Data
        Panel cartPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
        cartPanel.add(makeShoppingPanel());
        topPanel.add(cartPanel);

        if (searchPanelBoolean) {
            //GridLayout 2 : Search Bar
            Panel searchPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
            searchIcon = new ImageComponent("Search.png");
            searchIcon.addMouseListener(this);
            searchPanel.add(searchIcon);
            TextField searchTextField = new TextField(25);
            searchPanel.add(searchTextField);

            topPanel.add(searchPanel);
        }

        if (backButtonPanelBoolean) {
            //Back Button - Top Panel
            Panel backButtonPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
            backButton = new Button("Back");
            backButton.addActionListener(this);
            backButtonPanel.add(backButton);
            topPanel.add(backButtonPanel);
        }

        //Admin Button
        if (user.getUserName().equals("Administrator")) {
            Panel adminPanel = new Panel(new FlowLayout(FlowLayout.RIGHT));
            addBookButtonAdmin = new Button("Add Book");

            addBookButtonAdmin.addActionListener(this);
            adminPanel.add(addBookButtonAdmin);
            topPanel.add(adminPanel);
        }

        return topPanel;
    }

    //Used by topPanel()
    private Panel makeShoppingPanel() {
        Panel shoppingPanel;
        GridBagConstraints gbc = new GridBagConstraints();

        //Shopping Cart Data
        shoppingPanel = new Panel();
        shoppingPanel.setBackground(Color.ORANGE);
        shoppingPanel.setLayout(new GridBagLayout());

        //User Label
        userLabel = new Label(user.getUserName());
        userLabel.addMouseListener(this);
        gbc.gridx = 1;
        gbc.gridy = 0;
        shoppingPanel.add(userLabel, gbc);

        //Log Out Button
        logOutButton = new Button("Log Out");
        logOutButton.addActionListener(this);
        gbc.gridx = 2;
        gbc.gridy = 0;
        shoppingPanel.add(logOutButton, gbc);

        //Shopping Cart Image
        shoppingCartIcon = new ImageComponent("ShoppingCart.png");
        shoppingCartIcon.addMouseListener(this);
        gbc.gridx = 0;
        gbc.gridy = 1;
        shoppingPanel.add(shoppingCartIcon, gbc);

        //Books Label
        Label booksLabel = new Label("Books(" + shoppingCart.getNrOfBooks() + ")");
        gbc.gridx = 1;
        gbc.gridy = 1;
        shoppingPanel.add(booksLabel, gbc);

        //Price Label
        Label priceLabel = new Label("Price: " + shoppingCart.getTotalPrice() + " $");
        gbc.gridx = 2;
        gbc.gridy = 1;
        shoppingPanel.add(priceLabel, gbc);

        return shoppingPanel;
    }

    //Used by storeHomeFrame()
    private Panel makeBookPanelList() {
        int size = bookDatabase.getBookDatabaseNoModify().size();
        ArrayList<Book> bookList = bookDatabase.getBookDatabaseNoModify();

        //List Panel
        Panel bookListPanel = new Panel(new FlowLayout());
        bookPanelList = new ArrayList<>();

        ScrollPane scrollPane = new ScrollPane();
        Panel listPanel = new Panel();
        scrollPane.setPreferredSize(new Dimension(850, 500));
        listPanel.setLayout(new GridLayout(size, 1, 10, 10));
        for (int i = 0; i < size; i++) {
            bookPanelList.add(new BookPanelList(
                    bookList.get(i).getTitle(),
                    bookList.get(i).getAuthor(),
                    bookList.get(i).getYear(),
                    bookList.get(i).getPrice()).getBookListPanel());
            bookPanelList.get(i).addMouseListener(this);
            listPanel.add(bookPanelList.get(i));
        }
        scrollPane.add(listPanel);
        bookListPanel.add(scrollPane);

        return bookListPanel;
    }

    private void bookDetailsFrame(int i) {
        frame.setTitle("Store: Book Details");
        frame.removeAll();
        storeMainPanel.removeAll();

        //Add Top Panel
        storeMainPanel.add(topPanel(false, true), BorderLayout.NORTH);

        //Add Center Panel
        Panel centerPanel = new Panel(new GridLayout(1, 2, 5, 5));
        centerPanel.add(bookDetailsPanel(i));
        centerPanel.add(reviewPanel(i));
        storeMainPanel.add(centerPanel, BorderLayout.CENTER);

        storeMainPanel.validate();
        frame.add(storeMainPanel);
        frame.validate();
    }

    //Used by bookDetailsFrame(int i)
    private Panel bookDetailsPanel(int bookIndex) {
        selectedBook = bookIndex;
        Panel bookDetailsPanel = new Panel(new FlowLayout());

        //Center Panel
        Panel centerPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        //Shipping Label
        Label deliveryLabel = new Label("Delivered in 1-5 days");
        deliveryLabel.setBackground(Color.GREEN);
        deliveryLabel.setFont(new Font("Ariel", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(deliveryLabel, gbc);

        //Title Label
        Label titleLabel = new Label("Book Details");
        titleLabel.setBackground(Color.ORANGE);
        titleLabel.setAlignment(Label.CENTER);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(titleLabel, gbc);

        //1.Book Title Label
        Label bookTitleLabel = new Label("Book Title:");
        bookTitleLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(bookTitleLabel, gbc);

        //1.Book Title Value
        Label bookTitleValue = new Label(bookDatabase.getBookDatabaseNoModify().get(bookIndex).getTitle());
        bookTitleValue.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(bookTitleValue, gbc);

        //2.Book Author Label
        Label authorLabel = new Label("Author:");
        authorLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(authorLabel, gbc);

        //2.Book Author Value
        Label authorValue = new Label(bookDatabase.getBookDatabaseNoModify().get(bookIndex).getAuthor());
        authorValue.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(authorValue, gbc);

        //3.Book Year Label
        Label yearLabel = new Label("Year:");
        yearLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(yearLabel, gbc);

        //3.Book Year Value
        Label yearValue = new Label(String.valueOf(bookDatabase.getBookDatabaseNoModify().get(bookIndex).getYear()));
        yearValue.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(yearValue, gbc);

        //4.Book Pages Label
        Label pagesLabel = new Label("Pages:");
        pagesLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(pagesLabel, gbc);

        //4.Book Pages Value
        Label pagesValue = new Label(String.valueOf(bookDatabase.getBookDatabaseNoModify().get(bookIndex).getPages()));
        pagesValue.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(pagesValue, gbc);

        //5.Book Description Label
        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(descriptionLabel, gbc);

        //5.Book Description Value
        TextArea descriptionValue = new TextArea(bookDatabase.getBookDatabaseNoModify().get(bookIndex).getDescription(),
                5, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        descriptionValue.setFont(new Font("Ariel", Font.BOLD, 16));
        descriptionValue.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(descriptionValue, gbc);

        //6.Book Price Label
        Label priceLabel = new Label("Price:");
        priceLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.LINE_END;
        centerPanel.add(priceLabel, gbc);

        //6.Book Price Value
        priceValue = new Label(String.valueOf(bookDatabase.getBookDatabaseNoModify().get(bookIndex).getPrice()));
        priceValue.setFont(new Font("Ariel", Font.BOLD, 19));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.LINE_START;
        centerPanel.add(priceValue, gbc);

        //7.Button Layout
        Panel buttonLayout = new Panel(new FlowLayout(FlowLayout.CENTER));

        //Lower Button
        lowerButton = new Button("-");
        lowerButton.setFont(new Font("Ariel", Font.BOLD, 16));
        lowerButton.addActionListener(this);
        buttonLayout.add(lowerButton);

        //Quantity Label
        quantityLabel = new Label("1");
        quantityLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        buttonLayout.add(quantityLabel);

        //Higher Button
        higherButton = new Button("+");
        higherButton.setFont(new Font("Ariel", Font.BOLD, 16));
        higherButton.addActionListener(this);
        buttonLayout.add(higherButton);

        //6. AddToCart Button
        addToCartButton = new Button("Add To Cart");
        addToCartButton.setFont(new Font("Ariel", Font.BOLD, 16));
        addToCartButton.addActionListener(this);
        buttonLayout.add(addToCartButton);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(buttonLayout, gbc);

        //7.Total Label
        totalLabel = new Label("Total: " + String.valueOf(bookDatabase.getBookDatabaseNoModify().get(bookIndex).getPrice()));
        totalLabel.setBackground(Color.ORANGE);
        totalLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        gbc.gridx = 1;
        gbc.gridy = 8;
        centerPanel.add(totalLabel, gbc);

        bookDetailsPanel.add(centerPanel);

        return bookDetailsPanel;
    }

    //Used by bookDetailsFrame(int i)
    private Panel reviewPanel(int bookIndex) {

        int size = bookDatabase.getBookDatabaseNoModify().get(bookIndex).getReviews().size();
        ArrayList<Review> reviews = bookDatabase.getBookDatabaseNoModify().get(bookIndex).getReviews();

        //List Panel
        Panel reviewPanel = new Panel(new FlowLayout());
        ArrayList<Panel> reviewPanels = new ArrayList<>();

        ScrollPane scrollPane = new ScrollPane();
        Panel listPanel = new Panel();
        scrollPane.setPreferredSize(new Dimension(500, 400));
        listPanel.setLayout(new GridLayout(size, 1, 10, 10));
        for (int j = 0; j < size; j++) {
            reviewPanels.add(new ReviewsPanelList(
                    reviews.get(j).getUser(),
                    reviews.get(j).getDate(),
                    reviews.get(j).getMessage()).getReviewsPanel());

            listPanel.add(reviewPanels.get(j));
        }
        scrollPane.add(listPanel);
        reviewPanel.add(scrollPane);

        Panel mainPanel = new Panel(new FlowLayout());

        //Review TextArea
        reviewTextArea = new TextArea(4, 69);
        reviewTextArea.setEditable(true);
        mainPanel.add(reviewPanel);
        mainPanel.add(reviewTextArea);

        //Add review Button
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        addReviewButton = new Button("Add Review");
        addReviewButton.addActionListener(this);
        buttonPanel.add(addReviewButton);
        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private void shoppingCartFrame() {
        frame.setTitle("Store: Shopping Cart");
        frame.removeAll();
        storeMainPanel.removeAll();

        //Add Top Panel
        storeMainPanel.add(topPanel(false, true), BorderLayout.NORTH);

        //Add Center Panel
        storeMainPanel.add(cartPanelList(), BorderLayout.CENTER);

        storeMainPanel.validate();
        frame.add(storeMainPanel);
        frame.validate();
    }

    //Used by shoppingCartFrame()
    private Panel cartPanelList() {
        int size = shoppingCart.getItems().size();

        ArrayList<Item> items = shoppingCart.getItems();

        //List Panel
        Panel itemListPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 250, 1));
        ArrayList<Panel> itemPanel = new ArrayList<>();

        ScrollPane scrollPane = new ScrollPane();
        Panel listPanel = new Panel();
        scrollPane.setPreferredSize(new Dimension(1250, 500));
        listPanel.setLayout(new GridLayout(size, 1, 10, 10));
        for (int i = 0; i < size; i++) {
            itemPanel.add(new ShoppingCartPanelList(
                    items.get(i).getBook().getTitle(),
                    items.get(i).getBook().getAuthor(),
                    items.get(i).getBook().getYear(),
                    items.get(i).getBook().getPrice(),
                    items.get(i).getQuantity(),
                    i).getBookListPanel());
            itemRemoveButtons.get(i).addActionListener(this);
            listPanel.add(itemPanel.get(i));
        }
        scrollPane.add(listPanel);
        itemListPanel.add(scrollPane);

        //Order Summary Panel
        Panel bottomPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        //Books Label
        Label booksNrLabel = new Label("Books: " + shoppingCart.getNrOfBooks());
        booksNrLabel.setBackground(Color.ORANGE);
        booksNrLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(booksNrLabel, gbc);

        //Total Label
        Label totalLabel = new Label("Total: " + shoppingCart.getTotalPrice());
        totalLabel.setBackground(Color.ORANGE);
        totalLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        gbc.gridx = 1;
        gbc.gridy = 0;
        bottomPanel.add(totalLabel, gbc);

        //Purchase Button
        purchaseButton = new Button("Purchase");
        purchaseButton.setFont(new Font("Ariel", Font.BOLD, 24));
        purchaseButton.setBackground(Color.YELLOW);
        purchaseButton.addActionListener(this);
        gbc.gridx = 2;
        gbc.gridy = 0;
        bottomPanel.add(purchaseButton, gbc);

        //Error Label
        Panel errorPanel = new Panel(new FlowLayout());
        errorLabel.setAlignment(Label.CENTER);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        errorPanel.add(errorLabel);
        bottomPanel.add(errorPanel, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NONE;

        itemListPanel.add(bottomPanel);

        return itemListPanel;
    }

    private void purchaseFrame() {
        frame.setTitle("Store: Purchase");
        frame.removeAll();
        storeMainPanel.removeAll();

        if (!finishPurchase) {
            //Add Top Panel
            storeMainPanel.add(topPanel(false, true), BorderLayout.NORTH);
            //Add Left Panel
            Panel leftPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.add(deliverySelectPanel());
            storeMainPanel.add(leftPanel, BorderLayout.WEST);

            //Add Center Panel
            Panel centerPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
            centerPanel.add(payMethodSelectPanel());
            centerPanel.add(payMethodFillOutPanel());
            centerPanel.add(orderSummeryPanel());
            storeMainPanel.add(centerPanel, BorderLayout.CENTER);

            storeMainPanel.validate();
            frame.add(storeMainPanel);
            frame.validate();
        } else {
            //Add Center Panel
            Panel centerPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.add(finishPurchasePanel());
            storeMainPanel.add(centerPanel, BorderLayout.CENTER);

            storeMainPanel.validate();
            frame.add(storeMainPanel);
            frame.validate();

            purchaseAnimation();

            //Add Top Panel
            storeMainPanel.add(topPanel(false, true), BorderLayout.NORTH);
            storeMainPanel.validate();
            frame.validate();
        }
    }

    //Used by purchaseFrame()
    private Panel deliverySelectPanel() {
        Panel deliveryPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(100, 2, 50, 2);

        //Title Label
        Panel titlePanel = new Panel(new FlowLayout());
        titlePanel.setBackground(Color.ORANGE);
        Label titleLabel = new Label("Choose Delivery Method:");
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        deliveryPanel.add(titlePanel, gbc);
        gbc.insets = new Insets(2, 2, 50, 2);

        //No Delivery Label
        Label noDeliveryLabel = new Label("Pick-up in Store: 0 $");
        noDeliveryLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        deliveryPanel.add(noDeliveryLabel, gbc);

        //No Delivery CheckBox Options
        gbc.gridx = 1;
        gbc.gridy = 1;
        deliveryPanel.add(noDeliveryCheckbox, gbc);

        //Mail  Label
        Label postMailLabel = new Label("Post by Mail: 5 $");
        postMailLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        deliveryPanel.add(postMailLabel, gbc);

        //Mail CheckBox Options
        gbc.gridx = 1;
        gbc.gridy = 2;
        deliveryPanel.add(postMailCheckbox, gbc);

        //Courier  Label
        Label courierLabel = new Label("Sent by Courier: 10 $");
        courierLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        deliveryPanel.add(courierLabel, gbc);

        //Courier CheckBox Options
        gbc.gridx = 1;
        gbc.gridy = 3;
        deliveryPanel.add(courierCheckbox, gbc);

        return deliveryPanel;
    }

    //Used by purchaseFrame()
    private Panel payMethodSelectPanel() {
        Panel payMethodPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(100, 2, 50, 2);

        //Title Label
        Panel titlePanel = new Panel(new FlowLayout());
        titlePanel.setBackground(Color.ORANGE);
        Label titleLabel = new Label("Choose Payment Method:");
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        payMethodPanel.add(titlePanel, gbc);
        gbc.insets = new Insets(2, 2, 50, 2);

        //Bank Account Label
        Label bankAccountLabel = new Label("Bank Account");
        bankAccountLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        payMethodPanel.add(bankAccountLabel, gbc);

        //Bank Account CheckBox Options
        gbc.gridx = 1;
        gbc.gridy = 1;
        payMethodPanel.add(bankAccountCheckbox, gbc);

        //Card Label
        Label cardLabel = new Label("Card");
        cardLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        payMethodPanel.add(cardLabel, gbc);

        //Card CheckBox Options
        gbc.gridx = 1;
        gbc.gridy = 2;
        payMethodPanel.add(cardCheckbox, gbc);

        //Pay Safe Label
        Label paySafeLabel = new Label("PaySafe");
        paySafeLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        payMethodPanel.add(paySafeLabel, gbc);

        //Pay Safe CheckBox Options
        gbc.gridx = 1;
        gbc.gridy = 3;
        payMethodPanel.add(paySafeCheckbox, gbc);

        return payMethodPanel;
    }

    //Used by purchaseFrame()
    private Panel payMethodFillOutPanel() {
        Panel payMethodPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(75, 2, 50, 2);

        //Bank Account Selected
        if (bankAccountSelected) {
            //Title Label
            Panel titlePanel = new Panel(new FlowLayout());
            titlePanel.setBackground(Color.ORANGE);
            Label titleLabel = new Label("Fill Out The Information:");
            titleLabel.setFont(new Font("Ariel", Font.BOLD, 18));
            titlePanel.add(titleLabel);
            gbc.gridx = 1;
            gbc.gridy = 0;
            payMethodPanel.add(titlePanel, gbc);
            gbc.insets = new Insets(2, 2, 25, 2);

            //Bank Label
            Label bankLabel = new Label("Choose Bank:");
            bankLabel.setFont(new Font("Ariel", Font.BOLD, 12));
            gbc.gridx = 0;
            gbc.gridy = 1;
            payMethodPanel.add(bankLabel, gbc);

            //Bank Choice
            gbc.gridx = 1;
            gbc.gridy = 1;
            payMethodPanel.add(bankChoice, gbc);

            if (!emptyChoiceSelected) {
                //ID Label
                Label bankIdIdLabel = new Label("IBAN:");
                bankIdIdLabel.setFont(new Font("Ariel", Font.BOLD, 12));
                gbc.gridx = 0;
                gbc.gridy = 2;
                payMethodPanel.add(bankIdIdLabel, gbc);

                //Bank ID TextField
                pinTextField = new TextField();
                pinTextField.setColumns(30);
                gbc.gridx = 1;
                gbc.gridy = 2;
                payMethodPanel.add(pinTextField, gbc);

                //Confirm Purchase Button
                gbc.gridx = 1;
                gbc.gridy = 3;
                payMethodPanel.add(confirmPurchase, gbc);

                //Error Label
                errorLabel.setAlignment(Label.CENTER);
                gbc.gridx = 1;
                gbc.gridy = 4;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.CENTER;
                payMethodPanel.add(errorLabel, gbc);

                //Error Two Label
                errorLabelTwo.setAlignment(Label.CENTER);
                gbc.gridx = 1;
                gbc.gridy = 5;
                payMethodPanel.add(errorLabelTwo, gbc);

                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.NONE;
            }
        }

        //Card Selected
        if (cardSelected) {
            //Title Label
            Panel titlePanel = new Panel(new FlowLayout());
            titlePanel.setBackground(Color.ORANGE);
            Label titleLabel = new Label("Fill Out The Information:");
            titleLabel.setFont(new Font("Ariel", Font.BOLD, 18));
            titlePanel.add(titleLabel);
            gbc.gridx = 1;
            gbc.gridy = 0;
            payMethodPanel.add(titlePanel, gbc);
            gbc.insets = new Insets(2, 2, 25, 2);

            //Card Label
            Label cardLabel = new Label("Choose Card Type:");
            cardLabel.setFont(new Font("Ariel", Font.BOLD, 12));
            gbc.gridx = 0;
            gbc.gridy = 1;
            payMethodPanel.add(cardLabel, gbc);

            //Card Choice
            gbc.gridx = 1;
            gbc.gridy = 1;
            payMethodPanel.add(cardChoice, gbc);

            if (!emptyChoiceSelected) {
                //ID Label
                Label cardIdLabel = new Label(cardChoice.getSelectedItem() + " ID:");
                cardIdLabel.setFont(new Font("Ariel", Font.BOLD, 12));
                gbc.gridx = 0;
                gbc.gridy = 2;
                payMethodPanel.add(cardIdLabel, gbc);

                //Card ID TextField
                pinTextField = new TextField();
                pinTextField.setColumns(30);
                gbc.gridx = 1;
                gbc.gridy = 2;
                payMethodPanel.add(pinTextField, gbc);

                //Expiration Date Label
                Label expireLabel = new Label("Expiration Date:");
                expireLabel.setFont(new Font("Ariel", Font.BOLD, 12));
                gbc.gridx = 0;
                gbc.gridy = 3;
                payMethodPanel.add(expireLabel, gbc);

                //Expiration Date TextField
                expireDateTextField = new TextField();
                expireDateTextField.setColumns(30);
                gbc.gridx = 1;
                gbc.gridy = 3;
                payMethodPanel.add(expireDateTextField, gbc);

                //Confirm Purchase Button
                gbc.gridx = 1;
                gbc.gridy = 4;
                payMethodPanel.add(confirmPurchase, gbc);

                //Error Label
                errorLabel.setAlignment(Label.CENTER);
                gbc.gridx = 1;
                gbc.gridy = 5;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.CENTER;
                payMethodPanel.add(errorLabel, gbc);

                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.NONE;
            }
        }

        //Safe Pay Selected
        if (paySafeSelected) {
            //Title Label
            Panel titlePanel = new Panel(new FlowLayout());
            titlePanel.setBackground(Color.ORANGE);
            Label titleLabel = new Label("Please Fill Out:");
            titleLabel.setFont(new Font("Ariel", Font.BOLD, 18));
            titlePanel.add(titleLabel);
            gbc.gridx = 1;
            gbc.gridy = 0;
            payMethodPanel.add(titlePanel, gbc);
            gbc.insets = new Insets(2, 2, 25, 2);

            //Safe Pay Label
            Label paySafeLabel = new Label("Pay Safe Code:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            payMethodPanel.add(paySafeLabel, gbc);

            //Safe Pay TextFiled
            codeTextField = new TextField();
            codeTextField.setColumns(30);
            gbc.gridx = 1;
            gbc.gridy = 1;
            payMethodPanel.add(codeTextField, gbc);

            //Confirm Purchase Button
            gbc.gridx = 1;
            gbc.gridy = 2;
            payMethodPanel.add(confirmPurchase, gbc);

            //Error Label
            errorLabel.setAlignment(Label.CENTER);
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.CENTER;
            payMethodPanel.add(errorLabel, gbc);

            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.NONE;
        }

        return payMethodPanel;
    }

    //Used by purchaseFrame()
    private Panel orderSummeryPanel() {
        Panel orderSummeryPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(50, 2, 2, 2);

        //Title Label
        Panel titlePanel = new Panel(new FlowLayout());
        titlePanel.setBackground(Color.ORANGE);
        Label titleLabel = new Label("Order Summary");
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        orderSummeryPanel.add(titlePanel, gbc);
        gbc.insets = new Insets(2, 2, 2, 2);

        //Name Label
        Label nameLabel = new Label("Name: " + user.getLastName() + " " + user.getFirstName());
        nameLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        orderSummeryPanel.add(nameLabel, gbc);

        //Address  Label
        Label addressLabel = new Label("Address: " + user.getAddress());
        addressLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        orderSummeryPanel.add(addressLabel, gbc);

        //Items  Label
        TextArea itemsTextArea = new TextArea("", 8, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        itemsTextArea.setBackground(Color.WHITE);
        itemsTextArea.setFont(new Font("Ariel", Font.BOLD, 16));
        itemsTextArea.setEditable(false);
        for (Item item : shoppingCart.getItems()) {
            Double itemTotal = (item.getBook().getPrice() * item.getQuantity());
            itemsTextArea.append(item.getBook().getTitle() + "\n    x "
                    + item.getQuantity() + " = "
                    + String.format("%.2f $", itemTotal) + "\n\n");
        }
        gbc.gridx = 0;
        gbc.gridy = 3;
        orderSummeryPanel.add(itemsTextArea, gbc);

        //Delivery Label
        Label deliveryLabel = new Label(deliveryMethod);
        deliveryLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        orderSummeryPanel.add(deliveryLabel, gbc);

        //Payment Label
        Label paymentLabel = new Label(paymentMethod);
        paymentLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 5;
        orderSummeryPanel.add(paymentLabel, gbc);

        //Total Label
        double total = Double.parseDouble(shoppingCart.getTotalPrice()) + extraCost;
        Label totalLabel = new Label("Total: " + String.format("%.2f $", total));
        totalLabel.setFont(new Font("Ariel", Font.BOLD, 20));
        totalLabel.setBackground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        orderSummeryPanel.add(totalLabel, gbc);

        return orderSummeryPanel;
    }

    //Used by purchaseFrame()
    private Panel finishPurchasePanel() {
        Panel finishPurchasePanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(250, 2, 50, 2);

        //Title Label
        Panel titlePanel = new Panel(new FlowLayout());
        Label titleLabel = new Label("Completing Purchase");
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 50));
        titlePanel.add(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        finishPurchasePanel.add(titlePanel, gbc);
        gbc.insets = new Insets(2, 2, 50, 2);

        //Loading Label
        completePurchaseLabel = new Label();
        completePurchaseLabel.setText("Loading...");
        completePurchaseLabel.setBackground(Color.ORANGE);
        completePurchaseLabel.setFont(new Font("Ariel", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        finishPurchasePanel.add(completePurchaseLabel, gbc);

        return finishPurchasePanel;
    }

    private void purchaseAnimation() {
        int procent = 0;
        while (procent < 15) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            frame.repaint();
            procent++;
            switch (procent % 4) {
                case 0: {
                    completePurchaseLabel.setText("Loading");
                    break;
                }
                case 1: {
                    completePurchaseLabel.setText("Loading.");
                    break;
                }
                case 2: {
                    completePurchaseLabel.setText("Loading..");
                    break;
                }
                case 3: {
                    completePurchaseLabel.setText("Loading...");
                    break;
                }
            }

        }
        completePurchaseLabel.setText("Complete!");
    }

    private void adminFrame() {
        frame.setTitle("Admin Panel: Add Book Screen");
        frame.removeAll();
        storeMainPanel.removeAll();

        //Back Button - Top Panel
        Panel topPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.ORANGE);
        backButton = new Button("Back");
        backButton.addActionListener(this);
        topPanel.add(backButton);

        //Add Top Panel
        storeMainPanel.add(topPanel(false, true), BorderLayout.NORTH);

        //Add Center Panel
        storeMainPanel.add(addBookPanel(), BorderLayout.CENTER);

        storeMainPanel.validate();
        frame.add(storeMainPanel);
        frame.validate();
    }

    //Used by adminFrame()
    private Panel addBookPanel() {
        Panel addBookPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        //Title Label
        Label titleLabel = new Label("Add Book Details");
        titleLabel.setBackground(Color.ORANGE);
        titleLabel.setAlignment(Label.CENTER);
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        addBookPanel.add(titleLabel, gbc);

        //1.Book Title Label
        Label bookTitleLabel = new Label("Book Title:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        addBookPanel.add(bookTitleLabel, gbc);

        //1.Book Title TextFiled
        bookTitleTextField = new TextField();
        bookTitleTextField.setColumns(30);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        addBookPanel.add(bookTitleTextField, gbc);

        //2.Book Author Label
        Label authorLabel = new Label("Author:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        addBookPanel.add(authorLabel, gbc);

        //2.Book Author TextFiled
        authorTextField = new TextField();
        authorTextField.setColumns(30);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        addBookPanel.add(authorTextField, gbc);

        //3.Book Year Label
        Label yearLabel = new Label("Year:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        addBookPanel.add(yearLabel, gbc);

        //3.Book Year TextFiled
        yearTextField = new TextField();
        yearTextField.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        addBookPanel.add(yearTextField, gbc);

        //4.Book Pages Label
        Label pagesLabel = new Label("Pages:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        addBookPanel.add(pagesLabel, gbc);

        //4.Book Pages TextFiled
        pagesTextField = new TextField();
        pagesTextField.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        addBookPanel.add(pagesTextField, gbc);

        //5.Book Description Label
        Label descriptionLabel = new Label("Description:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        addBookPanel.add(descriptionLabel, gbc);

        //5.Book Description TextArea
        descriptionTextArea = new TextArea(6, 60);
        descriptionTextArea.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_START;
        addBookPanel.add(descriptionTextArea, gbc);

        //6.Book Price Label
        Label priceLabel = new Label("Price:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.LINE_END;
        addBookPanel.add(priceLabel, gbc);

        //6.Book Price TextFiled
        priceTextField = new TextField();
        priceTextField.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.LINE_START;
        addBookPanel.add(priceTextField, gbc);

        //Button Layout
        Panel buttonLayout = new Panel(new FlowLayout(FlowLayout.CENTER));

        //Add Book Button
        addButtonAdmin = new Button("Add Book");
        addButtonAdmin.addActionListener(this);
        buttonLayout.add(addButtonAdmin);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.LINE_END;
        addBookPanel.add(buttonLayout, gbc);

        //Error Label
        errorLabel.setAlignment(Label.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        addBookPanel.add(errorLabel, gbc);

        return addBookPanel;
    }

    private void RegisterBookAttempt(String title, String author, String year, String pages, String description, String price) {
        for (Book book : bookDatabase.getBookDatabaseNoModify()) {
            if (book.isThisTheBook(title, author)) {
                errorLabel.setText("Book already in the database");
                bookTitleTextField.setText("");
                return;
            }
        }
        int yearValue;
        try {
            yearValue = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            errorLabel.setText("Year must be a number");
            yearTextField.setText("");
            return;
        }
        int pagesValue;
        try {
            pagesValue = Integer.parseInt(pages);
        } catch (NumberFormatException e) {
            errorLabel.setText("Pages must be a number");
            pagesTextField.setText("");
            return;
        }
        double priceValue;
        try {
            priceValue = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            errorLabel.setText("Price must be a number");
            priceTextField.setText("");
            return;
        }
        bookDatabase.addToBookDatabase(new Book(title, author, yearValue, pagesValue, description, priceValue));
        bookDatabase.Upload();
        bookTitleTextField.setText("");
        authorTextField.setText("");
        yearTextField.setText("");
        pagesTextField.setText("");
        descriptionTextArea.setText("");
        priceTextField.setText("");
        errorLabel.setText("Book was added to database");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        errorLabel.setText("");
        finishPurchase = false;
        Object id = e.getSource();

        if (id == logOutButton) {
            frame.dispose();
            LoginSystem.main(null);
        }
        if (id == backButton) {
            frame.removeAll();
            storeMainPanel.removeAll();
            bookPanelList.clear();
            storeHomeFrame();
        }

        //Book Details Frame
        if (id == lowerButton) {
            int number = Integer.parseInt(quantityLabel.getText());
            if (number > 1) {
                quantityLabel.setText(String.valueOf(--number));
                double total = Double.parseDouble(priceValue.getText()) * number;
                totalLabel.setText("Total: " + String.valueOf(total));
            }
        }
        if (id == higherButton) {
            int number = Integer.parseInt(quantityLabel.getText());
            quantityLabel.setText(String.valueOf(++number));
            double total = Double.parseDouble(priceValue.getText()) * number;
            totalLabel.setText("Total: " + String.valueOf(total));
        }
        if (id == addToCartButton) {
            shoppingCart.addToCart(bookDatabase.getBookDatabaseNoModify().get(selectedBook),
                    Integer.parseInt(quantityLabel.getText()));
            bookDetailsFrame(selectedBook);

        }
        if (id == addReviewButton) {
            String review = reviewTextArea.getText();
            if (!review.equals("")) {
                addReview();
            }
        }

        //Purchase Frame
        if (id == confirmPurchase && bankAccountCheckbox.getState()) {
            if (checkBankInfo()) {
                finishPurchase = true;
                shoppingCart.getItems().clear();
                purchaseFrame();
            } else {
                purchaseFrame();
            }
        }
        if (id == confirmPurchase && cardCheckbox.getState()) {
            if (checkCardInfo()) {
                finishPurchase = true;
                shoppingCart.getItems().clear();
                purchaseFrame();
            } else {
                purchaseFrame();
            }
        }
        if (id == confirmPurchase && paySafeCheckbox.getState()) {
            if (checkPaySafeInfo()) {
                finishPurchase = true;
                shoppingCart.getItems().clear();
                purchaseFrame();
            } else {
                purchaseFrame();
            }
        }

        //Shopping Cart Frame
        if (id == purchaseButton) {
            if (shoppingCart.getItems().size() > 0) {
                purchaseFrame();
            } else {
                errorLabel.setText("Add items to the Cart first.");
                shoppingCartFrame();
            }
        }
        for (int i = 0; i < itemRemoveButtons.size(); i++) {
            if (id == itemRemoveButtons.get(i)) {
                itemRemoveButtons.remove(itemRemoveButtons.get(i));
                shoppingCart.removeFromCar(shoppingCart.getItems().get(i));
                shoppingCartFrame();
            }
        }

        //Admin Frame
        if (id == addBookButtonAdmin) {
            adminFrame();
        }
        if (id == addButtonAdmin) {
            RegisterBookAttempt(bookTitleTextField.getText(),
                    authorTextField.getText(),
                    yearTextField.getText(),
                    pagesTextField.getText(),
                    descriptionTextArea.getText(),
                    priceTextField.getText());
        }
    }

    private boolean checkBankInfo() {
        String bankID = pinTextField.getText();
        Pattern pattern = Pattern.compile("^[A-Z]{2}[0-9]{2}[0-9A-Z]{10}$");
        Matcher matcher = pattern.matcher(bankID);

        if (!matcher.find()) {
            errorLabel.setText("IBAN pattern: LLDDXXXXXXXXXX");
            errorLabelTwo.setText("L = Caps letter |  D = digit |  X = L/D");
            pinTextField.setText("");
            return false;
        }
        return true;
    }

    private boolean checkCardInfo() {
        String cardID = pinTextField.getText();
        String expireDate = expireDateTextField.getText();

        Pattern pattern = Pattern.compile("^[0-9]{4}[-][0-9]{4}[-][0-9]{4}[-][0-9]{4}$");
        Matcher matcher = pattern.matcher(cardID);

        if (!matcher.find()) {
            errorLabel.setText("ID pattern: XXXX-XXXX-XXXX-XXXX");
            pinTextField.setText("");
            return false;
        }

        pattern = Pattern.compile("^(([012])[1-9]|3(01))[-](0[1-9]|1(012))[-][1-9][0-9]{3}$");
        matcher = pattern.matcher(expireDate);

        if (!matcher.find()) {
            errorLabel.setText("Expiration date pattern: DD-MM-YYYY");
            expireDateTextField.setText("");
            return false;
        }

        return true;
    }

    private boolean checkPaySafeInfo() {
        String code = codeTextField.getText();
        Pattern pattern = Pattern.compile("^[0-9]{4}[-][0-9]{4}[-][0-9]{4}[-][0-9]{4}$");
        Matcher matcher = pattern.matcher(code);

        if (!matcher.find()) {
            errorLabel.setText("Code pattern: XXXX-XXXX-XXXX-XXXX");
            codeTextField.setText("");
            return false;
        }
        return true;
    }

    private void addReview() {
        Date localDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        String date = dateFormat.format(localDate);

        bookDatabase.getBookDatabaseModify().get(selectedBook)
                .addReview(new Review(user.getUserName(), reviewTextArea.getText(), date));
        bookDatabase.Upload();

        bookDetailsFrame(selectedBook);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object id = e.getSource();
        if (id == userLabel) {
            System.out.println("Username: Clicked");
        }
        if (id == shoppingCartIcon) {
            shoppingCartFrame();
        }
        if (id == searchIcon) {
            System.out.println("Search: Clicked");
        }
        for (int i = 0; i < bookPanelList.size(); i++) {
            if (id == bookPanelList.get(i)) {
                bookDetailsFrame(i);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        errorLabel.setText("");
        Object id = e.getSource();

        //Delivery Checkboxes
        if (id == noDeliveryCheckbox) {
            extraCost = 0;
            deliveryMethod = "Pick-up in Store x " + extraCost + " $";
            deliveryMethodSelect(true, false, false);
            purchaseFrame();
        }
        if (id == postMailCheckbox) {
            extraCost = 5;
            deliveryMethod = "Post by Mail x " + extraCost + " $";
            deliveryMethodSelect(false, true, false);
            purchaseFrame();
        }
        if (id == courierCheckbox) {
            extraCost = 10;
            deliveryMethod = "Sent by Courier x " + extraCost + " $";
            deliveryMethodSelect(false, false, true);
            purchaseFrame();
        }

        //Payment Checkboxes
        if (id == bankAccountCheckbox) {
            paymentMethod = "Payment by: Bank Account";
            paymentMethodSelect(true, false, false);
            purchaseFrame();
        }
        if (id == cardCheckbox) {
            paymentMethod = "Payment by: Credit Cart";
            paymentMethodSelect(false, true, false);
            purchaseFrame();
        }
        if (id == paySafeCheckbox) {
            paymentMethod = "Payment by: Pay Safe";
            paymentMethodSelect(false, false, true);
            purchaseFrame();
        }

        //Payment Bank Choices
        if (id == bankChoice) {
            if (bankChoice.getSelectedItem().equals("-")) {
                emptyChoiceSelected = true;
                purchaseFrame();
            }
            if (!bankChoice.getSelectedItem().equals("-")) {
                emptyChoiceSelected = false;
                purchaseFrame();
            }
        }

        //Payment Card Choices
        if (id == cardChoice) {
            if (cardChoice.getSelectedItem().equals("-")) {
                emptyChoiceSelected = true;
                purchaseFrame();
            }
            if (!cardChoice.getSelectedItem().equals("-")) {
                emptyChoiceSelected = false;
                purchaseFrame();
            }
        }
    }

    private void deliveryMethodSelect(boolean noDelivery, boolean postMail, boolean courier) {
        noDeliveryCheckbox.setState(noDelivery);
        postMailCheckbox.setState(postMail);
        courierCheckbox.setState(courier);
    }

    private void paymentMethodSelect(boolean bankAccount, boolean card, boolean paySafe) {
        bankAccountSelected = bankAccount;
        bankAccountCheckbox.setState(bankAccount);
        cardSelected = card;
        cardCheckbox.setState(card);
        paySafeSelected = paySafe;
        paySafeCheckbox.setState(paySafe);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    class ShoppingCartPanelList {

        private final Panel shoppingCartPanel;

        ShoppingCartPanelList(String name, String author, int yearValue, double priceValue, int quantity, int i) {
            GridBagConstraints gbc = new GridBagConstraints();

            shoppingCartPanel = new Panel(new GridLayout(1, 2));
            //BookList Data
            Panel mainPanelOne = new Panel(new GridBagLayout());
            mainPanelOne.setBackground(Color.YELLOW);

            gbc.insets = new Insets(2, 2, 2, 2);
            //Book Title Label
            Label titleLabel = new Label("Title:");
            titleLabel.setFont(new Font("Ariel", Font.BOLD, 12));
            gbc.gridx = 0;
            gbc.gridy = 0;
            mainPanelOne.add(titleLabel, gbc);
            Label titleNameLabel = new Label(name);
            titleNameLabel.setFont(new Font("Ariel", Font.BOLD, 16));
            gbc.gridx = 1;
            gbc.gridy = 0;
            mainPanelOne.add(titleNameLabel, gbc);

            //Author Label
            Label authorLabel = new Label("Author:");
            authorLabel.setFont(new Font("Ariel", Font.BOLD, 12));
            gbc.gridx = 0;
            gbc.gridy = 1;
            mainPanelOne.add(authorLabel, gbc);
            Label authorNameLabel = new Label(author);
            authorNameLabel.setFont(new Font("Ariel", Font.BOLD, 16));
            gbc.gridx = 1;
            gbc.gridy = 1;
            mainPanelOne.add(authorNameLabel, gbc);

            //Year Label
            Label yearLabel = new Label("Year:");
            yearLabel.setFont(new Font("Ariel", Font.BOLD, 12));
            gbc.gridx = 0;
            gbc.gridy = 2;
            mainPanelOne.add(yearLabel, gbc);
            Label yearValueLabel = new Label(Integer.toString(yearValue));
            yearValueLabel.setFont(new Font("Ariel", Font.BOLD, 16));
            gbc.gridx = 1;
            gbc.gridy = 2;
            mainPanelOne.add(yearValueLabel, gbc);

            Panel mainPanelTwo = new Panel(new GridBagLayout());
            mainPanelTwo.setBackground(Color.YELLOW);
            gbc.insets = new Insets(2, 150, 2, 2);
            //Quantity Label
            Label quantityLabel = new Label("Quantity:");
            quantityLabel.setFont(new Font("Ariel", Font.BOLD, 12));
            quantityLabel.setAlignment(Label.RIGHT);
            gbc.gridx = 0;
            gbc.gridy = 0;
            mainPanelTwo.add(quantityLabel, gbc);
            Label quantityValueLabel = new Label(Double.toString(quantity));
            quantityValueLabel.setFont(new Font("Ariel", Font.BOLD, 18));
            quantityValueLabel.setAlignment(Label.RIGHT);
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(2, 2, 2, 2);
            mainPanelTwo.add(quantityValueLabel, gbc);

            gbc.insets = new Insets(2, 150, 2, 2);
            //Price Label
            Label priceLabel = new Label("Price:");
            priceLabel.setFont(new Font("Ariel", Font.BOLD, 12));
            priceLabel.setAlignment(Label.RIGHT);
            gbc.gridx = 0;
            gbc.gridy = 1;
            mainPanelTwo.add(priceLabel, gbc);
            Label priceValueLabel = new Label(Double.toString(priceValue));
            priceValueLabel.setFont(new Font("Ariel", Font.BOLD, 18));
            priceValueLabel.setAlignment(Label.RIGHT);
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.insets = new Insets(2, 2, 2, 2);
            mainPanelTwo.add(priceValueLabel, gbc);

            gbc.insets = new Insets(2, 150, 2, 2);
            //Total Label
            Label totalLabel = new Label("Total:");
            totalLabel.setFont(new Font("Ariel", Font.BOLD, 12));
            totalLabel.setAlignment(Label.RIGHT);
            gbc.gridx = 0;
            gbc.gridy = 2;
            mainPanelTwo.add(totalLabel, gbc);
            Label totalValueLabel = new Label(String.format("%.2f", (priceValue * quantity)));
            totalValueLabel.setFont(new Font("Ariel", Font.BOLD, 18));
            totalValueLabel.setAlignment(Label.RIGHT);
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.insets = new Insets(2, 2, 2, 2);
            mainPanelTwo.add(totalValueLabel, gbc);

            //Remove Button
            Button removeButton = new Button("Remove");
            gbc.gridx = 2;
            gbc.gridy = 1;
            itemRemoveButtons.add(removeButton);
            mainPanelTwo.add(itemRemoveButtons.get(i), gbc);

            shoppingCartPanel.add(mainPanelOne);
            shoppingCartPanel.add(mainPanelTwo);
        }

        private Panel getBookListPanel() {
            return shoppingCartPanel;
        }
    }
}
