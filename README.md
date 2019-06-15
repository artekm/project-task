# Project task
This is task description for project part of your IT-Academy course.
## Getting Started
While completing this task you will learn:
1. How to work with text files
2. How to work with binary files
3. How to modify MS Office documents
4. How to obtain information from Internet
5. Get familiar with REST API
6. Learn what is Query String
7. Get introduction into XML and JSON formats
8. Get basics of object's serializaion and deserialization
9. How to work with property files
### Task
You need to develop application for generating lesson schedule and saving it to Excel workbook.
Application should get the next input parameters:
* -d lesson_days
* -b lesson_begin_time
* -e lesson_end_time
* -n required_number_of_hours
* -s start_date
* -f file_name
* -h (show application help)

Attached file **example1.xlsx** supposed to be generated after the next command:
java -jar application_name.jar -d mon_thu -b 17:00 -e 18:30 -n 21 -s 30.05.2019 -f example1

Attached file **example2.xlsx** supposed to be generated after the next command:
java -jar application_name.jar -d sat -b 9:00 -e 12:00 -n 39 -s 06.04.2019 -f example2

-h key should show application usage message (input parameters with possible values)

File extension should be provided from properties file.
If file name does not provided - should be used default file name from properties file.

If planned hours does not fits in hours for lessons, the last lesson should be shorter, and:
* warning message generated to console output;
* **warning.txt** file generated ans saved near generated *.xlsx file with message about problem details.
For example, we need 7 hours, but lessons from 9:00 to 12:00. In this case schedule should be: 
1. 9:00 - 12:00
2. 9:00 - 12:00
3. 9:00 - 10:00

Finally, application should obtain information from Internet about public holidays, and exclude that days from schedule.

Application should be developed as Maven project.
Remember about clean code rules and Unit Tests.
All work should be done in separate git branch.
