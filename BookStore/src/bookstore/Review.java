package bookstore;

class Review {

    private final String user;
    private final String message;
    private final String date;

    Review(String user, String message, String date) {
        this.user = user;
        this.message = message;
        this.date = date;
    }

    public String getUser() {
        return this.user;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDate() {
        return this.date;
    }

}
