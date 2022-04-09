package console.menus;

import bank.Bank;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.interest.exceptions.InvalidPercentException;
import console.menus.exceptions.NoAmountSetException;
import console.menus.exceptions.NoOptionException;
import manager.categories.CategoriesDTO;
import manager.customers.CustomerDTO;
import manager.investments.InvestDTO;
import manager.investments.RequestDTO;
import manager.loans.LoanDTO;
import manager.loans.LoansDTO;
import utils.PrintUtils;

import java.util.*;

public class SetLoanMenu {
    int amount;
    Set<String> categories;
    float minInterest;
    int minLoanDuration;
    int balance;
    String requesterName;
    Bank bankInstance;

    public SetLoanMenu(Bank bank, CustomerDTO customerDTO) {
        this.requesterName = customerDTO.getName();
        amount = 0;
        balance = customerDTO.getAccount().getBalance();
        categories = new HashSet<>();
        minInterest = 0;
        minLoanDuration = 0;
        bankInstance = bank;

        setAllCategories();
    }

    public void printMenu() {

        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("Loan Investment requirements: \n1.Set Investment Amount [MUST].\n" +
                    "2.Set Loan Category.\n" + "3.Set Loan's Minimum Interest.\n" +
                    "4.Set Loan's Minimum Duration.\n" + "5.Request Loan Investment.\n\n" + "9.Cancel Request.");
            option = scanner.nextInt();
            try {
                switch (option) {
                    case 1:
                        setAmount();
                        break;
                    case 2:
                        setCategory();
                        break;
                    case 3:
                        setInterest();
                        break;
                    case 4:
                        setDuration();
                        break;
                    case 5:
                        requestInvestment();
                        option = 9;
                        break;
                    default:
                        throw new NoOptionException();
                }
            } catch (NoMoneyException | DataNotFoundException | NoOptionException | NonPositiveAmountException | NoAmountSetException | InvalidPercentException e) {
                System.out.println(e.getMessage());
            }
        } while (option != 9);
    }

    private void requestInvestment() throws DataNotFoundException, NoMoneyException, NonPositiveAmountException, NoAmountSetException, InvalidPercentException {
        if(amount == 0)
            throw new NoAmountSetException();

        RequestDTO requestDTO = new RequestDTO(requesterName, amount, new CategoriesDTO(categories),
                minInterest, minLoanDuration);

        LoansDTO loansDTO = bankInstance.loanAssignmentRequest(requestDTO);
        List<LoanDTO> loanDTOList = loansDTO.getLoansList();
        int listSize = loanDTOList.size();

        if(listSize == 0) {
            System.out.println("No relevant loans found.");
        } else {
            int i = 1;
            System.out.println("Found " + loanDTOList.size() + " relevant loans:");
            for(LoanDTO loan : loanDTOList) {
                System.out.println("Loan #" + i + " Details:");
                i++;
                PrintUtils.printLoan(loan);
            }

            List<LoanDTO> chosenLoans = getChosenLoans(loanDTOList);
            int amountLeft = amount;
            int minAmount, loansAmount;
            int avgAmount = amountLeft / chosenLoans.size();
            boolean averageSplit = false;

            chosenLoans.sort(Comparator.comparingInt(p -> p.getActiveLoanDTO().getAmountToActive()));
            minAmount = chosenLoans.get(0).getActiveLoanDTO().getAmountToActive();

            while(avgAmount > minAmount) {
                List<LoanDTO> tempList = new ArrayList<>();

                for (LoanDTO loan : chosenLoans) {

                    if (minAmount <= amountLeft) {
                        InvestDTO investDTO = new InvestDTO(requesterName, loan.getDetails().getName(), minAmount);
                        bankInstance.createInvestment(investDTO);

                        amountLeft -= minAmount;
                        if (loan.getActiveLoanDTO().getAmountToActive() != minAmount)
                            tempList.add(loan);
                    }
                    else {
                        break;
                    }
                }

                chosenLoans = tempList;

                if(chosenLoans.size() == 0)
                    break;

                avgAmount = amountLeft / chosenLoans.size();
                minAmount = chosenLoans.get(0).getActiveLoanDTO().getAmountToActive();
            }

            for(LoanDTO loan : chosenLoans) {
                InvestDTO investDTO = new InvestDTO(requesterName, loan.getDetails().getName(), avgAmount);
                bankInstance.createInvestment(investDTO);
                amountLeft -= avgAmount;
            }

        }
    }

    private List<LoanDTO> getChosenLoans(List<LoanDTO> loanDTOList) {

        List<LoanDTO> chosenLoans = new ArrayList<>();
        System.out.println("Choose the loans you want to invest in:");
        System.out.println(("[for example: 1 3 5]"));

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        line = line.replaceAll("[^-?0-9]+", " ");
        System.out.println();

        for(String numStr : line.trim().split(" ")) {
            int index = Integer.parseInt(numStr) - 1;
            chosenLoans.add(loanDTOList.get(index));
        }

        return chosenLoans;
    }

    private List<String> getChosenCategories(List<String> allCategories) {

        List<String> chosenCategories = new ArrayList<>();
        System.out.println("Choose the categories:");
        System.out.println(("[for example: 1 3 5]"));

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        line = line.replaceAll("[^-?0-9]+", " ");
        System.out.println();

        for(String numStr : line.trim().split(" ")) {
            int index = Integer.parseInt(numStr) - 1;
            chosenCategories.add(allCategories.get(index));
        }

        return chosenCategories;
    }

    private void setCategory() {
        System.out.println("Choose a category to add:");
        CategoriesDTO categoriesDTO = bankInstance.getCategoriesDTO();
        List<String> categoryList = categoriesDTO.getCategories();

        int num = 1;
        for(String str : categoryList) {
            System.out.println(num + ". " + str);
            num++;
        }

        categories.clear();

        categories.addAll(getChosenCategories(categoryList));
    }

    private void setAllCategories() {
        categories.addAll(bankInstance.getCategoriesDTO().getCategories());
    }

    private void setDuration() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type min loan's duration");
        minInterest = scanner.nextInt();
    }

    private void setInterest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type min amount of interest percentage");
        minInterest = scanner.nextFloat();
    }

    private void setAmount() throws NoMoneyException, NonPositiveAmountException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type max amount to invest [Current Amount: " + amount + ", Account Balance: " + balance + "]");
        int investAmount = scanner.nextInt();

        if(investAmount > balance)
            throw new NoMoneyException();

        if(investAmount <= 0)
            throw new NonPositiveAmountException();

        amount = investAmount;
    }
}
