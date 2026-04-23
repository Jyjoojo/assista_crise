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

describe('Utilisateur e2e test', () => {
  const utilisateurPageUrl = '/utilisateur';
  const utilisateurPageUrlPattern = new RegExp('/utilisateur(\\?.*)?$');
  let username: string;
  let password: string;
  const utilisateurSample = {
    login: 'derrière ah',
    email: 'fF@9.`&Sw<]',
    motDePasse: 'brave entre parce que',
    prenom: 'ronron hé',
    nom: 'communauté étudiante',
    role: 'ADMINISTRATEUR',
    actif: true,
    dateInscription: '2026-04-22T21:21:46.899Z',
    estBanni: true,
  };

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
    cy.intercept('GET', '/api/utilisateurs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/utilisateurs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/utilisateurs/*').as('deleteEntityRequest');
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

  it('Utilisateurs menu should load Utilisateurs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('utilisateur');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Utilisateur').should('exist');
    cy.url().should('match', utilisateurPageUrlPattern);
  });

  describe('Utilisateur page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(utilisateurPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Utilisateur page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/utilisateur/new$'));
        cy.getEntityCreateUpdateHeading('Utilisateur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/utilisateurs',
          body: utilisateurSample,
        }).then(({ body }) => {
          utilisateur = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/utilisateurs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [utilisateur],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(utilisateurPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Utilisateur page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('utilisateur');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);
      });

      it('edit button click should load edit Utilisateur page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Utilisateur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);
      });

      it('edit button click should load edit Utilisateur page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Utilisateur');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);
      });

      it('last delete button click should delete instance of Utilisateur', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('utilisateur').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', utilisateurPageUrlPattern);

        utilisateur = undefined;
      });
    });
  });

  describe('new Utilisateur page', () => {
    beforeEach(() => {
      cy.visit(utilisateurPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Utilisateur');
    });

    it('should create an instance of Utilisateur', () => {
      cy.get(`[data-cy="login"]`).type('derechef adversaire relire');
      cy.get(`[data-cy="login"]`).should('have.value', 'derechef adversaire relire');

      cy.get(`[data-cy="email"]`).type('xhj_m@uvH.P');
      cy.get(`[data-cy="email"]`).should('have.value', 'xhj_m@uvH.P');

      cy.get(`[data-cy="motDePasse"]`).type('tendre accentuer');
      cy.get(`[data-cy="motDePasse"]`).should('have.value', 'tendre accentuer');

      cy.get(`[data-cy="prenom"]`).type('chut rudement snif');
      cy.get(`[data-cy="prenom"]`).should('have.value', 'chut rudement snif');

      cy.get(`[data-cy="nom"]`).type('vers');
      cy.get(`[data-cy="nom"]`).should('have.value', 'vers');

      cy.get(`[data-cy="telephone"]`).type('+33 495201422');
      cy.get(`[data-cy="telephone"]`).should('have.value', '+33 495201422');

      cy.get(`[data-cy="role"]`).select('AUTORITE_LOCALE');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(`[data-cy="dateInscription"]`).type('2026-04-23T07:55');
      cy.get(`[data-cy="dateInscription"]`).blur();
      cy.get(`[data-cy="dateInscription"]`).should('have.value', '2026-04-23T07:55');

      cy.get(`[data-cy="dateBannissement"]`).type('2026-04-22T14:44');
      cy.get(`[data-cy="dateBannissement"]`).blur();
      cy.get(`[data-cy="dateBannissement"]`).should('have.value', '2026-04-22T14:44');

      cy.get(`[data-cy="estBanni"]`).should('not.be.checked');
      cy.get(`[data-cy="estBanni"]`).click();
      cy.get(`[data-cy="estBanni"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        utilisateur = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', utilisateurPageUrlPattern);
    });
  });
});
