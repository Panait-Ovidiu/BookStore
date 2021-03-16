package bookstore;

import java.awt.*;

class ReviewsPanelList {

    private final Panel reviewsPanel;

    ReviewsPanelList(String user, String date, String message) {

        //ReviewList Data
        reviewsPanel = new Panel();
        reviewsPanel.setBackground(Color.YELLOW);
        reviewsPanel.setLayout(new BorderLayout());

        //Top Panel
        Panel topPanel = new Panel(new FlowLayout(FlowLayout.LEFT));

        //User Label
        Label userLabel = new Label(user);
        userLabel.setFont(new Font("Ariel", Font.BOLD, 14));
        topPanel.add(userLabel);

        //Date Label
        Label dateLabel = new Label("Date:" + date);
        dateLabel.setFont(new Font("Ariel", Font.BOLD, 10));
        topPanel.add(dateLabel);

        reviewsPanel.add(topPanel, BorderLayout.NORTH);

        //Message Label
        TextArea messageLabel = new TextArea("", 4, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        messageLabel.setText(message);
        messageLabel.setEditable(false);
        messageLabel.setFont(new Font("Ariel", Font.BOLD, 16));

        reviewsPanel.add(messageLabel, BorderLayout.CENTER);
    }

    public Panel getReviewsPanel() {
        return reviewsPanel;
    }
}
