package service;

import java.util.Optional;


public interface CategoryMockService
{
	Optional<String> findCategoryBySkuName(String skuName);
}
