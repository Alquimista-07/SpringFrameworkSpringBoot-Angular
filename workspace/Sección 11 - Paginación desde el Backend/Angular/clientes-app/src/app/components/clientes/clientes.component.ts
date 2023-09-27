import { Component, OnInit } from '@angular/core';

// Sweet Alert
import Swal from 'sweetalert2';

import { Cliente } from 'src/app/interfaces/cliente';
import { ClienteService } from 'src/app/services/cliente.service';

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {

  public clientes: Cliente[] = [];

  constructor(private clienteService: ClienteService){ }

  ngOnInit(): void {
    this.clienteService.getClientes()
        .subscribe( clientes => this.clientes = clientes );
  }

  public delete(cliente: Cliente): void {
        Swal.fire({
          title: '¿Está seguro que desea eliminar?',
          text: `¿Está seguro que desea eliminar al cliente ${cliente.nombre} ${cliente.apellido}?`,
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Si, eliminarlo'
        }).then((result) => {
          if (result.isConfirmed) {
            this.clienteService.delete(cliente.id!).subscribe(
              response => {
                this.clientes = this.clientes.filter(cli => cli != cliente)
                Swal.fire(
                  'Eliminado!',
                  `El cliente ${cliente.nombre} ha sido eliminado!`,
                  'success'
                )
              }
            )
          }
        });
  }

}
