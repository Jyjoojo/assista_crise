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

describe('Information e2e test', () => {
  const informationPageUrl = '/information';
  const informationPageUrlPattern = new RegExp('/information(\\?.*)?$');
  let username: string;
  let password: string;
  // const informationSample = {"titre":"user davantage","contenu":"cocorico cyan","datePublication":"2026-04-23T04:58:27.567Z","estVisible":false};

  let information;
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
      body: {"titre":"partenaire considérable","description":"collègue jusqu’à ce que","type":"GLISSEMENT_DE_TERRAIN","statut":"ACTIVE","dateDebut":"2026-04-23T05:01:47.370Z","dateFermeture":"2026-04-22T13:04:26.910Z","latitude":-14.84,"longitude":46.62,"rayonKm":29236.83},
    }).then(({ body }) => {
      crise = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/utilisateurs',
      body: {"login":"présidence au lieu de","email":"ARTJ:?@D.cU","motDePasse":"pschitt durant cadre","prenom":"communauté étudiante","nom":"à l'égard de selon lunatique","telephone":"0362997422","role":"ADMINISTRATEUR","actif":false,"dateInscription":"2026-04-23T10:00:15.742Z","dateBannissement":"2026-04-23T00:29:39.484Z","estBanni":true},
    }).then(({ body }) => {
      utilisateur = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/information+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/information').as('postEntityRequest');
    cy.intercept('DELETE', '/api/information/*').as('deleteEntityRequest');
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

  });
   */

  afterEach(() => {
    if (information) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/information/${information.id}`,
      }).then(() => {
        information = undefined;
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

  it('Informations menu should load Informations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('information');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Information').should('exist');
    cy.url().should('match', informationPageUrlPattern);
  });

  describe('Information page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(informationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Information page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/information/new$'));
        cy.getEntityCreateUpdateHeading('Information');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/information',
          body: {
            ...informationSample,
            crise: crise,
            auteur: utilisateur,
          },
        }).then(({ body }) => {
          information = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/information+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/information?page=0&size=20>; rel="last",<http://localhost/api/information?page=0&size=20>; rel="first"',
              },
              body: [information],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(informationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(informationPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Information page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('information');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPageUrlPattern);
      });

      it('edit button click should load edit Information page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Information');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPageUrlPattern);
      });

      it('edit button click should load edit Information page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Information');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Information', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('information').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPageUrlPattern);

        information = undefined;
      });
    });
  });

  describe('new Information page', () => {
    beforeEach(() => {
      cy.visit(informationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Information');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Information', () => {
      cy.get(`[data-cy="titre"]`).type('de peur que chut');
      cy.get(`[data-cy="titre"]`).should('have.value', 'de peur que chut');

      cy.get(`[data-cy="contenu"]`).type('calme de sorte que oups');
      cy.get(`[data-cy="contenu"]`).should('have.value', 'calme de sorte que oups');

      cy.get(`[data-cy="datePublication"]`).type('2026-04-23T06:15');
      cy.get(`[data-cy="datePublication"]`).blur();
      cy.get(`[data-cy="datePublication"]`).should('have.value', '2026-04-23T06:15');

      cy.get(`[data-cy="dateMiseAJour"]`).type('2026-04-22T23:50');
      cy.get(`[data-cy="dateMiseAJour"]`).blur();
      cy.get(`[data-cy="dateMiseAJour"]`).should('have.value', '2026-04-22T23:50');

      cy.get(`[data-cy="estVisible"]`).should('not.be.checked');
      cy.get(`[data-cy="estVisible"]`).click();
      cy.get(`[data-cy="estVisible"]`).should('be.checked');

      cy.get(`[data-cy="crise"]`).select(1);
      cy.get(`[data-cy="auteur"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        information = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', informationPageUrlPattern);
    });
  });
});
