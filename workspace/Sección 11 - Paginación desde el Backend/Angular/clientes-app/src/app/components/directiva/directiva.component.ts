import { Component } from '@angular/core';

@Component({
  selector: 'app-directiva',
  templateUrl: './directiva.component.html'
})
export class DirectivaComponent {

  public listaCurso: string[] = ['TypeScript', 'JavaScript', 'Java SE', 'C#', 'PHP'];

  public habilitar: boolean = true;

  public setHabilitar() {
    this.habilitar = (this.habilitar == true) ? false : true
  }

}
