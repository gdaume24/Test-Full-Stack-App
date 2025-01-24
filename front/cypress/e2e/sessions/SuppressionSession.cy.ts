
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
            }
        );
        // Mock API reponse des sessions services
        cy.fixture('ApiReponsesSimulation/session/postApiSessionMockReponse.json').then((data) => {
            this.sessionData = data;
            cy.intercept('GET', '/api/session', {
                body : [this.sessionData]
            });
            cy.intercept('GET', '/api/session/1', {
                body : this.sessionData
            });
            cy.intercept('DELETE', '/api/session/1', {
                body : null
                })
            cy.intercept('DELETE', '/api/session/1', (req) => {
                req.on('response', (res) => {
                this.sessionData = null;
                })
            })
        });

        cy.fixture('ApiReponsesSimulation/teacher/getApiTeacher1Reponse.json').then((data) => {
            cy.intercept('GET', '/api/teacher/1', {
                body: data,
            });
        });
    });
    
    it("Supprime une session", function () {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(this.user.email);
    cy.get('input[formControlName=password]').type(this.user.password);
    cy.get('button[type=submit]').click();
    cy.contains('Detail').click();
    cy.contains('Delete').click().debug();
    // cy.wait('@sessionData'); 
    });
});


