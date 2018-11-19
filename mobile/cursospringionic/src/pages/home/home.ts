import { Component } from '@angular/core';
import { NavController, IonicPage, MenuController } from 'ionic-angular';
import { CredenciaisDTO } from '../../models/credenciais.dto';
import { AuthService } from '../../services/auth.service';

@IonicPage()
@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  credenciais : CredenciaisDTO = {
    email: "",
    senha: ""
  }

  constructor(
    public navCtrl: NavController,
    public menu: MenuController,
    public auth: AuthService) {
  }

  
  //Quando entrar na página ele desabilita o menu lateral
  ionViewWillEnter(){
    this.menu.swipeEnable(false);
  }
  
  //Quando sair da página ele habilita o menu novamente
  ionViewDidLeave(){
    this.menu.swipeEnable(true);
  }
  
  login() {
    this.auth.authenticate(this.credenciais)
    .subscribe(result => {
      console.log(result.headers.get('Authorization'));
      
      //setRoot navega para página sem a possibilidade de retornar para tela que chamou, 
      //ao contrário do push que sobrepõe uma tela em cima da outra
      this.navCtrl.setRoot('CategoriasPage');
    },
    error => {});
  }
}
