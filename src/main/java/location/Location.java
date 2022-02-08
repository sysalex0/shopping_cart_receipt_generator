package location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public enum Location
{
	CA(BigDecimal.valueOf(0.0975)),
	NY(BigDecimal.valueOf(0.08875)),
	UNKNOWN;

	private BigDecimal salesTaxRate;

	public static Location find(String location)
	{
		try
		{
			return Location.valueOf(location);
		}
		catch (Exception e)
		{
			return UNKNOWN;
		}
	}
}
