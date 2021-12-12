import { Component, OnInit } from '@angular/core';
import { Account } from '../../core/auth/account.model';
import { LoginService } from '../../login/login.service';
import { AccountService } from '../../core/auth/account.service';
import { ProfileService } from '../profiles/profile.service';
import { Router } from '@angular/router';
import { VERSION } from '../../app.constants';

@Component({
  selector: 'jhi-ccr-nav-bar',
  templateUrl: './ccr-nav-bar.component.html',
  styleUrls: ['./ccr-nav-bar.component.scss'],
})
export class CcrNavBarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
}
