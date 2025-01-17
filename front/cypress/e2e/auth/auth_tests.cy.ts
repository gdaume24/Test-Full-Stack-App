


    it("La connexion d'un utilisateur renvoie bien à la page des sessions", () => {  
        cy.intercept('POST', '/api/auth/login', {
            body: {  
                token: "blablabla",
                type: "JWT",
                id: 1,
                username: firstName + " " + lastName,
                firstName: firstName,
                lastName: lastName,
                admin: false
            }});

        cy.visit("/")
        cy.contains('Login').click()
        cy.url().should('include', '/login')    
        cy.get('input[formControlName=email]').type(email)
        cy.get('input[formControlName=password]').type(password)
        cy.get('button[type=submit]').click()
        cy.url().should('include', '/sessions')
    })
})

