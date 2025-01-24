describe("Suppression d'une session", () => {
    before(() => {
      // Charger les données de connexion
      cy.fixture('authFormData').then(function (data) {
        this.user = data; // Stocker les informations utilisateur
      });
  
      // Mock de la réponse de login
      cy.fixture('ApiReponsesSimulation/auth/AdminUserLoginReponse.json').then((data) => {
        cy.intercept('POST', '/api/auth/login', { body: data });
      });
  
      // Mock des réponses pour les sessions
      cy.fixture('ApiReponsesSimulation/session/postApiSessionMockReponse.json').then((data) => {
        cy.wrap(data).as('sessionData'); // Stocker sessionData comme alias
        cy.intercept('GET', '/api/session', { body: [data] }); // Liste des sessions
        cy.intercept('GET', '/api/session/1', { body: data }); // Détails de la session
        cy.intercept('DELETE', '/api/session/1', (req) => {
          req.reply({ statusCode: 200 }); // Réponse simulée pour DELETE
          cy.wrap(null).as('sessionData'); // Mettre sessionData à null après suppression
        }).as('deleteSession'); // Alias pour DELETE
      });
  
      // Mock des réponses pour teacher
      cy.fixture('ApiReponsesSimulation/teacher/getApiTeacher1Reponse.json').then((data) => {
        cy.intercept('GET', '/api/teacher/1', { body: data });
      });
    });
  
    it('Supprime une session', function () {
      // Accéder à la page de login
      cy.visit('/login');
  
      // Remplir et soumettre le formulaire de connexion
      cy.get('input[formControlName=email]').type(this.user.email);
      cy.get('input[formControlName=password]').type(this.user.password);
      cy.get('button[type=submit]').click();
  
      // Accéder aux détails de la session
      cy.contains('Detail').click();
  
      // Supprimer la session
      cy.contains('Delete').click();
  
      // Vérifier que la requête DELETE a été envoyée et a réussi
      cy.wait('@deleteSession').then((interception) => {
        expect(interception.request.method).to.eq('DELETE'); // Vérifier le type de requête
        expect(interception.response.statusCode).to.eq(200); // Vérifier le code de réponse
      });
  
    });
  });