describe('Logout session', () => {
  before(function () {
    // Informations de formulaire d'authentification
    cy.fixture('authFormData').then((data) => {
      this.user = data;
    });
    // Mock API reponse de login as admin
    cy.fixture('ApiReponsesSimulation/auth/AdminUserLoginReponse.json').then(
      (data) => {
        cy.intercept('POST', '/api/auth/login', {
          body: data,
        });
      }
    );
    cy.fixture('ApiReponsesSimulation/user/mockApiReponseUserById.json').then(
      (data) => {
        cy.intercept('GET', '/api/user/1', {
          body: data,
        });
      }
    );
    cy.fixture(
      'ApiReponsesSimulation/session/postApiSessionMockReponse.json'
    ).then((data) => {
      this.sessionData = data;
      cy.intercept('GET', '/api/session', {
        body: [this.sessionData],
      });
    });
  });

  it("Déconnexion d'un utilisateur", function () {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(this.user.email);
    cy.get('input[formControlName=password]').type(this.user.password);
    cy.get('button[type=submit]').click();
    cy.contains('Logout').click();
    cy.contains('Login');
    cy.contains('Register');
  });
});
