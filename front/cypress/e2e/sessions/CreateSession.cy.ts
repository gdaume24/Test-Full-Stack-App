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
        cy.intercept('POST', '/api/session', {
          body: data,
        });
        cy.intercept('GET', '/api/session', {
          body: [data],
        });
      }
    );
    cy.fixture('ApiReponsesSimulation/teacher/getApiTeacherMockReponse.json').then(
        function (data) {
          cy.intercept('GET', '/api/teacher', {
            body: data,
          });
        });
      });
  
  it("Crée une session en tant qu'admin", function () {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(this.user.email);
    cy.get('input[formControlName=password]').type(this.user.password);
    cy.get('button[type=submit]').click();
    cy.contains('Create').click();

    // Remplit tout sauf name
    cy.get('input[type="date"]').type('2028-12-12');
    cy.get('mat-select[ng-reflect-name="teacher_id"]').click().get('mat-option').contains('John Doe').click();
    cy.get('textarea[formcontrolname="description"]').type('Session xtreme');
    // Le bouton n'est pas cliquable
    cy.contains("Save").should('be.disabled')
    cy.get('input[formcontrolname="name"]').type('Session Soleil');
    cy.contains("Save").click()
    cy.contains("Session Soleil")
  });
  