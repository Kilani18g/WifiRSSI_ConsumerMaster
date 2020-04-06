# WifiRSSI_ConsumerMaster
This application is intended to be used by the consumer to locate themselves within an indoor environment. 
After creating a dataset with a DataMiner Application, and establishing a Deep Learning model, the model is to be deployed on the application. 
The model is to be converted to tensorflow lite (.tflite or .lite). 
Keep a text file with labels (in this case it is X Y, but model can be trained for X Y and floor #) as well as a file with the SSID,BSSID,WAP# to organize the input of the model. 
The application is still crashing and we are trying to resolve the issue.
