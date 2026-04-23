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

describe('Moderation e2e test', () => {
  const moderationPageUrl = '/moderation';
  const moderationPageUrlPattern = new RegExp('/moderation(\\?.*)?$');
  let username: string;
  let password: string;
  const moderationSample = { motif: 'pff', dateModeration: '2026-04-22T17:14:16.532Z', action: 'EXCLUSION_TEMPORAIRE' };

  let moderation;
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
        login: 'atchoum',
        email: 'luf@PYG.HY',
        motDePasse: 'oupsXXXX',
        prenom: 'avant',
        nom: 'tellement',
        telephone: '+33 268121915',
        role: 'AUTORITE_LOCALE',
        actif: false,
        dateInscription: '2026-04-22T17:12:30.568Z',
        dateBannissement: '2026-04-23T04:14:18.390Z',
        estBanni: true,
      },
    }).then(({ body }) => {
      utilisateur = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/moderations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/moderations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/moderations/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/utilisateurs', {
      statusCode: 200,
      body: [utilisateur],
    });

    cy.intercept('GET', '/api/demandes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/offres', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (moderation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/moderations/${moderation.id}`,
      }).then(() => {
        moderation = undefined;
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

  it('Moderations menu should load Moderations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('moderation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Moderation').should('exist');
    cy.url().should('match', moderationPageUrlPattern);
  });

  describe('Moderation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(moderationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Moderation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/moderation/new$'));
        cy.getEntityCreateUpdateHeading('Moderation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/moderations',
          body: {
            ...moderationSample,
            administrateur: utilisateur,
          },
        }).then(({ body }) => {
          moderation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/moderations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/moderations?page=0&size=20>; rel="last",<http://localhost/api/moderations?page=0&size=20>; rel="first"',
              },
              body: [moderation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(moderationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Moderation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('moderation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationPageUrlPattern);
      });

      it('edit button click should load edit Moderation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Moderation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationPageUrlPattern);
      });

      it('edit button click should load edit Moderation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Moderation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationPageUrlPattern);
      });

      it('last delete button click should delete instance of Moderation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('moderation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moderationPageUrlPattern);

        moderation = undefined;
      });
    });
  });

  describe('new Moderation page', () => {
    beforeEach(() => {
      cy.visit(moderationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Moderation');
    });

    it('should create an instance of Moderation', () => {
      cy.get(`[data-cy="motif"]`).type('charitable');
      cy.get(`[data-cy="motif"]`).should('have.value', 'charitable');

      cy.get(`[data-cy="dateModeration"]`).type('2026-04-23T03:05');
      cy.get(`[data-cy="dateModeration"]`).blur();
      cy.get(`[data-cy="dateModeration"]`).should('have.value', '2026-04-23T03:05');

      cy.get(`[data-cy="action"]`).select('AVERTISSEMENT');

      cy.get(`[data-cy="administrateur"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        moderation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', moderationPageUrlPattern);
    });
  });
});
