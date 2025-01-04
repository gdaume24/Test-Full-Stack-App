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

    it("should require a password with at least 3 characters", () => {
      const passwordControl = component.form.get('password');
      passwordControl?.setValue('12');
      expect(passwordControl?.hasError('minlength')).toBeTruthy();
    });
  });

  describe('submit', () => {
});