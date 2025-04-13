import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  standalone: true,
  imports: [CommonModule, RouterLink]
})
export class HeaderComponent implements OnInit {
  isloggedIn = true;
  username: any;
  userRole: any;
  isNotificationDropdownOpen = false;
  isMessageDropdownOpen = false;
  isProfileDropdownOpen = false;
  isSidebarCollapsed = false;

  constructor(
    private router: Router,
    //private sidenavService: SidenavServicesService
  ) {
  }
  

  ngOnInit(): void {
    this.getUserRole();
    this.isloggedIn = !!localStorage.getItem('token');
    this.getUsername() ;
  }

  toggle() {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
    //this.sidenavService.toggleSidebar();
    
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
      sidebar.classList.toggle('collapsed', this.isSidebarCollapsed);
    }

    if (window.innerWidth <= 768) {
      sidebar?.classList.toggle('show');
    }
  }

  toggleNotificationDropdown(event: Event) {
    event.preventDefault();
    this.isNotificationDropdownOpen = !this.isNotificationDropdownOpen;
    this.isMessageDropdownOpen = false;
    this.isProfileDropdownOpen = false;
  }

  toggleMessageDropdown(event: Event) {
    event.preventDefault();
    this.isMessageDropdownOpen = !this.isMessageDropdownOpen;
    this.isNotificationDropdownOpen = false;
    this.isProfileDropdownOpen = false;
  }

  toggleProfileDropdown(event: Event) {
    event.preventDefault();
    this.isProfileDropdownOpen = !this.isProfileDropdownOpen;
    this.isNotificationDropdownOpen = false;
    this.isMessageDropdownOpen = false;
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  closeDropdowns(event: Event) {
    if (!event.target) return;
    
    const target = event.target as HTMLElement;
    if (!target.closest('.nav-item.dropdown')) {
      this.isNotificationDropdownOpen = false;
      this.isMessageDropdownOpen = false;
      this.isProfileDropdownOpen = false;
    }
  }

  getUserRole(): void {
    const role = localStorage.getItem('userRole');
    this.userRole = role ? role : 'Unknown';
  }


  getUsername(): void {
    const username = localStorage.getItem('UserName');
    this.username = username ;
  }
}
