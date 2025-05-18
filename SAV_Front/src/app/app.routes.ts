import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AcceuilComponent } from './acceuil/acceuil.component';
import { ProduitComponent } from './produit/produit.component';
import { AcceuilClientComponent } from './ClientCompnent/components/acceuil-client.component';
import { ProduitDetailComponent } from './produit-detail/produit-detail.component';
import { ProfileComponent } from './profile/profile.component';
import { TechnicienComponent } from './technicien/technicien.component';
import { ReclamationComponent } from './reclamation/reclamation.component';
import { ResponsableSAVComponent } from './responsable-sav/responsable-sav.component';
import { PieceDeRechangeComponent } from './piece-de-rechange/piece-de-rechange.component';
import { InterventionComponent } from './intervention/intervention.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AuthGuard } from './services/Auth-Guard';
import { RoleGuard } from './services/RoleGuard';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { RestpassowrdComponent } from './restpassowrd/restpassowrd.component';
import { VetrineProduitComponent } from './ClientCompnent/components/vetrine-produit/vetrine-produit.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'produit/:id', component: ProduitDetailComponent },
    { path: 'home', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    {path: 'restPassword' , component: RestpassowrdComponent},
    { path: 'register', component: RegisterComponent },
    { path: 'acceuil', component: AcceuilComponent ,  canActivate: [AuthGuard, RoleGuard],
        data: { expectedRoles: ['ADMIN','RESPONSABLE_SAV','TECHNICIEN'] }},
  
    { path: 'produit', component: ProduitComponent, canActivate: [AuthGuard] },
    { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
    {
        path: 'unauthorized',
        component: UnauthorizedComponent
      },
      
    {
      path: 'technicien',
      component: TechnicienComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: { expectedRoles: ['ADMIN','RESPONSABLE_SAV'] }
    },
    {
      path: 'ResponsableSAV',
      component: ResponsableSAVComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: { expectedRole: 'ADMIN' }
    },
    {
      path: 'reclamation',
      component: ReclamationComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: { expectedRoles: ['ADMIN','RESPONSABLE_SAV'] }
    },
    {
      path: 'piece',
      component: PieceDeRechangeComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: { expectedRoles: ['ADMIN','RESPONSABLE_SAV'] }
    },
    {
      path: 'intervention',
      component: InterventionComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: { expectedRoles: ['TECHNICIEN' , 'RESPONSABLE_SAV']}
    },
    {
      path: 'dashboard',
      component: DashboardComponent,
      canActivate: [AuthGuard, RoleGuard],
      data: { expectedRoles: ['ADMIN','RESPONSABLE_SAV','TECHNICIEN'] }
    },
  
    // Les routes pour les clients
    {
      path: 'client',
      loadChildren: () =>
        import('./ClientCompnent/components/client.routes').then(m => m.clientRoutes),
      canActivate: [AuthGuard, RoleGuard],
      data: { expectedRole: 'CLIENT' }
    }
  ];
  