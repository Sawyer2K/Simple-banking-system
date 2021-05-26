package banking;

public class UserController implements IUserController {

    BankSystem bankSystem;
    CommandHandler commandHandler;

    UserController(BankSystem bankSystem, CommandHandler commandHandler) {
        this.bankSystem = bankSystem;
        this.commandHandler = commandHandler;
    }

    public void runMainMenu() {
        RenderMainMenuUI mainMenuFrame = new RenderMainMenuUI();

        while (true) {
            mainMenuFrame.renderFrame();

            switch (commandHandler.processCommandInput()) {
                case 1:
                    onAccountCreateButtonClick(mainMenuFrame);
                    break;
                case 2:
                    onAccountLoginButtonClick(mainMenuFrame);
                    break;
                case 0:
                    onExitButtonClick();
                    break;
                default:
                    break;
            }
        }
    }

    public void runAccountMenu(Account account) {
        RenderAccountMenuUI accountMenuFrame = new RenderAccountMenuUI();

        while (true) {
            accountMenuFrame.renderFrame();

            switch (commandHandler.processCommandInput()) {
                case 1:
                    onBalanceButtonClick(account);
                    break;
                case 2:
                    onAddIncomeButtonCLick(account);
                    break;
                case 3:
                    onDoTransferButtonClick(account);
                    break;
                case 4:
                    onCloseAccountButtonClick(account);
                    break;
                case 5:
                    onLogOutButtonClick(accountMenuFrame);
                    return;
                case 0:
                    onExitButtonClick();
                    break;
                default:
                    break;
            }
        }
    }

    public void onAccountCreateButtonClick(RenderMainMenuUI mainMenuFrame) {
        Account account = bankSystem.createAccount();
        mainMenuFrame.showAccountCreationStatus(account.getCardNumber(), account.getPinCode());
    }

    public void onAccountLoginButtonClick(RenderMainMenuUI mainMenuFrame) {
        System.out.println("Enter your card number:");
        String inputCardNumber = commandHandler.processDialogInput();
        System.out.println("Enter your PIN:");
        String inputPIN = commandHandler.processDialogInput();
        System.out.println();

        boolean isLogInSuccess = bankSystem.tryLogIntoAccount(inputCardNumber, inputPIN);
        mainMenuFrame.showAuthenticationStatus(isLogInSuccess);

        if (isLogInSuccess) {
            Account account = bankSystem.accountInit(inputCardNumber, inputPIN);
            runAccountMenu(account);
        }
    }

    public void onBalanceButtonClick(Account account) {
        System.out.println("Balance: " + bankSystem.getAccountBalance(account));
        System.out.println();
    }

    public void onAddIncomeButtonCLick(Account account) {
        System.out.println("Enter income:");

        int income = Integer.parseInt(commandHandler.processDialogInput());
        bankSystem.addIncome(income, account);

        System.out.println("Income was added!\n");
    }

    public void onDoTransferButtonClick(Account account) {
        RenderTransferMenuUI transferMenu = new RenderTransferMenuUI();

        transferMenu.renderFrame();
        String cardNumberTo = commandHandler.processDialogInput();

        if (!bankSystem.isCardNumberValid(cardNumberTo)) {
            transferMenu.showCardNumberMistakeMessage();
            return;
        } else if (!bankSystem.cardStoreController.checkIfRecordExistInDataBase(cardNumberTo)) {
            transferMenu.showCardNotExistMessage();
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int transactionValue = Integer.parseInt(commandHandler.processDialogInput());

        boolean isSuccessTransaction = bankSystem.tryMakeTransaction(account, cardNumberTo,
                                                                    transactionValue);
        if (isSuccessTransaction) {
            transferMenu.showSuccessTransactionMessage();
        } else {
            transferMenu.showNotEnoughMoneyMessage();
        }
    }

    public void onCloseAccountButtonClick(Account account) {
        bankSystem.deleteAccount(account);
        System.out.println("The account has been closed!\n");
    }

    public void onLogOutButtonClick(RenderAccountMenuUI accountMenuFrame) {
        accountMenuFrame.showLogOutStatus();
    }

    public void onExitButtonClick() {
        System.out.println("Bye!");
        System.exit(0);
    }
}
