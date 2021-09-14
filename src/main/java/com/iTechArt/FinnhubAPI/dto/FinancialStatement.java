package com.iTechArt.FinnhubAPI.dto;

import com.iTechArt.FinnhubAPI.model.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FinancialStatement extends BaseEntity {
    private String accessNumber;
    private String symbol;
    private String cik;
    private Date year;
    private String quarter;
    private String form;
    private Date startDate;
    private Date endDate;
    private Date filedDate;
    private Date acceptedDate;

    List<Report> bs;
    List<Report> cf;
    List<Report> ic;
}


/**
 * {"accessNumber":"0001564590-21-004599",
 * "symbol":"TSLA",
 * "cik":"1318605",
 * "year":2020,
 * "quarter":0,
 * "form":"10-K",
 * "startDate":"2020-01-01 00:00:00",
 * "endDate":"2020-12-31 00:00:00",
 * "filedDate":"2021-02-08 00:00:00",
 * "acceptedDate":"2021-02-08 07:27:23",
 * "report":{
 *
 * "bs":[
 * {"unit":"usd",
 * "label":"Cash and cash equivalents",
 * "value":19384000000,
 * "concept":"CashAndCashEquivalentsAtCarryingValue"},
 * {"unit":"usd",
 * "label":"Accounts receivable, net",
 * "value":1886000000,
 * "concept":"AccountsReceivableNetCurrent"},
 * {"unit":"usd",
 * "label":"Inventory",
 * "value":4101000000,
 * "concept":"InventoryNet"},
 * {"unit":"usd",
 * "label":"Prepaid expenses and other current assets",
 * "value":1346000000,
 * "concept":"PrepaidExpenseAndOtherAssetsCurrent"},
 * {"unit":"usd","label":"Current assets","value":26717000000,"concept":"AssetsCurrent"},
 * {"unit":"usd","label":"Property, plant and equipment, net","value":12747000000,"concept":"PropertyPlantAndEquipmentNet"},
 * {"unit":"usd","label":"Operating lease right-of-use assets","value":1558000000,"concept":"OperatingLeaseRightOfUseAsset"},
 * {"unit":"usd","label":"Intangible assets, net","value":313000000,"concept":"IntangibleAssetsNetExcludingGoodwill"},
 * {"unit":"usd","label":"Goodwill","value":207000000,"concept":"Goodwill"},
 * {"unit":"usd","label":"Other non-current assets","value":1536000000,"concept":"OtherAssetsNoncurrent"},
 * {"unit":"usd","label":"Assets","value":52148000000,"concept":"Assets"},
 * {"unit":"usd","label":"Accounts payable","value":6051000000,"concept":"AccountsPayableCurrent"},
 * {"unit":"usd","label":"Accrued and other current liabilities.","value":3855000000,"concept":"tsla:AccruedAndOtherCurrentLiabilities"},
 * {"unit":"usd","label":"Deferred revenue","value":1458000000,"concept":"ContractWithCustomerLiabilityCurrent"},
 * {"unit":"usd","label":"Customer deposits liabilities current.","value":752000000,"concept":"tsla:CustomerDepositsLiabilitiesCurrent"},
 * {"unit":"usd","label":"Long term debt and finance leases current.","value":2132000000,"concept":"tsla:LongTermDebtAndFinanceLeasesCurrent"},
 * {"unit":"usd","label":"Current liabilities","value":14248000000,"concept":"LiabilitiesCurrent"},
 * {"unit":"usd","label":"Long term debt and finance leases, noncurrent","value":9556000000,"concept":"tsla:LongTermDebtAndFinanceLeasesNoncurrent"},
 * {"unit":"usd","label":"Deferred revenue, net of current portion","value":1284000000,"concept":"ContractWithCustomerLiabilityNoncurrent"},
 * {"unit":"usd","label":"Other long-term liabilities","value":3330000000,"concept":"OtherLiabilitiesNoncurrent"},
 * {"unit":"usd","label":"Liabilities","value":28418000000,"concept":"Liabilities"},
 * {"unit":"usd","label":"Commitments and contingencies (Note 16)","value":"N/A","concept":"CommitmentsAndContingencies"},
 * {"unit":"usd","label":"Redeemable noncontrolling interests in subsidiaries","value":604000000,"concept":"RedeemableNoncontrollingInterestEquityCarryingAmount"},
 * {"unit":"usd","label":"Convertible senior notes (Note 12)","value":51000000,"concept":"TemporaryEquityCarryingAmountAttributableToParent"},
 * {"unit":"usd","label":"Preferred stock; $0.001 par value; 100 shares authorized; no shares issued and outstanding","value":"N/A","concept":"PreferredStockValue"},
 * {"unit":"usd","label":"Common stock; $0.001 par value; 2,000 shares authorized; 960 and 905 shares issued and outstanding as of December 31, 2020 and December 31, 2019, respectively","value":1000000,"concept":"CommonStockValue"},
 * {"unit":"usd","label":"Additional paid-in capital","value":27260000000,"concept":"AdditionalPaidInCapitalCommonStock"},
 * {"unit":"usd","label":"Accumulated other comprehensive income (loss)","value":363000000,"concept":"AccumulatedOtherComprehensiveIncomeLossNetOfTax"},
 * {"unit":"usd","label":"Accumulated deficit","value":-5399000000,"concept":"RetainedEarningsAccumulatedDeficit"},
 * {"unit":"usd","label":"Equity","value":22225000000,"concept":"StockholdersEquity"},
 * {"unit":"usd","label":"Noncontrolling interests in subsidiaries","value":850000000,"concept":"MinorityInterest"},
 * {"unit":"usd","label":"Total liabilities and equity","value":52148000000,"concept":"LiabilitiesAndStockholdersEquity"}],
 * <p>
 * "cf":[
 * {"unit":"usd",
 * "label":"Net income (loss)",
 * "value":862000000,
 * "concept":"ProfitLoss"},
 * {"unit":"usd","label":"Depreciation amortization and impairment.","value":2322000000,"concept":"tsla:DepreciationAmortizationAndImpairment"},
 * {"unit":"usd","label":"Stock-based compensation","value":1734000000,"concept":"ShareBasedCompensation"},
 * {"unit":"usd","label":"Amortization of debt discounts and issuance costs","value":180000000,"concept":"AmortizationOfFinancingCostsAndDiscounts"},
 * {"unit":"usd","label":"Inventory and purchase commitments write-downs","value":202000000,"concept":"InventoryWriteDown"},
 * {"unit":"usd","label":"Loss on disposals of fixed assets","value":-117000000,"concept":"GainLossOnSaleOfPropertyPlantEquipment"},
 * {"unit":"usd","label":"Foreign currency transaction net loss (gain)","value":-114000000,"concept":"ForeignCurrencyTransactionGainLossRealized"},
 * {"unit":"usd","label":"Noncash interest income (expense) and other operating activities.","value":-228000000,"concept":"tsla:NoncashInterestIncomeExpenseAndOtherOperatingActivities"},
 * {"unit":"usd","label":"Accounts receivable","value":652000000,"concept":"IncreaseDecreaseInAccountsReceivable"},
 * {"unit":"usd","label":"Inventory","value":422000000,"concept":"IncreaseDecreaseInInventories"},
 * {"unit":"usd","label":"Increase decrease in operating lease vehicles.","value":1072000000,"concept":"tsla:IncreaseDecreaseInOperatingLeaseVehicles"},
 * {"unit":"usd","label":"Prepaid expenses and other current assets","value":251000000,"concept":"IncreaseDecreaseInPrepaidDeferredExpenseAndOtherAssets"},
 * {"unit":"usd","label":"Other non-current assets","value":344000000,"concept":"IncreaseDecreaseInOtherNoncurrentAssets"},
 * {"unit":"usd","label":"Accounts payable and accrued liabilities","value":2102000000,"concept":"IncreaseDecreaseInAccountsPayableAndAccruedLiabilities"},
 * {"unit":"usd","label":"Deferred revenue","value":321000000,"concept":"IncreaseDecreaseInContractWithCustomerLiability"},
 * {"unit":"usd","label":"Increase decrease in contract with customer liability customer deposits.","value":7000000,"concept":"tsla:IncreaseDecreaseInContractWithCustomerLiabilityCustomerDeposits"},
 * {"unit":"usd","label":"Other long-term liabilities","value":495000000,"concept":"IncreaseDecreaseInOtherNoncurrentLiabilities"},
 * {"unit":"usd","label":"Cash Flows from Operating Activities","value":5943000000,"concept":"NetCashProvidedByUsedInOperatingActivities"},
 * {"unit":"usd","label":"Purchases of property and equipment excluding finance leases, net of sales","value":3157000000,"concept":"PaymentsToAcquirePropertyPlantAndEquipment"},
 * {"unit":"usd","label":"Payments for solar energy systems, net of sales.","value":75000000,"concept":"tsla:PaymentsForSolarEnergySystemsNetOfSales"},
 * {"unit":"usd","label":"Receipt of government grants.","value":123000000,"concept":"tsla:GovernmentGrantReceipt"},
 * {"unit":"usd","label":"Purchase of intangible assets","value":10000000,"concept":"PaymentsToAcquireIntangibleAssets"},
 * {"unit":"usd","label":"Business combinations, net of cash acquired","value":13000000,"concept":"PaymentsToAcquireBusinessesNetOfCashAcquired"},
 * {"unit":"usd","label":"Cash Flows from Investing Activities","value":-3132000000,"concept":"NetCashProvidedByUsedInInvestingActivities"},
 * {"unit":"usd","label":"Proceeds from issuances of common stock in public offerings, net of issuance costs","value":12269000000,"concept":"ProceedsFromIssuanceOfCommonStock"},
 * {"unit":"usd","label":"Proceeds from convertible and other debt.","value":9713000000,"concept":"tsla:ProceedsFromConvertibleAndOtherDebt"},
 * {"unit":"usd","label":"Repayment of convertible and other debt.","value":11623000000,"concept":"tsla:RepaymentsOfConvertibleAndOtherDebt"},
 * {"unit":"usd","label":"Collateralized lease repayments","value":-240000000,"concept":"ProceedsFromRepaymentsOfSecuredDebt"},
 * {"unit":"usd","label":"Proceeds from exercises of stock options and other stock issuances","value":417000000,"concept":"ProceedsFromIssuanceOfSharesUnderIncentiveAndShareBasedCompensationPlansIncludingStockOptions"},
 * {"unit":"usd","label":"Principal payments on finance leases","value":338000000,"concept":"FinanceLeasePrincipalPayments"},
 * {"unit":"usd","label":"Debt issuance costs","value":6000000,"concept":"PaymentsOfDebtIssuanceCosts"},
 * {"unit":"usd","label":"Proceeds from investments by noncontrolling interests in subsidiaries","value":24000000,"concept":"ProceedsFromMinorityShareholders"},
 * {"unit":"usd","label":"Distributions paid to noncontrolling interests in subsidiaries","value":208000000,"concept":"PaymentsToMinorityShareholders"},
 * {"unit":"usd","label":"Payments for buy-outs of noncontrolling interests in subsidiaries.","value":35000000,"concept":"tsla:PaymentsForBuyOutsOfNoncontrollingInterestsInSubsidiaries"},
 * {"unit":"usd","label":"Cash Flows from Financing Activities","value":9973000000,"concept":"NetCashProvidedByUsedInFinancingActivities"},
 * {"unit":"usd","label":"Effect of exchange rate changes on cash and cash equivalents and restricted cash","value":334000000,"concept":"EffectOfExchangeRateOnCashCashEquivalentsRestrictedCashAndRestrictedCashEquivalentsIncludingDisposalGroupAndDiscontinuedOperations"},
 * {"unit":"usd","label":"Net increase in cash and cash equivalents and restricted cash","value":13118000000,"concept":"CashCashEquivalentsRestrictedCashAndRestrictedCashEquivalentsPeriodIncreaseDecreaseIncludingExchangeRateEffect"},
 * {"unit":"usd","label":"Acquisitions of property and equipment included in liabilities","value":1088000000,"concept":"NoncashOrPartNoncashAcquisitionValueOfAssetsAcquired1"},
 * {"unit":"usd","label":"Cash paid during the period for interest, net of amounts capitalized","value":444000000,"concept":"InterestPaidNet"},
 * {"unit":"usd","label":"Cash paid during the period for taxes, net of refunds","value":115000000,"concept":"IncomeTaxesPaid"}],
 * <p>
 * "ic":[
 * {"unit":"usd",
 * "label":"Automotive leasing",
 * "value":1052000000,
 * "concept":"OperatingLeasesIncomeStatementLeaseRevenue"},
 * {"unit":"usd","label":"Sales revenue automotive.","value":27236000000,"concept":"tsla:SalesRevenueAutomotive"},
 * {"unit":"usd","label":"Sales revenue services and other net.","value":2306000000,"concept":"tsla:SalesRevenueServicesAndOtherNet"},
 * {"unit":"usd","label":"Revenues","value":31536000000,"concept":"Revenues"},
 * {"unit":"usd","label":"Cost of automotive leasing.","value":563000000,"concept":"tsla:CostOfAutomotiveLeasing"},
 * {"unit":"usd","label":"Cost of revenues automotive.","value":20259000000,"concept":"tsla:CostOfRevenuesAutomotive"},
 * {"unit":"usd","label":"Cost of services and other.","value":2671000000,"concept":"tsla:CostOfServicesAndOther"},
 * {"unit":"usd","label":"Cost of revenues","value":24906000000,"concept":"CostOfRevenue"},
 * {"unit":"usd","label":"Gross profit","value":6630000000,"concept":"GrossProfit"},{"unit":"usd","label":"Research and development","value":1491000000,"concept":"ResearchAndDevelopmentExpense"},
 * {"unit":"usd","label":"Selling, general and administrative","value":3145000000,"concept":"SellingGeneralAndAdministrativeExpense"},
 * {"unit":"usd","label":"Operating expenses","value":4636000000,"concept":"OperatingExpenses"},
 * {"unit":"usd","label":"Income (loss) from operations","value":1994000000,"concept":"OperatingIncomeLoss"},
 * {"unit":"usd","label":"Interest income","value":30000000,"concept":"InvestmentIncomeInterest"},
 * {"unit":"usd","label":"Interest expense","value":748000000,"concept":"InterestExpense"},
 * {"unit":"usd","label":"Other (expense) income, net","value":-122000000,"concept":"OtherNonoperatingIncomeExpense"},
 * {"unit":"usd","label":"Income (loss) before income taxes","value":1154000000,"concept":"IncomeLossFromContinuingOperationsBeforeIncomeTaxesExtraordinaryItemsNoncontrollingInterest"},
 * {"unit":"usd","label":"Provision for income taxes","value":292000000,"concept":"IncomeTaxExpenseBenefit"},
 * {"unit":"usd","label":"Net income (loss)","value":862000000,"concept":"ProfitLoss"},
 * {"unit":"usd","label":"Net income (loss) attributable to noncontrolling interests and redeemable noncontrolling interests in subsidiaries","value":141000000,"concept":"NetIncomeLossAttributableToNoncontrollingInterest"},
 * {"unit":"usd","label":"Net income (loss) attributable to noncontrolling interests and redeemable noncontrolling interests in subsidiaries","value":721000000,"concept":"NetIncomeLoss"},
 * {"unit":"usd","label":"Buy-out of noncontrolling interest.","value":31000000,"concept":"tsla:BuyOutOfNoncontrollingInterest"},
 * {"unit":"usd","label":"Net income (loss) used in computing net income (loss) per share of common stock","value":690000000,"concept":"NetIncomeLossAvailableToCommonStockholdersBasic"},
 * {"unit":"usd/shares","label":"Basic","value":0.74,"concept":"EarningsPerShareBasic"},
 * {"unit":"usd/shares","label":"Diluted","value":0.64,"concept":"EarningsPerShareDiluted"},
 * {"unit":"shares","label":"Basic","value":933000000,"concept":"WeightedAverageNumberOfSharesOutstandingBasic"},
 * {"unit":"shares","label":"Diluted","value":1083000000,"concept":"WeightedAverageNumberOfDilutedSharesOutstanding"},
 * {"unit":"usd","label":"Foreign currency translation adjustment","value":399000000,"concept":"OtherComprehensiveIncomeLossForeignCurrencyTransactionAndTranslationAdjustmentNetOfTax"},
 * {"unit":"usd","label":"Comprehensive income (loss)","value":1261000000,"concept":"ComprehensiveIncomeNetOfTaxIncludingPortionAttributableToNoncontrollingInterest"},
 * {"unit":"usd","label":"Less: Comprehensive income (loss) attributable to noncontrolling interests and redeemable noncontrolling interests in subsidiaries","value":141000000,"concept":"ComprehensiveIncomeNetOfTaxAttributableToNoncontrollingInterest"},
 * {"unit":"usd","label":"Comprehensive income (loss)","value":1120000000,"concept":"ComprehensiveIncomeNetOfTax"}]}}
 */