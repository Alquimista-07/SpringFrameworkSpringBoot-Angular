import { Injectable } from '@angular/core';
import { Observable, catchError, map, throwError, tap } from 'rxjs';
import { DatePipe, formatDate, registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es-CO';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
//import { of } from 'rxjs';

import { HttpClient, HttpHeaders } from '@angular/common/http';

//import { CLIENTES } from '../components/clientes/clientes.json';
import { Cliente } from '../interfaces/cliente';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private urlEndpoint: string = 'http://localhost:8080/api/clientes';

  private httpHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient, private router: Router) { }

  getClientes(): Observable<Cliente[]> {
    //return of(CLIENTES);
    return this.http.get<Cliente[]>(this.urlEndpoint)
        .pipe(

          tap(resp => {
            let clientes = resp as Cliente[];
            resp.forEach(cliente => {
              console.log(cliente.nombre);
            })
          }),

          map(resp => {

            let clientes = resp as Cliente[];

            return clientes.map(cliente => {
              cliente.nombre = cliente.nombre.toUpperCase();
              
              // Esta es una forma para el formato de fecha
              //cliente.createAt = formatDate(cliente.createAt!, 'dd-MM-yyyy', 'en-US')
              
              // Esta es otra forma para el formato de fecha
              //let datePipe = new DatePipe('en-US');
              //cliente.createAt = datePipe.transform(cliente.createAt, 'dd/MM/yyyy')!;
              
              // Otras formas
              //let datePipe = new DatePipe('en-US');
              //cliente.createAt = datePipe.transform(cliente.createAt, 'EEEE dd, MMMM yyyy')!;
              
              // Otra forma pero para colocar en español
              registerLocaleData(localeEs, 'es');
              let datePipe = new DatePipe('es');
              //cliente.createAt = datePipe.transform(cliente.createAt, 'fullDate')!;

              return cliente;
            });

          })
        );
  }

  create(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.urlEndpoint, cliente, { headers: this.httpHeaders })
      .pipe(
        catchError(err => {

          if (err.status == 400) {
            return throwError(() => err);
          }

          console.log(err.error.mensaje);

          Swal.fire(
            err.error.mensaje,
            err.error.error,
            'error'
          );

          return throwError(() => err);

        })
      );
  }

  getCliente(id: string): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.urlEndpoint}/${id}`)
      .pipe(
        catchError(err => {

          this.router.navigateByUrl("/clientes");
          console.log(err.error.mensaje);

          Swal.fire(
            'Error al editar',
            err.error.mensaje,
            'error'
          );

          return throwError(() => err);
        })
      );
  }

  update(cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.urlEndpoint}/${cliente.id}`, cliente, { headers: this.httpHeaders })
      .pipe(
        catchError(err => {

          if (err.status == 400) {
            return throwError(() => err);
          }

          console.log(err.error.mensaje);

          Swal.fire(
            err.error.mensaje,
            err.error.error,
            'error'
          );

          return throwError(() => err);

        })
      );
  }

  delete(id: number): Observable<Cliente> {
    return this.http.delete<Cliente>(`${this.urlEndpoint}/${id}`, { headers: this.httpHeaders })
      .pipe(
        catchError(err => {

          console.log(err.error.mensaje);

          Swal.fire(
            err.error.mensaje,
            err.error.error,
            'error'
          );

          return throwError(() => err);

        })
      );
  }

}
