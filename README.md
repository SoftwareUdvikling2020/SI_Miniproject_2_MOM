# SI Miniproject 2 MOM

###### Magnus Klitmose, Rasmus Lynge, Mathias Kristensen



We have chosen assignment 3. 
We have used Doras code examples for rabbitMQ to complete the task. 

For the program to work, rabbitMQ needs to run in localhost.

In the consumer application, it would send out real e-mails via javamail, but for it to work you need to uncomment line 57 in EmailSender.java, and provide a real username/password combination in lines 8 & 9. 

The producer application fetches genders based on names in recipients.json file. It publishes a message to RabbitMQ for each name, every 30 seconds. 

