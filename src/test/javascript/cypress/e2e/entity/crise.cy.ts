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

describe('Crise e2e test', () => {
  const crisePageUrl = '/crise';
  const crisePageUrlPattern = new RegExp('/crise(\\?.*)?$');
  let username: string;
  let password: string;
  const criseSample = {
    titre: 'ding ailleurs perplexe',
    type: 'GLISSEMENT_DE_TERRAIN',
    statut: 'ARCHIVEE',
    dateDebut: '2026-04-23T04:19:53.249Z',
    latitude: -57.32,
    longitude: -141.59,
  };

  let crise;
  let utilisateur;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/utilisateurs',
      body: {
        login: 'toc-toc pour',
        email: '"df/@e4<`.vK$A',
        motDePasse: 'hors de hypocrite',
        prenom: 'mériter pendant que',
        nom: 'vraiment trop lectorat',
        telephone: '+33 236136173',
        role: 'SECOURISTE',
        actif: true,
        dateInscription: '2026-04-23T04:11:45.148Z',
        dateBannissement: '2026-04-22T12:55:05.705Z',
        estBanni: true,
      },
    }).then(({ body }) => {
      utilisateur = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crises+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crises').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crises/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/utilisateurs', {
      statusCode: 200,
      body: [utilisateur],
    });
  });

  afterEach(() => {
    if (crise) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crises/${crise.id}`,
      }).then(() => {
        crise = undefined;
      });
    }
  });

  afterEach(() => {
    if (utilisateur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/utilisateurs/${utilisateur.id}`,
      }).then(() => {
        utilisateur = undefined;
      });
    }
  });

  it('Crises menu should load Crises page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crise');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Crise').should('exist');
    cy.url().should('match', crisePageUrlPattern);
  });

  describe('Crise page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crisePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Crise page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/crise/new$'));
        cy.getEntityCreateUpdateHeading('Crise');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', crisePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crises',
          body: {
            ...criseSample,
            declarant: utilisateur,
          },
        }).then(({ body }) => {
          crise = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crises+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crise],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(crisePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Crise page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crise');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', crisePageUrlPattern);
      });

      it('edit button click should load edit Crise page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Crise');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', crisePageUrlPattern);
      });

      it('edit button click should load edit Crise page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Crise');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', crisePageUrlPattern);
      });

      it('last delete button click should delete instance of Crise', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crise').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', crisePageUrlPattern);

        crise = undefined;
      });
    });
  });

  describe('new Crise page', () => {
    beforeEach(() => {
      cy.visit(crisePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Crise');
    });

    it('should create an instance of Crise', () => {
      cy.get(`[data-cy="titre"]`).type('contre contre');
      cy.get(`[data-cy="titre"]`).should('have.value', 'contre contre');

      cy.get(`[data-cy="description"]`).type('timide splendide organiser');
      cy.get(`[data-cy="description"]`).should('have.value', 'timide splendide organiser');

      cy.get(`[data-cy="type"]`).select('GLISSEMENT_DE_TERRAIN');

      cy.get(`[data-cy="statut"]`).select('ARCHIVEE');

      cy.get(`[data-cy="dateDebut"]`).type('2026-04-22T17:20');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2026-04-22T17:20');

      cy.get(`[data-cy="dateFermeture"]`).type('2026-04-23T11:21');
      cy.get(`[data-cy="dateFermeture"]`).blur();
      cy.get(`[data-cy="dateFermeture"]`).should('have.value', '2026-04-23T11:21');

      cy.get(`[data-cy="latitude"]`).type('-88.12');
      cy.get(`[data-cy="latitude"]`).should('have.value', '-88.12');

      cy.get(`[data-cy="longitude"]`).type('122.63');
      cy.get(`[data-cy="longitude"]`).should('have.value', '122.63');

      cy.get(`[data-cy="rayonKm"]`).type('22803.29');
      cy.get(`[data-cy="rayonKm"]`).should('have.value', '22803.29');

      cy.get(`[data-cy="declarant"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        crise = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', crisePageUrlPattern);
    });
  });
});
