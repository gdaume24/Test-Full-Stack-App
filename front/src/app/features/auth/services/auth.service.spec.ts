import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { HttpClient } from '@angular/common/http';

TestBed.configureTestingModule({
  providers: [
    // ... other test providers
    provideHttpClient(),
    provideHttpClientTesting(),
  ],
});
describe('Testing AuthService', () => {
  let service: AuthService;
  let httpClient: HttpClient;
  let registerRequest: RegisterRequest;
  let loginRequest: LoginRequest;

  beforeEach(() => {
    httpClient = TestBed.inject(HttpClient);
    service = new AuthService(httpClient);
    component = new LoginComponent(service);
});

})
