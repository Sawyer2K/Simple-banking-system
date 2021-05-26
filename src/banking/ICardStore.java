package banking;

import java.sql.Connection;
import java.sql.SQLException;

public interface ICardStore {
    void setBalance(String cardNumber, int balance);
    void setBalance(Connection connection, String cardNumber, int balance);
    int getId(String cardNumber);
    String returnLastAddedCardNumber();
    String getPin(String cardNumber);
    int getBalance(String cardNumber);
    void checkOrCreateCardTable();
    void insertNewRecord(String cardNumber, String pinCode);
    boolean checkIfRecordExistInDataBase(String cardNumber);
    void makeTransaction(Account accountFrom, String cardNumberTo, int transactionValue);
    void deleteRecordFromTable(Account account);
    Connection beginTransaction() throws SQLException;
    void endOfTransaction(Connection connection);
}
