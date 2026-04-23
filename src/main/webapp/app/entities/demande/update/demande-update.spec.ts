import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICrise } from 'app/entities/crise/crise.model';
import { CriseService } from 'app/entities/crise/service/crise.service';
import { IOffre } from 'app/entities/offre/offre.model';
import { OffreService } from 'app/entities/offre/service/offre.service';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { IDemande } from '../demande.model';
import { DemandeService } from '../service/demande.service';

import { DemandeFormService } from './demande-form.service';
import { DemandeUpdate } from './demande-update';

describe('Demande Management Update Component', () => {
  let comp: DemandeUpdate;
  let fixture: ComponentFixture<DemandeUpdate>;
  let activatedRoute: ActivatedRoute;
  let demandeFormService: DemandeFormService;
  let demandeService: DemandeService;
  let utilisateurService: UtilisateurService;
  let criseService: CriseService;
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

    fixture = TestBed.createComponent(DemandeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeFormService = TestBed.inject(DemandeFormService);
    demandeService = TestBed.inject(DemandeService);
    utilisateurService = TestBed.inject(UtilisateurService);
    criseService = TestBed.inject(CriseService);
    offreService = TestBed.inject(OffreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Utilisateur query and add missing value', () => {
      const demande: IDemande = { id: 24127 };
      const sinistre: IUtilisateur = { id: 2179 };
      demande.sinistre = sinistre;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      vitest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [sinistre];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      vitest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.utilisateursSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Crise query and add missing value', () => {
      const demande: IDemande = { id: 24127 };
      const crise: ICrise = { id: 11332 };
      demande.crise = crise;

      const criseCollection: ICrise[] = [{ id: 11332 }];
      vitest.spyOn(criseService, 'query').mockReturnValue(of(new HttpResponse({ body: criseCollection })));
      const additionalCrises = [crise];
      const expectedCollection: ICrise[] = [...additionalCrises, ...criseCollection];
      vitest.spyOn(criseService, 'addCriseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(criseService.query).toHaveBeenCalled();
      expect(criseService.addCriseToCollectionIfMissing).toHaveBeenCalledWith(
        criseCollection,
        ...additionalCrises.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.crisesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Offre query and add missing value', () => {
      const demande: IDemande = { id: 24127 };
      const offres: IOffre[] = [{ id: 9345 }];
      demande.offres = offres;

      const offreCollection: IOffre[] = [{ id: 9345 }];
      vitest.spyOn(offreService, 'query').mockReturnValue(of(new HttpResponse({ body: offreCollection })));
      const additionalOffres = [...offres];
      const expectedCollection: IOffre[] = [...additionalOffres, ...offreCollection];
      vitest.spyOn(offreService, 'addOffreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(offreService.query).toHaveBeenCalled();
      expect(offreService.addOffreToCollectionIfMissing).toHaveBeenCalledWith(
        offreCollection,
        ...additionalOffres.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.offresSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const demande: IDemande = { id: 24127 };
      const sinistre: IUtilisateur = { id: 2179 };
      demande.sinistre = sinistre;
      const crise: ICrise = { id: 11332 };
      demande.crise = crise;
      const offre: IOffre = { id: 9345 };
      demande.offres = [offre];

      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      expect(comp.utilisateursSharedCollection()).toContainEqual(sinistre);
      expect(comp.crisesSharedCollection()).toContainEqual(crise);
      expect(comp.offresSharedCollection()).toContainEqual(offre);
      expect(comp.demande).toEqual(demande);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDemande>();
      const demande = { id: 27574 };
      vitest.spyOn(demandeFormService, 'getDemande').mockReturnValue(demande);
      vitest.spyOn(demandeService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(demande);
      saveSubject.complete();

      // THEN
      expect(demandeFormService.getDemande).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeService.update).toHaveBeenCalledWith(expect.objectContaining(demande));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDemande>();
      const demande = { id: 27574 };
      vitest.spyOn(demandeFormService, 'getDemande').mockReturnValue({ id: null });
      vitest.spyOn(demandeService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(demande);
      saveSubject.complete();

      // THEN
      expect(demandeFormService.getDemande).toHaveBeenCalled();
      expect(demandeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IDemande>();
      const demande = { id: 27574 };
      vitest.spyOn(demandeService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeService.update).toHaveBeenCalled();
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

    describe('compareCrise', () => {
      it('should forward to criseService', () => {
        const entity = { id: 11332 };
        const entity2 = { id: 22123 };
        vitest.spyOn(criseService, 'compareCrise');
        comp.compareCrise(entity, entity2);
        expect(criseService.compareCrise).toHaveBeenCalledWith(entity, entity2);
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
