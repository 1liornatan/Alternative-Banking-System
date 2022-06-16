package files.xmls;



import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import files.schema.generated.v2.AbsCategories;
import files.schema.generated.v2.AbsDescriptor;
import files.schema.generated.v2.AbsLoan;
import files.schema.generated.v2.AbsLoans;
import files.xmls.exceptions.XmlPaymentsException;
import manager.loans.LoanData;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlLoanReader {
    private static final String JAXB_XML_ABS_PACKAGE_NAME_V2 = "files.schema.generated.v2";

    private static AbsDescriptor deserializeLoanFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_ABS_PACKAGE_NAME_V2);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);
    }

    public static List<LoanData> getLoansFromXML(String filePath) throws XmlPaymentsException, NonPositiveAmountException {
        List<LoanData> loanList = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(filePath);

            AbsDescriptor descriptor = deserializeLoanFrom(inputStream);
            AbsCategories categories = descriptor.getAbsCategories();
            AbsLoans loans = descriptor.getAbsLoans();
            List<AbsLoan> absLoan = loans.getAbsLoan();

            List<String> categoryList = categories.getAbsCategory();

            for (AbsLoan currLoan : absLoan) {
                String loanName = currLoan.getId();
                String categoryName = currLoan.getAbsCategory();
                int amount = currLoan.getAbsCapital();
                int totalTime = currLoan.getAbsTotalYazTime();
                int payPerTime = currLoan.getAbsPaysEveryYaz();
                int interestPercent = currLoan.getAbsIntristPerPayment();

                if (totalTime <= 0 || payPerTime <= 0 || totalTime % payPerTime != 0)
                    throw new XmlPaymentsException();

                if(amount <= 0)
                    throw new NonPositiveAmountException();

                LoanData currLoanData = new LoanData();

                currLoanData.setName(loanName);
                currLoanData.setCategory(categoryName);
                currLoanData.setBaseAmount(amount);
                currLoanData.setCyclesPerPayment(payPerTime);
                currLoanData.setFinishedYaz(totalTime);
                currLoanData.setInterest(interestPercent);

                loanList.add(currLoanData);
            }
        } catch (FileNotFoundException | JAXBException e) {
            System.out.println(e.getMessage());
        }
        return loanList;
    }
}
