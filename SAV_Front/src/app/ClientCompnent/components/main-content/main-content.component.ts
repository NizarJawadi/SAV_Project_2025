import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-main-content',
  standalone: true,
  imports: [],
  templateUrl: './main-content.component.html',
  styleUrl: './main-content.component.css'
})
export class MainContentComponent implements OnInit {
 
 
  ngOnInit(): void {
    this.autoSlideCarousel();
  }
  carouselInterval: any;






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
