before(() => {
  cy.fixture('authFormData').then(function (data) {
    this.user = data;
  });
  cy.fixture('ApiReponsesSimulation/auth/nonAdminUserLoginReponse.json').then(
    function (data) {
      cy.intercept('POST', '/api/auth/login', {
        body: data,
      });
    }
  );
  cy.fixture('ApiReponsesSimulation/session/getApiSessionReponse.json').then(
    function (data) {
      cy.intercept('GET', '/api/session', {
        body: data,
      });
    }
  );
});

it("La connexion d'un utilisateur renvoie bien à la page des sessions", function () {
  cy.visit('/');
  cy.contains('Login').click();
  cy.url().should('include', '/login');
  cy.get('input[formControlName=email]').type(this.user.email);
  cy.get('input[formControlName=password]').type(this.user.password);
  cy.get('button[type=submit]').click();
  cy.url().should('include', '/sessions');
});
