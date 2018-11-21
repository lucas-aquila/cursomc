import { API_CONFIG } from './../../config/api.config';
import { CategoriaService } from './../../services/domain/categoria.service';
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { CategoriaDTO } from '../../models/categoria.dto';

@IonicPage()
@Component({
  selector: 'page-categorias',
  templateUrl: 'categorias.html',
})
export class CategoriasPage {

  bucketUrl: string = API_CONFIG.bucketBaseUrl;

  categorias:CategoriaDTO[];

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public categoriaService: CategoriaService) {
  }

  ionViewDidLoad() {
    //É uma chamada assincrona, você precisa se inscrever(subscribe) para fazer algo quando a resposta chegar
    this.categoriaService.findAll().subscribe(result => {
      this.categorias = result;
    },
    error => {});
  }

  showProdutos() {
    this.navCtrl.push('ProdutosPage');
  }

}
