import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { MainBodyComponent } from './main-body/main-body.component';
import { MainHeaderComponent } from './main-header/main-header.component';
import { GalleryComponent } from './gallery/gallery.component';
import { XboxComponent } from './xbox/xbox.component';
import { ProductCategoryComponent } from './product-category/product-category.component';
import { HttpClientModule } from '@angular/common/http';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { ProductOrderComponent } from './product-order/product-order.component';
import { CustomerServiceComponent } from './customer-service/customer-service.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { ContactUsComponent } from './contact-us/contact-us.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    MainBodyComponent,
    MainHeaderComponent,
    GalleryComponent,
    XboxComponent,
    ProductCategoryComponent,
    ShoppingCartComponent,
    ProductOrderComponent,
    CustomerServiceComponent,
    AboutUsComponent,
    ContactUsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

