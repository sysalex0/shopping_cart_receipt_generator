package service.impl;

import service.CategoryMockService;

import java.util.*;

// This class is to simulate category is from db
public class CategorMockServiceImpl implements CategoryMockService
{
	private Map<String, List<String>> categoryAndSkuNames = new HashMap<>()
	{{
		put("food", Arrays.asList("potato chips"));
		put("clothing", Arrays.asList("shirt"));
	}};

	@Override
	// simulate database
	public Optional<String> findCategoryBySkuName(String skuName)
	{
		for(Map.Entry<String,List<String>> catAndName: categoryAndSkuNames.entrySet())
		{
			if(catAndName.getValue().contains(skuName))
			{
				return Optional.of(catAndName.getKey());
			}
		}
		return Optional.empty();
	}
}
