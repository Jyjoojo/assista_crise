import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { StatutCrise } from 'app/entities/enumerations/statut-crise.model';
import { TypeCrise } from 'app/entities/enumerations/type-crise.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICrise } from '../crise.model';
import { CriseService } from '../service/crise.service';

import { CriseFormGroup, CriseFormService } from './crise-form.service';

@Component({
  selector: 'jhi-crise-update',
  templateUrl: './crise-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CriseUpdate implements OnInit {
  readonly isSaving = signal(false);
  crise: ICrise | null = null;
  typeCriseValues = Object.keys(TypeCrise);
  statutCriseValues = Object.keys(StatutCrise);

  utilisateursSharedCollection = signal<IUtilisateur[]>([]);

  protected criseService = inject(CriseService);
  protected criseFormService = inject(CriseFormService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CriseFormGroup = this.criseFormService.createCriseFormGroup();

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crise }) => {
      this.crise = crise;
      if (crise) {
        this.updateForm(crise);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const crise = this.criseFormService.getCrise(this.editForm);
    if (crise.id === null) {
      this.subscribeToSaveResponse(this.criseService.create(crise));
    } else {
      this.subscribeToSaveResponse(this.criseService.update(crise));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICrise | null>): void {
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

  protected updateForm(crise: ICrise): void {
    this.crise = crise;
    this.criseFormService.resetForm(this.editForm, crise);

    this.utilisateursSharedCollection.update(utilisateurs =>
      this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, crise.declarant),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.crise?.declarant),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => this.utilisateursSharedCollection.set(utilisateurs));
  }
}
