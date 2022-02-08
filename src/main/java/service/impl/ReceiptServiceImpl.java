package service.impl;

import location.Location;
import model.ColumnsFormatReference;
import model.ReceiptData;
import model.SkuData;
import service.ReceiptService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ReceiptServiceImpl implements ReceiptService
{
	private final String SKU_DATA_REGEX = "(\\d+) (.*) at (\\d*\\.?\\d+)";
	private final int COLUMN_SPACE = 6;

	@Override
	public ReceiptData readRawInput(String input) throws Exception
	{
		// get location
		List<String> inputSplitByComma = Arrays.asList(input.split(","));
		String rawLocation = inputSplitByComma.get(0).replace("Location: ", "");
		Location location = Location.find(rawLocation);
		if (location.equals(Location.UNKNOWN))
		{
			throw new Exception("Invalid Location Code");
		}

		// skip the first element
		inputSplitByComma = inputSplitByComma.subList(1, inputSplitByComma.size()).stream().map(String::trim)
				.collect(Collectors.toList());

		// get sku data
		try
		{
			Pattern skuDataRegexPattern = Pattern.compile(SKU_DATA_REGEX);
			List<SkuData> skus = inputSplitByComma.stream().map(skuRawData -> {
				Matcher matcher = skuDataRegexPattern.matcher(skuRawData.trim());
				matcher.matches();

				// extract from raw data
				int quantity = Integer.parseInt(matcher.group(1));
				String skuName = matcher.group(2);
				BigDecimal price = new BigDecimal(matcher.group(3));

				return SkuData.builder()
						.name(skuName)
						.price(price)
						.quantity(quantity)
						.build();
			}).collect(Collectors.toList());

			return ReceiptData.builder()
					.location(location)
					.skus(skus)
					.build();
		}
		catch (NumberFormatException e)
		{
			throw new Exception("Invalid product data");
		}
	}

	@Override
	public ColumnsFormatReference calculateFormattingSpace(List<SkuData> skus)
	{
		int nameMaxLength = skus.stream().map(SkuData::getName).mapToInt(String::length).max().orElse(0);
		int priceMaxLength = skus.stream().map(SkuData::getPrice).map(BigDecimal::toString).mapToInt(String::length).max()
				.orElse(0);
		int quantityMaxLength = skus.stream().map(SkuData::getQuantity).map(String::valueOf).mapToInt(String::length).max()
				.orElse(0);

		int nameColumnLength = nameMaxLength + COLUMN_SPACE;
		int priceColumnLength = priceMaxLength + COLUMN_SPACE;
		int quantityColumnLength = quantityMaxLength + COLUMN_SPACE;


		String nameFormat = "%-" + nameColumnLength + "." + nameColumnLength + "s"; // fixed size n characters, left aligned
		String priceFormat = "%" + priceColumnLength + "." + priceColumnLength + "s"; // fixed size n characters, right aligned
		String quantityFormat =
				"%" + quantityColumnLength + "." + quantityColumnLength + "s";   // fixed size n characters, right aligned

		ColumnsFormatReference columnSpaceWidth = ColumnsFormatReference.builder()
				.nameColumnFormat(nameFormat)
				.priceColumnFormat(priceFormat)
				.quantityColumnFormat(quantityFormat)
				.build();

		return columnSpaceWidth;
	}

	@Override
	public void printReceipt(ReceiptData receiptData, ColumnsFormatReference columnsFormatReference)
	{
		String formatInfo = columnsFormatReference.getNameColumnFormat() + " " + columnsFormatReference.getPriceColumnFormat() + " "
				+ columnsFormatReference.getQuantityColumnFormat();
		String formattedHeaders = String.format(formatInfo, "item", "price", "qty");

		List<String> formattedSkus = receiptData.getSkus().stream()
				.map(skuData -> String.format(formatInfo, skuData.getName(), "$" + skuData.getPrice(), skuData.getQuantity()))
				.collect(
						Collectors.toList());
		String formattedSubtotal = String.format(formatInfo, "subtotal:", "", "$" + receiptData.getSubTotal().toString());
		String formattedTax = String.format(formatInfo, "tax:", "", "$" + receiptData.getTax());
		String formattedTotal = String.format(formatInfo, "total:", "", "$" + receiptData.getTotal());

		System.out.println(formattedHeaders);
		System.out.println();
		formattedSkus.forEach(System.out::println);
		System.out.println(formattedSubtotal);
		System.out.println(formattedTax);
		System.out.println(formattedTotal);
	}
}
