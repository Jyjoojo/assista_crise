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

describe('Offre e2e test', () => {
  const offrePageUrl = '/offre';
  const offrePageUrlPattern = new RegExp('/offre(\\?.*)?$');
  let username: string;
  let password: string;
  // const offreSample = {"titre":"gestionnaire","description":"enrichir","statut":"CLOTUREE","dateCreation":"2026-04-23T08:45:54.681Z","estArchivee":true};

  let offre;
  // let crise;
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
      url: '/api/crises',
      body: {"titre":"blablabla snif","description":"hors de","type":"FEU_DE_FORET","statut":"FERMEE","dateDebut":"2026-04-23T07:16:20.720Z","dateFermeture":"2026-04-22T13:05:48.220Z","latitude":-57.92,"longitude":-134.79,"rayonKm":30290.51},
    }).then(({ body }) => {
      crise = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/utilisateurs',
      body: {"login":"nier à demi","email":"T@jHDK-.#e#YQ!","motDePasse":"au cas où patientèle","prenom":"tourner au-dessus à travers","nom":"vu que assigner","telephone":"+33 215075026","role":"ADMINISTRATEUR","actif":false,"dateInscription":"2026-04-23T10:54:04.684Z","dateBannissement":"2026-04-22T16:18:23.100Z","estBanni":false},
    }).then(({ body }) => {
      utilisateur = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/offres+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/offres').as('postEntityRequest');
    cy.intercept('DELETE', '/api/offres/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/crises', {
      statusCode: 200,
      body: [crise],
    });

    cy.intercept('GET', '/api/utilisateurs', {
      statusCode: 200,
      body: [utilisateur],
    });

    cy.intercept('GET', '/api/demandes', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (offre) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/offres/${offre.id}`,
      }).then(() => {
        offre = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (crise) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crises/${crise.id}`,
      }).then(() => {
        crise = undefined;
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

  it('Offres menu should load Offres page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('offre');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Offre').should('exist');
    cy.url().should('match', offrePageUrlPattern);
  });

  describe('Offre page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(offrePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Offre page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/offre/new$'));
        cy.getEntityCreateUpdateHeading('Offre');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', offrePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/offres',
          body: {
            ...offreSample,
            crise: crise,
            aidant: utilisateur,
          },
        }).then(({ body }) => {
          offre = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/offres+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/offres?page=0&size=20>; rel="last",<http://localhost/api/offres?page=0&size=20>; rel="first"',
              },
              body: [offre],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(offrePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(offrePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Offre page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('offre');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', offrePageUrlPattern);
      });

      it('edit button click should load edit Offre page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Offre');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', offrePageUrlPattern);
      });

      it('edit button click should load edit Offre page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Offre');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', offrePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Offre', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('offre').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', offrePageUrlPattern);

        offre = undefined;
      });
    });
  });

  describe('new Offre page', () => {
    beforeEach(() => {
      cy.visit(offrePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Offre');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Offre', () => {
      cy.get(`[data-cy="titre"]`).type('jeune enfant');
      cy.get(`[data-cy="titre"]`).should('have.value', 'jeune enfant');

      cy.get(`[data-cy="description"]`).type('concurrence oups');
      cy.get(`[data-cy="description"]`).should('have.value', 'concurrence oups');

      cy.get(`[data-cy="statut"]`).select('CLOTUREE');

      cy.get(`[data-cy="dateCreation"]`).type('2026-04-22T23:47');
      cy.get(`[data-cy="dateCreation"]`).blur();
      cy.get(`[data-cy="dateCreation"]`).should('have.value', '2026-04-22T23:47');

      cy.get(`[data-cy="dateMiseAJour"]`).type('2026-04-22T15:21');
      cy.get(`[data-cy="dateMiseAJour"]`).blur();
      cy.get(`[data-cy="dateMiseAJour"]`).should('have.value', '2026-04-22T15:21');

      cy.get(`[data-cy="latitude"]`).type('-13.2');
      cy.get(`[data-cy="latitude"]`).should('have.value', '-13.2');

      cy.get(`[data-cy="longitude"]`).type('114.7');
      cy.get(`[data-cy="longitude"]`).should('have.value', '114.7');

      cy.get(`[data-cy="estArchivee"]`).should('not.be.checked');
      cy.get(`[data-cy="estArchivee"]`).click();
      cy.get(`[data-cy="estArchivee"]`).should('be.checked');

      cy.get(`[data-cy="dateDeferencement"]`).type('2026-04-22T13:59');
      cy.get(`[data-cy="dateDeferencement"]`).blur();
      cy.get(`[data-cy="dateDeferencement"]`).should('have.value', '2026-04-22T13:59');

      cy.get(`[data-cy="crise"]`).select(1);
      cy.get(`[data-cy="aidant"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        offre = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', offrePageUrlPattern);
    });
  });
});
