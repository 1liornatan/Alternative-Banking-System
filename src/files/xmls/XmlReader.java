package files.xmls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import bank.Bank;
import bank.accounts.Account;
import bank.accounts.impl.CustomerAccount;
import bank.data.storage.DataStorage;
import bank.data.storage.impl.BankDataStorage;
import bank.loans.Loan;
import bank.loans.impl.BasicLoan;
import bank.loans.impl.builder.LoanBuilder;
import bank.loans.interest.Interest;
import bank.loans.interest.impl.BasicInterest;
import files.schema.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlReader {
    private static final String JAXB_XML_ABS_PACKAGE_NAME = "files.schema.generated";
    private Set<String> categoryNames;
    private DataStorage<Account> customersDataStorage;
    private DataStorage<Loan> loansDataStorage;

    public XmlReader(String filePath) {
        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            Map<String, Integer> customersDataDecoder = new HashMap<>();
            Map<String, Integer> loansDataDecoder = new HashMap<>();
            categoryNames = new HashSet<>();

            AbsDescriptor descriptor = deserializeFrom(inputStream);
            AbsCategories categories = descriptor.getAbsCategories();
            AbsLoans loans = descriptor.getAbsLoans();

            List<String> categoryList = categories.getAbsCategory();
            List<AbsCustomer> customersList = descriptor.getAbsCustomers().getAbsCustomer();
            List<AbsLoan> loansList = descriptor.getAbsLoans().getAbsLoan();

            customersDataStorage = new BankDataStorage<>();
            loansDataStorage = new BankDataStorage<>();

            // add all categories to a set

            for(String currCategory : categoryList) {
                categoryNames.add(currCategory);
            }

            // add all accounts to data storage

            for(AbsCustomer absCustomer : customersList) {
                String currName = absCustomer.getName();
                Account currCustomer = new CustomerAccount(currName, absCustomer.getAbsBalance());
                customersDataStorage.addData(currCustomer);
                customersDataDecoder.put(currName, currCustomer.getId());
            }

            for(AbsLoan currLoan : loansList) {
                String loanName = currLoan.getId();
                String ownerName = currLoan.getAbsOwner();
                String categoryName = currLoan.getAbsCategory();
                float amount = currLoan.getAbsCapital();
                int totalTime = currLoan.getAbsTotalYazTime();
                int payPerTime = currLoan.getAbsPaysEveryYaz();
                int interestPercent = currLoan.getAbsIntristPerPayment();
                int ownerId = customersDataDecoder.get(ownerName);

                Interest interest = new BasicInterest(interestPercent, amount, payPerTime, totalTime);
                LoanBuilder loanBuilder = new LoanBuilder(ownerId, categoryName, loanName);

                loansDataStorage.addData(new BasicLoan(loanBuilder, interest));
                // TODO: ADD LOAN ID TO CUSTOMER'S LOANS SET


            }


        } catch (FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getCategoryNames() {
        return categoryNames;
    }

    public DataStorage<Account> getCustomersDataStorage() {
        return customersDataStorage;
    }

    public DataStorage<Loan> getLoansDataStorage() {
        return loansDataStorage;
    }

    private AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_ABS_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);
    }


}
