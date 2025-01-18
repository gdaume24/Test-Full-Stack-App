before(() => {
  cy.fixture('authFormData').then(function (data) {
    this.user = data;
  });
  cy.intercept('POST', '/api/auth/register', {
    body: {},
  });
});

it("L'inscription d'un utilisateur renvoie bien à la page de connexion", function () {
  cy.visit('/');
  cy.contains('Register').click();
  cy.url().should('include', '/register');
  cy.get('input[formControlName=firstName]').type(this.user.firstName);
  cy.get('input[formControlName=lastName]').type(this.user.lastName);
  cy.get('input[formControlName=email]').type(this.user.email);
  cy.get('input[formControlName=password]').type(this.user.password);
  cy.get('button[type=submit]').click();
  cy.url().should('include', '/login');
});
