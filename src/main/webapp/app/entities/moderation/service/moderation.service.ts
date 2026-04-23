import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IModeration, NewModeration } from '../moderation.model';

export type PartialUpdateModeration = Partial<IModeration> & Pick<IModeration, 'id'>;

type RestOf<T extends IModeration | NewModeration> = Omit<T, 'dateModeration'> & {
  dateModeration?: string | null;
};

export type RestModeration = RestOf<IModeration>;

export type NewRestModeration = RestOf<NewModeration>;

export type PartialUpdateRestModeration = RestOf<PartialUpdateModeration>;

@Injectable()
export class ModerationsService {
  readonly moderationsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly moderationsResource = httpResource<RestModeration[]>(() => {
    const params = this.moderationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of moderation that have been fetched. It is updated when the moderationsResource emits a new value.
   * In case of error while fetching the moderations, the signal is set to an empty array.
   */
  readonly moderations = computed(() =>
    (this.moderationsResource.hasValue() ? this.moderationsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/moderations');

  protected convertValueFromServer(restModeration: RestModeration): IModeration {
    return {
      ...restModeration,
      dateModeration: restModeration.dateModeration ? dayjs(restModeration.dateModeration) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class ModerationService extends ModerationsService {
  protected readonly http = inject(HttpClient);

  create(moderation: NewModeration): Observable<IModeration> {
    const copy = this.convertValueFromClient(moderation);
    return this.http.post<RestModeration>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(moderation: IModeration): Observable<IModeration> {
    const copy = this.convertValueFromClient(moderation);
    return this.http
      .put<RestModeration>(`${this.resourceUrl}/${encodeURIComponent(this.getModerationIdentifier(moderation))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(moderation: PartialUpdateModeration): Observable<IModeration> {
    const copy = this.convertValueFromClient(moderation);
    return this.http
      .patch<RestModeration>(`${this.resourceUrl}/${encodeURIComponent(this.getModerationIdentifier(moderation))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IModeration> {
    return this.http
      .get<RestModeration>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IModeration[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestModeration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getModerationIdentifier(moderation: Pick<IModeration, 'id'>): number {
    return moderation.id;
  }

  compareModeration(o1: Pick<IModeration, 'id'> | null, o2: Pick<IModeration, 'id'> | null): boolean {
    return o1 && o2 ? this.getModerationIdentifier(o1) === this.getModerationIdentifier(o2) : o1 === o2;
  }

  addModerationToCollectionIfMissing<Type extends Pick<IModeration, 'id'>>(
    moderationCollection: Type[],
    ...moderationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const moderations: Type[] = moderationsToCheck.filter(isPresent);
    if (moderations.length > 0) {
      const moderationCollectionIdentifiers = moderationCollection.map(moderationItem => this.getModerationIdentifier(moderationItem));
      const moderationsToAdd = moderations.filter(moderationItem => {
        const moderationIdentifier = this.getModerationIdentifier(moderationItem);
        if (moderationCollectionIdentifiers.includes(moderationIdentifier)) {
          return false;
        }
        moderationCollectionIdentifiers.push(moderationIdentifier);
        return true;
      });
      return [...moderationsToAdd, ...moderationCollection];
    }
    return moderationCollection;
  }

  protected convertValueFromClient<T extends IModeration | NewModeration | PartialUpdateModeration>(moderation: T): RestOf<T> {
    return {
      ...moderation,
      dateModeration: moderation.dateModeration?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestModeration): IModeration {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestModeration[]): IModeration[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
