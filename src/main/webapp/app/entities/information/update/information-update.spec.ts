import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICrise } from 'app/entities/crise/crise.model';
import { CriseService } from 'app/entities/crise/service/crise.service';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { IInformation } from '../information.model';
import { InformationService } from '../service/information.service';

import { InformationFormService } from './information-form.service';
import { InformationUpdate } from './information-update';

describe('Information Management Update Component', () => {
  let comp: InformationUpdate;
  let fixture: ComponentFixture<InformationUpdate>;
  let activatedRoute: ActivatedRoute;
  let informationFormService: InformationFormService;
  let informationService: InformationService;
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

    fixture = TestBed.createComponent(InformationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    informationFormService = TestBed.inject(InformationFormService);
    informationService = TestBed.inject(InformationService);
    criseService = TestBed.inject(CriseService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Crise query and add missing value', () => {
      const information: IInformation = { id: 23476 };
      const crise: ICrise = { id: 11332 };
      information.crise = crise;

      const criseCollection: ICrise[] = [{ id: 11332 }];
      vitest.spyOn(criseService, 'query').mockReturnValue(of(new HttpResponse({ body: criseCollection })));
      const additionalCrises = [crise];
      const expectedCollection: ICrise[] = [...additionalCrises, ...criseCollection];
      vitest.spyOn(criseService, 'addCriseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ information });
      comp.ngOnInit();

      expect(criseService.query).toHaveBeenCalled();
      expect(criseService.addCriseToCollectionIfMissing).toHaveBeenCalledWith(
        criseCollection,
        ...additionalCrises.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.crisesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Utilisateur query and add missing value', () => {
      const information: IInformation = { id: 23476 };
      const auteur: IUtilisateur = { id: 2179 };
      information.auteur = auteur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      vitest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [auteur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      vitest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ information });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.utilisateursSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const information: IInformation = { id: 23476 };
      const crise: ICrise = { id: 11332 };
      information.crise = crise;
      const auteur: IUtilisateur = { id: 2179 };
      information.auteur = auteur;

      activatedRoute.data = of({ information });
      comp.ngOnInit();

      expect(comp.crisesSharedCollection()).toContainEqual(crise);
      expect(comp.utilisateursSharedCollection()).toContainEqual(auteur);
      expect(comp.information).toEqual(information);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInformation>();
      const information = { id: 27708 };
      vitest.spyOn(informationFormService, 'getInformation').mockReturnValue(information);
      vitest.spyOn(informationService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ information });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(information);
      saveSubject.complete();

      // THEN
      expect(informationFormService.getInformation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(informationService.update).toHaveBeenCalledWith(expect.objectContaining(information));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInformation>();
      const information = { id: 27708 };
      vitest.spyOn(informationFormService, 'getInformation').mockReturnValue({ id: null });
      vitest.spyOn(informationService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ information: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(information);
      saveSubject.complete();

      // THEN
      expect(informationFormService.getInformation).toHaveBeenCalled();
      expect(informationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInformation>();
      const information = { id: 27708 };
      vitest.spyOn(informationService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ information });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(informationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCrise', () => {
      it('should forward to criseService', () => {
        const entity = { id: 11332 };
        const entity2 = { id: 22123 };
        vitest.spyOn(criseService, 'compareCrise');
        comp.compareCrise(entity, entity2);
        expect(criseService.compareCrise).toHaveBeenCalledWith(entity, entity2);
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
