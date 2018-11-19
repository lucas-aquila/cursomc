import { Component } from '@angular/core';
import { NavController, IonicPage, MenuController } from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(
    public navCtrl: NavController,
    public menu: MenuController) {
  }

  login() {
    //setRoot navega para página sem a possibilidade de retornar para tela que chamou, 
    //ao contrário do push que sobrepõe uma tela em cima da outra
    this.navCtrl.setRoot('CategoriasPage');
  }

  //Quando entrar na página ele desabilita o menu lateral
  ionViewWillEnter(){
    this.menu.swipeEnable(false);
  }
  
  //Quando sair da página ele habilita o menu novamente
  ionViewDidLeave(){
    this.menu.swipeEnable(true);
  }
}
