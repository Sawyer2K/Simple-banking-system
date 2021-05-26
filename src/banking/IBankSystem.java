package banking;
public interface IBankSystem {

    Account createAccount();
    boolean tryLogIntoAccount(String cardNumber, String pinCode);
    String generateCardNumber();
    boolean isCardNumberValid(String cardNumber);
    boolean isCardNumberUnique(String cardNumber);
    String generatePinCode();
}
