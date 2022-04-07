package files.xmls;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import bank.accounts.Account;
import bank.accounts.impl.CustomerAccount;
import bank.data.storage.DataStorage;
import bank.data.storage.impl.BankDataStorage;
import bank.impl.exceptions.DataNotFoundException;
import bank.loans.Loan;
import bank.loans.impl.BasicLoan;
import bank.loans.impl.builder.LoanBuilder;
import bank.loans.interest.Interest;
import bank.loans.interest.impl.BasicInterest;
import bank.time.TimeHandler;
import files.schema.generated.*;
import files.xmls.exceptions.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlReader {
    private static final String JAXB_XML_ABS_PACKAGE_NAME = "files.schema.generated";
    private Set<String> categoryNames;
    private DataStorage<Account> customersDataStorage;
    private DataStorage<Loan> loansDataStorage;
    private boolean validation;

    public XmlReader(String filePath, TimeHandler timeHandler) throws NotXmlException, XmlNoLoanOwnerException, XmlNoCategoryException, XmlPaymentsException, XmlAccountExistsException, XmlNotFoundException, DataNotFoundException {
        Path path = Paths.get(filePath);

        if(!Files.exists(path))
            throw new XmlNotFoundException(filePath);

        int extIndex = filePath.lastIndexOf('.');
        if(!filePath.substring(extIndex).equalsIgnoreCase(".xml"))
            throw new NotXmlException(filePath);
        try {
            InputStream inputStream = new FileInputStream(filePath);
            categoryNames = new HashSet<>();

            AbsDescriptor descriptor = deserializeFrom(inputStream);
            AbsCategories categories = descriptor.getAbsCategories();

            List<String> categoryList = categories.getAbsCategory();
            List<AbsCustomer> customersList = descriptor.getAbsCustomers().getAbsCustomer();
            List<AbsLoan> loansList = descriptor.getAbsLoans().getAbsLoan();

            customersDataStorage = new BankDataStorage<>(timeHandler);
            loansDataStorage = new BankDataStorage<>(timeHandler);

            // add all categories to a set

            categoryNames.addAll(categoryList);

            // add all accounts to data storage

            for(AbsCustomer absCustomer : customersList) {
                String currName = absCustomer.getName();

                if(customersDataStorage.isDataExists(currName))
                    throw new XmlAccountExistsException();

                Account currCustomer = new CustomerAccount(currName, absCustomer.getAbsBalance());
                customersDataStorage.addData(currCustomer);
            }

            for(AbsLoan currLoan : loansList) {
                String loanName = currLoan.getId();
                String ownerName = currLoan.getAbsOwner();
                String categoryName = currLoan.getAbsCategory();
                int amount = currLoan.getAbsCapital();
                int totalTime = currLoan.getAbsTotalYazTime();
                int payPerTime = currLoan.getAbsPaysEveryYaz();
                int interestPercent = currLoan.getAbsIntristPerPayment();

                if(!categoryNames.contains(categoryName))
                    throw new XmlNoCategoryException();

                if(!customersDataStorage.isDataExists(ownerName))
                    throw new XmlNoLoanOwnerException();

                if(totalTime % payPerTime != 0)
                    throw new XmlPaymentsException();


                Interest interest = new BasicInterest(interestPercent, amount, payPerTime, totalTime);
                LoanBuilder loanBuilder = new LoanBuilder(ownerName, categoryName, loanName);

                Loan loanData = new BasicLoan(loanBuilder, interest, timeHandler);
                customersDataStorage.getDataById(ownerName).addRequestedLoan(loanData);
                loansDataStorage.addData(loanData);
                // TODO: ADD LOAN ID TO CUSTOMER'S LOANS SET


            }
            validation = true;

        } catch (FileNotFoundException | JAXBException e) {
            validation = false;
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


    public boolean isValid() {
        return validation;
    }
}
