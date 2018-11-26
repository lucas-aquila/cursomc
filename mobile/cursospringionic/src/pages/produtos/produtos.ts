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

  items : ProdutoDTO[] = [];
  page: number = 0;




  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public produtoService: ProdutoService,
    public loadingController: LoadingController) {
  }

  imageFolder: string = API_CONFIG.imageFolder;

  ionViewDidLoad() {
    this.loadingCategorias();
  }

  loadingCategorias() {
    let categoriaId = this.navParams.get('categoriaId');
    let loader = this.presentLoading();
    this.produtoService.findByCategoria(categoriaId, this.page, 10)
      .subscribe(result => {
        let start = this.items.length;
        this.items = this.items.concat(result['content']);
        let end = this.items.length - 1;
        loader.dismiss();
        this.loadImageUrls(start, end);
      }, error => {
        loader.dismiss();
      });
  }

  // Foi necessário passar o start e o end como parâmetro
  //para não carregarmos as imagens toda vez que chamar o doInfinite, assim só é chamada as categorias concatenadas
  loadImageUrls(start: number, end: number) {
    for( var i=start; i <= end; i++) {
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

  doRefresh(refresher) {
    this.page = 0;
    this.items = [];
    this.loadingCategorias();
    setTimeout(() => {
      refresher.complete();
    }, 1000);
  }

  doInfinite(infiniteScroll) {
    this.page++;
    this.loadingCategorias();
    setTimeout(() => {
      infiniteScroll.complete();
    }, 1000);
  }

}
