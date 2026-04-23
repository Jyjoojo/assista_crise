import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoleUtilisateur } from 'app/entities/enumerations/role-utilisateur.model';
import { ISalon } from 'app/entities/salon/salon.model';
import { SalonService } from 'app/entities/salon/service/salon.service';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { UtilisateurService } from '../service/utilisateur.service';
import { IUtilisateur } from '../utilisateur.model';

import { UtilisateurFormGroup, UtilisateurFormService } from './utilisateur-form.service';

@Component({
  selector: 'jhi-utilisateur-update',
  templateUrl: './utilisateur-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class UtilisateurUpdate implements OnInit {
  readonly isSaving = signal(false);
  utilisateur: IUtilisateur | null = null;
  roleUtilisateurValues = Object.keys(RoleUtilisateur);

  salonsSharedCollection = signal<ISalon[]>([]);

  protected utilisateurService = inject(UtilisateurService);
  protected utilisateurFormService = inject(UtilisateurFormService);
  protected salonService = inject(SalonService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UtilisateurFormGroup = this.utilisateurFormService.createUtilisateurFormGroup();

  compareSalon = (o1: ISalon | null, o2: ISalon | null): boolean => this.salonService.compareSalon(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateur }) => {
      this.utilisateur = utilisateur;
      if (utilisateur) {
        this.updateForm(utilisateur);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const utilisateur = this.utilisateurFormService.getUtilisateur(this.editForm);
    if (utilisateur.id === null) {
      this.subscribeToSaveResponse(this.utilisateurService.create(utilisateur));
    } else {
      this.subscribeToSaveResponse(this.utilisateurService.update(utilisateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IUtilisateur | null>): void {
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

  protected updateForm(utilisateur: IUtilisateur): void {
    this.utilisateur = utilisateur;
    this.utilisateurFormService.resetForm(this.editForm, utilisateur);

    this.salonsSharedCollection.update(salons =>
      this.salonService.addSalonToCollectionIfMissing<ISalon>(salons, ...(utilisateur.salons ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.salonService
      .query()
      .pipe(map((res: HttpResponse<ISalon[]>) => res.body ?? []))
      .pipe(map((salons: ISalon[]) => this.salonService.addSalonToCollectionIfMissing<ISalon>(salons, ...(this.utilisateur?.salons ?? []))))
      .subscribe((salons: ISalon[]) => this.salonsSharedCollection.set(salons));
  }
}
