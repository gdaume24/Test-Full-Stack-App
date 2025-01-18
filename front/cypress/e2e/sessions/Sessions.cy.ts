before(() => {
  cy.fixture('authFormData').then(function (data) {
    this.user = data;
  });
  cy.fixture('ApiReponsesSimulation/auth/AdminUserLoginReponse.json').then(
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

it("Affichage de la liste des sessions, verification des boutons Detail et Create en tant qu'admin", function () {
  cy.visit('/login');
  cy.get('input[formControlName=email]').type(this.user.email);
  cy.get('input[formControlName=password]').type(this.user.password);
  cy.get('button[type=submit]').click();
  cy.get('mat-card mat-card:first-child');
  cy.get('mat-card mat-card:nth-child(2)');
  cy.contains('Detail');
  cy.contains('Create');
});
