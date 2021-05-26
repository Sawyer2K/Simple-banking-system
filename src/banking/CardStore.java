package banking;

import java.sql.*;

public class CardStore implements ICardStore {
    static String URL;
    final static String ID = "id";
    final static String NUMBER = "number";
    final static String PIN = "pin";
    final static String BALANCE = "balance";
    Savepoint savepoint;

    //the constructor connects to the database when creating an instance
    public CardStore(String pathToDB) {
        URL = "jdbc:sqlite:" + pathToDB;
    }

    public void setBalance(String cardNumber, int balance) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "UPDATE card SET balance=? WHERE number=?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, balance);
                statement.setString(2, cardNumber);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }
    }

    public void setBalance(Connection connection, String cardNumber, int balance) {
        String query = "UPDATE card SET balance=? WHERE number=?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, balance);
            statement.setString(2, cardNumber);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getId(String cardNumber) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT id FROM card WHERE number=?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, cardNumber);
                ResultSet rs = statement.executeQuery();

                return rs.getInt(CardStore.ID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }
        return -1;
    }

    public String getNumber(int id) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT number FROM card WHERE id=?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();

                return rs.getString(CardStore.NUMBER);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }
        return null;
    }

    public String getPin(String cardNumber) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT * FROM card WHERE number = ?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, cardNumber);
                ResultSet rs = statement.executeQuery();

                return rs.getString(CardStore.PIN);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }
        return null;
    }

    public int getBalance(String cardNumber) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT balance FROM card WHERE number=?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, cardNumber);
                ResultSet rs = statement.executeQuery();

                return rs.getInt(CardStore.BALANCE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }
        return -1;
    }

    //create table with a stub record
    public void checkOrCreateCardTable() {
        try (Connection connection = DriverManager.getConnection(URL)) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (\n" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "  number TEXT NOT NULL,\n" +
                        "  pin TEXT NOT NULL,\n" +
                        "  balance INTEGER DEFAULT 0\n" +
                        ");");

                if (!checkIfRecordExistInDataBase("4000000000000000")) {
                    statement.executeUpdate("INSERT INTO card (number, pin) " +
                            "VALUES ('4000000000000000', '0000');");

                }
            } catch (SQLException e) {
                System.err.println("The table was not created");
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }
    }

    public void insertNewRecord(String cardNumber, String pinCode) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "INSERT INTO card ('number', 'pin') VALUES (?, ?);";

            try {
                if (connection.getAutoCommit()) {
                    connection.setAutoCommit(false);
                }

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, cardNumber);
                    statement.setString(2, pinCode);
                    statement.executeUpdate();

                    connection.commit();
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.print("Insertion a new record is being rolled back");
                    e.printStackTrace();
                    connection.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }


    }

    public String returnLastAddedCardNumber() {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT number FROM card ORDER BY 1 DESC LIMIT 1;";

            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(query);

                return rs.getString(CardStore.NUMBER);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }

        return "Not return any result";
    }

    public boolean checkIfRecordExistInDataBase(String cardNumber) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "SELECT * FROM card WHERE number=? LIMIT 1;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, cardNumber);
                ResultSet rs = statement.executeQuery();

                return rs.getString("number").equals(cardNumber);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }

        return false;
    }

    public void makeTransaction(Account accountFrom, String cardNumberTo, int transactionValue) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "UPDATE card SET balance=? WHERE number=?;";

            try {
                if (connection.getAutoCommit()) {
                    connection.setAutoCommit(false);
                }

                try (PreparedStatement withdrawalStat = connection.prepareStatement(query);
                     PreparedStatement incomeStat = connection.prepareStatement(query)) {

                    withdrawalStat.setInt(1, accountFrom.getBalance() - transactionValue);
                    withdrawalStat.setString(2, accountFrom.getCardNumber());
                    withdrawalStat.executeUpdate();

                    incomeStat.setInt(1, getBalance(cardNumberTo) + transactionValue);
                    incomeStat.setString(2, cardNumberTo);
                    incomeStat.executeUpdate();

                    connection.commit();
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.print("Transaction was not completed and is being rolled back");
                    e.printStackTrace();
                    connection.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }


    }

    public void deleteRecordFromTable(Account account) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            String query = "DELETE FROM card WHERE number=?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, account.getCardNumber());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.err.print("The record was not deleted");
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.err.println("The connection to the database was not established");
            throwables.printStackTrace();
        }
    }

    public Connection beginTransaction() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
            savepoint = connection.setSavepoint();
            connection.setAutoCommit(false);
            return connection;
//        try (Connection connection = DriverManager.getConnection(URL)) {
//            savepoint = connection.setSavepoint();
//            connection.setAutoCommit(false);
//            return connection;
//        } catch (SQLException throwables) {
//            System.err.println("The connection to the database was not established");
//            throwables.printStackTrace();
//        }
//        return null;
    }

    public void endOfTransaction(Connection connection) {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            System.err.println("Transaction is being rolled back");
            try {
                connection.rollback(savepoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
    }
}
