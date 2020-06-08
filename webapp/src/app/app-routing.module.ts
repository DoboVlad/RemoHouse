import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BooksComponent} from "./books/books.component";
import {ClientsComponent} from "./clients/clients.component";
import {PurchasesComponent} from "./purchases/purchases.component";
import {ClientNewComponent} from "./clients/client-new/client-new.component";
import {BookNewComponent} from "./books/book-new/book-new.component";
import {BookUpdateComponent} from "./books/book-update/book-update.component";
import {ClientUpdateComponent} from "./clients/client-update/client-update.component";
import {PurchaseNewComponent} from "./purchases/purchase-new/purchase-new.component";


const routes: Routes = [
  // { path: '', redirectTo: '/home', pathMatch: 'full' },
  {path: 'books', component: BooksComponent},
  {path: 'clients', component: ClientsComponent},
  {path: 'purchases', component: PurchasesComponent},
  {path:'book/new', component: BookNewComponent},
  {path:'client/new', component: ClientNewComponent},
  {path:'book/update/:id', component: BookUpdateComponent},
  {path:'client/buy/:id', component: PurchaseNewComponent},
  {path:'client/update/:id', component: ClientUpdateComponent}


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
