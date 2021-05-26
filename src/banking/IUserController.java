package banking;

public interface IUserController {

    void runMainMenu();
    void runAccountMenu(Account account);
    void onAccountCreateButtonClick(RenderMainMenuUI mainMenuFrame);
    void onAccountLoginButtonClick(RenderMainMenuUI mainMenuFrame);
    void onBalanceButtonClick(Account account);
    void onLogOutButtonClick(RenderAccountMenuUI accountMenuFrame);
    void onExitButtonClick();
}
