package banking;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Random;

public class BankSystem implements IBankSystem {

    ICardStore cardStoreController;
    int[] cardNumberArray = {0, 0, 0, 0,
                            0, 0, 0, 0,
                            0, 0, 0, 0,
                            0, 0, 0, 0};

    BankSystem(ICardStore cardStoreController) {
        this.cardStoreController = cardStoreController;

        cardStoreController.checkOrCreateCardTable();
    }

    public Account accountInit(String cardNumber, String pinCode) {
        int id = cardStoreController.getId(cardNumber);
        int balance = cardStoreController.getBalance(cardNumber);
        return new Account(id, cardNumber, pinCode, balance);
    }

    public Account createAccount() {
        Account account = new Account(generateCardNumber(), generatePinCode());

        cardStoreController.insertNewRecord(account.getCardNumber(), account.getPinCode());

        return account;
    }

    public boolean tryLogIntoAccount(String cardNumber, String pinCode) {
        return pinCode.equals(cardStoreController.getPin(cardNumber));
    }

    public String generateCardNumber() {
        cardNumberBlankInit();
        return incrementCardNumber();
    }

    public void cardNumberBlankInit() {
        String lastCardNumber = cardStoreController.returnLastAddedCardNumber();

        for (int i = 0; i < lastCardNumber.length(); i++) {
            cardNumberArray[i] = Character.getNumericValue(lastCardNumber.charAt(i));
        }
    }

    public String incrementCardNumber() {
        StringBuilder sb = new StringBuilder();

        for (int i = cardNumberArray.length - 1; i >= 0; i--) {
            int currentDigit = cardNumberArray[i];
            if (currentDigit < 9) {
                cardNumberArray[i] = currentDigit + 1;
                break;
            } else {
                cardNumberArray[i] = 0;
            }
        }

        for (int digit : cardNumberArray) {
            sb.append(digit);
        }

        String cardNumber = sb.toString();

        if (isCardNumberValid(cardNumber) & isCardNumberUnique(cardNumber)) {
            return cardNumber;
        } else {
            return incrementCardNumber();
        }
    }

    public boolean isCardNumberValid(String cardNumber) {
        if (cardNumber.length() != 16) {
            return false;
        }

        final int nDigits = cardNumber.length();
        int sum1 = 0;
        int sum2 = 0;

        for (int i = nDigits; i > 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i - 1));
            int z = digit;

            if (i % 2 != 0) {
                z *= 2;

                if (z > 9) {
                    z -= 9;
                }

                sum1 += z;
            } else {
                sum2 += digit;
            }
        }

        int sum = sum1 + sum2;

        return sum % 10 == 0;
    }

    public boolean isCardNumberUnique(String cardNumber) {
        return !cardStoreController.checkIfRecordExistInDataBase(cardNumber);
    }

    public String generatePinCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public void addIncome(int income, Account account) {
        account.setBalance(account.getBalance() + income);
        cardStoreController.setBalance(account.getCardNumber(), account.getBalance());
    }

    public boolean isEnoughFundsForTransfer(Account account, int transactionValue) {
        return account.getBalance() > transactionValue;
    }

    public boolean tryMakeTransaction(Account account, String cardNumberTo, int transactionValue) {
        boolean isEnoughFunds = isEnoughFundsForTransfer(account, transactionValue);

        if (isEnoughFunds) {
            cardStoreController.makeTransaction(account, cardNumberTo, transactionValue);
            return true;
        } else {
            return false;
        }

//        try (Connection connection = cardStoreController.beginTransaction()) {
//            Savepoint savepoint = connection.setSavepoint();
//
//            boolean isEnoughFunds = isEnoughFundsForTransfer(account, transactionValue);
//
//            if (isEnoughFunds) {
//                String cardNumberFrom = account.getCardNumber();
//
//                int newBalanceOfAccountFrom = cardStoreController.getBalance(cardNumberFrom) - transactionValue;
//                int newBalanceOfAccountTo = cardStoreController.getBalance(cardNumberTo) + transactionValue;
//
//                cardStoreController.setBalance(connection, cardNumberFrom, newBalanceOfAccountFrom);
//                cardStoreController.setBalance(connection, cardNumberTo, newBalanceOfAccountTo);
//
//                cardStoreController.endOfTransaction(connection);
//
//                return true;
//            } else {
//                return false;
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return false;
    }

    public void deleteAccount(Account account) {
        cardStoreController.deleteRecordFromTable(account);
    }

    public int getAccountBalance(Account account) {
        return cardStoreController.getBalance(account.getCardNumber());
    }
}
