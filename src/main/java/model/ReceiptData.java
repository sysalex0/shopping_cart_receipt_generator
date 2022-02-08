package model;

import location.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@Builder
public class ReceiptData
{
	private Location location;
	private List<SkuData> skus;
	private BigDecimal subTotal;
	private BigDecimal tax;
	private BigDecimal total;
}
