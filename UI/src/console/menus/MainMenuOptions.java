package console.menus;

public enum MainMenuOptions {
    XML("XML"),
    LOANS("Loans status"),
    COSTUMERS("Costumers"),
    DEPOSIT("Deposition"),
    WITHDRAW("Withdrawal");

    private String optionLine;

    MainMenuOptions(String optionLine) {
        this.optionLine = optionLine;
    }

    @Override
    public String toString() {
        return optionLine;
    }


}
