package bank.impl;

import bank.Bank;
import bank.accounts.CustomerAccount;
import bank.accounts.impl.Customer;
import bank.accounts.impl.exceptions.NoMoneyException;
import bank.accounts.impl.exceptions.NonPositiveAmountException;
import bank.data.storage.DataStorage;
import bank.data.storage.impl.BankDataStorage;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import bank.loans.LoanStatus;
import bank.loans.handler.impl.BankLoanHandler;
import bank.loans.interest.Interest;
import bank.loans.interest.exceptions.InvalidPercentException;
import bank.loans.interest.impl.BasicInterest;
import bank.loans.investments.Investment;
import bank.loans.investments.impl.LoanInvestment;
import bank.time.TimeHandler;
import bank.time.handler.BankTimeHandler;
import bank.transactions.Transaction;
import files.saver.Saver;
import files.saver.SystemSaver;
import files.xmls.XmlReader;
import files.xmls.exceptions.*;
import javafx.util.Pair;
import manager.accounts.AccountDTO;
import manager.customers.CustomerDTO;
import manager.customers.CustomerData;
import manager.customers.CustomersDTO;
import manager.customers.CustomersData;
import manager.investments.InvestDTO;
import manager.investments.RequestDTO;
import manager.loans.LoanDTO;
import manager.loans.LoanData;
import manager.loans.LoansDTO;
import manager.loans.LoansData;
import manager.loans.details.*;
import manager.categories.CategoriesDTO;
import manager.time.YazSystemDTO;
import manager.transactions.TransactionDTO;
import manager.transactions.TransactionsDTO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BankImpl implements Bank {
    private DataStorage<bank.accounts.CustomerAccount> customersAccounts;
    private DataStorage<bank.accounts.Account> loanAccounts;
    private DataStorage<Transaction> transactions;
    private DataStorage<Loan> loans;
    private Set<String> categories;
    private BankLoanHandler loanHandler;
    private TimeHandler timeHandler;

    public BankImpl() {
        timeHandler = new BankTimeHandler();
        customersAccounts = new BankDataStorage<>(timeHandler);
        loanAccounts = new BankDataStorage<>(timeHandler);
        transactions = new BankDataStorage<>(timeHandler);
        loans = new BankDataStorage<>(timeHandler);
        loanHandler = new BankLoanHandler(transactions, loans, customersAccounts, timeHandler);
        categories = new HashSet<>();
    }

    @Override
    public DataStorage<bank.accounts.CustomerAccount> getCustomersAccounts() {
        return customersAccounts;
    }

    @Override
    public DataStorage<bank.accounts.Account> getLoanAccounts() {
        return loanAccounts;
    }

    @Override
    public DataStorage<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public DataStorage<Loan> getLoans() {
        return loans;
    }

    @Override
    public TimeHandler getTimeHandler() {
        return timeHandler;
    }


    @Override
    public void loadData(String filename) throws NotXmlException, XmlNoLoanOwnerException, XmlNoCategoryException, XmlPaymentsException, XmlAccountExistsException, XmlNotFoundException, DataNotFoundException {
        TimeHandler timeHandler = new BankTimeHandler();
        XmlReader xmlReader = new XmlReader(filename, timeHandler);

        if(xmlReader.isValid()) {

            customersAccounts = xmlReader.getCustomersDataStorage();
            loans = xmlReader.getLoansDataStorage();
            categories = xmlReader.getCategoryNames();

            loanAccounts = new BankDataStorage<>(timeHandler);
            transactions = new BankDataStorage<>(timeHandler);

            loanHandler = new BankLoanHandler(transactions, loans, customersAccounts, timeHandler);

            this.timeHandler = timeHandler;
        }
    }

    @Override
    public void advanceOneYaz() throws DataNotFoundException, NonPositiveAmountException {
        timeHandler.advanceTime();
        loanHandler.oneCycle();
    }

    @Override
    public int getCurrentYaz() {
        return timeHandler.getCurrentTime();
    }

    @Override
    public void withdraw(String accountId, int amount, String description) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        bank.accounts.Account account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.withdraw(amount, description);
        transactions.addData(transaction);
    }

    @Override
    public void deposit(String accountId, int amount, String description) throws NonPositiveAmountException, DataNotFoundException {
        bank.accounts.Account account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.deposit(amount, description);
        transactions.addData(transaction);
    }

    @Override
    public void createAccount(String name, int balance) {
        bank.accounts.CustomerAccount account = new Customer(name, balance);

        customersAccounts.addData(account);
    }

    @Override
    public void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        loanHandler.deriskLoan(loan);
    }

    @Override
    public int getDeriskAmount(Loan loan) {
        return loan.getDeriskAmount();
    }


    @Override
    public void createInvestment(InvestDTO investDetails) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        Loan loan = loans.getDataById(investDetails.getLoanName());
        String investor = investDetails.getInvestorName();
        bank.accounts.CustomerAccount investingAccount = customersAccounts.getDataById(investor);

        float percent = loan.getInterestPercent();
        int amountInvesting = investDetails.getAmount();
        int cyclesPerPayment = loan.getCyclesPerPayment();
        int duration = loan.getDuration();

        Interest interest = new BasicInterest(percent, amountInvesting, cyclesPerPayment, duration);

        Investment loanInvestment = new LoanInvestment(investor, interest);
        loanHandler.addInvestment(loan, loanInvestment, investingAccount);
    }

    @Override
    public LoansDTO loanAssignmentRequest(RequestDTO requestDTO) throws InvalidPercentException {
        int amount = requestDTO.getAmount();
        String requesterName = requestDTO.getRequesterName();
        float minInterest = requestDTO.getMinInterest();
        List<String> categories = requestDTO.getCategoriesDTO().getCategories();
        int minDuration = requestDTO.getMinLoanDuration();
        int maxRelatedLoans = requestDTO.getMaxRelatedLoans();

        if(minInterest < 0 || minInterest > 100)
            throw new InvalidPercentException();

        List<Pair<Loan, Integer>> relevantLoans = loans.getAllPairs().stream()
                .filter(p -> !p.getKey().getOwnerId().equals(requesterName))
                .filter(p -> p.getKey().isInvestable())
                .filter(p -> p.getKey().getInterestPercent() >= minInterest)
                .filter(p -> categories.contains(p.getKey().getCategory()))
                .filter(p -> p.getKey().getDuration() >= minDuration)
                .filter(p->p.getKey().getLoanAccount().getLoansRequested().size() <= maxRelatedLoans)
                .collect(Collectors.toList());

        List<LoanDTO> loansDTOList = new ArrayList<>();

        for(Pair<Loan, Integer> loanPair : relevantLoans) {
            loansDTOList.add(getLoanDTO(loanPair.getKey()));
        }

        return new LoansDTO(loansDTOList);

    }

    @Override
    public Set<String> getCategories() {
        return categories;
    }

    @Override
    public CategoriesDTO getCategoriesDTO() {

        return new CategoriesDTO(categories);
    }

    @Override
    public CustomersDTO getCustomersDTO() throws DataNotFoundException {
        List<CustomerDTO> customersDTOList = new ArrayList<>();
        Collection<Pair<bank.accounts.CustomerAccount, Integer>> customersList = customersAccounts.getAllPairs();

        for(Pair<bank.accounts.CustomerAccount, Integer> account : customersList) {
            customersDTOList.add(getCustomerDTO(account.getKey().getId()));
        }

        return new CustomersDTO(customersDTOList);
    }

    @Override
    public CustomerDTO getCustomerDTO(String id) throws DataNotFoundException {
        bank.accounts.Account account = customersAccounts.getDataById(id);
        List<LoanDTO> loansInvestedDTOList = new ArrayList<>();
        List<LoanDTO> loansRequestedDTOList = new ArrayList<>();
        List<Loan> loansInvested = account.getLoansInvested();
        List<Loan> loansRequested = account.getLoansRequested();

        for(Loan loan : loansInvested)
            loansInvestedDTOList.add(getLoanDTO(loan));
        for(Loan loan : loansRequested)
            loansRequestedDTOList.add(getLoanDTO(loan));

        TransactionsDTO transactions = getTransactionsDTO(account);
        LoansDTO loansInvestedDTO = new LoansDTO(loansInvestedDTOList);
        LoansDTO loansRequestedDTO = new LoansDTO(loansRequestedDTOList);
        AccountDTO accountDTO = new AccountDTO(account.getId(),account.getBalance(),transactions);
        return new CustomerDTO(loansInvestedDTO,loansRequestedDTO,accountDTO);
    }

    @Override
    public LoanDTO getLoanDTO(Loan loan) {
        InterestDTO interestDTO = new InterestDTO(loan.getBaseAmount(),loan.getFinalAmount(),loan.getInterestPercent());
        LoanDetailsDTO loanDetailsDTO = new LoanDetailsDTO(loan.getId(),loan.getCategory(),loan.getStatus().name());
        LoanPaymentDTO loanPaymentDTO = new LoanPaymentDTO(loan.getPayment(),loan.getNextYaz(),loan.getCyclesPerPayment());
        ActiveLoanDTO activeLoanDTO = new ActiveLoanDTO(loan.getAmountToActive(),loan.getDeriskAmount(),loan.getMissingCycles());
        YazDTO yazDTO = new YazDTO(loan.getStartedYaz(),loan.getFinishedYaz());
        return new LoanDTO(loanDetailsDTO,interestDTO,yazDTO,loanPaymentDTO,activeLoanDTO);
    }

    @Override
    public LoansDTO getAllLoansDTO() {
        List<LoanDTO> allLoans = new ArrayList<>();
        for(Pair<Loan,Integer> loanPair : loans.getAllPairs()) {
            allLoans.add(getLoanDTO(loanPair.getKey()));
        }
        return new LoansDTO(allLoans);
    }

    @Override
    public TransactionDTO getTransactionDTO(Transaction transaction) throws DataNotFoundException {
        return new TransactionDTO(transaction.getDescription(),transaction.getAmount(),
                    transaction.getPreviousBalance(),transactions.getDataPair(transaction.getId()).getValue());
    }

    @Override
    public TransactionsDTO getTransactionsDTO(bank.accounts.Account account) throws DataNotFoundException {
        List<TransactionDTO> transactionsList = new ArrayList<>();
        List<Transaction> accountTransactions = account.getTransactions();

        for(Transaction transaction : accountTransactions) {
            transactionsList.add(getTransactionDTO(transaction));
        }
        return new TransactionsDTO(transactionsList);
    }

    @Override
    public YazSystemDTO getYazSystemDTO() {
        return new YazSystemDTO(timeHandler.getCurrentTime(),timeHandler.getPreviousTime());
    }

    @Override
    public void saveToFile(String filePath) throws IOException {
        Saver saver = new SystemSaver(this);

        saver.saveToFile(filePath);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        Saver saver = new SystemSaver(this);

        saver.loadFile(filePath);

        if(saver.isValid()) {
            timeHandler.setPreviousTime(saver.getPrevYaz());
            timeHandler.setCurrentTime(saver.getCurrYaz());

            categories = (Set<String>) saver.getCategories();
            customersAccounts = (DataStorage<bank.accounts.CustomerAccount>) saver.getCustomers();
            loanAccounts = (DataStorage<bank.accounts.Account>) saver.getLoanAccounts();
            loans = (DataStorage<Loan>)saver.getLoans();
            transactions = (DataStorage<Transaction>) saver.getTransactions();
        }
    }

    @Override
    public LoanData getLoanData(Loan loan) throws DataNotFoundException {
        LoanData loanData = new LoanData();
        loanData.setAmountToActive(loan.getAmountToActive());
        loanData.setBaseAmount(loan.getBaseAmount());
        loanData.setCategory(loan.getCategory());
        loanData.setDeriskAmount(loan.getDeriskAmount());
        loanData.setFinalAmount(loan.getFinalAmount());
        loanData.setFinishedYaz(loan.getFinishedYaz());
        loanData.setInterest(loan.getInterestPercent());
        loanData.setName(loan.getId());
        loanData.setLoanRequester(customersAccounts.getDataById(loan.getOwnerId()).getId());
        loanData.setStatus(loan.getStatus().name());
        loanData.setNextPaymentAmount(loan.getPayment());
        loanData.setCyclesPerPayment(loan.getCyclesPerPayment());
        loanData.setMissingCycles(loan.getMissingCycles());
        loanData.setNextPaymentInYaz(loan.getNextYaz());
        loanData.setStartedYaz(loan.getStartedYaz());
        return loanData;
    }

    @Override
    public CustomerData getCustomerData(CustomerAccount customer) throws DataNotFoundException {
        CustomerData customerData = new CustomerData();
        customerData.setBalance(customer.getBalance());
        customerData.setName(customer.getId());
        customerData.setNumOfActiveLoansInvested(customer.getNumOfRequestedLoansByStatus(LoanStatus.ACTIVE));
        customerData.setNumOfPendingLoansInvested(customer.getNumOfRequestedLoansByStatus(LoanStatus.PENDING));
        customerData.setNumOfRiskLoansInvested(customer.getNumOfRequestedLoansByStatus(LoanStatus.RISK));
        customerData.setNumOfFinishedLoansInvested(customer.getNumOfRequestedLoansByStatus(LoanStatus.FINISHED));
        customerData.setNumOfNewLoansInvested(customer.getNumOfRequestedLoansByStatus(LoanStatus.NEW));
        customerData.setNumOfActiveLoansRequested(customer.getNumOfInvestedLoansByStatus(LoanStatus.ACTIVE));
        customerData.setNumOfPendingLoansRequested(customer.getNumOfInvestedLoansByStatus(LoanStatus.PENDING));
        customerData.setNumOfRiskLoansRequested(customer.getNumOfInvestedLoansByStatus(LoanStatus.RISK));
        customerData.setNumOfFinishedLoansRequested(customer.getNumOfInvestedLoansByStatus(LoanStatus.FINISHED));
        customerData.setNumOfNewLoansRequested(customer.getNumOfInvestedLoansByStatus(LoanStatus.NEW));
        return customerData;
    }

    @Override
    public CustomersData getCustomersData() throws DataNotFoundException {
        CustomersData customersData = new CustomersData();
        List<CustomerData> customersList = new ArrayList<>();
        for(Pair<CustomerAccount, Integer> customerPair : customersAccounts.getAllPairs()) {
            customersList.add(getCustomerData(customerPair.getKey()));
        }
        customersData.setCustomers(customersList);
        return customersData;


    }

    @Override
    public LoansData getLoansData() throws DataNotFoundException {
        LoansData loansData = new LoansData();
        List<LoanData> loansList= new ArrayList<>();
        for(Pair<Loan, Integer> loanPair : loans.getAllPairs()) {
            loansList.add(getLoanData(loanPair.getKey()));
        }
        loansData.setLoans(loansList);
        return loansData;
    }
}
