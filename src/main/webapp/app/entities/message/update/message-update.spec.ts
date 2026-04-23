import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ISalon } from 'app/entities/salon/salon.model';
import { SalonService } from 'app/entities/salon/service/salon.service';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { IMessage } from '../message.model';
import { MessageService } from '../service/message.service';

import { MessageFormService } from './message-form.service';
import { MessageUpdate } from './message-update';

describe('Message Management Update Component', () => {
  let comp: MessageUpdate;
  let fixture: ComponentFixture<MessageUpdate>;
  let activatedRoute: ActivatedRoute;
  let messageFormService: MessageFormService;
  let messageService: MessageService;
  let salonService: SalonService;
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

    fixture = TestBed.createComponent(MessageUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    messageFormService = TestBed.inject(MessageFormService);
    messageService = TestBed.inject(MessageService);
    salonService = TestBed.inject(SalonService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Salon query and add missing value', () => {
      const message: IMessage = { id: 11110 };
      const salon: ISalon = { id: 4107 };
      message.salon = salon;

      const salonCollection: ISalon[] = [{ id: 4107 }];
      vitest.spyOn(salonService, 'query').mockReturnValue(of(new HttpResponse({ body: salonCollection })));
      const additionalSalons = [salon];
      const expectedCollection: ISalon[] = [...additionalSalons, ...salonCollection];
      vitest.spyOn(salonService, 'addSalonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(salonService.query).toHaveBeenCalled();
      expect(salonService.addSalonToCollectionIfMissing).toHaveBeenCalledWith(
        salonCollection,
        ...additionalSalons.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.salonsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Utilisateur query and add missing value', () => {
      const message: IMessage = { id: 11110 };
      const auteur: IUtilisateur = { id: 2179 };
      message.auteur = auteur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 2179 }];
      vitest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [auteur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      vitest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(
        utilisateurCollection,
        ...additionalUtilisateurs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.utilisateursSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const message: IMessage = { id: 11110 };
      const salon: ISalon = { id: 4107 };
      message.salon = salon;
      const auteur: IUtilisateur = { id: 2179 };
      message.auteur = auteur;

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(comp.salonsSharedCollection()).toContainEqual(salon);
      expect(comp.utilisateursSharedCollection()).toContainEqual(auteur);
      expect(comp.message).toEqual(message);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMessage>();
      const message = { id: 6456 };
      vitest.spyOn(messageFormService, 'getMessage').mockReturnValue(message);
      vitest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(message);
      saveSubject.complete();

      // THEN
      expect(messageFormService.getMessage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(messageService.update).toHaveBeenCalledWith(expect.objectContaining(message));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMessage>();
      const message = { id: 6456 };
      vitest.spyOn(messageFormService, 'getMessage').mockReturnValue({ id: null });
      vitest.spyOn(messageService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(message);
      saveSubject.complete();

      // THEN
      expect(messageFormService.getMessage).toHaveBeenCalled();
      expect(messageService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IMessage>();
      const message = { id: 6456 };
      vitest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(messageService.update).toHaveBeenCalled();
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
