package service;

import model.ColumnsFormatReference;
import model.ReceiptData;
import model.SkuData;

import java.util.List;


public interface ReceiptService
{
	ReceiptData readRawInput(String input) throws Exception;

	ColumnsFormatReference calculateFormattingSpace(List<SkuData> skus);

	void printReceipt(ReceiptData receiptData, ColumnsFormatReference columnsFormatReference);
}
