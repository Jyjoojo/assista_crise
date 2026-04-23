import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ICrise } from '../crise.model';
import { CriseService } from '../service/crise.service';

import { CriseFormService } from './crise-form.service';
import { CriseUpdate } from './crise-update';

describe('Crise Management Update Component', () => {
  let comp: CriseUpdate;
  let fixture: ComponentFixture<CriseUpdate>;
  let activatedRoute: ActivatedRoute;
  let criseFormService: CriseFormService;
  let criseService: CriseService;
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

    fixture = TestBed.createComponent(CriseUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    criseFormService = TestBed.inject(CriseFormService);
    criseService = TestBed.inject(CriseService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Utilisateur query and add missing value', () => {
      const crise: ICrise = { id: 22123 };
      const declarant: IUtilisateur = { id: 2179 };
      crise.declarant = declarant;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      vitest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [declarant];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      vitest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ crise });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.utilisateursSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const crise: ICrise = { id: 22123 };
      const declarant: IUtilisateur = { id: 2179 };
      crise.declarant = declarant;

      activatedRoute.data = of({ crise });
      comp.ngOnInit();

      expect(comp.utilisateursSharedCollection()).toContainEqual(declarant);
      expect(comp.crise).toEqual(crise);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICrise>();
      const crise = { id: 11332 };
      vitest.spyOn(criseFormService, 'getCrise').mockReturnValue(crise);
      vitest.spyOn(criseService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(crise);
      saveSubject.complete();

      // THEN
      expect(criseFormService.getCrise).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(criseService.update).toHaveBeenCalledWith(expect.objectContaining(crise));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICrise>();
      const crise = { id: 11332 };
      vitest.spyOn(criseFormService, 'getCrise').mockReturnValue({ id: null });
      vitest.spyOn(criseService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crise: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(crise);
      saveSubject.complete();

      // THEN
      expect(criseFormService.getCrise).toHaveBeenCalled();
      expect(criseService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICrise>();
      const crise = { id: 11332 };
      vitest.spyOn(criseService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crise });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(criseService.update).toHaveBeenCalled();
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
  });
});
