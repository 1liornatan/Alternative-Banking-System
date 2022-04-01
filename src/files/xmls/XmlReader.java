package files.xmls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import bank.accounts.Account;
import bank.accounts.impl.CustomerAccount;
import bank.data.storage.DataStorage;
import bank.data.storage.impl.BankDataStorage;
import files.schema.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlReader {

    public static final String JAXB_XML_ABS_PACKAGE_NAME = "files.schema.generated";

    public static void main(String[] args) {
        try {
            InputStream inputStream = new FileInputStream(new File("src/resources/ex1-big.xml"));
            Map<String, Integer> customersDataDecoder = new HashMap<>();
            Map<String, Integer> loansDataDecoder = new HashMap<>();
            Set<String> categoryNames = new HashSet<>();

            AbsDescriptor descriptor = deserializeFrom(inputStream);
            AbsCategories categories = descriptor.getAbsCategories();
            AbsLoans loans = descriptor.getAbsLoans();

            List<String> categoryList = categories.getAbsCategory();
            List<AbsCustomer> customersList = descriptor.getAbsCustomers().getAbsCustomer();
            List<AbsLoan> loansList = descriptor.getAbsLoans().getAbsLoan();
            DataStorage<Account> customersDataStorage = new BankDataStorage<>();

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


            }






            System.out.println(test.toString());

        } catch (FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
    }

    private static AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_ABS_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);
    }


}
