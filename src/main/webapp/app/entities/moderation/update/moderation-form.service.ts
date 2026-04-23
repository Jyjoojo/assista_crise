import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IModeration, NewModeration } from '../moderation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IModeration for edit and NewModerationFormGroupInput for create.
 */
type ModerationFormGroupInput = IModeration | PartialWithRequiredKeyOf<NewModeration>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IModeration | NewModeration> = Omit<T, 'dateModeration'> & {
  dateModeration?: string | null;
};

type ModerationFormRawValue = FormValueOf<IModeration>;

type NewModerationFormRawValue = FormValueOf<NewModeration>;

type ModerationFormDefaults = Pick<NewModeration, 'id' | 'dateModeration'>;

type ModerationFormGroupContent = {
  id: FormControl<ModerationFormRawValue['id'] | NewModeration['id']>;
  motif: FormControl<ModerationFormRawValue['motif']>;
  dateModeration: FormControl<ModerationFormRawValue['dateModeration']>;
  action: FormControl<ModerationFormRawValue['action']>;
  administrateur: FormControl<ModerationFormRawValue['administrateur']>;
  demande: FormControl<ModerationFormRawValue['demande']>;
  offre: FormControl<ModerationFormRawValue['offre']>;
  utilisateurCible: FormControl<ModerationFormRawValue['utilisateurCible']>;
};

export type ModerationFormGroup = FormGroup<ModerationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ModerationFormService {
  createModerationFormGroup(moderation?: ModerationFormGroupInput): ModerationFormGroup {
    const moderationRawValue = this.convertModerationToModerationRawValue({
      ...this.getFormDefaults(),
      ...(moderation ?? { id: null }),
    });
    return new FormGroup<ModerationFormGroupContent>({
      id: new FormControl(
        { value: moderationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      motif: new FormControl(moderationRawValue.motif, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      dateModeration: new FormControl(moderationRawValue.dateModeration, {
        validators: [Validators.required],
      }),
      action: new FormControl(moderationRawValue.action, {
        validators: [Validators.required],
      }),
      administrateur: new FormControl(moderationRawValue.administrateur, {
        validators: [Validators.required],
      }),
      demande: new FormControl(moderationRawValue.demande),
      offre: new FormControl(moderationRawValue.offre),
      utilisateurCible: new FormControl(moderationRawValue.utilisateurCible),
    });
  }

  getModeration(form: ModerationFormGroup): IModeration | NewModeration {
    return this.convertModerationRawValueToModeration(form.getRawValue() as ModerationFormRawValue | NewModerationFormRawValue);
  }

  resetForm(form: ModerationFormGroup, moderation: ModerationFormGroupInput): void {
    const moderationRawValue = this.convertModerationToModerationRawValue({ ...this.getFormDefaults(), ...moderation });
    form.reset({
      ...moderationRawValue,
      id: { value: moderationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ModerationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateModeration: currentTime,
    };
  }

  private convertModerationRawValueToModeration(
    rawModeration: ModerationFormRawValue | NewModerationFormRawValue,
  ): IModeration | NewModeration {
    return {
      ...rawModeration,
      dateModeration: dayjs(rawModeration.dateModeration, DATE_TIME_FORMAT),
    };
  }

  private convertModerationToModerationRawValue(
    moderation: IModeration | (Partial<NewModeration> & ModerationFormDefaults),
  ): ModerationFormRawValue | PartialWithRequiredKeyOf<NewModerationFormRawValue> {
    return {
      ...moderation,
      dateModeration: moderation.dateModeration ? moderation.dateModeration.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
