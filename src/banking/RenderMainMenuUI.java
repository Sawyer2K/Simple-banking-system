package banking;

public class RenderMainMenuUI implements IRenderUI {

    public void renderFrame() {
        System.out.println("1. Create an account\n" +
                           "2. Log into account\n" +
                           "0. Exit");
        System.out.println();
    }

    public void showAuthenticationStatus(boolean isSuccessAuth) {
        System.out.println(isSuccessAuth ? "You have successfully logged in!" :
                                           "Wrong card number or PIN!");
        System.out.println();
    }

    public void showAccountCreationStatus(String cardNumber, String pinCode) {
        System.out.printf("Your card has been created\nYour card number:\n%s\nYour card PIN:\n%s\n",
                cardNumber, pinCode);
        System.out.println();
    }
}
