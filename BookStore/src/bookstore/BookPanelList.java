package bookstore;

import java.awt.*;

class BookPanelList {

    private final Panel bookListPanel;

    BookPanelList(String name, String author, int yearValue, double priceValue) {
        GridBagConstraints gbc = new GridBagConstraints();
        //BookList Data
        bookListPanel = new Panel();
        bookListPanel.setBackground(Color.YELLOW);
        bookListPanel.setLayout(new GridBagLayout());

        gbc.insets = new Insets(2, 2, 2, 2);
        //Book Title Label
        Label titleLabel = new Label("Title:");
        titleLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        bookListPanel.add(titleLabel, gbc);
        Label titleNameLabel = new Label(name);
        titleNameLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 0;
        bookListPanel.add(titleNameLabel, gbc);

        //Author Label
        Label authorLabel = new Label("Author:");
        authorLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        bookListPanel.add(authorLabel, gbc);
        Label authorNameLabel = new Label(author);
        authorNameLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 1;
        bookListPanel.add(authorNameLabel, gbc);

        //Year Label
        Label yearLabel = new Label("Year:");
        yearLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        bookListPanel.add(yearLabel, gbc);
        Label yearValueLabel = new Label(Integer.toString(yearValue));
        yearValueLabel.setFont(new Font("Ariel", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        bookListPanel.add(yearValueLabel, gbc);

        gbc.insets = new Insets(2, 150, 2, 2);
        //Price Label
        Label priceLabel = new Label("Price:");
        priceLabel.setFont(new Font("Ariel", Font.BOLD, 12));
        priceLabel.setAlignment(Label.RIGHT);
        gbc.gridx = 2;
        gbc.gridy = 1;
        bookListPanel.add(priceLabel, gbc);
        Label priceValueLabel = new Label(Double.toString(priceValue));
        priceValueLabel.setFont(new Font("Ariel", Font.BOLD, 18));
        priceValueLabel.setAlignment(Label.RIGHT);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.insets = new Insets(2, 2, 2, 2);
        bookListPanel.add(priceValueLabel, gbc);
    }

    public Panel getBookListPanel() {
        return bookListPanel;
    }
}
