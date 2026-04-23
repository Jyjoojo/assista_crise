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

describe('Demande e2e test', () => {
  const demandePageUrl = '/demande';
  const demandePageUrlPattern = new RegExp('/demande(\\?.*)?$');
  let username: string;
  let password: string;
  // const demandeSample = {"titre":"au-dessous conquérir","description":"à la merci meuh par","statut":"RESOLUE","dateCreation":"2026-04-23T09:37:47.038Z","estArchivee":true};

  let demande;
  // let utilisateur;
  // let crise;

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
      url: '/api/utilisateurs',
      body: {"login":"làX","email":"b}wW@p\"8._=k","motDePasse":"jusqu’à ce que habile que","prenom":"athlète euh glouglou","nom":"corps enseignant ramasser vide","telephone":"0375599781","role":"ADMINISTRATEUR","actif":true,"dateInscription":"2026-04-22T15:40:54.563Z","dateBannissement":"2026-04-23T10:52:17.937Z","estBanni":true},
    }).then(({ body }) => {
      utilisateur = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/crises',
      body: {"titre":"étudier","description":"immense enfoncer","type":"AUTRE","statut":"ACTIVE","dateDebut":"2026-04-22T19:10:00.352Z","dateFermeture":"2026-04-22T21:20:50.042Z","latitude":-12.2,"longitude":39.78,"rayonKm":24397.48},
    }).then(({ body }) => {
      crise = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/demandes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/demandes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/demandes/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/utilisateurs', {
      statusCode: 200,
      body: [utilisateur],
    });

    cy.intercept('GET', '/api/crises', {
      statusCode: 200,
      body: [crise],
    });

    cy.intercept('GET', '/api/offres', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/salons', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (demande) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/demandes/${demande.id}`,
      }).then(() => {
        demande = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (utilisateur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/utilisateurs/${utilisateur.id}`,
      }).then(() => {
        utilisateur = undefined;
      });
    }
    if (crise) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crises/${crise.id}`,
      }).then(() => {
        crise = undefined;
      });
    }
  });
   */

  it('Demandes menu should load Demandes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('demande');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Demande').should('exist');
    cy.url().should('match', demandePageUrlPattern);
  });

  describe('Demande page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(demandePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Demande page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/demande/new$'));
        cy.getEntityCreateUpdateHeading('Demande');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', demandePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/demandes',
          body: {
            ...demandeSample,
            sinistre: utilisateur,
            crise: crise,
          },
        }).then(({ body }) => {
          demande = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/demandes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/demandes?page=0&size=20>; rel="last",<http://localhost/api/demandes?page=0&size=20>; rel="first"',
              },
              body: [demande],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(demandePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(demandePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Demande page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('demande');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', demandePageUrlPattern);
      });

      it('edit button click should load edit Demande page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Demande');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', demandePageUrlPattern);
      });

      it('edit button click should load edit Demande page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Demande');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', demandePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Demande', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('demande').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', demandePageUrlPattern);

        demande = undefined;
      });
    });
  });

  describe('new Demande page', () => {
    beforeEach(() => {
      cy.visit(demandePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Demande');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Demande', () => {
      cy.get(`[data-cy="titre"]`).type('en guise de lentement');
      cy.get(`[data-cy="titre"]`).should('have.value', 'en guise de lentement');

      cy.get(`[data-cy="description"]`).type('clac spécialiste tant');
      cy.get(`[data-cy="description"]`).should('have.value', 'clac spécialiste tant');

      cy.get(`[data-cy="statut"]`).select('EN_COURS_DE_RESOLUTION');

      cy.get(`[data-cy="dateCreation"]`).type('2026-04-23T04:10');
      cy.get(`[data-cy="dateCreation"]`).blur();
      cy.get(`[data-cy="dateCreation"]`).should('have.value', '2026-04-23T04:10');

      cy.get(`[data-cy="dateMiseAJour"]`).type('2026-04-22T19:51');
      cy.get(`[data-cy="dateMiseAJour"]`).blur();
      cy.get(`[data-cy="dateMiseAJour"]`).should('have.value', '2026-04-22T19:51');

      cy.get(`[data-cy="dateFermeture"]`).type('2026-04-22T16:54');
      cy.get(`[data-cy="dateFermeture"]`).blur();
      cy.get(`[data-cy="dateFermeture"]`).should('have.value', '2026-04-22T16:54');

      cy.get(`[data-cy="latitude"]`).type('-62.39');
      cy.get(`[data-cy="latitude"]`).should('have.value', '-62.39');

      cy.get(`[data-cy="longitude"]`).type('-94.02');
      cy.get(`[data-cy="longitude"]`).should('have.value', '-94.02');

      cy.get(`[data-cy="estArchivee"]`).should('not.be.checked');
      cy.get(`[data-cy="estArchivee"]`).click();
      cy.get(`[data-cy="estArchivee"]`).should('be.checked');

      cy.get(`[data-cy="dateDeferencement"]`).type('2026-04-23T00:36');
      cy.get(`[data-cy="dateDeferencement"]`).blur();
      cy.get(`[data-cy="dateDeferencement"]`).should('have.value', '2026-04-23T00:36');

      cy.get(`[data-cy="sinistre"]`).select(1);
      cy.get(`[data-cy="crise"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        demande = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', demandePageUrlPattern);
    });
  });
});
