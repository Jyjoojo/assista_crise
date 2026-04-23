import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../moderation.test-samples';

import { ModerationFormService } from './moderation-form.service';

describe('Moderation Form Service', () => {
  let service: ModerationFormService;

  beforeEach(() => {
    service = TestBed.inject(ModerationFormService);
  });

  describe('Service methods', () => {
    describe('createModerationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createModerationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            motif: expect.any(Object),
            dateModeration: expect.any(Object),
            action: expect.any(Object),
            administrateur: expect.any(Object),
            demande: expect.any(Object),
            offre: expect.any(Object),
            utilisateurCible: expect.any(Object),
          }),
        );
      });

      it('passing IModeration should create a new form with FormGroup', () => {
        const formGroup = service.createModerationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            motif: expect.any(Object),
            dateModeration: expect.any(Object),
            action: expect.any(Object),
            administrateur: expect.any(Object),
            demande: expect.any(Object),
            offre: expect.any(Object),
            utilisateurCible: expect.any(Object),
          }),
        );
      });
    });

    describe('getModeration', () => {
      it('should return NewModeration for default Moderation initial value', () => {
        const formGroup = service.createModerationFormGroup(sampleWithNewData);

        const moderation = service.getModeration(formGroup);

        expect(moderation).toMatchObject(sampleWithNewData);
      });

      it('should return NewModeration for empty Moderation initial value', () => {
        const formGroup = service.createModerationFormGroup();

        const moderation = service.getModeration(formGroup);

        expect(moderation).toMatchObject({});
      });

      it('should return IModeration', () => {
        const formGroup = service.createModerationFormGroup(sampleWithRequiredData);

        const moderation = service.getModeration(formGroup);

        expect(moderation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IModeration should not enable id FormControl', () => {
        const formGroup = service.createModerationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewModeration should disable id FormControl', () => {
        const formGroup = service.createModerationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
