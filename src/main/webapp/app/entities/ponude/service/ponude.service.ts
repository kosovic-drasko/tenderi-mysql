import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPonude, getPonudeIdentifier } from '../ponude.model';

export type EntityResponseType = HttpResponse<IPonude>;
export type EntityArrayResponseType = HttpResponse<IPonude[]>;

@Injectable({ providedIn: 'root' })
export class PonudeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ponudes');
  public urlUpdateSeleced = this.applicationConfigService.getEndpointFor('api/ponude/update/selected');
  protected resourceUrlPonudePonudjaci = this.applicationConfigService.getEndpointFor('api/ponude-ponudjaci');
  // protected resourceUrlPonudePostupci = this.applicationConfigService.getEndpointFor('api/ponude-postupci');
  // protected resourceUrlPostupciSifra = this.applicationConfigService.getEndpointFor('api/sifra-postupka');
  public resourceUrlExcelUpload = SERVER_API_URL + 'api/upload';

  public resourceUrlSifraPonudeDelete = this.applicationConfigService.getEndpointFor('api/ponude-delete');
  public urlDeleSeleced = this.applicationConfigService.getEndpointFor('api/ponude/delete/selected');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  updateSelected(id: number): void {
    this.http.put(`${this.urlUpdateSeleced}/${id}`, null).subscribe();
  }
  create(ponude: IPonude): Observable<EntityResponseType> {
    return this.http.post<IPonude>(this.resourceUrl, ponude, { observe: 'response' });
  }

  deleteSifraPonude(sifraPonude: null | undefined): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrlSifraPonudeDelete}/${sifraPonude}`, { observe: 'response' });
  }

  update(ponude: IPonude): Observable<EntityResponseType> {
    return this.http.put<IPonude>(`${this.resourceUrl}/${getPonudeIdentifier(ponude) as number}`, ponude, { observe: 'response' });
  }

  partialUpdate(ponude: IPonude): Observable<EntityResponseType> {
    return this.http.patch<IPonude>(`${this.resourceUrl}/${getPonudeIdentifier(ponude) as number}`, ponude, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPonude>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  deleteSelected(): void {
    this.http.delete(`${this.urlDeleSeleced}`).subscribe();
  }
  ponudePonudjaci(sifraPostupka: number): Observable<IPonude> {
    return this.http.get<IPonude>(`${this.resourceUrlPonudePonudjaci}/${sifraPostupka}`);
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPonude[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  UploadExcel(formData: FormData): any {
    const headers = new HttpHeaders();

    headers.append('Content-Type', 'multipart/form-data');
    headers.append('Accept', 'application/json');

    return this.http.post(this.resourceUrlExcelUpload, formData, { headers });
  }
}
