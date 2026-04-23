import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../salon.test-samples';

import { SalonFormService } from './salon-form.service';

describe('Salon Form Service', () => {
  let service: SalonFormService;

  beforeEach(() => {
    service = TestBed.inject(SalonFormService);
  });

  describe('Service methods', () => {
    describe('createSalonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateCreation: expect.any(Object),
            demande: expect.any(Object),
            participants: expect.any(Object),
          }),
        );
      });

      it('passing ISalon should create a new form with FormGroup', () => {
        const formGroup = service.createSalonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateCreation: expect.any(Object),
            demande: expect.any(Object),
            participants: expect.any(Object),
          }),
        );
      });
    });

    describe('getSalon', () => {
      it('should return NewSalon for default Salon initial value', () => {
        const formGroup = service.createSalonFormGroup(sampleWithNewData);

        const salon = service.getSalon(formGroup);

        expect(salon).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalon for empty Salon initial value', () => {
        const formGroup = service.createSalonFormGroup();

        const salon = service.getSalon(formGroup);

        expect(salon).toMatchObject({});
      });

      it('should return ISalon', () => {
        const formGroup = service.createSalonFormGroup(sampleWithRequiredData);

        const salon = service.getSalon(formGroup);

        expect(salon).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalon should not enable id FormControl', () => {
        const formGroup = service.createSalonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalon should disable id FormControl', () => {
        const formGroup = service.createSalonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
