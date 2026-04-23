import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ISalon, NewSalon } from '../salon.model';

export type PartialUpdateSalon = Partial<ISalon> & Pick<ISalon, 'id'>;

type RestOf<T extends ISalon | NewSalon> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestSalon = RestOf<ISalon>;

export type NewRestSalon = RestOf<NewSalon>;

export type PartialUpdateRestSalon = RestOf<PartialUpdateSalon>;

@Injectable()
export class SalonsService {
  readonly salonsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly salonsResource = httpResource<RestSalon[]>(() => {
    const params = this.salonsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of salon that have been fetched. It is updated when the salonsResource emits a new value.
   * In case of error while fetching the salons, the signal is set to an empty array.
   */
  readonly salons = computed(() =>
    (this.salonsResource.hasValue() ? this.salonsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/salons');

  protected convertValueFromServer(restSalon: RestSalon): ISalon {
    return {
      ...restSalon,
      dateCreation: restSalon.dateCreation ? dayjs(restSalon.dateCreation) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class SalonService extends SalonsService {
  protected readonly http = inject(HttpClient);

  create(salon: NewSalon): Observable<ISalon> {
    const copy = this.convertValueFromClient(salon);
    return this.http.post<RestSalon>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(salon: ISalon): Observable<ISalon> {
    const copy = this.convertValueFromClient(salon);
    return this.http
      .put<RestSalon>(`${this.resourceUrl}/${encodeURIComponent(this.getSalonIdentifier(salon))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(salon: PartialUpdateSalon): Observable<ISalon> {
    const copy = this.convertValueFromClient(salon);
    return this.http
      .patch<RestSalon>(`${this.resourceUrl}/${encodeURIComponent(this.getSalonIdentifier(salon))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ISalon> {
    return this.http.get<RestSalon>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ISalon[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSalon[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getSalonIdentifier(salon: Pick<ISalon, 'id'>): number {
    return salon.id;
  }

  compareSalon(o1: Pick<ISalon, 'id'> | null, o2: Pick<ISalon, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalonIdentifier(o1) === this.getSalonIdentifier(o2) : o1 === o2;
  }

  addSalonToCollectionIfMissing<Type extends Pick<ISalon, 'id'>>(
    salonCollection: Type[],
    ...salonsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salons: Type[] = salonsToCheck.filter(isPresent);
    if (salons.length > 0) {
      const salonCollectionIdentifiers = salonCollection.map(salonItem => this.getSalonIdentifier(salonItem));
      const salonsToAdd = salons.filter(salonItem => {
        const salonIdentifier = this.getSalonIdentifier(salonItem);
        if (salonCollectionIdentifiers.includes(salonIdentifier)) {
          return false;
        }
        salonCollectionIdentifiers.push(salonIdentifier);
        return true;
      });
      return [...salonsToAdd, ...salonCollection];
    }
    return salonCollection;
  }

  protected convertValueFromClient<T extends ISalon | NewSalon | PartialUpdateSalon>(salon: T): RestOf<T> {
    return {
      ...salon,
      dateCreation: salon.dateCreation?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestSalon): ISalon {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestSalon[]): ISalon[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
