package bookstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

class UserDatabase {

    private final String FILE_PATH = "Users.txt";
    private final TreeSet<User> userDatabase;

    UserDatabase() {
        this.userDatabase = new TreeSet<>();

        Load();
    }

    public TreeSet<User> getUserDatabase() {
        return new TreeSet<>(userDatabase);
    }

    public void addToUserDatabase(User user) {
        this.userDatabase.add(user);
    }

    private void Load() {
        Scanner input;
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) {
                System.out.println("Error: File didn't exist, but has been created");
                try {
                    boolean created;
                    created = file.createNewFile();
                    if (!created) {
                        throw new IOException("File could not be created");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            input = new Scanner(file);
            if (!input.hasNext()) {
                System.out.println("User Database is Empty");
                return;
            }
            String line;
            String[] lines;
            while (input.hasNext()) {
                line = Decrypt(input.nextLine());
                lines = line.split(" ");
                userDatabase.add(new User(Integer.parseInt(lines[0]),
                        lines[1], lines[2], lines[3], lines[4], lines[5], lines[6]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void Upload() {
        try {
            PrintWriter printWriter = new PrintWriter(FILE_PATH, "UTF-8");
            for (User user : userDatabase) {
                printWriter.println(user.SendEncryption());
            }
            printWriter.close();
        } catch (IOException e) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private String Decrypt(String line) {
        int aux;
        char[] array = line.toCharArray();
        for (int i = 0; i < array.length; i++) {
            aux = (int) (array[i]);
            switch (aux) {
                case 35:
                    aux = 32;
                    array[i] = (char) aux;
                    continue;
                case 42:
                    aux = 64;
                    array[i] = (char) aux;
                    continue;
                case 43:
                    aux = 46;
                    array[i] = (char) aux;
                    continue;
                case 45:
                    aux = 33;
                    array[i] = (char) aux;
                    continue;
                case 95:
                    aux = 63;
                    array[i] = (char) aux;
                    continue;
            }

            aux -= 5;
            if (aux < 48 && aux > 42) {
                aux += 10;
            } else if (aux < 65 && aux > 59) {
                aux += 25;
            } else if (aux < 97 && aux > 90) {
                aux += 25;
            }

            array[i] = (char) aux;
        }
        return String.valueOf(array);
    }

}
