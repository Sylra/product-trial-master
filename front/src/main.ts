import { APP_INITIALIZER, enableProdMode, importProvidersFrom } from "@angular/core";

import { registerLocaleData } from "@angular/common";
import {
  provideHttpClient,
  withInterceptorsFromDi,
} from "@angular/common/http";
import localeFr from "@angular/common/locales/fr";
import { BrowserModule, bootstrapApplication } from "@angular/platform-browser";
import { provideAnimations } from "@angular/platform-browser/animations";
import { provideRouter } from "@angular/router";
import { APP_ROUTES } from "app/app.routes";
import { ConfirmationService, MessageService } from "primeng/api";
import { DialogService } from "primeng/dynamicdialog";
import { AppComponent } from "./app/app.component";
import { environment } from "./environments/environment";
import { TokenService } from "app/shared/services/token.service";

if (environment.production) {
  enableProdMode();
}

const CREDENTIALS = {
  email: "admin@admin.com",
  password: "password",
}

function initializeApp(authService: TokenService) {
  return () => authService.fetchToken(CREDENTIALS);
}

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(BrowserModule),
    provideHttpClient(
      withInterceptorsFromDi(),
    ),
    provideAnimations(),
    provideRouter(APP_ROUTES),
    ConfirmationService,
    MessageService,
    DialogService,
    TokenService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [TokenService],
      multi: true
    },
  ],
}).catch((err) => console.log(err));

registerLocaleData(localeFr, "fr-FR");
