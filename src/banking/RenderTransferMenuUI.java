package banking;

public class RenderTransferMenuUI implements IRenderUI {
    public void renderFrame() {
        System.out.println("Transfer\n" +
                           "Enter card number:");
    }

    public void showCardNumberMistakeMessage() {
        System.out.println("Probably you made a mistake in the card number. Please try again!\n");
    }

    public void showCardNotExistMessage() {
        System.out.println("Such a card does not exist.\n");
    }

    public void showSuccessTransactionMessage() {
        System.out.println("Success!\n");
    }

    public void showNotEnoughMoneyMessage() {
        System.out.println("Not enough money!\n");
    }
}
