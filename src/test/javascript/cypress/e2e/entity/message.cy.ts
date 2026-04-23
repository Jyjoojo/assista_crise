import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Message e2e test', () => {
  const messagePageUrl = '/message';
  const messagePageUrlPattern = new RegExp('/message(\\?.*)?$');
  let username: string;
  let password: string;
  // const messageSample = {"contenu":"triste puis","dateEnvoi":"2026-04-22T20:07:20.392Z","estModere":false};

  let message;
  // let salon;
  // let utilisateur;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/salons',
      body: {"dateCreation":"2026-04-22T20:43:10.031Z"},
    }).then(({ body }) => {
      salon = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/utilisateurs',
      body: {"login":"sauver pschitt collègue","email":"A$@<p.$v/yL","motDePasse":"adepte guide ding","prenom":"trop de peur de perfectionner","nom":"cadre insipide","telephone":"+33 261536123","role":"SECOURISTE","actif":false,"dateInscription":"2026-04-22T17:22:19.602Z","dateBannissement":"2026-04-22T19:20:18.837Z","estBanni":true},
    }).then(({ body }) => {
      utilisateur = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/messages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/messages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/messages/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/salons', {
      statusCode: 200,
      body: [salon],
    });

    cy.intercept('GET', '/api/utilisateurs', {
      statusCode: 200,
      body: [utilisateur],
    });

  });
   */

  afterEach(() => {
    if (message) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/messages/${message.id}`,
      }).then(() => {
        message = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (salon) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/salons/${salon.id}`,
      }).then(() => {
        salon = undefined;
      });
    }
    if (utilisateur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/utilisateurs/${utilisateur.id}`,
      }).then(() => {
        utilisateur = undefined;
      });
    }
  });
   */

  it('Messages menu should load Messages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('message');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Message').should('exist');
    cy.url().should('match', messagePageUrlPattern);
  });

  describe('Message page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(messagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Message page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/message/new$'));
        cy.getEntityCreateUpdateHeading('Message');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', messagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/messages',
          body: {
            ...messageSample,
            salon: salon,
            auteur: utilisateur,
          },
        }).then(({ body }) => {
          message = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/messages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/messages?page=0&size=20>; rel="last",<http://localhost/api/messages?page=0&size=20>; rel="first"',
              },
              body: [message],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(messagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(messagePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Message page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('message');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', messagePageUrlPattern);
      });

      it('edit button click should load edit Message page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Message');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', messagePageUrlPattern);
      });

      it('edit button click should load edit Message page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Message');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', messagePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Message', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('message').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', messagePageUrlPattern);

        message = undefined;
      });
    });
  });

  describe('new Message page', () => {
    beforeEach(() => {
      cy.visit(messagePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Message');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Message', () => {
      cy.get(`[data-cy="contenu"]`).type("guère à l'instar de pin-pon");
      cy.get(`[data-cy="contenu"]`).should('have.value', "guère à l'instar de pin-pon");

      cy.get(`[data-cy="dateEnvoi"]`).type('2026-04-23T10:13');
      cy.get(`[data-cy="dateEnvoi"]`).blur();
      cy.get(`[data-cy="dateEnvoi"]`).should('have.value', '2026-04-23T10:13');

      cy.get(`[data-cy="estModere"]`).should('not.be.checked');
      cy.get(`[data-cy="estModere"]`).click();
      cy.get(`[data-cy="estModere"]`).should('be.checked');

      cy.get(`[data-cy="salon"]`).select(1);
      cy.get(`[data-cy="auteur"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        message = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', messagePageUrlPattern);
    });
  });
});
