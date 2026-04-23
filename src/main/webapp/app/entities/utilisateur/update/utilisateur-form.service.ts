import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisateur for edit and NewUtilisateurFormGroupInput for create.
 */
type UtilisateurFormGroupInput = IUtilisateur | PartialWithRequiredKeyOf<NewUtilisateur>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUtilisateur | NewUtilisateur> = Omit<T, 'dateInscription' | 'dateBannissement'> & {
  dateInscription?: string | null;
  dateBannissement?: string | null;
};

type UtilisateurFormRawValue = FormValueOf<IUtilisateur>;

type NewUtilisateurFormRawValue = FormValueOf<NewUtilisateur>;

type UtilisateurFormDefaults = Pick<NewUtilisateur, 'id' | 'actif' | 'dateInscription' | 'dateBannissement' | 'estBanni' | 'salons'>;

type UtilisateurFormGroupContent = {
  id: FormControl<UtilisateurFormRawValue['id'] | NewUtilisateur['id']>;
  login: FormControl<UtilisateurFormRawValue['login']>;
  email: FormControl<UtilisateurFormRawValue['email']>;
  motDePasse: FormControl<UtilisateurFormRawValue['motDePasse']>;
  prenom: FormControl<UtilisateurFormRawValue['prenom']>;
  nom: FormControl<UtilisateurFormRawValue['nom']>;
  telephone: FormControl<UtilisateurFormRawValue['telephone']>;
  role: FormControl<UtilisateurFormRawValue['role']>;
  actif: FormControl<UtilisateurFormRawValue['actif']>;
  dateInscription: FormControl<UtilisateurFormRawValue['dateInscription']>;
  dateBannissement: FormControl<UtilisateurFormRawValue['dateBannissement']>;
  estBanni: FormControl<UtilisateurFormRawValue['estBanni']>;
  salons: FormControl<UtilisateurFormRawValue['salons']>;
};

export type UtilisateurFormGroup = FormGroup<UtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFormService {
  createUtilisateurFormGroup(utilisateur?: UtilisateurFormGroupInput): UtilisateurFormGroup {
    const utilisateurRawValue = this.convertUtilisateurToUtilisateurRawValue({
      ...this.getFormDefaults(),
      ...(utilisateur ?? { id: null }),
    });
    return new FormGroup<UtilisateurFormGroupContent>({
      id: new FormControl(
        { value: utilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      login: new FormControl(utilisateurRawValue.login, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      email: new FormControl(utilisateurRawValue.email, {
        validators: [
          Validators.required,
          Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$'), // NOSONAR
        ],
      }),
      motDePasse: new FormControl(utilisateurRawValue.motDePasse, {
        validators: [Validators.required, Validators.minLength(8), Validators.maxLength(100)],
      }),
      prenom: new FormControl(utilisateurRawValue.prenom, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      nom: new FormControl(utilisateurRawValue.nom, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      telephone: new FormControl(utilisateurRawValue.telephone, {
        validators: [Validators.maxLength(20)],
      }),
      role: new FormControl(utilisateurRawValue.role, {
        validators: [Validators.required],
      }),
      actif: new FormControl(utilisateurRawValue.actif, {
        validators: [Validators.required],
      }),
      dateInscription: new FormControl(utilisateurRawValue.dateInscription, {
        validators: [Validators.required],
      }),
      dateBannissement: new FormControl(utilisateurRawValue.dateBannissement),
      estBanni: new FormControl(utilisateurRawValue.estBanni, {
        validators: [Validators.required],
      }),
      salons: new FormControl(utilisateurRawValue.salons ?? []),
    });
  }

  getUtilisateur(form: UtilisateurFormGroup): IUtilisateur | NewUtilisateur {
    return this.convertUtilisateurRawValueToUtilisateur(form.getRawValue() as UtilisateurFormRawValue | NewUtilisateurFormRawValue);
  }

  resetForm(form: UtilisateurFormGroup, utilisateur: UtilisateurFormGroupInput): void {
    const utilisateurRawValue = this.convertUtilisateurToUtilisateurRawValue({ ...this.getFormDefaults(), ...utilisateur });
    form.reset({
      ...utilisateurRawValue,
      id: { value: utilisateurRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): UtilisateurFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      actif: false,
      dateInscription: currentTime,
      dateBannissement: currentTime,
      estBanni: false,
      salons: [],
    };
  }

  private convertUtilisateurRawValueToUtilisateur(
    rawUtilisateur: UtilisateurFormRawValue | NewUtilisateurFormRawValue,
  ): IUtilisateur | NewUtilisateur {
    return {
      ...rawUtilisateur,
      dateInscription: dayjs(rawUtilisateur.dateInscription, DATE_TIME_FORMAT),
      dateBannissement: dayjs(rawUtilisateur.dateBannissement, DATE_TIME_FORMAT),
    };
  }

  private convertUtilisateurToUtilisateurRawValue(
    utilisateur: IUtilisateur | (Partial<NewUtilisateur> & UtilisateurFormDefaults),
  ): UtilisateurFormRawValue | PartialWithRequiredKeyOf<NewUtilisateurFormRawValue> {
    return {
      ...utilisateur,
      dateInscription: utilisateur.dateInscription ? utilisateur.dateInscription.format(DATE_TIME_FORMAT) : undefined,
      dateBannissement: utilisateur.dateBannissement ? utilisateur.dateBannissement.format(DATE_TIME_FORMAT) : undefined,
      salons: utilisateur.salons ?? [],
    };
  }
}
