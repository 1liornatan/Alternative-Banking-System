package files.xmls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

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
            AbsDescriptor descriptor = deserializeFrom(inputStream);
            AbsCategories categories = descriptor.getAbsCategories();

            List<String> lst = categories.getAbsCategory();
            System.out.println(lst.toString());
            System.out.println(descriptor.getAbsCustomers().getAbsCustomer().get(0).getAbsBalance());

            DataStorage<CustomerAccount> test = new BankDataStorage<>();

            for(AbsCustomer absCustomer : descriptor.getAbsCustomers().getAbsCustomer()) {
                test.addData(new CustomerAccount(absCustomer.getName(), absCustomer.getAbsBalance()));
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
