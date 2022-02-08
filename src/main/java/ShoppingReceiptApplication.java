import service.ShoppingCartService;
import service.impl.ShoppingCartServiceImpl;

import java.util.Scanner;


public class ShoppingReceiptApplication
{
	private static ShoppingCartService shoppingCartService;

	public static void main(String[] args) throws Exception
	{

		System.out.print("Input: ");
		Scanner scanner = new Scanner(System.in);

		String input = scanner.nextLine();

		shoppingCartService = new ShoppingCartServiceImpl();
		shoppingCartService.generateReceipt(input);
	}
}