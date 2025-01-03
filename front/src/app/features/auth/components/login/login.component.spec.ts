import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call authService.login with correct data', () => {
    let authSerivce = TestBed.inject(AuthService);
    const mockLoginRequest = {
      email: 'emile.goudot@yopmail.com',
      password: 'testPass',
    };
    const mockResponse = {
      token: 'testToken',
      user: { id: '1', username: 'testUser' },
    };

    component.form.setValue(mockLoginRequest); // Simule la saisie dans le formulaire

    jest.spyOn(authService, 'login').mockReturnValue(of(mockResponse)); // Simule une réponse réussie

    component.submit();

    expect(authService.login).toHaveBeenCalledWith(mockLoginRequest); // Vérifie l'appel avec les bonnes données
  });
});
