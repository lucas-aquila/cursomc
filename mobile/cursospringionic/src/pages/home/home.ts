import { Component } from '@angular/core';
import { NavController, IonicPage } from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController) {

  }

  login() {
    //setRoot navega para página sem a possibilidade de retornar para tela que chamou, 
    //ao contrário do push que sobrepõe uma tela em cima da outra
    this.navCtrl.setRoot('CategoriasPage');
  }

}
