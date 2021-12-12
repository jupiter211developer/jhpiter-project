import { Route } from '@angular/router';
import { CcrNavBarComponent } from './ccr-nav-bar.component';

export const ccrNavBarRoute: Route = {
  path: '',
  component: CcrNavBarComponent,
  outlet: 'ccr-nav-bar',
};
