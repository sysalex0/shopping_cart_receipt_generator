package model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class SkuData
{
	private String name;
	private BigDecimal price;
	private int quantity;
}
