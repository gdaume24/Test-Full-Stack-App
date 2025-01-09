import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe("submit method", () => {
    let authService: AuthService;
    let router: Router;

    beforeEach(() => {
      authService = TestBed.inject(AuthService);
      router = TestBed.inject(Router);
    })

    it("should register successfully", () => {
      jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
      const routerSpy = jest.spyOn(router, 'navigate');

      component.submit();
      expect(authService.register).toHaveBeenCalledWith({
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'mypassword'
      });
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
    it('should handle login failure with invalid credentials', () => {
      component.form.setValue({
        email: '',
        firstName: 'John',
        lastName: 'Doe',
        password: 'mypassword'
      });
      const emailControl = component.form.get('email');
      expect(emailControl?.hasError('required')).toBeTruthy();

      component.submit();

      expect(component.onError).toBe(true);

    });
  });
});
