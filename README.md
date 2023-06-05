# electronic-store

## Prerequisite
- Java sdk 11 or above
- Gradle 7.2 or above

## Service Port
### 8432

## Run the application (in memory mode)
`./gradlew :store-gateway-service:bootRun --args='--spring.profiles.active=demo,withSampleData'
`
## Run the application (external h2 mode)
`docker-compose up -d && ./gradlew :store-gateway-service:bootRun --args='--spring.profiles.active=dev,withSampleData'`

## Run the test
`./gradlew test`

### test report
`./store-gateway-service/build/reports/tests/test/index.html`

## Api Specification

### Admin Api
the prefix started with `/admin`
[electronic_store_service-admin-openapi.yaml](electronic_store_service-admin-openapi.yaml)
### Customer Api
the prefix started with `/customer`
[electronic_store_service-customer-openapi.yaml](electronic_store_service-customer-openapi.yaml)

#### Receipt Sample response
`[{"Deal":"0.0000","Name":"Apple","Remark":"","Quantity":"1","Price":"0.9900"},{"Deal":"0.0000","Name":"Apple","Remark":"","Quantity":"1","Price":"0.9900"},{"Deal":"0.0000","Name":"Banana","Remark":"","Quantity":"1","Price":"0.9900"},{"Deal":"0.3000","Name":"Banana","Remark":"30% off from the second item","Quantity":"1","Price":"0.9900"},{"Price":"3.6600 USD","Name":"Total Price"}]`

```agsl
 | Name | Quantity | Price | Deal | Remark | 
|------|----------|-------|------|--------|
| Apple |  | 1 | 0.9900 | 0.0000 | 
| Apple |  | 1 | 0.9900 | 0.0000 | 
| Banana |  | 1 | 0.9900 | 0.0000 | 
| Banana | 30% off from the second item | 1 | 0.9900 | 0.3000 | 
| Total Price |  |  | 3.6600 USD |  | 

remark:
https://github.com/hungchai/electronic-store/blob/80e9e21b4fe1d95f24b97080a8cab57bdd088b03/store-gateway-service/src/test/java/com/tommazon/gatewayservice/CartControllerTests.java#L136-L136
```