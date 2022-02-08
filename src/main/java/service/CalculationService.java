package service;

import model.ReceiptData;
import model.SkuData;

import java.math.BigDecimal;


public interface CalculationService
{
	ReceiptData calculateAmount(ReceiptData receiptData);

	BigDecimal calculateTax(ReceiptData receiptData);

	boolean isTaxFree(SkuData skuData);
}
