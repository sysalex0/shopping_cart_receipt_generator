#Shopping Receipt

##Assumption:
Suppose category should be registered in sku data since the data import process (before public to sell), otherwise it is not possible appearing in the shopping cart. 
So I assume that this information is store in database already.
Given that the requirement is: inputs don't contain any information of categories, 
I further assume the category table in database holds the uniquely identify field of the product, in this case will be product name

## Install and execute guide
1. ``mvn clean install``
2. ``java -jar .\target\shopping_receipt-1.0-SNAPSHOT.jar``