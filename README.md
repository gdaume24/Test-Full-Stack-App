# Testing a full stack application
* Unit and integration test of Angular front-end with Jest
* End to end tests on Angular mocking API response with Cypress
* Unit and integration tests of Spring back-end using Junit and Mockito

## Guide to run the app :
1. Clone the repository somewhere in your machine and go inside it :  
```powershell
git clone https://github.com/gdaume24/Test-Full-Stack-App.git
cd Test-Full-Stack-App
```
Now we want to run first the database :  
2. Ensure you have your docker engine running  
3. Create a .env file in db folder. (Docker is unable to detect it if it is in the root folder).  
Set your personal variables in the .env file
```
MYSQL_USERNAME=your_user
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_PASSWORD=your_password
```
4. Create the MySQL databse
```powershell
cd db
docker-compose up -d
```
Now that your database is running, we will prepare the back-end dependencies :  
5. Create .env file in the "back" folder, here is the template of the file for you to set your personal variables in.  
You need to set the 3 same variables from the .env of the database as in the template
```
MYSQL_URL=your_mysql_url
MYSQL_USERNAME=your_user
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_PASSWORD=your_password

SECURITY_JWT_SECRET_KEY=your_jwt_key
SECURITY_JWT_EXPIRATION_TIME=your_jwt_expiration_time
```
6. Make sure you have Extension pack for Java installed in your VSCode, and a jdk-11 folder set in your JAVA_HOME system environment variable
Before starting the back, we want to prepare the front-end dependencies
7. Install nodejs 16 (uninstall your current version if needed)
8. Install Angular CLI 14 :
  ```npm install -g @angular/cli@14```
9. Install dependencies :
```
cd front
npm i
```
10. Run back server
In your VSCode file explorer, find the file back/src/main/java/com/openclassrooms/startedjwt/SpringBootSecurityJwtApplication.java, right click on it and click 'Run Java'
12. Run front server
```ng serve```

Now the whole stack is running, you can test the app manually  
-> <http://localhost:4200/>

## Guide to obtain test coverages

### To run unit and integration tests coverage on front-end :
```
cd front
npm run test
```
The report will be shown in the terminal, the html file with the details is located at `coverage/jest/index.html`  
<ins>Note</ins> : The tests that have been edited manually are :
- features/auth/component/login/login.component.spec.ts
- features/auth/component/register/register.component.spec.ts
- features/sessions/components/form/form.component.spec.ts 
  
### To run 'end to end' tests coverage on front-end :  
```
npm run e2e:ci
```  
The report will be shown in the terminal, the html file with the details is located at `coverage/lcov-report/index.html`

### To run unit and integration tests coverage on back-end : 
In file explorer of VSCode, in `back/src`, right click on test, and click on "Run Tests with Coverage" option.  
You will see the details of the code coverage directly in the file explorer of VSCode.



