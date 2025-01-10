import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
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
  
  describe('Email field validation', () => {
    it('should require an email', () => {
      // 1. Récupére le contrôle email
      const emailControl = component.form.get('email');
      // 2. Vérifie qu'il y a une erreur 'required'
      expect(emailControl?.hasError('required')).toBeTruthy();
    });

    it('should require a valid email', () => {
      const emailControl = component.form.get('email');
      emailControl?.setValue('invalid-email');
      expect(emailControl?.hasError('email')).toBeTruthy();
      expect(emailControl?.hasError('required')).toBeFalsy();
    });
  });

  describe('Password field validation', () => {
    it('should require a password', () => {
      const passwordControl = component.form.get('password');
      expect(passwordControl?.hasError('required')).toBeTruthy();
    });

    it('should be invalid with password shorter than 3 characters', () => {
      const passwordControl = component.form.get('password');
      passwordControl?.setValue('12');
      expect(passwordControl?.hasError('minlength')).toBeTruthy();
    });
  });

  describe('submit method', () => {
    let authService: AuthService;
    let router: Router;
    let sessionService: SessionService;
  
    beforeEach(() => {
      authService = TestBed.inject(AuthService);
      router = TestBed.inject(Router);
      sessionService = TestBed.inject(SessionService);
    });

    it("should login successfully", () => {
      // Arrange
      const mockSessionInfo: SessionInformation = {
        token: 'fake-token',
        type: 'Bearer',
        id: 1,
        username: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false
      };      
      jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInfo));
      const routerSpy = jest.spyOn(router, 'navigate');
      const sessionServiceSpy = jest.spyOn(sessionService, 'logIn');

      component.form.setValue({
        email: 'test@example.com',
        password: 'password123'
      })
      component.submit();
      expect(sessionService.isLogged).toBe(true);
      expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
      expect(sessionServiceSpy).toHaveBeenCalledWith(mockSessionInfo);
      expect(authService.login).toHaveBeenCalledWith({
        email: 'test@example.com',
        password: 'password123'
      });
    });

    it('should handle login failure with invalid credentials', () => {
      // Arrange
      const errorResponse = { error: 'Invalid credentials' };
      jest.spyOn(authService, 'login').mockReturnValue(throwError(() => errorResponse));
      
      component.form.setValue({
        email: 'test@example.com',
        password: 'wrongpassword'
      });

      // Act
      component.submit();

      // Assert
      expect(component.onError).toBe(true);
    });

    it('should show form validation errors when submitting empty form', () => {
      // Act
      component.submit();

      // Assert
      expect(component.form.get('email')?.errors?.['required']).toBeTruthy();
      expect(component.form.get('password')?.errors?.['required']).toBeTruthy();
    });
  });
});