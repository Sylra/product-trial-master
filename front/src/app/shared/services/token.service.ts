import { Injectable, inject, signal } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { catchError, map, Observable, of, tap } from "rxjs";
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: "root"
}) export class TokenService {
    headers = new HttpHeaders({
        'Content-Type': 'application/json',
    })

    private readonly http = inject(HttpClient);

    private readonly apiUrl = environment.apiUrl
    private readonly path = `${this.apiUrl}/token`;

    private token: string | null = null;


    public fetchToken(credentials: any): Observable<void> {
        return this.http.post<string>(this.path, credentials, {headers: this.headers, responseType: 'text' as 'json'}).pipe(
            catchError(() => {
                return "";
            }),
            map(response => {
                this.token = response.trim();
                localStorage.setItem('token', this.token);
            }),
        );
    }

    getToken(): string | null {
      return this.token || localStorage.getItem('token');
    }
}
