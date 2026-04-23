import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICrise } from 'app/entities/crise/crise.model';
import { CriseService } from 'app/entities/crise/service/crise.service';
import { IDemande } from 'app/entities/demande/demande.model';
import { DemandeService } from 'app/entities/demande/service/demande.service';
import { StatutOffre } from 'app/entities/enumerations/statut-offre.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IOffre } from '../offre.model';
import { OffreService } from '../service/offre.service';

import { OffreFormGroup, OffreFormService } from './offre-form.service';

@Component({
  selector: 'jhi-offre-update',
  templateUrl: './offre-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OffreUpdate implements OnInit {
  readonly isSaving = signal(false);
  offre: IOffre | null = null;
  statutOffreValues = Object.keys(StatutOffre);

  crisesSharedCollection = signal<ICrise[]>([]);
  utilisateursSharedCollection = signal<IUtilisateur[]>([]);
  demandesSharedCollection = signal<IDemande[]>([]);

  protected offreService = inject(OffreService);
  protected offreFormService = inject(OffreFormService);
  protected criseService = inject(CriseService);
  protected utilisateurService = inject(UtilisateurService);
  protected demandeService = inject(DemandeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OffreFormGroup = this.offreFormService.createOffreFormGroup();

  compareCrise = (o1: ICrise | null, o2: ICrise | null): boolean => this.criseService.compareCrise(o1, o2);

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offre }) => {
      this.offre = offre;
      if (offre) {
        this.updateForm(offre);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const offre = this.offreFormService.getOffre(this.editForm);
    if (offre.id === null) {
      this.subscribeToSaveResponse(this.offreService.create(offre));
    } else {
      this.subscribeToSaveResponse(this.offreService.update(offre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOffre | null>): void {
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

  protected updateForm(offre: IOffre): void {
    this.offre = offre;
    this.offreFormService.resetForm(this.editForm, offre);

    this.crisesSharedCollection.update(crises => this.criseService.addCriseToCollectionIfMissing<ICrise>(crises, offre.crise));
    this.utilisateursSharedCollection.update(utilisateurs =>
      this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, offre.aidant),
    );
    this.demandesSharedCollection.update(demandes =>
      this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, ...(offre.demandes ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.criseService
      .query()
      .pipe(map((res: HttpResponse<ICrise[]>) => res.body ?? []))
      .pipe(map((crises: ICrise[]) => this.criseService.addCriseToCollectionIfMissing<ICrise>(crises, this.offre?.crise)))
      .subscribe((crises: ICrise[]) => this.crisesSharedCollection.set(crises));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.offre?.aidant),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => this.utilisateursSharedCollection.set(utilisateurs));

    this.demandeService
      .query()
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(
        map((demandes: IDemande[]) =>
          this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, ...(this.offre?.demandes ?? [])),
        ),
      )
      .subscribe((demandes: IDemande[]) => this.demandesSharedCollection.set(demandes));
  }
}
