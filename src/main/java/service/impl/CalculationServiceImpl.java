package service.impl;

import model.ReceiptData;
import model.SkuData;
import service.CalculationService;
import service.CategoryMockService;
import utils.CalculationUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;


public class CalculationServiceImpl implements CalculationService
{
	private final CategoryMockService categoryMockService;

	public CalculationServiceImpl()
	{
		this.categoryMockService = new CategorMockServiceImpl();
	}

	@Override
	public ReceiptData calculateAmount(ReceiptData receiptData)
	{
		List<SkuData> skus = receiptData.getSkus();
		BigDecimal subTotal = skus.stream().map(sku -> sku.getPrice().multiply(BigDecimal.valueOf(sku.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// TODO tax need clarify about the category
		BigDecimal tax = this.calculateTax(receiptData);

		// total
		BigDecimal total = subTotal.add(tax);

		receiptData.setSubTotal(subTotal);
		receiptData.setTax(tax);
		receiptData.setTotal(total);

		return receiptData;
	}

	@Override
	public BigDecimal calculateTax(ReceiptData receiptData)
	{
		// TODO tax calculation
		BigDecimal taxSum = BigDecimal.ZERO;

		for (SkuData sku : receiptData.getSkus())
		{
			boolean isFreeTax = this.isTaxFree(sku);
			if (isFreeTax)
			{
				continue;
			}
			else
			{
				/*
					Sales tax = roundup(price * quantity * sales tax rate);
					sales tax amount should be rounded up to the nearest 0.05 (e.g. 1.13->1.15, 1.16->1.20, 1.151->1.20)
				*/
				BigDecimal taxRate = receiptData.getLocation().getSalesTaxRate();
				BigDecimal tax = sku.getPrice().multiply(BigDecimal.valueOf(sku.getQuantity())).multiply(taxRate);
				BigDecimal taxRounded = CalculationUtils.round(tax, BigDecimal.valueOf(0.05), RoundingMode.UP);

				taxSum = taxSum.add(taxRounded);
			}
		}

		return taxSum;
	}

	@Override
	public boolean isTaxFree(SkuData skuData)
	{
		// Suppose category should be registered in sku data since the data import process (before public to sell), otherwise it is not possible in the shopping cart.
		// so I assume that this information is store in database.
		// Given that the requirement is: inputs don't contain any info of category, I further assume the category table in database
		// holds the uniquely identify field of the sku -> eg. name
		Optional<String> categoryBySkuNameOptional = categoryMockService.findCategoryBySkuName(skuData.getName());

		return categoryBySkuNameOptional.isPresent();
	}
}
