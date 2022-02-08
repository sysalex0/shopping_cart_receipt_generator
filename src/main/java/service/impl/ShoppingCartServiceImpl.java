package service.impl;

import model.ColumnsFormatReference;
import model.ReceiptData;
import service.CalculationService;
import service.ReceiptService;
import service.ShoppingCartService;


public class ShoppingCartServiceImpl implements ShoppingCartService
{
	private final ReceiptService receiptService;
	private final CalculationService calculationService;

	public ShoppingCartServiceImpl()
	{
		this.receiptService = new ReceiptServiceImpl();
		this.calculationService = new CalculationServiceImpl();
	}

	@Override
	public void generateReceipt(String input) throws Exception
	{
		ReceiptData receiptData = receiptService.readRawInput(input);
		receiptData = calculationService.calculateAmount(receiptData);
		ColumnsFormatReference columnsFormatReference = receiptService.calculateFormattingSpace(receiptData.getSkus());
		receiptService.printReceipt(receiptData, columnsFormatReference);
	}
}
