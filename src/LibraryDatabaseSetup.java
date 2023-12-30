import java.sql.*;

public class LibraryDatabaseSetup {

    private static final String DB_URL = "jdbc:mysql://localhost/";
    private static final String DB_NAME = "Library";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    public static void main(String[] args) {
        createDatabase();
        createTables();
        insertSampleData();
    }

    private static void createDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            String createDBQuery = "CREATE DATABASE " + DB_NAME;
            statement.executeUpdate(createDBQuery);
            System.out.println("Database created: " + DB_NAME);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables() {
        String createBooksTableQuery = "CREATE TABLE " + DB_NAME + ".Books (\n" +
                "  ISBN VARCHAR(20) PRIMARY KEY,\n" +
                "  Title VARCHAR(100) NOT NULL,\n" +
                "  Authors VARCHAR(100) NOT NULL,\n" +
                "  Publisher VARCHAR(100) NOT NULL,\n" +
                "  EditionNumber INT NOT NULL,\n" +
                "  PublicationDate DATE NOT NULL,\n" +
                "  Type VARCHAR(50) NOT NULL\n" +
                ");";

        String createReaderTableQuery = "CREATE TABLE " + DB_NAME + ".Reader (\n" +
                "  ReaderID INT PRIMARY KEY,\n" +
                "  FirstName VARCHAR(50) NOT NULL,\n" +
                "  LastName VARCHAR(50) NOT NULL,\n" +
                "  Address VARCHAR(200) NOT NULL,\n" +
                "  PhoneNumber VARCHAR(20) NOT NULL,\n" +
                "  Limits INT NOT NULL\n" +
                ");";

        String createRecordTableQuery = "CREATE TABLE " + DB_NAME + ".Record (\n" +
                "  RecordID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "  ISBN VARCHAR(20) NOT NULL,\n" +
                "  ReaderID INT NOT NULL,\n" +
                "  BorrowingDate DATE NOT NULL,\n" +
                "  ReturnDate DATE NOT NULL,\n" +
                "  FOREIGN KEY (ISBN) REFERENCES " + DB_NAME + ".Books(ISBN),\n" +
                "  FOREIGN KEY (ReaderID) REFERENCES " + DB_NAME + ".Reader(ReaderID)\n" +
                ");";

        try (Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createBooksTableQuery);
            statement.executeUpdate(createReaderTableQuery);
            statement.executeUpdate(createRecordTableQuery);
            System.out.println("Tables created.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertSampleData() {
        String insertBooksQuery = "INSERT INTO " + DB_NAME + ".Books (ISBN, Title, Authors, Publisher, EditionNumber, PublicationDate, Type) VALUES " +
                "('ISBN001', 'Book 1', 'Author 1', 'Publisher 1', 1, '2021-01-01', 'Fiction'), " +
                "('ISBN002', 'Book 2', 'Author 2', 'Publisher 2', 2, '2021-02-01', 'Non-Fiction');";

        String insertReaderQuery = "INSERT INTO " + DB_NAME + ".Reader (ReaderID, FirstName, LastName, Address, PhoneNumber, Limits) VALUES " +
                "(1, 'John', 'Doe', '123 Main St', '123-456-7890', 3), " +
                "(2, 'Jane', 'Smith', '456 Elm St', '987-654-3210', 5);";

        try (Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(insertBooksQuery);
            statement.executeUpdate(insertReaderQuery);
            System.out.println("Sample data inserted.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
