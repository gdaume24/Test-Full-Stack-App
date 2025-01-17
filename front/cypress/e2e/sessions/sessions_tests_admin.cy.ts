const adminUserLoginReponse = cy.fixture('ApiReponsesSimulation/adminUserLoginReponse.json');
const getApiSessionReponse = cy.fixture('ApiReponsesSimulation/getApiSessionReponse.json');

it("Affichage de la liste des sessions, verification des boutons Detail et Create en tant qu'admin", () => {  
cy.intercept('POST', '/api/auth/login', {
    body: adminUserLoginReponse
});

cy.intercept('GET', '/api/session', {
    body: getApiSessionReponse
});

cy.visit("/login")  
cy.get('input[formControlName=email]').type(user.email)
cy.get('input[formControlName=password]').type(user.password)
cy.get('button[type=submit]').click()
cy.get('mat-card mat-card:first-child')
cy.get('mat-card mat-card:nth-child(2)')
cy.contains('Detail')
cy.contains('Create')
})

