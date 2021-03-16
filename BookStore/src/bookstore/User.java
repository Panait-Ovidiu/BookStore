package bookstore;

class User implements Comparable<User> {

    private final int ID;
    private static int maxID = -1;
    private final String userName;
    private final String password;
    private final String mailAddress;
    private final String firstName;
    private final String lastName;
    private final String address;

    User(int ID, String userName, String password, String mailAddress, String firstName, String lastName, String address) {
        if (ID > maxID) {
            maxID = ID;
        }
        this.ID = ID;
        this.userName = userName;
        this.password = password;
        this.mailAddress = mailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    User(String userName, String password, String mailAddress, String firstName, String lastName, String address) {
        this.ID = ++maxID;
        this.userName = userName;
        this.password = password;
        this.mailAddress = mailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public boolean isThisTheUser(String userName) {
        return this.userName.equals(userName);
    }

    public boolean isThisThePassword(String password) {
        return this.password.equals(password);
    }

    private String Encrypt(String line) {
        int aux;
        char[] array = line.toCharArray();
        for (int i = 0; i < array.length; i++) {
            aux = (int) (array[i]);
            switch (aux) {
                case 32:
                    aux = 35;
                    array[i] = (char) aux;
                    continue;
                case 64:
                    aux = 42;
                    array[i] = (char) aux;
                    continue;
                case 46:
                    aux = 43;
                    array[i] = (char) aux;
                    continue;
                case 33:
                    aux = 45;
                    array[i] = (char) aux;
                    continue;
                case 63:
                    aux = 95;
                    array[i] = (char) aux;
                    continue;
            }
            aux += 5;

            if (aux > 122) {
                aux -= 25;
            } else if (aux > 90 && aux < 97) {
                aux -= 25;
            } else if (aux > 57 && aux < 65) {
                aux -= 10;
            }

            array[i] = (char) aux;

        }
        return new String(array);
    }

    public String SendEncryption() {
        return Encrypt(String.format("%d %s %s %s %s %s %s", ID, userName, password, mailAddress, firstName, lastName, address));
    }

    @Override
    public int compareTo(User user) {
        return Integer.compare(this.ID, user.ID);
    }

    public String toString() {
        return String.format("User name: %s\nMail: %s\n", userName, mailAddress);
    }
}
