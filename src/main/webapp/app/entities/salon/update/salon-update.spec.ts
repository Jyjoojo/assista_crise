import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ISalon } from '../salon.model';
import { SalonService } from '../service/salon.service';

import { SalonFormService } from './salon-form.service';
import { SalonUpdate } from './salon-update';

describe('Salon Management Update Component', () => {
  let comp: SalonUpdate;
  let fixture: ComponentFixture<SalonUpdate>;
  let activatedRoute: ActivatedRoute;
  let salonFormService: SalonFormService;
  let salonService: SalonService;
  let demandeService: DemandeService;
  let utilisateurService: UtilisateurService;

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

    fixture = TestBed.createComponent(SalonUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salonFormService = TestBed.inject(SalonFormService);
    salonService = TestBed.inject(SalonService);
    demandeService = TestBed.inject(DemandeService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call demande query and add missing value', () => {
      const salon: ISalon = { id: 31318 };
      const demande: IDemande = { id: 27574 };
      salon.demande = demande;

      const demandeCollection: IDemande[] = [{ id: 27574 }];
      vitest.spyOn(demandeService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeCollection })));
      const expectedCollection: IDemande[] = [demande, ...demandeCollection];
      vitest.spyOn(demandeService, 'addDemandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ salon });
      comp.ngOnInit();

      expect(demandeService.query).toHaveBeenCalled();
      expect(demandeService.addDemandeToCollectionIfMissing).toHaveBeenCalledWith(demandeCollection, demande);
      expect(comp.demandesCollection()).toEqual(expectedCollection);
    });

    it('should call Utilisateur query and add missing value', () => {
      const salon: ISalon = { id: 31318 };
      const participants: IUtilisateur[] = [{ id: 2179 }];
      salon.participants = participants;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      vitest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [...participants];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      vitest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ salon });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.utilisateursSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const salon: ISalon = { id: 31318 };
      const demande: IDemande = { id: 27574 };
      salon.demande = demande;
      const participant: IUtilisateur = { id: 2179 };
      salon.participants = [participant];

      activatedRoute.data = of({ salon });
      comp.ngOnInit();

      expect(comp.demandesCollection()).toContainEqual(demande);
      expect(comp.utilisateursSharedCollection()).toContainEqual(participant);
      expect(comp.salon).toEqual(salon);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISalon>();
      const salon = { id: 4107 };
      vitest.spyOn(salonFormService, 'getSalon').mockReturnValue(salon);
      vitest.spyOn(salonService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(salon);
      saveSubject.complete();

      // THEN
      expect(salonFormService.getSalon).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salonService.update).toHaveBeenCalledWith(expect.objectContaining(salon));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISalon>();
      const salon = { id: 4107 };
      vitest.spyOn(salonFormService, 'getSalon').mockReturnValue({ id: null });
      vitest.spyOn(salonService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salon: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(salon);
      saveSubject.complete();

      // THEN
      expect(salonFormService.getSalon).toHaveBeenCalled();
      expect(salonService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ISalon>();
      const salon = { id: 4107 };
      vitest.spyOn(salonService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salonService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDemande', () => {
      it('should forward to demandeService', () => {
        const entity = { id: 27574 };
        const entity2 = { id: 24127 };
        vitest.spyOn(demandeService, 'compareDemande');
        comp.compareDemande(entity, entity2);
        expect(demandeService.compareDemande).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUtilisateur', () => {
      it('should forward to utilisateurService', () => {
        const entity = { id: 2179 };
        const entity2 = { id: 31928 };
        vitest.spyOn(utilisateurService, 'compareUtilisateur');
        comp.compareUtilisateur(entity, entity2);
        expect(utilisateurService.compareUtilisateur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
