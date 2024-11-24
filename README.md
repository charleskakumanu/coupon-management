# coupon-management
## Steps to run:
  Just start the application in any IDE, do not need any DB, using Lists for storing coupons. 
  **mvn clean install** would install dependencies and then we can start
  

## Implementation for BXGY coupon logic:
1. Saw the OR logic after the whole implementation.
2. Currently iterating through each buy product inside the cuopon and getting the discount value from it's get products details. It's been some days since I started this project, so I thought I would submit without any delay.

## Implementation for Product Wise Coupon:
1. Iterating through each proeduct and calculating the total cost of each product (quantity * price).
2. Applying the discount on the whole product cost as per the coupon

## Implementation logic of cart wise coupon:
1. Iterate through available coupons and apply the largest discount price accordingly.
