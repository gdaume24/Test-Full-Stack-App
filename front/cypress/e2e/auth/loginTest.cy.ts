context('Login', () => {
    beforeEach(() => {
        cy.fixture('authFormData.json').then(function(authFormData) {
            this.authFormData = authFormData})   
    })
    
    it("L'inscription d'un utilisateur renvoie bien à la page de connexion", () => {  
        cy.intercept('POST', '/api/auth/register', {
            body: {},
          })
        cy.visit("/")
        cy.contains('Register').click()
        cy.url().should('include', '/register') 
        cy.get('input[formControlName=firstName]').type(this.authFormData.firstName)
        cy.get('input[formControlName=lastName]').type(this.authFormData.lastName)
        cy.get('input[formControlName=email]').type(this.authFormData.email)
        cy.get('input[formControlName=password]').type(this.authFormData.password)
        cy.get('button[type=submit]').click()
        cy.url().should('include', '/login')
    })  
})

