import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SubscriptionComponent} from "./component/subscription/subscription.component";
import {UserComponent} from "./component/user/user.component";

const routes: Routes = [
  {path: 'subscriptions', component: SubscriptionComponent, pathMatch: 'full'},
  {path: 'users', component: UserComponent, pathMatch: 'full'},
  {path: '', redirectTo: '/subscriptions', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
