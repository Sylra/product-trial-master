import { Injectable, inject, signal } from "@angular/core";
import { Product } from "./product.model";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { catchError, Observable, of, tap } from "rxjs";
import { environment } from '../../../environments/environment';
import { TokenService } from "app/shared/services/token.service";

@Injectable({
    providedIn: "root"
}) export class ProductsService {

    private readonly http = inject(HttpClient);
    private readonly tokenService = inject(TokenService);

    private readonly apiUrl = environment.apiUrl
    private readonly path = `${this.apiUrl}/products`;

    private readonly _products = signal<Product[]>([]);

    public readonly products = this._products.asReadonly();

    public get(): Observable<Product[]> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + this.tokenService.getToken(),
        });
        return this.http.get<Product[]>(this.path, { headers: headers }).pipe(
            catchError((error) => {
                return this.http.get<Product[]>("assets/products.json");
            }),
            tap((products) => this._products.set(products)),
        );
    }

    public create(product: Product): Observable<boolean> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + this.tokenService.getToken(),
        });
        const fixedProduct: Product = { ...product, code: "ABCD", quantity: 1, inventoryStatus: "INSTOCK" };
        return this.http.post<boolean>(this.path, fixedProduct, { headers: headers }).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => [fixedProduct, ...products])),
        );
    }

    public update(product: Product): Observable<boolean> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + this.tokenService.getToken(),
        });
        return this.http.patch<boolean>(`${this.path}/${product.id}`, product, { headers: headers }).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => {
                return products.map(p => p.id === product.id ? product : p)
            })),
        );
    }

    public delete(productId: number): Observable<boolean> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + this.tokenService.getToken(),
        });
        return this.http.delete<boolean>(`${this.path}/${productId}`, { headers: headers }).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => products.filter(product => product.id !== productId))),
        );
    }
}
