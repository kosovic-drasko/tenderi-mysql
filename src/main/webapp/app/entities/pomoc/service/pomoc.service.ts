import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPomoc, getPomocIdentifier } from '../pomoc.model';

export type EntityResponseType = HttpResponse<IPomoc>;
export type EntityArrayResponseType = HttpResponse<IPomoc[]>;

@Injectable({ providedIn: 'root' })
export class PomocService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pomocs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPomoc>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPomoc[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  addPomocToCollectionIfMissing(pomocCollection: IPomoc[], ...pomocsToCheck: (IPomoc | null | undefined)[]): IPomoc[] {
    const pomocs: IPomoc[] = pomocsToCheck.filter(isPresent);
    if (pomocs.length > 0) {
      const pomocCollectionIdentifiers = pomocCollection.map(pomocItem => getPomocIdentifier(pomocItem)!);
      const pomocsToAdd = pomocs.filter(pomocItem => {
        const pomocIdentifier = getPomocIdentifier(pomocItem);
        if (pomocIdentifier == null || pomocCollectionIdentifiers.includes(pomocIdentifier)) {
          return false;
        }
        pomocCollectionIdentifiers.push(pomocIdentifier);
        return true;
      });
      return [...pomocsToAdd, ...pomocCollection];
    }
    return pomocCollection;
  }
}
