import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISalon } from 'app/entities/salon/salon.model';
import { SalonService } from 'app/entities/salon/service/salon.service';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IMessage } from '../message.model';
import { MessageService } from '../service/message.service';

import { MessageFormGroup, MessageFormService } from './message-form.service';

@Component({
  selector: 'jhi-message-update',
  templateUrl: './message-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class MessageUpdate implements OnInit {
  readonly isSaving = signal(false);
  message: IMessage | null = null;

  salonsSharedCollection = signal<ISalon[]>([]);
  utilisateursSharedCollection = signal<IUtilisateur[]>([]);

  protected messageService = inject(MessageService);
  protected messageFormService = inject(MessageFormService);
  protected salonService = inject(SalonService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MessageFormGroup = this.messageFormService.createMessageFormGroup();

  compareSalon = (o1: ISalon | null, o2: ISalon | null): boolean => this.salonService.compareSalon(o1, o2);

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ message }) => {
      this.message = message;
      if (message) {
        this.updateForm(message);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const message = this.messageFormService.getMessage(this.editForm);
    if (message.id === null) {
      this.subscribeToSaveResponse(this.messageService.create(message));
    } else {
      this.subscribeToSaveResponse(this.messageService.update(message));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IMessage | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(message: IMessage): void {
    this.message = message;
    this.messageFormService.resetForm(this.editForm, message);

    this.salonsSharedCollection.update(salons => this.salonService.addSalonToCollectionIfMissing<ISalon>(salons, message.salon));
    this.utilisateursSharedCollection.update(utilisateurs =>
      this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, message.auteur),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.salonService
      .query()
      .pipe(map((res: HttpResponse<ISalon[]>) => res.body ?? []))
      .pipe(map((salons: ISalon[]) => this.salonService.addSalonToCollectionIfMissing<ISalon>(salons, this.message?.salon)))
      .subscribe((salons: ISalon[]) => this.salonsSharedCollection.set(salons));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.message?.auteur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => this.utilisateursSharedCollection.set(utilisateurs));
  }
}
