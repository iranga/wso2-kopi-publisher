1.Create MySql  database ‘test and import table_backup.sql’

2.Deploy  WSO2KopiOutletPlatform.war to Web Server/ WSO2 Application Server/WSO2 APP Cloudp

3.Deploy "Streaming-CApp"  CApp to WSO2 CEP 
  Note: Configure Datasource ‘CEP-DS to point to tables created in step 1’
        Add Machine Learning extentions

4.Copy  the custom implementation (org.wso2.api.publish-1.0-SNAPSHOT.jar ) library to  $API_MGR_HOME /repository/components/lib


5.Configure WSO2 API Manager to pass end user attributes as JWT Token.

6.Create  and publish Kopi API
Note: Change custom mediation flow using datapublisher.xml


7.Invoke API with an order exceeding available quantity


{
   "Order":{
       "drinkName":"Doppio",
       "additions":"cream",
       "orderQuantity":2000
   }
}

