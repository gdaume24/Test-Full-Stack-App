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
      this.session1Data = data[0];
      cy.intercept('GET', '/api/session/1', {
        body: data[0],
      });
    }
  );
  cy.fixture('ApiReponsesSimulation/teacher/getApiTeacher1Reponse.json').then(
    function (data) {
      this.teacher1 = data;
      cy.intercept('GET', '/api/teacher/1', {
        body: data,
      });
    }
  );
});

it("Press 'Detail' button, session informations are shown correctly. Verify delete button is present as admin", function () {
  cy.visit('/login');
  cy.get('input[formControlName=email]').type(this.user.email);
  cy.get('input[formControlName=password]').type(this.user.password);
  cy.get('button[type=submit]').click();
  cy.get('mat-card:nth-child(1)').scrollIntoView().contains('Detail').click();
  cy.get('h1').contains(this.session1Data.name, { matchCase: false });
  cy.get('mat-card-subtitle').contains(
    `${this.teacher1.firstName} ${this.teacher1.lastName}`,
    { matchCase: false }
  );
  cy.contains('Delete');
});
