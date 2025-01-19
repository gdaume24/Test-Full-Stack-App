Project to test a full stack app

To run project :

Install the repository in your machine :
git clone https://github.com/gdaume24/Test-Full-Stack-App.git

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
