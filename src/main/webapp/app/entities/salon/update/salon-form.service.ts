import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISalon, NewSalon } from '../salon.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalon for edit and NewSalonFormGroupInput for create.
 */
type SalonFormGroupInput = ISalon | PartialWithRequiredKeyOf<NewSalon>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISalon | NewSalon> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type SalonFormRawValue = FormValueOf<ISalon>;

type NewSalonFormRawValue = FormValueOf<NewSalon>;

type SalonFormDefaults = Pick<NewSalon, 'id' | 'dateCreation' | 'participants'>;

type SalonFormGroupContent = {
  id: FormControl<SalonFormRawValue['id'] | NewSalon['id']>;
  dateCreation: FormControl<SalonFormRawValue['dateCreation']>;
  demande: FormControl<SalonFormRawValue['demande']>;
  participants: FormControl<SalonFormRawValue['participants']>;
};

export type SalonFormGroup = FormGroup<SalonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SalonFormService {
  createSalonFormGroup(salon?: SalonFormGroupInput): SalonFormGroup {
    const salonRawValue = this.convertSalonToSalonRawValue({
      ...this.getFormDefaults(),
      ...(salon ?? { id: null }),
    });
    return new FormGroup<SalonFormGroupContent>({
      id: new FormControl(
        { value: salonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateCreation: new FormControl(salonRawValue.dateCreation, {
        validators: [Validators.required],
      }),
      demande: new FormControl(salonRawValue.demande, {
        validators: [Validators.required],
      }),
      participants: new FormControl(salonRawValue.participants ?? []),
    });
  }

  getSalon(form: SalonFormGroup): ISalon | NewSalon {
    return this.convertSalonRawValueToSalon(form.getRawValue() as SalonFormRawValue | NewSalonFormRawValue);
  }

  resetForm(form: SalonFormGroup, salon: SalonFormGroupInput): void {
    const salonRawValue = this.convertSalonToSalonRawValue({ ...this.getFormDefaults(), ...salon });
    form.reset({
      ...salonRawValue,
      id: { value: salonRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SalonFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
      participants: [],
    };
  }

  private convertSalonRawValueToSalon(rawSalon: SalonFormRawValue | NewSalonFormRawValue): ISalon | NewSalon {
    return {
      ...rawSalon,
      dateCreation: dayjs(rawSalon.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertSalonToSalonRawValue(
    salon: ISalon | (Partial<NewSalon> & SalonFormDefaults),
  ): SalonFormRawValue | PartialWithRequiredKeyOf<NewSalonFormRawValue> {
    return {
      ...salon,
      dateCreation: salon.dateCreation ? salon.dateCreation.format(DATE_TIME_FORMAT) : undefined,
      participants: salon.participants ?? [],
    };
  }
}
