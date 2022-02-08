package model;

import lombok.*;


@Getter
@Setter
@Builder
@ToString
public class ColumnsFormatReference
{
	private String nameColumnFormat;
	private String priceColumnFormat;
	private String quantityColumnFormat;
}
