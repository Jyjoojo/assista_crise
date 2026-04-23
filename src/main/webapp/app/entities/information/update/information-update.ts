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
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IInformation } from '../information.model';
import { InformationService } from '../service/information.service';

import { InformationFormGroup, InformationFormService } from './information-form.service';

@Component({
  selector: 'jhi-information-update',
  templateUrl: './information-update.html',
  imports: [TranslateDirective, TranslateModule, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InformationUpdate implements OnInit {
  readonly isSaving = signal(false);
  information: IInformation | null = null;

  crisesSharedCollection = signal<ICrise[]>([]);
  utilisateursSharedCollection = signal<IUtilisateur[]>([]);

  protected informationService = inject(InformationService);
  protected informationFormService = inject(InformationFormService);
  protected criseService = inject(CriseService);
  protected utilisateurService = inject(UtilisateurService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InformationFormGroup = this.informationFormService.createInformationFormGroup();

  compareCrise = (o1: ICrise | null, o2: ICrise | null): boolean => this.criseService.compareCrise(o1, o2);

  compareUtilisateur = (o1: IUtilisateur | null, o2: IUtilisateur | null): boolean => this.utilisateurService.compareUtilisateur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ information }) => {
      this.information = information;
      if (information) {
        this.updateForm(information);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const information = this.informationFormService.getInformation(this.editForm);
    if (information.id === null) {
      this.subscribeToSaveResponse(this.informationService.create(information));
    } else {
      this.subscribeToSaveResponse(this.informationService.update(information));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInformation | null>): void {
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

  protected updateForm(information: IInformation): void {
    this.information = information;
    this.informationFormService.resetForm(this.editForm, information);

    this.crisesSharedCollection.update(crises => this.criseService.addCriseToCollectionIfMissing<ICrise>(crises, information.crise));
    this.utilisateursSharedCollection.update(utilisateurs =>
      this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, information.auteur),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.criseService
      .query()
      .pipe(map((res: HttpResponse<ICrise[]>) => res.body ?? []))
      .pipe(map((crises: ICrise[]) => this.criseService.addCriseToCollectionIfMissing<ICrise>(crises, this.information?.crise)))
      .subscribe((crises: ICrise[]) => this.crisesSharedCollection.set(crises));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing<IUtilisateur>(utilisateurs, this.information?.auteur),
        ),
      )
      .subscribe((utilisateurs: IUtilisateur[]) => this.utilisateursSharedCollection.set(utilisateurs));
  }
}
