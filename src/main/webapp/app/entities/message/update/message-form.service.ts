import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMessage, NewMessage } from '../message.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMessage for edit and NewMessageFormGroupInput for create.
 */
type MessageFormGroupInput = IMessage | PartialWithRequiredKeyOf<NewMessage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMessage | NewMessage> = Omit<T, 'dateEnvoi'> & {
  dateEnvoi?: string | null;
};

type MessageFormRawValue = FormValueOf<IMessage>;

type NewMessageFormRawValue = FormValueOf<NewMessage>;

type MessageFormDefaults = Pick<NewMessage, 'id' | 'dateEnvoi' | 'estModere'>;

type MessageFormGroupContent = {
  id: FormControl<MessageFormRawValue['id'] | NewMessage['id']>;
  contenu: FormControl<MessageFormRawValue['contenu']>;
  dateEnvoi: FormControl<MessageFormRawValue['dateEnvoi']>;
  estModere: FormControl<MessageFormRawValue['estModere']>;
  salon: FormControl<MessageFormRawValue['salon']>;
  auteur: FormControl<MessageFormRawValue['auteur']>;
};

export type MessageFormGroup = FormGroup<MessageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MessageFormService {
  createMessageFormGroup(message?: MessageFormGroupInput): MessageFormGroup {
    const messageRawValue = this.convertMessageToMessageRawValue({
      ...this.getFormDefaults(),
      ...(message ?? { id: null }),
    });
    return new FormGroup<MessageFormGroupContent>({
      id: new FormControl(
        { value: messageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      contenu: new FormControl(messageRawValue.contenu, {
        validators: [Validators.required, Validators.maxLength(2000)],
      }),
      dateEnvoi: new FormControl(messageRawValue.dateEnvoi, {
        validators: [Validators.required],
      }),
      estModere: new FormControl(messageRawValue.estModere, {
        validators: [Validators.required],
      }),
      salon: new FormControl(messageRawValue.salon, {
        validators: [Validators.required],
      }),
      auteur: new FormControl(messageRawValue.auteur, {
        validators: [Validators.required],
      }),
    });
  }

  getMessage(form: MessageFormGroup): IMessage | NewMessage {
    return this.convertMessageRawValueToMessage(form.getRawValue() as MessageFormRawValue | NewMessageFormRawValue);
  }

  resetForm(form: MessageFormGroup, message: MessageFormGroupInput): void {
    const messageRawValue = this.convertMessageToMessageRawValue({ ...this.getFormDefaults(), ...message });
    form.reset({
      ...messageRawValue,
      id: { value: messageRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): MessageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateEnvoi: currentTime,
      estModere: false,
    };
  }

  private convertMessageRawValueToMessage(rawMessage: MessageFormRawValue | NewMessageFormRawValue): IMessage | NewMessage {
    return {
      ...rawMessage,
      dateEnvoi: dayjs(rawMessage.dateEnvoi, DATE_TIME_FORMAT),
    };
  }

  private convertMessageToMessageRawValue(
    message: IMessage | (Partial<NewMessage> & MessageFormDefaults),
  ): MessageFormRawValue | PartialWithRequiredKeyOf<NewMessageFormRawValue> {
    return {
      ...message,
      dateEnvoi: message.dateEnvoi ? message.dateEnvoi.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
