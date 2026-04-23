import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

export type PartialUpdateUtilisateur = Partial<IUtilisateur> & Pick<IUtilisateur, 'id'>;

type RestOf<T extends IUtilisateur | NewUtilisateur> = Omit<T, 'dateInscription' | 'dateBannissement'> & {
  dateInscription?: string | null;
  dateBannissement?: string | null;
};

export type RestUtilisateur = RestOf<IUtilisateur>;

export type NewRestUtilisateur = RestOf<NewUtilisateur>;

export type PartialUpdateRestUtilisateur = RestOf<PartialUpdateUtilisateur>;

@Injectable()
export class UtilisateursService {
  readonly utilisateursParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly utilisateursResource = httpResource<RestUtilisateur[]>(() => {
    const params = this.utilisateursParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of utilisateur that have been fetched. It is updated when the utilisateursResource emits a new value.
   * In case of error while fetching the utilisateurs, the signal is set to an empty array.
   */
  readonly utilisateurs = computed(() =>
    (this.utilisateursResource.hasValue() ? this.utilisateursResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/utilisateurs');

  protected convertValueFromServer(restUtilisateur: RestUtilisateur): IUtilisateur {
    return {
      ...restUtilisateur,
      dateInscription: restUtilisateur.dateInscription ? dayjs(restUtilisateur.dateInscription) : undefined,
      dateBannissement: restUtilisateur.dateBannissement ? dayjs(restUtilisateur.dateBannissement) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class UtilisateurService extends UtilisateursService {
  protected readonly http = inject(HttpClient);

  create(utilisateur: NewUtilisateur): Observable<IUtilisateur> {
    const copy = this.convertValueFromClient(utilisateur);
    return this.http.post<RestUtilisateur>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(utilisateur: IUtilisateur): Observable<IUtilisateur> {
    const copy = this.convertValueFromClient(utilisateur);
    return this.http
      .put<RestUtilisateur>(`${this.resourceUrl}/${encodeURIComponent(this.getUtilisateurIdentifier(utilisateur))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(utilisateur: PartialUpdateUtilisateur): Observable<IUtilisateur> {
    const copy = this.convertValueFromClient(utilisateur);
    return this.http
      .patch<RestUtilisateur>(`${this.resourceUrl}/${encodeURIComponent(this.getUtilisateurIdentifier(utilisateur))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IUtilisateur> {
    return this.http
      .get<RestUtilisateur>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IUtilisateur[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUtilisateur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getUtilisateurIdentifier(utilisateur: Pick<IUtilisateur, 'id'>): number {
    return utilisateur.id;
  }

  compareUtilisateur(o1: Pick<IUtilisateur, 'id'> | null, o2: Pick<IUtilisateur, 'id'> | null): boolean {
    return o1 && o2 ? this.getUtilisateurIdentifier(o1) === this.getUtilisateurIdentifier(o2) : o1 === o2;
  }

  addUtilisateurToCollectionIfMissing<Type extends Pick<IUtilisateur, 'id'>>(
    utilisateurCollection: Type[],
    ...utilisateursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const utilisateurs: Type[] = utilisateursToCheck.filter(isPresent);
    if (utilisateurs.length > 0) {
      const utilisateurCollectionIdentifiers = utilisateurCollection.map(utilisateurItem => this.getUtilisateurIdentifier(utilisateurItem));
      const utilisateursToAdd = utilisateurs.filter(utilisateurItem => {
        const utilisateurIdentifier = this.getUtilisateurIdentifier(utilisateurItem);
        if (utilisateurCollectionIdentifiers.includes(utilisateurIdentifier)) {
          return false;
        }
        utilisateurCollectionIdentifiers.push(utilisateurIdentifier);
        return true;
      });
      return [...utilisateursToAdd, ...utilisateurCollection];
    }
    return utilisateurCollection;
  }

  protected convertValueFromClient<T extends IUtilisateur | NewUtilisateur | PartialUpdateUtilisateur>(utilisateur: T): RestOf<T> {
    return {
      ...utilisateur,
      dateInscription: utilisateur.dateInscription?.toJSON() ?? null,
      dateBannissement: utilisateur.dateBannissement?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestUtilisateur): IUtilisateur {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestUtilisateur[]): IUtilisateur[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
