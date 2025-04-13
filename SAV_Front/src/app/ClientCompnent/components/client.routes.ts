import { Routes } from '@angular/router';
import { ReclamationComponent } from './reclamation/reclamation.component';
import { MainContentComponent } from './main-content/main-content.component';
import { HeaderComponent } from './header/header.component';
import { AcceuilClientComponent } from './acceuil-client.component';
import { VetrineProduitComponent } from './vetrine-produit/vetrine-produit.component';
import { HistoriqueAchatComponent } from './historique-achat/historique-achat.component';
import { ProfileClientComponent } from './profile-client/profile-client.component';


export const clientRoutes: Routes = [

    {
        path: '', component: AcceuilClientComponent, // Garde le header et le footer
        children: [
            { path: '', component: MainContentComponent },
            { path: 'reclamation', component: ReclamationComponent },    
            { path: 'vetrine', component: VetrineProduitComponent }, 
            { path: 'historique', component: HistoriqueAchatComponent }, 
            { path: 'profile', component: ProfileClientComponent }, 
        ],
         
        // Ajoute d'autres routes ici
      }
];
