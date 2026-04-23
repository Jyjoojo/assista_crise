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
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ISalon } from '../salon.model';
import { SalonService } from '../service/salon.service';

import { SalonFormGroup, SalonFormService } from './salon-form.service';

@Component({
  selector: 'jhi-salon-update',
  templateUrl: './salon-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class SalonUpdate implements OnInit {
  readonly isSaving = signal(false);
  salon: ISalon | null = null;

  demandesCollection = signal<IDemande[]>([]);
  utilisateursSharedCollection = signal<IUtilisateur[]>([]);

  protected salonService = inject(SalonService);
  protected salonFormService = inject(SalonFormService);
  protected demandeService = inject(DemandeService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SalonFormGroup = this.salonFormService.createSalonFormGroup();

  compareDemande = (o1: IDemande | null, o2: IDemande | null): boolean => this.demandeService.compareDemande(o1, o2);

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salon }) => {
      this.salon = salon;
      if (salon) {
        this.updateForm(salon);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const salon = this.salonFormService.getSalon(this.editForm);
    if (salon.id === null) {
      this.subscribeToSaveResponse(this.salonService.create(salon));
    } else {
      this.subscribeToSaveResponse(this.salonService.update(salon));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ISalon | null>): void {
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

  protected updateForm(salon: ISalon): void {
    this.salon = salon;
    this.salonFormService.resetForm(this.editForm, salon);

    this.demandesCollection.set(this.demandeService.addDemandeToCollectionIfMissing<IDemande>(this.demandesCollection(), salon.demande));
    this.utilisateursSharedCollection.update(utilisateurs =>
      this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, ...(salon.participants ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeService
      .query({ 'salonId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDemande[]>) => res.body ?? []))
      .pipe(map((demandes: IDemande[]) => this.demandeService.addDemandeToCollectionIfMissing<IDemande>(demandes, this.salon?.demande)))
      .subscribe((demandes: IDemande[]) => this.demandesCollection.set(demandes));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, ...(this.salon?.participants ?? [])),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => this.utilisateursSharedCollection.set(utilisateurs));
  }
}
