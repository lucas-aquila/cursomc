import { ProdutoService } from './../../services/domain/produto.service';
import { API_CONFIG } from './../../config/api.config';
import { ProdutoDTO } from './../../models/produto.dto';
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, LoadingController } from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-produtos',
  templateUrl: 'produtos.html',
})
export class ProdutosPage {

  items : ProdutoDTO[];

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public produtoService: ProdutoService,
    public loadingController: LoadingController) {
  }

  imageFolder: string = API_CONFIG.imageFolder;

  ionViewDidLoad() {
    let categoriaId = this.navParams.get('categoriaId');
    let loader = this.presentLoading();
    this.produtoService.findByCategoria(categoriaId)
      .subscribe(result => {
        this.items = result['content'];
        loader.dismiss();
        this.loadImageUrls();
      }, error => {
        loader.dismiss();
      });
  }

  loadImageUrls() {
    for( var i=0; i < this.items.length; i++) {
      let item = this.items[i];
      this.produtoService.getSmallImageFromBucket(item.id)
        .subscribe(result => {
          item.imageUrl = `${API_CONFIG.bucketBaseUrl}/prod${item.id}-small.jpg`;
        },
        error => {});
    }
  }

  showDetail(produtoId : string) {
    this.navCtrl.push('ProdutoDetailPage', {produtoId: produtoId});
  }

  presentLoading() {
    const loader = this.loadingController.create({
      content: "Aguarde...",
    });
    loader.present();
    return loader;
  }

}
