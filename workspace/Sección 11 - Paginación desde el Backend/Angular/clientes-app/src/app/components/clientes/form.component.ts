import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

// Sweet Alert
import Swal from 'sweetalert2';

import { Cliente } from 'src/app/interfaces/cliente';
import { ClienteService } from 'src/app/services/cliente.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit {

  public titulo = 'Crear Cliente';

  public errores:string[] = [];

  constructor(private clienteService: ClienteService, private router: Router, private activatedRoute: ActivatedRoute) { }
  public cliente: Cliente = {
    nombre: '',
    apellido: '',
    email: ''
  };

  ngOnInit(): void {
    this.cargarCliente();
  }

  public create(): void {
    this.clienteService.create(this.cliente).subscribe(
      (resp: any) => {
        this.router.navigateByUrl("/clientes")
        Swal.fire(
          'Nuevo Cliente',
          `El cliente ${resp.clienteNuevo.nombre} se ha creado con éxito!`,
          'success'
        )
      }, ({error, status}) => {
        if(status === 400){
          this.errores = error.error;
          console.error('Status desde el backend: ' + status);
        }
      });
  }

  public cargarCliente(): void {
    this.activatedRoute.params.subscribe(({ id }) => {
      if (id) {
        this.clienteService.getCliente(id)
          .subscribe(cliente => this.cliente = cliente)
      }
    });
  }

  public update(): void {
    this.clienteService.update(this.cliente)
      .subscribe((cliente:any) => {
        this.router.navigateByUrl("/clientes")
        Swal.fire(
          'Cliente Actualizado',
          `El cliente ${cliente.clienteNuevo.nombre} se ha actualizado con éxito!`,
          'success'
        )
      }, ({error, status}) => {
        if(status === 400){
          this.errores = error.error;
          console.error('Status desde el backend: ' + status);
        }
      });
  }

}
