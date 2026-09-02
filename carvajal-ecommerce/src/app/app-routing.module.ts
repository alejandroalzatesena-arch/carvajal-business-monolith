import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';

const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./pages/home/home.module').then(m => m.HomeModule)
      },
      {
        path: 'catalog',
        loadChildren: () =>
          import('./pages/catalog/catalog.module').then(m => m.CatalogModule)
      },
      {
        path: 'wishlist',
        loadChildren: () =>
          import('./pages/wishlist/wishlist.module').then(m => m.WishlistModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'profile',
        loadChildren: () =>
          import('./pages/profile/profile.module').then(m => m.ProfileModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'admin',
        loadChildren: () =>
          import('./pages/admin/admin.module').then(m => m.AdminModule),
        canActivate: [AuthGuard]
      }
    ]
  },
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      {
        path: 'login',
        loadChildren: () =>
          import('./pages/login/login.module').then(m => m.LoginModule)
      },
      {
        path: 'register',
        loadChildren: () =>
          import('./pages/register/register.module').then(m => m.RegisterModule)
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
