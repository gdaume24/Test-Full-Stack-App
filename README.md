# Testing a full stack application
* Unit and integration test of Angular front-end with Jest
* End to end tests on Angular mocking API response with Cypress
* Unit and integration tests of Spring back-end using Junit and Mockito

## Project launch guide :

1. Clone the repository in somewhere in your machine and go inside it :  
```powershell
git clone https://github.com/gdaume24/Test-Full-Stack-App.git
cd Test-Full-Stack-App
```
2. 

To start unit and integration tests on front-end part :
Go into front folder :
cd front

Install Angular project dependencies :
npm i

Start test with coverage report :
npm test

Details about those tests :
The tests that have been edited manually are features/auth/component/login/login.component.spec.ts
                                             features/auth/component/register/register.component.spec.ts
                                             features/sessions/components/form/form.component.spec.ts


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
