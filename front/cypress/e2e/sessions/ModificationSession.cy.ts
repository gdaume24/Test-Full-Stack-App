before(() => {
    // Informations de formulaire d'authentification
    cy.fixture('authFormData').then(function (data) {
      this.user = data;
    });
    // Mock API reponse de login as admin
    cy.fixture('ApiReponsesSimulation/auth/AdminUserLoginReponse.json').then(
      function (data) {
        cy.intercept('POST', '/api/auth/login', {
          body: data,
        });
      }
    );
    // Mock API reponse de session
    cy.fixture('ApiReponsesSimulation/session/postApiSessionMockReponse.json').then(
      function (data) {
        cy.intercept('GET', '/api/session', {
          body: [data],
        });
        cy.intercept('GET', '/api/session/1', {
          body: data,
        });
        cy.intercept('PUT', '/api/session/1', {
          body: data,
        });
      });
    cy.fixture('ApiReponsesSimulation/teacher/getApiTeacherMockReponse.json').then(
        function (data) {
          cy.intercept('GET', '/api/teacher', {
            body: data,
          });
        });
});

  it("Modifie une session, vérifie que le bouton est désactivé en l'absence d'un champ obligatoire", function () {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(this.user.email);
    cy.get('input[formControlName=password]').type(this.user.password);
    cy.get('button[type=submit]').click();
    cy.contains('Edit').click();
    // On modifie le prof
    cy.get('mat-select[formcontrolname="teacher_id"]').click().get('mat-option').contains('Jane Smith').click();
    // On enlève la description et on check que le bouton 'Save' est désactivé
    cy.get('textarea[formcontrolname="description"]').clear();
    cy.contains("Save").should('be.disabled')
    cy.get('textarea[formcontrolname="description"]').type('Session cool');
    cy.contains('Save').click();
    cy.url().should("contain", "sessions");
  });