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

describe('Salon e2e test', () => {
  const salonPageUrl = '/salon';
  const salonPageUrlPattern = new RegExp('/salon(\\?.*)?$');
  let username: string;
  let password: string;
  // const salonSample = {"dateCreation":"2026-04-23T04:29:07.800Z"};

  let salon;
  // let demande;

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
      url: '/api/demandes',
      body: {"titre":"personnel professionnel bof toc-toc","description":"sus solitaire","statut":"EN_COURS_DE_RESOLUTION","dateCreation":"2026-04-23T03:20:16.005Z","dateMiseAJour":"2026-04-23T04:53:56.805Z","dateFermeture":"2026-04-23T05:47:31.030Z","latitude":50.64,"longitude":-133.48,"estArchivee":false,"dateDeferencement":"2026-04-22T19:28:16.537Z"},
    }).then(({ body }) => {
      demande = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/salons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/salons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/salons/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/demandes', {
      statusCode: 200,
      body: [demande],
    });

    cy.intercept('GET', '/api/utilisateurs', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (salon) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/salons/${salon.id}`,
      }).then(() => {
        salon = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('Salons menu should load Salons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('salon');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Salon').should('exist');
    cy.url().should('match', salonPageUrlPattern);
  });

  describe('Salon page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(salonPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Salon page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/salon/new$'));
        cy.getEntityCreateUpdateHeading('Salon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salonPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/salons',
          body: {
            ...salonSample,
            demande: demande,
          },
        }).then(({ body }) => {
          salon = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/salons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [salon],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(salonPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(salonPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Salon page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('salon');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salonPageUrlPattern);
      });

      it('edit button click should load edit Salon page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Salon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salonPageUrlPattern);
      });

      it('edit button click should load edit Salon page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Salon');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salonPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Salon', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('salon').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', salonPageUrlPattern);

        salon = undefined;
      });
    });
  });

  describe('new Salon page', () => {
    beforeEach(() => {
      cy.visit(salonPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Salon');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Salon', () => {
      cy.get(`[data-cy="dateCreation"]`).type('2026-04-23T09:50');
      cy.get(`[data-cy="dateCreation"]`).blur();
      cy.get(`[data-cy="dateCreation"]`).should('have.value', '2026-04-23T09:50');

      cy.get(`[data-cy="demande"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        salon = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', salonPageUrlPattern);
    });
  });
});
