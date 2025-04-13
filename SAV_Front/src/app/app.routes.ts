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

export const routes: Routes = [

    { path: '', component: HomeComponent },
    { path: 'produit/:id', component: ProduitDetailComponent },
    { path: 'home', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent }, // Temporairement réutilisé pour le register
    { path: 'acceuil', component: AcceuilComponent },
    { path: 'produit', component: ProduitComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'technicien', component: TechnicienComponent },
    { path: 'ResponsableSAV', component: ResponsableSAVComponent },
    { path: 'reclamation', component: ReclamationComponent },
    { path: 'piece', component: PieceDeRechangeComponent },
    { path: 'intervention', component: InterventionComponent },

    {path: 'client',
    loadChildren: () => import('./ClientCompnent/components/client.routes').then(m => m.clientRoutes)  // Lazy loading des routes client];
    }
];
