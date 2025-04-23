import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-acceuilclient',
  standalone: true,
  imports: [],
  templateUrl: './acceuilclient.component.html',
  styleUrl: './acceuilclient.component.css'
})
export class AcceuilclientComponent implements OnInit {
  username : any ;
  carouselInterval: any;
  isDropdownOpen = false;


  
  ngOnInit(): void {
      this.getUsername() ;
      this.autoSlideCarousel();
      history.pushState(null, '', location.href);
      window.onpopstate = function () {
        history.go(1); // Annule le retour arrière
      };
  }


  getUsername(): void {
    const username = localStorage.getItem('UserName');
    this.username = username ;
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
}

logout() {
    // Ajoutez ici la logique de déconnexion (ex: suppression du token)
    console.log("Déconnexion...");
}


  autoSlideCarousel(): void {
    this.carouselInterval = setInterval(() => {
      this.moveToNextSlide(); // Utilisation de la méthode de contrôle pour avancer le carrousel
    }, 5000); // 5000 ms = 5 secondes
  }

  moveToPrevSlide(): void {
    const prevButton = document.querySelector('.carousel-control-prev') as HTMLElement;
    if (prevButton) {
      prevButton.click(); // Cliquez sur le bouton "Previous" pour changer d'image
    }
  }

  moveToNextSlide(): void {
    const nextButton = document.querySelector('.carousel-control-next') as HTMLElement;
    if (nextButton) {
      nextButton.click(); // Cliquez sur le bouton "Next" pour changer d'image
    }
  }

  ngOnDestroy(): void {
    if (this.carouselInterval) {
      clearInterval(this.carouselInterval);
    }
  }

}
