import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { IOffre } from 'app/entities/offre/offre.model';
import { OffreService } from 'app/entities/offre/service/offre.service';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { IModeration } from '../moderation.model';
import { ModerationService } from '../service/moderation.service';

import { ModerationFormService } from './moderation-form.service';
import { ModerationUpdate } from './moderation-update';

describe('Moderation Management Update Component', () => {
  let comp: ModerationUpdate;
  let fixture: ComponentFixture<ModerationUpdate>;
  let activatedRoute: ActivatedRoute;
  let moderationFormService: ModerationFormService;
  let moderationService: ModerationService;
  let utilisateurService: UtilisateurService;
  let demandeService: DemandeService;
  let offreService: OffreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ModerationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moderationFormService = TestBed.inject(ModerationFormService);
    moderationService = TestBed.inject(ModerationService);
    utilisateurService = TestBed.inject(UtilisateurService);
    demandeService = TestBed.inject(DemandeService);
    offreService = TestBed.inject(OffreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Utilisateur query and add missing value', () => {
      const moderation: IModeration = { id: 29152 };
      const administrateur: IUtilisateur = { id: 2179 };
      moderation.administrateur = administrateur;
      const utilisateurCible: IUtilisateur = { id: 2179 };
      moderation.utilisateurCible = utilisateurCible;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      vitest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [administrateur, utilisateurCible];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      vitest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moderation });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.utilisateursSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Demande query and add missing value', () => {
      const moderation: IModeration = { id: 29152 };
      const demande: IDemande = { id: 27574 };
      moderation.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 27574 }];
      vitest.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const additionalDemandes = [demande];
      const expectedCollection: IDemande[] = [...additionalDemandes, ...demandeCollection];
      vitest.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moderation });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(
        demandeCollection,
        ...additionalDemandes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.demandesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Offre query and add missing value', () => {
      const moderation: IModeration = { id: 29152 };
      const offre: IOffre = { id: 9345 };
      moderation.offre = offre;

      const offreCollection: IOffre[] = [{ id: 9345 }];
      vitest.spyOn(offreService, 'query').mockReturnValue(of(new HttpResponse({ body: offreCollection })));
      const additionalOffres = [offre];
      const expectedCollection: IOffre[] = [...additionalOffres, ...offreCollection];
      vitest.spyOn(offreService, 'addOffreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ moderation });
      comp.ngOnInit();

      expect(offreService.query).toHaveBeenCalled();
      expect(offreService.addOffreToCollectionIfMissing).toHaveBeenCalledWith(
        offreCollection,
        ...additionalOffres.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.offresSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const moderation: IModeration = { id: 29152 };
      const administrateur: IUtilisateur = { id: 2179 };
      moderation.administrateur = administrateur;
      const utilisateurCible: IUtilisateur = { id: 2179 };
      moderation.utilisateurCible = utilisateurCible;
      const demande: IDemande = { id: 27574 };
      moderation.demande = demande;
      const offre: IOffre = { id: 9345 };
      moderation.offre = offre;

      activatedRoute.data = of({ moderation });
      comp.ngOnInit();

      expect(comp.utilisateursSharedCollection()).toContainEqual(administrateur);
      expect(comp.utilisateursSharedCollection()).toContainEqual(utilisateurCible);
      expect(comp.demandesSharedCollection()).toContainEqual(demande);
      expect(comp.offresSharedCollection()).toContainEqual(offre);
      expect(comp.moderation).toEqual(moderation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IModeration>();
      const moderation = { id: 4073 };
      vitest.spyOn(moderationFormService, 'getModeration').mockReturnValue(moderation);
      vitest.spyOn(moderationService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moderation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(moderation);
      saveSubject.complete();

      // THEN
      expect(moderationFormService.getModeration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moderationService.update).toHaveBeenCalledWith(expect.objectContaining(moderation));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IModeration>();
      const moderation = { id: 4073 };
      vitest.spyOn(moderationFormService, 'getModeration').mockReturnValue({ id: null });
      vitest.spyOn(moderationService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moderation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(moderation);
      saveSubject.complete();

      // THEN
      expect(moderationFormService.getModeration).toHaveBeenCalled();
      expect(moderationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IModeration>();
      const moderation = { id: 4073 };
      vitest.spyOn(moderationService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ moderation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moderationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUtilisateur', () => {
      it('should forward to utilisateurService', () => {
        const entity = { id: 2179 };
        const entity2 = { id: 31928 };
        vitest.spyOn(utilisateurService, 'compareUtilisateur');
        comp.compareUtilisateur(entity, entity2);
        expect(utilisateurService.compareUtilisateur).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDemande', () => {
      it('should forward to demandeService', () => {
        const entity = { id: 27574 };
        const entity2 = { id: 24127 };
        vitest.spyOn(demandeService, 'compareDemande');
        comp.compareDemande(entity, entity2);
        expect(demandeService.compareDemande).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOffre', () => {
      it('should forward to offreService', () => {
        const entity = { id: 9345 };
        const entity2 = { id: 8458 };
        vitest.spyOn(offreService, 'compareOffre');
        comp.compareOffre(entity, entity2);
        expect(offreService.compareOffre).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
