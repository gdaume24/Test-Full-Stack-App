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

  it('Component should create correctly', () => {
    expect(component).toBeTruthy();
  });

  describe('Form email test', () => {
    it('A value should be necessary', () => {
      component.form.controls['email'].setValue('');
      const emailControl = component.form.get('email');
      expect(emailControl?.hasError('required')).toBeTruthy();
    });

    it('A valid email should be required', () => {
      const emailControl = component.form.get('email');
      emailControl?.setValue('invalid-email');
      expect(emailControl?.hasError('email')).toBeTruthy();
    });
  });

  describe('Form password test', () => {
    it('Should require a password non null', () => {
      const passwordControl = component.form.get('password');
      passwordControl?.setValue('');
      expect(passwordControl?.hasError('required')).toBeTruthy();
    });

    it('Should be invalid with a password shorter than 3 characters', () => {
      const passwordControl = component.form.get('password');
      passwordControl?.setValue('ab');
      expect(passwordControl?.hasError('minlength')).toBeTruthy();
    });
  });

  describe('Submit method', () => {
    let authService: AuthService;
    let router: Router;
    let sessionService: SessionService;
    let mockSessionInfo: SessionInformation;

    beforeEach(() => {
      authService = TestBed.inject(AuthService);
      router = TestBed.inject(Router);
      sessionService = TestBed.inject(SessionService);
      mockSessionInfo = {
        token: 'fake-token',
        type: 'Bearer',
        id: 1,
        username: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false,
      };
    });

    it('Should login successfully', () => {
      // Arrange
      component.form.setValue({
        email: 'test@example.com',
        password: 'password123',
      });
      const authServiceSpy = jest
        .spyOn(authService, 'login')
        .mockReturnValue(of(mockSessionInfo));
      const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
      const sessionServiceSpy = jest
        .spyOn(sessionService, 'logIn')
        .mockReturnValue(void 0);

      // Act
      component.submit();

      // Assert
      expect(authService.login).toHaveBeenCalledWith({
        email: 'test@example.com',
        password: 'password123',
      });
      expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
      expect(sessionServiceSpy).toHaveBeenCalledWith(mockSessionInfo);
    });

    it('Should fail', () => {
      // Arrange
      const errorResponse = { error: 'Invalid credentials' };
      jest
        .spyOn(authService, 'login')
        .mockReturnValue(throwError(() => errorResponse));
      component.form.setValue({
        email: 'test@example.com',
        password: 'wrrrrrrrrrrrr',
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
