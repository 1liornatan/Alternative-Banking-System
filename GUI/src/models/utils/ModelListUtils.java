package models.utils;

import manager.investments.InvestmentsSellData;
import manager.loans.LoanData;
import models.InvestmentModel;
import models.LoanModel;

import java.util.ArrayList;
import java.util.List;

public class ModelListUtils {


    public static List<InvestmentModel> makeInvestmentModelList(InvestmentsSellData investmentsForSell) {
        List<InvestmentModel> investmentModels = new ArrayList<>();

        List<String> investorsIds = investmentsForSell.getInvestorsIds();
        List<String> investmentIds = investmentsForSell.getInvIds();
        List<String> loansIds = investmentsForSell.getLoansIds();
        List<Integer> amounts = investmentsForSell.getAmounts();
        List<Integer> yazPlaced = investmentsForSell.getYazPlaced();
        List<Boolean> forSale = investmentsForSell.getForSale();

        int arrSize = loansIds.size();

        for(int i = 0; i < arrSize; i++) {
            investmentModels.add(new InvestmentModel.InvestmentModelBuilder()
                    .loan(loansIds.get(i))
                    .owner(investorsIds.get(i))
                    .amount(amounts.get(i))
                    .yaz(yazPlaced.get(i))
                    .id(investmentIds.get(i))
                    .forSale(forSale.get(i))
                    .build());
        }
        return investmentModels;
    }

    public static List<LoanModel> makeLoanModelList(List<LoanData> loanDataList) {
        List<LoanModel> tempLoanModelList = new ArrayList<>();
        for(LoanData loanData : loanDataList) {
            LoanModel loanModel = new LoanModel.LoanModelBuilder()
                    .id(loanData.getName())
                    .amount(loanData.getBaseAmount())
                    .endYaz(loanData.getFinishedYaz())
                    .startYaz(loanData.getStartedYaz())
                    .nextPaymentInYaz(loanData.getNextPaymentInYaz())
                    .finalAmount(loanData.getFinalAmount())
                    .status(loanData.getStatus())
                    .investorsAmount(loanData.getInvestorsAmount())
                    .amountToActive(loanData.getAmountToActive())
                    .deriskAmount(loanData.getDeriskAmount())
                    .missingCycles(loanData.getMissingCycles())
                    .payment(loanData.getNextPaymentAmount())
                    .left(loanData.getCloseAmount())
                    .build();

            tempLoanModelList.add(loanModel);
        }
        return tempLoanModelList;
    }
}
