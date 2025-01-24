describe("Suppression d'une session", () => {
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
        });
        cy.fixture('ApiReponsesSimulation/user/mockApiReponseUserById.json').then(
            (data) => {
            cy.intercept('GET', '/api/user/1', {
                body: data,
            });
        });
        cy.fixture('ApiReponsesSimulation/session/postApiSessionMockReponse.json').then((data) => {
            this.sessionData = data;
            cy.intercept('GET', '/api/session', {
                body : [this.sessionData]
            });
        });
    });
    
    it("Affichage des informations de l’utilisateur", function () {
        cy.visit('/login');
        cy.get('input[formControlName=email]').type(this.user.email);
        cy.get('input[formControlName=password]').type(this.user.password);
        cy.get('button[type=submit]').click();
        cy.contains('Account').click();
        cy.url().should("contain", "me");
        cy.get("p").should("contain", "Name:");
        cy.get("p").should("contain", "Email:");
        cy.get("p").should("contain", "admin");
        cy.get("p").should("contain", "Create at:");
        cy.get("p").should("contain", "Last update:");
        });
});
