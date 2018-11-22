import { API_CONFIG } from './../../config/api.config';
import { ProdutoService } from './../../services/domain/produto.service';
import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { ProdutoDTO } from '../../models/produto.dto';
import { CartService } from '../../services/domain/cart.service';

@IonicPage()
@Component({
  selector: 'page-produto-detail',
  templateUrl: 'produto-detail.html',
})
export class ProdutoDetailPage {

  item: ProdutoDTO;

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    public produtoService: ProdutoService,
    public cartService: CartService) {
  }

  ionViewDidLoad() {
    let produtoId = this.navParams.get('produtoId');
    this.produtoService.findById(produtoId)
      .subscribe(result => {
        this.item = result;
        this.getImageUrlIfExists();
      },
      error => {})
  }

  getImageUrlIfExists() {
    this.produtoService.getImageFromBucket(this.item.id)
      .subscribe(result => {
        this.item.imageUrl = `${API_CONFIG.bucketBaseUrl}/prod${this.item.id}.jpg`;
      },
      error =>{});
  }

  addToCart(produto: ProdutoDTO) {
    this.cartService.addProduto(produto);
    this.navCtrl.setRoot("CartPage");
  }

}
