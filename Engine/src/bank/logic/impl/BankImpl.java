package bank.logic.impl;

import bank.logic.Bank;
import bank.logic.accounts.Account;
import bank.logic.accounts.CustomerAccount;
import bank.logic.accounts.impl.Customer;
import bank.logic.accounts.impl.exceptions.NoMoneyException;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.data.storage.DataStorage;
import bank.logic.data.storage.impl.BankDataStorage;
import bank.logic.impl.exceptions.DataAlreadyExistsException;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.loans.Loan;
import bank.logic.loans.LoanStatus;
import bank.logic.loans.handler.impl.BankLoanHandler;
import bank.logic.loans.impl.BasicLoan;
import bank.logic.loans.impl.builder.LoanBuilder;
import bank.logic.loans.interest.Interest;
import bank.logic.loans.interest.exceptions.InvalidPercentException;
import bank.logic.loans.interest.impl.BasicInterest;
import bank.logic.loans.investments.Investment;
import bank.logic.loans.investments.impl.LoanInvestment;
import bank.logic.messages.impl.BankNotification;
import bank.logic.time.TimeHandler;
import bank.logic.time.handler.BankTimeHandler;
import bank.logic.transactions.Transaction;
import files.saver.Saver;
import files.saver.SystemSaver;
import files.xmls.XmlLoanReader;
import files.xmls.XmlReader;
import files.xmls.exceptions.*;
import javafx.util.Pair;
import manager.accounts.AccountDTO;
import manager.customers.*;
import manager.info.ClientInfoData;
import manager.investments.*;
import manager.loans.LoanDTO;
import manager.loans.LoanData;
import manager.loans.LoansDTO;
import manager.loans.LoansData;
import manager.loans.details.*;
import manager.categories.CategoriesDTO;
import manager.messages.NotificationData;
import manager.messages.NotificationsData;
import manager.time.TimeData;
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
    private final DataStorage<Investment> sellInvestmentsDataStorage;
    private Set<String> categories;
    private BankLoanHandler loanHandler;
    private TimeHandler timeHandler;
    private int customersVer;
    private int loansVer;
    private int categoriesVer;
    private int forecastVer;

    public BankImpl() {
        timeHandler = new BankTimeHandler();
        customersAccounts = new BankDataStorage<>(timeHandler);
        loanAccounts = new BankDataStorage<>(timeHandler);
        transactions = new BankDataStorage<>(timeHandler);
        loans = new BankDataStorage<>(timeHandler);
        loanHandler = new BankLoanHandler(transactions, loans, customersAccounts, timeHandler);
        categories = new HashSet<>();
        sellInvestmentsDataStorage = new BankDataStorage<>(timeHandler);
        customersVer = 0;
        loansVer = 0;
        categoriesVer = 0;
        forecastVer = 0;
    }

    @Override
    public void addCustomer(String customerName) {
        CustomerAccount customer = new Customer(customerName, 0);
        customersAccounts.addData(customer);
        customersVer++;
    }

    @Override
    public void listInvestment(InvestmentData data) throws DataNotFoundException {
        Optional<Investment> first = loans.getDataPair(data.getLoanId()).getKey().getInvestments().stream().filter(inv -> inv.getId().equals(data.getInvestmentId())).findFirst();

        first.ifPresent(sellInvestmentsDataStorage::addData);
    }

    @Override
    public void unlistInvestment(InvestmentData data) throws DataNotFoundException {
        if(sellInvestmentsDataStorage.isDataExists(data.getInvestmentId()))
            sellInvestmentsDataStorage.remove(data.getInvestmentId());
    }


    @Override
    public void investmentTrade(InvestmentData data) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        buyInvestment(data.getInvestmentId(), data.getBuyerId(), data.getOwnerId());
        customersVer++;
        loansVer++;
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
        buyerAccount.updateNotificationsVersion();
        sellerAccount.addNotification(new BankNotification("Sold " + investPrinting, getCurrentYaz()));
        sellerAccount.updateNotificationsVersion();
        investment.setInvestorId(buyerId);

        sellInvestmentsDataStorage.remove(investmentId);
    }

    @Override
    public InvestmentsSellData getCustomerInvestments(String customerId) throws DataNotFoundException {
        CustomerAccount account = customersAccounts.getDataById(customerId);
        List<Investment> investments = new ArrayList<>();

        account.getLoansInvested().stream().filter(loan -> loans.isDataExists(loan.getId())).filter(loan -> loan.getStatus() == LoanStatus.ACTIVE).forEach(loan -> loan.getInvestments().stream().filter(investment -> investment.getInvestorId().equals(customerId)).forEach(investments::add));

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
        makePaymentNotifications();
        timeHandler.advanceTime();
        updateLoansStatus();
    }

    private void makePaymentNotifications() {
        int notificationYaz = getCurrentYaz() + 1;
        Collection<Pair<Loan, Integer>> allPairs = loans.getAllPairs();
        allPairs.stream()
                .filter(p -> {
                    Loan currLoan = p.getKey();
                    LoanStatus status = currLoan.getStatus();
                    return (status == LoanStatus.ACTIVE) || (status == LoanStatus.RISKED);
                })
                .filter(p -> {
                    Loan currLoan = p.getKey();
                    return (currLoan.getNextYaz() == 1);
                })
                .forEach(p -> {
                    Loan currLoan = p.getKey();
                    try {
                        CustomerAccount loanRequester = customersAccounts.getDataById(currLoan.getOwnerId());
                        int paymentAmount = currLoan.getNextPayment();
                        if(paymentAmount > 0) {
                            loanRequester.addNotification(new BankNotification(
                                    "New payment of " + paymentAmount + " from loan '" + currLoan.getId() + "'",
                                    notificationYaz));
                            loanRequester.updateNotificationsVersion();
                        }
                    } catch (DataNotFoundException e) {
                        System.out.println("Account '" + currLoan.getOwnerId() + "' was not found.");
                    }
                });
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
                .forEach(pair -> updateRiskStatus(pair.getKey()));
        loansVer++;
    }

    private void updateRiskStatus(Loan loan) {
        int notificationYaz = getCurrentYaz();
        loan.setStatus(LoanStatus.RISKED);
        Set<String> investors = new HashSet<>();
        loan.getInvestments().forEach(inv -> {
            investors.add(inv.getInvestorId());
            if(sellInvestmentsDataStorage.isDataExists(inv.getId())) {
                try {
                    sellInvestmentsDataStorage.remove(inv.getId());
                    CustomerAccount invAcc = customersAccounts.getDataById(inv.getInvestorId());
                    invAcc.addNotification(new BankNotification(
                            "Investment in '" + loan.getId() + "' is unlisted from trading (loan in risk)",
                            notificationYaz));
                    invAcc.updateNotificationsVersion();
                } catch (DataNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        investors.forEach(investor -> {
            try {
                CustomerAccount investorAcc = customersAccounts.getDataById(investor);
                investorAcc.addNotification(new BankNotification(
                        "Loan '" + loan.getId() + "' is in Risk", notificationYaz));
                investorAcc.updateNotificationsVersion();
            } catch (DataNotFoundException e) {
                System.out.println(e.getMessage());
            }
        });
        try {
            CustomerAccount owner = customersAccounts.getDataById(loan.getOwnerId());
            owner.addNotification(new BankNotification("Your loan '" + loan.getId() + "' is now in risk!", notificationYaz));
            owner.updateNotificationsVersion();
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
    private int getMissingCycles(Loan loan) {
        int delta = getCurrentYaz() - loan.getStartedYaz();
        if (delta > 0) {
            int cyclesHadToPay = delta / loan.getCyclesPerPayment();
            int cyclesPaid = loan.getFullPaidCycles();

            return Math.min((cyclesHadToPay - cyclesPaid), loan.getDuration() - cyclesPaid);
        } else {
            return 0;
        }
    }

    @Override
    public void advanceOneCycle() throws DataNotFoundException, NonPositiveAmountException {
        loanHandler.oneCycle();
    }

    @Override
    public void advanceCycle(String loanId) throws DataNotFoundException, NonPositiveAmountException {
        customersVer++;
        loansVer++;
        forecastVer++;
        Loan loan = loans.getDataById(loanId);
        loanHandler.payOneCycle(loan);
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
        customersVer++;
    }

    @Override
    public void deposit(String accountId, int amount, String description) throws NonPositiveAmountException, DataNotFoundException {
        CustomerAccount account = customersAccounts.getDataById(accountId);
        Transaction transaction = account.deposit(amount, description);
        transactions.addData(transaction);
        customersVer++;
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
            System.out.println(e.getMessage());
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

            if(investorId.equals(requesterId) || investment.isFullyPaid()) // filter all owned investments or finished
                continue;

            investors.add(investorId);
            loans.add(investment.getLoanId());
            sellAmounts.add((int)investment.getSellPrice());
            yazPlaced.add(invPair.getValue());
            invIds.add(investment.getId());
            forSale.add(true);
        }

        return new InvestmentsSellData.SellBuilder()
                .name(investors)
                .loans(loans)
                .amount(sellAmounts)
                .time(yazPlaced)
                .id(invIds)
                .forSale(forSale)
                .Build();

    }

    @Override
    public void setInvestmentsData(InvestmentsData investmentsData) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        List<String> loansIds = investmentsData.getLoansIds();
        List<Loan> loansList = new ArrayList<>();
        for(String loanId : loansIds) {
            loansList.add(loans.getDataById(loanId));
        }
        setInvestments(investmentsData.getInvestorId(), loansList, investmentsData.getAmount());
        loansVer++;
    }
    @Override
    public void createInvestment(String investor, Loan loan, int amount) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        CustomerAccount investingAccount = customersAccounts.getDataById(investor);

        int percent = loan.getInterestPercent();
        int cyclesPerPayment = loan.getCyclesPerPayment();
        int duration = loan.getDuration();

        Interest interest = new BasicInterest(percent, amount, cyclesPerPayment, duration);

        Investment loanInvestment = new LoanInvestment(investor, interest, loan.getId(), timeHandler);
        loanHandler.addInvestment(loan, loanInvestment, investingAccount);
        investingAccount.addNotification(new BankNotification("Invested " + amount + " in '" + loan.getId() + "'.",
                timeHandler.getCurrentTime()));
        investingAccount.updateNotificationsVersion();
    }

    @Override
    public void setInvestments(String requesterName, List<Loan> loanDataList, int amount) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        int amountLeft = amount;
        int minAmount, loansAmount;
        int avgAmount;
        List<Integer> amountInvestingArr = new ArrayList<>();
        int lowestLoan = 0;

        loansAmount = loanDataList.size();
        loanDataList.sort(Comparator.comparingInt(Loan::getAmountToActive));
        minAmount = loanDataList.get(0).getAmountToActive();
        avgAmount = (int) Math.ceil (amountLeft / (loansAmount - lowestLoan));
        loanDataList.forEach(loan -> amountInvestingArr.add(0));


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
        int ownership = requestDTO.getMaxOwnership();

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

                        long relatedLoansAmount = customersAccounts.getDataById(p.getKey().getOwnerId()).getLoansRequested()
                                .stream().filter(ownedLoan -> ownedLoan.getStatus() != LoanStatus.FINISHED).count();

                        return relatedLoansAmount <= maxRelatedLoans;

                    } catch (DataNotFoundException e) {
                        return false;
                    }
                })
                .filter(p -> {
                    if(ownership == 0)
                        return true;

                    Loan loan = p.getKey();
                    double percentLeft = (loan.getAmountToActive() / loan.getBaseAmount()) * 100;
                    return ownership >= percentLeft;
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
    public LoansData getUnfinishedLoans(String customerId) throws DataNotFoundException {
            List<LoanData> loansList= new ArrayList<>();
            customersAccounts.getDataById(customerId).getLoansRequested().stream()
                    .filter(loan -> loans.isDataExists(loan.getId()))
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
        LoansData loansData = new LoansData();
        loansData.setLoans(loansList);
        return loansData;
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

        int balance = timeHandler.isReadOnly() ? calculateBalance(account):account.getBalance();

        AccountDTO accountDTO = new AccountDTO(account.getId(), balance, transactions);
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
        if(!timeHandler.isReadOnly()) {
            List<Transaction> accountTransactions = account.getTransactions();

            for (Transaction transaction : accountTransactions) {
                transactionsList.add(getTransactionDTO(transaction));
            }
        }
        else {
            transactionsList = calculateTransactions(account);
        }
        return new TransactionsDTO(transactionsList);
    }

    private List<TransactionDTO> calculateTransactions(Account account) {

        HashSet<String> idSet = new HashSet<>();
        List<Transaction> transactions = account.getTransactions();
        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        transactions.stream().forEach(t -> idSet.add(t.getId()));
        this.transactions.getAllPairs().stream()
                .filter(t -> idSet.contains(t.getKey().getId())).forEach(pair -> {
                            Transaction key = pair.getKey();
                            transactionDTOList.add(new TransactionDTO(
                                    key.getDescription(),
                                    key.getAmount(),
                                    key.getPreviousBalance(),
                                    pair.getValue())
                            );
                        }
                );
        return transactionDTOList;
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
        loanData.setNextPaymentAmount(loan.getNextPayment());
        loanData.setCloseAmount(loan.getAmountToCloseLoan());
        return loanData;
    }

    private int getInvestorsAmount(Loan loan) {
        Set<String> investors = new HashSet<>();
        loan.getInvestments().forEach(inv -> investors.add(inv.getInvestorId()));
        return investors.size();
    }

    @Override
    public CustomerData getCustomerData(CustomerAccount customer) {
        CustomerData customerData = new CustomerData();
        customerData.setBalance(calculateBalance(customer));
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
    public CustomersData getCustomersData() {
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
                    .filter(loan -> loans.isDataExists(loan.getId()))
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
                    .filter(loan -> loans.isDataExists(loan.getId()))
                    .forEach(loan -> {
                        try {
                            loanDataList.add(getLoanData(loan));
                        } catch (DataNotFoundException e) {
                            System.out.println(e.getMessage());;
                        }
                    });
            loansData.setLoans(loanDataList);
        } catch (DataNotFoundException e) {
            e.printStackTrace();
        }
        return loansData;
    }

    @Override
    public TransactionData getTransactionData(Transaction transaction, int yazMade) {
        TransactionData transactionData = new TransactionData();
        transactionData.setAmount(transaction.getAmount());
        transactionData.setDescription(transaction.getDescription());
        transactionData.setPreviousBalance(transaction.getPreviousBalance());
        transactionData.setYazMade(yazMade);
        return transactionData;
    }

    @Override
    public TransactionsData getTransactionsData(String customerId) {
        TransactionsData transactionsData = new TransactionsData();
        List<TransactionData> transactionsDataList = new ArrayList<>();
        int currentYaz = getCurrentYaz();
        try {
            customersAccounts.getDataById(customerId).getTransactions()
                    .forEach(transaction -> {
                        try {
                            int yazMade = transactions.getDataPair(transaction.getId()).getValue();

                            if(yazMade <= currentYaz) {
                                transactionsDataList.add(getTransactionData(transaction, yazMade));
                            }
                        } catch (DataNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    });
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
        transactionsData.setTransactions(transactionsDataList);
        return transactionsData;
    }

    @Override
    public NotificationsData getNotificationsData(String customerId) throws DataNotFoundException {
        NotificationsData notificationsData = new NotificationsData();
        List<NotificationData> notificationDataList = new ArrayList<>();
        CustomerAccount customer = customersAccounts.getDataById(customerId);
        customer.getNotificationList()
                .forEach(notification -> notificationDataList.add(new NotificationData.NotificationDataBuilder()
                        .message(notification.getMessage())
                        .yazMade(notification.getYazMade())
                        .build()));
        notificationsData.setNotificationsList(notificationDataList);
        notificationsData.setNotificationVersion(customer.getNotificationsVersion());
        return notificationsData;
    }

    @Override
    public void deriskLoanByAmount(String id, int amount) { //TODO: DTO
        try {
            Loan loan = loans.getDataById(id);
            loanHandler.payLoanByAmount(loan, amount);
            customersVer++;
            loansVer++;
            forecastVer++;
            updateActiveStatus(loan);
        } catch (DataNotFoundException | NoMoneyException | NonPositiveAmountException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateActiveStatus(Loan loan) throws DataNotFoundException {
        if(loan.getMissingCycles() <= 1) { // TODO: CHECK CLOSE LOAN
            int messageYaz = getCurrentYaz();
            CustomerAccount ownerAcc = customersAccounts.getDataById(loan.getOwnerId());

            Set<String> investors = new HashSet<>();
            loan.getInvestments().forEach(inv -> investors.add(inv.getInvestorId()));

            ownerAcc.addNotification(new BankNotification("Your Loan '" + loan.getId() + "' is not in risk an is active again!", messageYaz));
            ownerAcc.updateNotificationsVersion();
            investors.forEach(investor -> {
                try {
                    CustomerAccount invAcc = customersAccounts.getDataById(investor);
                    invAcc.addNotification(new BankNotification("The Loan '" + loan.getId() + "' is not in risk and is active again!", messageYaz));
                    invAcc.updateNotificationsVersion();
                } catch (DataNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            });
            loan.setStatus(LoanStatus.ACTIVE);
        }
    }

    @Override
    public void closeLoan(String id) throws DataNotFoundException, NoMoneyException, NonPositiveAmountException {
        Loan loan = loans.getDataById(id);
        loanHandler.closeLoan(loan);
        customersVer++;
        forecastVer++;
        loansVer++;
    }

    @Override
    public PaymentsData getPaymentsData(String customerId) throws DataNotFoundException {
        List<Integer> payments = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();

        int currentYaz = getCurrentYaz();
        for (int i = 0; i < currentYaz; i++) {
            payments.add(0);
            amounts.add(0);
        }

        CustomerAccount customer = customersAccounts.getDataById(customerId);
        customer.getTransactions().stream()
                .filter(transaction -> transactions.isDataExists(transaction.getId()))
                .filter(transaction -> {
                    String description = transaction.getDescription().toLowerCase();
                    return description.contains("loan") && description.contains("payment");
                })
                .filter(transaction -> transaction.getAmount() > 0)
                .forEach(transaction -> {
                    try {
                        int yazIndex = transactions.getDataPair(transaction.getId()).getValue() - 1;
                        payments.set(yazIndex, payments.get(yazIndex) + 1);
                        amounts.set(yazIndex, amounts.get(yazIndex) + transaction.getAmount());
                    } catch (DataNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                });
        return new PaymentsData.PaymentsDataBuilder().payments(payments).amount(amounts).version(forecastVer).build();
    }

    @Override
    public PaymentsData getAllTransactionsData() {
        List<Integer> payments = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();

        int time = getCurrentYaz();
        for(int i = 0; i <= time; i++) {
            payments.add(0);
            amounts.add(0);
        }

        transactions.getAllPairs()
                .forEach(pair -> {
                    int yazIndex = pair.getValue();
                    int amount = Math.abs(pair.getKey().getAmount());
                    amounts.set(yazIndex, amounts.get(yazIndex) + amount);
                    payments.set(yazIndex, payments.get(yazIndex) + 1);
                });

        amounts.forEach(number -> number = number / 2);

        return new PaymentsData.PaymentsDataBuilder().payments(payments).amount(amounts).build();
    }

    @Override
    public PaymentsData getAllLoansData() {
        List<Integer> payments = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();

        int time = getCurrentYaz();
        for(int i = 0; i <= time; i++) {
            payments.add(0);
            amounts.add(0);
        }

        loans.getAllPairs()
                .forEach(pair -> {
                    int yazIndex = pair.getValue();
                    int amount = Math.abs(pair.getKey().getBaseAmount());
                    amounts.set(yazIndex, amounts.get(yazIndex) + amount);
                    payments.set(yazIndex, payments.get(yazIndex) + 1);
                });

        return new PaymentsData.PaymentsDataBuilder().payments(payments).amount(amounts).build();
    }

    @Override
    public PaymentsData getAllCustomersData() {
        List<Integer> payments = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();

        int time = getCurrentYaz();
        for(int i = 0; i <= time; i++) {
            payments.add(0);
            amounts.add(0);
        }

        customersAccounts.getAllPairs()
                .forEach(pair -> {
                    int yazIndex = pair.getValue();
                    int amount = Math.abs(pair.getKey().getBalance());
                    amounts.set(yazIndex, amounts.get(yazIndex) + amount);
                    payments.set(yazIndex, payments.get(yazIndex) + 1);
                });

        return new PaymentsData.PaymentsDataBuilder().payments(payments).amount(amounts).build();
    }

    @Override
    public boolean isCustomerExists(String username) {
        return customersAccounts.isDataExists(username);
    }

    @Override
    public void addLoansFromFile(String customer, String filePath) throws XmlPaymentsException, NonPositiveAmountException, DataNotFoundException, DataAlreadyExistsException {
        List<LoanData> loansFromXML = XmlLoanReader.getLoansFromXML(filePath);
        String categoryName, loanName;
        int interestPercent, amount, payPerTime, totalTime;

        CustomerAccount customerAccount = customersAccounts.getDataById(customer);

        for(LoanData loan : loansFromXML) {
            categoryName = loan.getCategory();
            loanName = loan.getName();
            interestPercent = loan.getInterest();
            amount = loan.getBaseAmount();
            payPerTime = loan.getCyclesPerPayment();
            totalTime = loan.getFinishedYaz();

            if(loans.isDataExists(loanName))
                throw new DataAlreadyExistsException(loanName);

            Interest interest = new BasicInterest(interestPercent, amount, payPerTime, totalTime);
            LoanBuilder loanBuilder = new LoanBuilder(customer, categoryName, loanName);

            Loan loanData = new BasicLoan(loanBuilder, interest, timeHandler);
            customerAccount.addRequestedLoan(loanData);
            loans.addData(loanData);
            categories.add(categoryName);
        }
        categoriesVer++;
        customersVer++;
        loansVer++;
    }

    @Override
    public ClientInfoData getClientInfo(String customer) throws DataNotFoundException {
        CustomerAccount customerAccount = customersAccounts.getDataById(customer);
        return new ClientInfoData.ClientInfoDataBuilder()
                .balance(calculateBalance(customerAccount))
                .categories(categories)
                .version(categoriesVer)
                .build();
    }

    @Override
    public int getCustomersVer() {
        return customersVer;
    }

    @Override
    public int getLoansVer() {
        return loansVer;
    }

    @Override
    public void setRewind(int time) {
        if(timeHandler.isReadOnly())
            timeHandler.resetRewind();

        timeHandler.rewindTime(time);
        loanHandler.calculateLoansStatus();
        customersVer++;
        loansVer++;
        forecastVer++;
    }

    @Override
    public void resetRewind() {
        if(timeHandler.isReadOnly())
            timeHandler.resetRewind();

        loanHandler.calculateLoansStatus();
    }

    @Override
    public TimeData getTimeData() {
        int time = timeHandler.getCurrentTime();
        boolean readOnly = timeHandler.isReadOnly();

        return new TimeData(time, readOnly);
    }

    @Override
    public void createLoan(String customer, LoanData data) throws Exception {

        CustomerAccount account = customersAccounts.getDataById(customer);

        String category = data.getCategory();
        String name = data.getName();
        int interestAmount = data.getInterest();
        int baseAmount = data.getBaseAmount();
        int cyclesPerPayment = data.getCyclesPerPayment();
        int duration = data.getFinishedYaz();

        if(loans.isDataExists(name))
            throw new DataAlreadyExistsException(name);

        if(interestAmount <= 0 || baseAmount <= 0 || cyclesPerPayment <= 0 || duration <= 0)
            throw new NonPositiveAmountException();

        if(category.isEmpty())
            throw new Exception("Category cannot be empty!");

        LoanBuilder details = new LoanBuilder(customer, category, name);

        Interest interest = new BasicInterest(interestAmount, baseAmount, cyclesPerPayment, duration);
        Loan loan = loanHandler.createLoan(details, interest);

        account.addRequestedLoan(loan);
        loans.addData(loan);
        loansVer++;
        customersVer++;
        forecastVer++;
    }

    private int calculateBalance(CustomerAccount account) {
        Integer balance = 0;
        int currYaz = getCurrentYaz();

        HashSet<String> idSet = new HashSet<>();
        List<Transaction> collect = account.getTransactions().stream()
                .filter(transaction -> transactions.isDataExists(transaction.getId())).collect(Collectors.toList());

        for(Transaction transaction : collect)
            balance += transaction.getAmount();

        return balance;
    }


}
