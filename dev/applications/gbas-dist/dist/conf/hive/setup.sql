DROP TABLE IF EXISTS source;  
DROP TABLE IF EXISTS model;  
DROP TABLE IF EXISTS process_instance;  
DROP TABLE IF EXISTS activity_instance;  
DROP TABLE IF EXISTS event_correlation;
DROP TABLE IF EXISTS event_payload;
DROP TABLE IF EXISTS event_data;
DROP TABLE IF EXISTS event;

CREATE EXTERNAL TABLE IF NOT EXISTS source(source_id string, name string, description string, inet_address string, port int) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
"hbase.columns.mapping" = ":key, source:name, source:description, source:inet_address, source:port#b"
)
TBLPROPERTIES ("hbase.table.name" = "source");  

CREATE EXTERNAL TABLE IF NOT EXISTS model(model_id bigint, model_src_id string, name string, source string, type string) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
"hbase.columns.mapping" = ":key#b, model:model_src_id, model:name, model:source, model:type"
)
TBLPROPERTIES ("hbase.table.name" = "model");  

CREATE EXTERNAL TABLE IF NOT EXISTS process_instance(instance_id bigint, instance_src_id string, model bigint, name string, description string, correlator_id bigint) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
"hbase.columns.mapping" = ":key#b, process_instance:instance_src_id, process_instance:model#b, process_instance:name, process_instance:description, process_instance:correlator_id#b"
)
TBLPROPERTIES ("hbase.table.name" = "process_instance");  

CREATE EXTERNAL TABLE IF NOT EXISTS activity_instance(instance_id bigint, instance_src_id string, model bigint, name string, description string, parent bigint) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
"hbase.columns.mapping" = ":key#b, activity_instance:instance_src_id, activity_instance:model#b, activity_instance:name, activity_instance:description, activity_instance:parent#b"
)
TBLPROPERTIES ("hbase.table.name" = "activity_instance");  

CREATE EXTERNAL TABLE IF NOT EXISTS event_correlation(event_id bigint, key string, value string) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
"hbase.columns.mapping" = ":key#b, event_correlation:key, event_correlation:value"
)
TBLPROPERTIES ("hbase.table.name" = "event_correlation");

CREATE EXTERNAL TABLE IF NOT EXISTS event_payload(event_id bigint, key string, value string) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
"hbase.columns.mapping" = ":key#b, event_payload:key, event_payload:value"
)
TBLPROPERTIES ("hbase.table.name" = "event_payload");

CREATE EXTERNAL TABLE IF NOT EXISTS event_data(event_id bigint, key string, value string) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
"hbase.columns.mapping" = ":key#b, event_data:key, event_data:value"
)
TBLPROPERTIES ("hbase.table.name" = "event_data");

CREATE EXTERNAL TABLE IF NOT EXISTS event(event_id bigint, process_instance bigint, process_model bigint, activity_instance bigint, activity_model bigint, tstamp bigint, current_state string, previous_state string) 
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
"hbase.columns.mapping" = ":key#b, event:process_instance#b, event:process_model#b, event:activity_instance#b, event:activity_model#b, event:tstamp#b, event:current_state, event:previous_state"
)
TBLPROPERTIES ("hbase.table.name" = "event");

