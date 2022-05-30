package bank.impl;

import bank.Bank;
import bank.accounts.Account;
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
import bank.messages.impl.BankNotification;
import bank.time.TimeHandler;
import bank.time.handler.BankTimeHandler;
import bank.transactions.Transaction;
import files.saver.Saver;
import files.saver.SystemSaver;
import files.xmls.XmlReader;
import files.xmls.exceptions.*;
import javafx.util.Pair;
import manager.accounts.AccountDTO;
import manager.customers.*;
import manager.investments.InvestmentData;
import manager.investments.InvestmentsData;
import manager.investments.InvestmentsSellData;
import manager.investments.RequestDTO;
import manager.loans.LoanDTO;
import manager.loans.LoanData;
import manager.loans.LoansDTO;
import manager.loans.LoansData;
import manager.loans.details.*;
import manager.categories.CategoriesDTO;
import manager.messages.NotificationData;
import manager.messages.NotificationsData;
import manager.time.YazSystemDTO;
import manager.transactions.TransactionDTO;
import manager.transactions.TransactionData;
import manager.transactions.TransactionsDTO;
import manager.transactions.TransactionsData;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BankImpl implements Bank {
    private DataStorage<CustomerAccount> customersAccounts;
    private DataStorage<Account> loanAccounts;
    private DataStorage<Transaction> transactions;
    private DataStorage<Loan> loans;
    private DataStorage<Investment> sellInvestmentsDataStorage;
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
        sellInvestmentsDataStorage = new BankDataStorage<>(timeHandler);
    }

    public void listInvestment(InvestmentData data) throws DataNotFoundException {
        Optional<Investment> first = loans.getDataPair(data.getLoanId()).getKey().getInvestments().stream().filter(inv -> inv.getId().equals(data.getInvestmentId())).findFirst();

        if(first.isPresent()) {
            sellInvestmentsDataStorage.addData(first.get());
        }
    }

    public void unlistInvestment(InvestmentData data) throws DataNotFoundException {
        if(sellInvestmentsDataStorage.isDataExists(data.getInvestmentId()))
            sellInvestmentsDataStorage.remove(data.getInvestmentId());
    }


    public void investmentTrade(InvestmentData data) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        buyInvestment(data.getInvestmentId(), data.getBuyerId(), data.getOwnerId());
    }
    private void buyInvestment(String investmentId, String buyerId, String sellerId) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        Investment investment = sellInvestmentsDataStorage.getDataById(investmentId);
        int amount = (int)investment.getSellPrice();
        CustomerAccount buyerAccount = customersAccounts.getDataById(buyerId);
        CustomerAccount sellerAccount = customersAccounts.getDataById(sellerId);
        Loan loan = loans.getDataById(investment.getLoanId());
        String investPrinting = investment.toString();

        transactions.addData(buyerAccount.withdraw(amount, "Buying Investment"));
        transactions.addData(sellerAccount.deposit(amount, "Selling Investment"));

        buyerAccount.getLoansInvested().add(loan);
        sellerAccount.getLoansInvested().remove(loan);
        buyerAccount.addNotification(new BankNotification("Bought " + investPrinting, getCurrentYaz()));
        sellerAccount.addNotification(new BankNotification("Sold " + investPrinting, getCurrentYaz()));
        investment.setInvestorId(buyerId);

        sellInvestmentsDataStorage.remove(investmentId);
    }

    public InvestmentsSellData getCustomerInvestments(String customerId) throws DataNotFoundException {
        CustomerAccount account = customersAccounts.getDataById(customerId);
        List<Investment> investments = new ArrayList<>();

        account.getLoansInvested().stream().forEach(loan -> {
            loan.getInvestments().stream().filter(investment -> investment.getInvestorId().equals(customerId)).forEach(investment -> {
                investments.add(investment);
            });
        });

        return investmentsListToData(investments);
    }

    private InvestmentsSellData investmentsListToData(List<Investment> investments) {
        List<String> ownerId = new ArrayList<>();
        List<String> loanId = new ArrayList<>();
        List<Integer> amount = new ArrayList<>();
        List<Integer> yazPlaced = new ArrayList<>();
        List<String> invIds = new ArrayList<>();
        List<Boolean> forSale = new ArrayList<>();

        for(Investment investment : investments) {
            ownerId.add(investment.getInvestorId());
            loanId.add(investment.getLoanId());
            amount.add((int)investment.getSellPrice());
            yazPlaced.add(0);
            invIds.add(investment.getId());
            forSale.add(sellInvestmentsDataStorage.isDataExists(investment.getId()));
        }

        return new InvestmentsSellData.SellBuilder()
                .name(ownerId)
                .loans(loanId)
                .amount(amount)
                .time(yazPlaced)
                .id(invIds)
                .forSale(forSale)
                .Build();
    }

    @Override
    public DataStorage<CustomerAccount> getCustomersAccounts() {
        return customersAccounts;
    }

    @Override
    public DataStorage<Account> getLoanAccounts() {
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
    public void advanceOneYaz() {
        timeHandler.advanceTime();
        updateLoansStatus();
    }

    private void updateLoansStatus() {
        Collection<Pair<Loan, Integer>> allPairs = loans.getAllPairs();
        allPairs.stream()
                .filter(pair -> {
                    Loan currLoan = pair.getKey();
                    LoanStatus status = currLoan.getStatus();
                    return (status == LoanStatus.ACTIVE);
                })
                .filter(pair -> {
                    Loan loan = pair.getKey();
                    return getMissingCycles(loan) > 1;
                })
                .forEach(pair -> pair.getKey().setStatus(LoanStatus.RISKED));
    }

    private int getMissingCycles(Loan loan) {
        int cyclesHadToPay = (getCurrentYaz() - loan.getStartedYaz()) / loan.getCyclesPerPayment();
        int cyclesPaid = loan.getFullPaidCycles();

        return Math.min((cyclesHadToPay - cyclesPaid), loan.getDuration() - cyclesPaid);
    }

    @Override
    public void advanceOneCycle() throws DataNotFoundException, NonPositiveAmountException {
        loanHandler.oneCycle();
    }

    @Override
    public int getCurrentYaz() {
        return timeHandler.getCurrentTime();
    }

    @Override
    public void withdraw(String accountId, int amount, String description) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        CustomerAccount account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.withdraw(amount, description);
        transactions.addData(transaction);
        account.addNotification(new BankNotification("Withdrew " + amount,
                timeHandler.getCurrentTime()));
    }

    @Override
    public void deposit(String accountId, int amount, String description) throws NonPositiveAmountException, DataNotFoundException {
        CustomerAccount account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.deposit(amount, description);
        transactions.addData(transaction);
        account.addNotification(new BankNotification("Deposited " + amount,
                timeHandler.getCurrentTime()));
    }

    @Override
    public void createAccount(String name, int balance) {
        CustomerAccount account = new Customer(name, balance);

        customersAccounts.addData(account);
    }

    @Override
    public void deriskLoan(Loan loan) throws NoMoneyException, NonPositiveAmountException, DataNotFoundException {
        loanHandler.deriskLoan(loan);
    }

    @Override
    public void deriskLoanRequest(String loanId) throws NoMoneyException, NonPositiveAmountException {
        try {
            deriskLoan(loans.getDataById(loanId));
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getDeriskAmount(Loan loan) {
        return loan.getDeriskAmount();
    }


    @Override
    public InvestmentsSellData getInvestmentsForSell(String requesterId) {
        List<String> investors = new ArrayList<>();
        List<String> loans = new ArrayList<>();
        List<Integer> sellAmounts = new ArrayList<>();
        List<Integer> yazPlaced = new ArrayList<>();
        List<String> invIds = new ArrayList<>();
        List<Boolean> forSale = new ArrayList<>();

        Collection<Pair<Investment, Integer>> allPairs = sellInvestmentsDataStorage.getAllPairs();

        for(Pair<Investment, Integer> invPair : allPairs) {
            Investment investment = invPair.getKey();
            String investorId = investment.getInvestorId();

            if(investorId.equals(requesterId)) // filter all owned investments
                continue;

            investors.add(investorId);
            loans.add(investment.getLoanId());
            sellAmounts.add((int)investment.getSellPrice());
            yazPlaced.add(invPair.getValue());
            invIds.add(investment.getId());
            forSale.add(true);
        }

        InvestmentsSellData sellData = new InvestmentsSellData.SellBuilder()
                .name(investors)
                .loans(loans)
                .amount(sellAmounts)
                .time(yazPlaced)
                .id(invIds)
                .forSale(forSale)
                .Build();

        return sellData;

    }
    public void setInvestmentsData(InvestmentsData investmentsData) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        List<String> loansIds = investmentsData.getLoansIds();
        List<Loan> loansList = new ArrayList<>();
        for(String loanId : loansIds) {
            loansList.add(loans.getDataById(loanId));
        }
        setInvestments(investmentsData.getInvestorId(),
                loansList,
                investmentsData.getAmount()
        );
    }
    @Override
    public void createInvestment(String investor, Loan loan, int amount) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        CustomerAccount investingAccount = customersAccounts.getDataById(investor);

        float percent = loan.getInterestPercent();
        int amountInvesting = amount;
        int cyclesPerPayment = loan.getCyclesPerPayment();
        int duration = loan.getDuration();

        Interest interest = new BasicInterest(percent, amountInvesting, cyclesPerPayment, duration);

        Investment loanInvestment = new LoanInvestment(investor, interest, loan.getId());
        loanHandler.addInvestment(loan, loanInvestment, investingAccount);
        investingAccount.addNotification(new BankNotification("Invested " + amountInvesting + " in '" + loan.getId() + "'.",
                timeHandler.getCurrentTime()));
    }

    @Override
    public void setInvestments(String requesterName, List<Loan> loanDataList, int amount) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        int amountLeft = amount;
        int minAmount, loansAmount;
        int avgAmount;
        List<Integer> amountInvestingArr = new ArrayList<>();
        int lowestLoan = 0;

        loansAmount = loanDataList.size();
        loanDataList.sort(Comparator.comparingInt(p -> p.getAmountToActive()));
        minAmount = loanDataList.get(0).getAmountToActive();
        avgAmount = (int) Math.ceil (amountLeft / (loansAmount - lowestLoan));
        loanDataList.stream().forEach(loan -> amountInvestingArr.add(0));


        if (minAmount > avgAmount) {
            for (Loan loan : loanDataList) {
                createInvestment(requesterName, loan, avgAmount);
            }
        }
        else
        {
            do {
                int i = lowestLoan;
                minAmount = loanDataList.get(i).getAmountToActive() - amountInvestingArr.get(i);
                avgAmount = (int) Math.ceil (amountLeft / (loansAmount - lowestLoan));

                if(minAmount > avgAmount) {
                    for(; i < loansAmount; i++) {
                        amountInvestingArr.set(i, amountInvestingArr.get(i) + avgAmount);
                    }
                    break;
                }

                for (; i < loansAmount; i++) {
                    if (minAmount <= amountLeft) {
                        amountInvestingArr.set(i, amountInvestingArr.get(i) + minAmount);
                        amountLeft -= minAmount;
                        if(loanDataList.get(i).getAmountToActive() == amountInvestingArr.get(i)) {
                            lowestLoan++;
                        }
                    } else {
                        break;
                    }
                }

                if(lowestLoan == loansAmount)
                    break;
            }
            while (amountLeft > 0);

            for (int i = 0; i < loansAmount; i++) {
                createInvestment(requesterName, loanDataList.get(i), amountInvestingArr.get(i));
            }
        }
    }
    @Override
    public LoansData loanAssignmentRequest(RequestDTO requestDTO) throws InvalidPercentException {
        int amount = requestDTO.getAmount();
        String requesterName = requestDTO.getRequesterName();
        float minInterest = requestDTO.getMinInterest();
        Collection<String> categories = requestDTO.getCategoriesDTO().getCategories();
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
                .filter(p -> {
                    try {
                        if(maxRelatedLoans == 0)
                            return true;

                        int relatedLoansAmount = customersAccounts.getDataById(p.getKey().getOwnerId()).getLoansRequested().size();
                        return relatedLoansAmount <= maxRelatedLoans;

                    } catch (DataNotFoundException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        List<LoanData> loansDataList = new ArrayList<>();

        for(Pair<Loan, Integer> loanPair : relevantLoans) {
            try {
                loansDataList.add(getLoanData(loanPair.getKey()));
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
        }

        LoansData loansData = new LoansData();
        loansData.setLoans(loansDataList);
        return loansData;

    }
    @Override
    public List<LoanData> getUnFinishedLoans(String customerId) throws DataNotFoundException {
            List<LoanData> loansList= new ArrayList<>();
            customersAccounts.getDataById(customerId).getLoansRequested().stream()
                    .filter(loan -> !loan.getStatus().equals(LoanStatus.FINISHED))
                    .filter(loan -> !loan.getStatus().equals(LoanStatus.NEW))
                    .filter(loan -> !loan.getStatus().equals(LoanStatus.PENDING))
                    .forEach(loan -> {
                        try {
                            loansList.add(getLoanData(loan));
                        } catch (DataNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
            return loansList;
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
        Collection<Pair<CustomerAccount, Integer>> customersList = customersAccounts.getAllPairs();

        for(Pair<CustomerAccount, Integer> account : customersList) {
            customersDTOList.add(getCustomerDTO(account.getKey().getId()));
        }

        return new CustomersDTO(customersDTOList);
    }

    @Override
    public CustomerDTO getCustomerDTO(String id) throws DataNotFoundException {
        CustomerAccount account = customersAccounts.getDataById(id);
        List<LoanDTO> loansInvestedDTOList = new ArrayList<>();
        List<LoanDTO> loansRequestedDTOList = new ArrayList<>();
        Collection<Loan> loansInvested = account.getLoansInvested();
        Collection<Loan> loansRequested = account.getLoansRequested();

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
    public TransactionsDTO getTransactionsDTO(Account account) throws DataNotFoundException {
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
            customersAccounts = (DataStorage<CustomerAccount>) saver.getCustomers();
            loanAccounts = (DataStorage<Account>) saver.getLoanAccounts();
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
        loanData.setStatus(loan.getStatus().toString());
        loanData.setNextPaymentAmount(loan.getPayment());
        loanData.setCyclesPerPayment(loan.getCyclesPerPayment());
        loanData.setNextPaymentInYaz(loan.getNextYaz());
        loanData.setStartedYaz(loan.getStartedYaz());
        loanData.setInvestorsAmount(getInvestorsAmount(loan));
        loanData.setMissingCycles(getMissingCycles(loan));
        return loanData;
    }

    private int getInvestorsAmount(Loan loan) {
        Set<String> investors = new HashSet<>();
        loan.getInvestments().stream().forEach(inv -> investors.add(inv.getInvestorId()));
        return investors.size();
    }

    @Override
    public CustomerData getCustomerData(CustomerAccount customer) throws DataNotFoundException {
        CustomerData customerData = new CustomerData();
        customerData.setBalance(customer.getBalance());
        customerData.setName(customer.getId());
        customerData.setNumOfActiveLoansInvested(customer.getNumOfInvestedLoansByStatus(LoanStatus.ACTIVE));
        customerData.setNumOfPendingLoansInvested(customer.getNumOfInvestedLoansByStatus(LoanStatus.PENDING));
        customerData.setNumOfRiskLoansInvested(customer.getNumOfInvestedLoansByStatus(LoanStatus.RISKED));
        customerData.setNumOfFinishedLoansInvested(customer.getNumOfInvestedLoansByStatus(LoanStatus.FINISHED));
        customerData.setNumOfNewLoansInvested(customer.getNumOfInvestedLoansByStatus(LoanStatus.NEW));
        customerData.setNumOfActiveLoansRequested(customer.getNumOfRequestedLoansByStatus(LoanStatus.ACTIVE));
        customerData.setNumOfPendingLoansRequested(customer.getNumOfRequestedLoansByStatus(LoanStatus.PENDING));
        customerData.setNumOfRiskLoansRequested(customer.getNumOfRequestedLoansByStatus(LoanStatus.RISKED));
        customerData.setNumOfFinishedLoansRequested(customer.getNumOfRequestedLoansByStatus(LoanStatus.FINISHED));
        customerData.setNumOfNewLoansRequested(customer.getNumOfRequestedLoansByStatus(LoanStatus.NEW));
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

    @Override
    public CustomersNames getCustomersNames() {
        List<String> names = new ArrayList<>();
        for(Pair<CustomerAccount, Integer> customerPair : customersAccounts.getAllPairs()) {
            names.add(customerPair.getKey().getId());
        }
        return new CustomersNames(names);
    }

    @Override
    public LoansData getInvestorData(String customerId) {
        List<LoanData> loanDataList = new ArrayList<>();
        LoansData loansData = new LoansData();
        try {
            customersAccounts.getDataById(customerId)
                    .getLoansInvested()
                    .stream()
                    .forEach(loan -> {
                        try {
                            loanDataList.add(getLoanData(loan));
                        } catch (DataNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
            loansData.setLoans(loanDataList);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        return loansData;
    }

    @Override
    public LoansData getLoanerData(String customerId) {
        List<LoanData> loanDataList = new ArrayList<>();
        LoansData loansData = new LoansData();
        try {
            customersAccounts.getDataById(customerId)
                    .getLoansRequested()
                    .stream()
                    .forEach(loan -> {
                        try {
                            loanDataList.add(getLoanData(loan));
                        } catch (DataNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
            loansData.setLoans(loanDataList);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        return loansData;
    }

    @Override
    public TransactionData getTransactionData(Transaction transaction) {
        TransactionData transactionData = new TransactionData();
        transactionData.setAmount(transaction.getAmount());
        transactionData.setDescription(transaction.getDescription());
        transactionData.setPreviousBalance(transaction.getPreviousBalance());
        try {
            transactionData.setYazMade(transactions.getDataPair(transaction.getId()).getValue());
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        return transactionData;
    }

    @Override
    public TransactionsData getTransactionsData(String customerId) {
        TransactionsData transactionsData = new TransactionsData();
        List<TransactionData> transactionsDataList = new ArrayList<>();
        try {
            customersAccounts.getDataById(customerId).getTransactions()
                    .stream()
                    .forEach(transaction -> { transactionsDataList.add(getTransactionData(transaction));});
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        transactionsData.setTransactions(transactionsDataList);
        return transactionsData;
    }

    @Override
    public NotificationsData getNotificationsData(String customerId) throws DataNotFoundException {
        NotificationsData notificationsData = new NotificationsData();
        List<NotificationData> notificationDataList = new ArrayList<>();
        customersAccounts.getDataById(customerId).getNotificationList().stream()
                .forEach(notification -> {notificationDataList.add(new NotificationData.NotificationDataBuilder()
                        .message(notification.getMessage())
                        .yazMade(notification.getYazMade())
                        .build());});
        notificationsData.setNotificationsList(notificationDataList);
        return notificationsData;
    }
}
