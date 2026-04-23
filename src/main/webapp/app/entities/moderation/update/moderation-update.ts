import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { ActionModeration } from 'app/entities/enumerations/action-moderation.model';
import { IOffre } from 'app/entities/offre/offre.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { IModeration } from '../moderation.model';
import { ModerationService } from '../service/moderation.service';

import { ModerationFormGroup, ModerationFormService } from './moderation-form.service';
import { OffreService } from 'app/entities/offre/service/offre.service';

@Component({
  selector: 'jhi-moderation-update',
  templateUrl: './moderation-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ModerationUpdate implements OnInit {
  readonly isSaving = signal(false);
  moderation: IModeration | null = null;
  actionModerationValues = Object.keys(ActionModeration);

  utilisateursSharedCollection = signal<IUtilisateur[]>([]);
  demandesSharedCollection = signal<IDemande[]>([]);
  offresSharedCollection = signal<IOffre[]>([]);

  protected moderationService = inject(ModerationService);
  protected moderationFormService = inject(ModerationFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected demandeService = inject(DemandeService);
  protected offreService = inject(OffreService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ModerationFormGroup = this.moderationFormService.createModerationFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  compareOffre = (o1: IOffre | null, o2: IOffre | null): boolean => this.offreService.compareOffre(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moderation }) => {
      this.moderation = moderation;
      if (moderation) {
        this.updateForm(moderation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const moderation = this.moderationFormService.getModeration(this.editForm);
    if (moderation.id === null) {
      this.subscribeToSaveResponse(this.moderationService.create(moderation));
    } else {
      this.subscribeToSaveResponse(this.moderationService.update(moderation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IModeration | null>): void {
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

  protected updateForm(moderation: IModeration): void {
    this.moderation = moderation;
    this.moderationFormService.resetForm(this.editForm, moderation);

    this.utilisateursSharedCollection.update(utilisateurs =>
      this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
        utilisateurs,
        moderation.administrateur,
        moderation.utilisateurCible,
      ),
    );
    this.demandesSharedCollection.update(demandes =>
      this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, moderation.demande),
    );
    this.offresSharedCollection.update(offres => this.offreService.addOffreToCollectionIfMissing<IOffre>(offres, moderation.offre));
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(
            utilisateurs,
            this.moderation?.administrateur,
            this.moderation?.utilisateurCible,
          ),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => this.utilisateursSharedCollection.set(utilisateurs));

    this.demandeService
      .query()
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(
        map((demandes: IDemande[]) => this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.moderation?.demande)),
      )
      .subscribe((demandes: IDemande[]) => this.demandesSharedCollection.set(demandes));

    this.offreService
      .query()
      .pipe(map((res: HttpResponse<IOffre[]>) => res.body ?? []))
      .pipe(map((offres: IOffre[]) => this.offreService.addOffreToCollectionIfMissing<IOffre>(offres, this.moderation?.offre)))
      .subscribe((offres: IOffre[]) => this.offresSharedCollection.set(offres));
  }
}
