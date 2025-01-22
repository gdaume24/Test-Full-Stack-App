# Testing a full stack application
* Unit and integration test of Angular front-end with Jest
* End to end tests on Angular mocking API response with Cypress
* Unit and integration tests of Spring back-end using Junit and Mockito

## Project launch guide :

1. Clone the repository somewhere in your machine and go inside it :  
```powershell
git clone https://github.com/gdaume24/Test-Full-Stack-App.git
cd Test-Full-Stack-App
```
Now we want to run first the database :
2. Ensure you have your docker engine running
3. Create a .env file in db folder, as you can't create it in the root folder of the project, because it will not be detected by docker.
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
Now that you're database is running, we will prepare the back-end dependencies :
5. Create .env file in back folder, here is the template of the file for you to set your personal variables in
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

## Project tests coverages guide

To start unit and integration tests coverage on front-end part :
```npm test```
The tests that have been edited manually are features/auth/component/login/login.component.spec.ts
                                             features/auth/component/register/register.component.spec.ts
                                             features/sessions/components/form/form.component.spec.ts







TODO
e2e testing with Cypress :

To start e2e test, do the following commands :
if you're not already in front folder :
cd front
run front server : ng serve
npx cypress verify
npx cypress open

Pour générer le rapport de code coverage :

npx update-browserslist-db@latest
npx nyc instrument --compact=false src instrumented 
