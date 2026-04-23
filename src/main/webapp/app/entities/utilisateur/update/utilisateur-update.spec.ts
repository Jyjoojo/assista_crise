import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ISalon } from 'app/entities/salon/salon.model';
import { SalonService } from 'app/entities/salon/service/salon.service';
import { UtilisateurService } from '../service/utilisateur.service';
import { IUtilisateur } from '../utilisateur.model';

import { UtilisateurFormService } from './utilisateur-form.service';
import { UtilisateurUpdate } from './utilisateur-update';

describe('Utilisateur Management Update Component', () => {
  let comp: UtilisateurUpdate;
  let fixture: ComponentFixture<UtilisateurUpdate>;
  let activatedRoute: ActivatedRoute;
  let utilisateurFormService: UtilisateurFormService;
  let utilisateurService: UtilisateurService;
  let salonService: SalonService;

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

    fixture = TestBed.createComponent(UtilisateurUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utilisateurFormService = TestBed.inject(UtilisateurFormService);
    utilisateurService = TestBed.inject(UtilisateurService);
    salonService = TestBed.inject(SalonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Salon query and add missing value', () => {
      const utilisateur: IUtilisateur = { id: 31928 };
      const salons: ISalon[] = [{ id: 4107 }];
      utilisateur.salons = salons;

      const salonCollection: ISalon[] = [{ id: 4107 }];
      vitest.spyOn(salonService, 'query').mockReturnValue(of(new HttpResponse({ body: salonCollection })));
      const additionalSalons = [...salons];
      const expectedCollection: ISalon[] = [...additionalSalons, ...salonCollection];
      vitest.spyOn(salonService, 'addSalonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      expect(salonService.query).toHaveBeenCalled();
      expect(salonService.addSalonToCollectionIfMissing).toHaveBeenCalledWith(
        salonCollection,
        ...additionalSalons.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.salonsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const utilisateur: IUtilisateur = { id: 31928 };
      const salon: ISalon = { id: 4107 };
      utilisateur.salons = [salon];

      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      expect(comp.salonsSharedCollection()).toContainEqual(salon);
      expect(comp.utilisateur).toEqual(utilisateur);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUtilisateur>();
      const utilisateur = { id: 2179 };
      vitest.spyOn(utilisateurFormService, 'getUtilisateur').mockReturnValue(utilisateur);
      vitest.spyOn(utilisateurService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(utilisateur);
      saveSubject.complete();

      // THEN
      expect(utilisateurFormService.getUtilisateur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(utilisateurService.update).toHaveBeenCalledWith(expect.objectContaining(utilisateur));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IUtilisateur>();
      const utilisateur = { id: 2179 };
      vitest.spyOn(utilisateurFormService, 'getUtilisateur').mockReturnValue({ id: null });
      vitest.spyOn(utilisateurService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(utilisateur);
      saveSubject.complete();

      // THEN
      expect(utilisateurFormService.getUtilisateur).toHaveBeenCalled();
      expect(utilisateurService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IUtilisateur>();
      const utilisateur = { id: 2179 };
      vitest.spyOn(utilisateurService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilisateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utilisateurService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSalon', () => {
      it('should forward to salonService', () => {
        const entity = { id: 4107 };
        const entity2 = { id: 31318 };
        vitest.spyOn(salonService, 'compareSalon');
        comp.compareSalon(entity, entity2);
        expect(salonService.compareSalon).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
