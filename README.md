Create MySql  database ‘test and import table_backup.sql’

Deploy  WSO2KopiOutletPlatform.war to WSO2 AS -https://192.168.57.73:9445/carbon/admin/login.jsp

Deploy Kopi CApp to WSO2 DAS -https://192.168.57.73:9447/carbon/
  Note: Configure Datasource ‘DAS-DS’

Deploy ‘Rewards’ CApp to WSO2 ESB-  https://192.168.57.73:9446/carbon/
Note:Configure Event Sink  ‘DAS-Publisher’


Configure WSO2 API Manager to pass end user attributes to WSO2 ESB through JWT Token.

Create  and publish Kopi API
Note: Add  a Custom Sequence in WSO2 API Manager by logging to the admin console https://192.168.57.73:9443/carbon   by adding  datapublisher.xml  in the registry path  /_system/governance/apimgt/customsequences/in/datapublisher.xml


Invoke API with an order exceeding available quantity.

{
   "Order":{
       "drinkName":"Milk",
       "additions":"cream",
       "orderQuantity":2000
   }
}

