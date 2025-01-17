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
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { LoginComponent } from '../login/login.component';

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
    let formBuilder: FormBuilder;
    
    beforeEach(() => {
      authService = TestBed.inject(AuthService);
      router = TestBed.inject(Router);
      formBuilder = TestBed.inject(FormBuilder);
    });

    it("should call service with proper args and navigate to login page", () => {

      // Arrange
      jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
      const navigateSpy = jest.spyOn(router, 'navigate');
      navigateSpy.mockResolvedValue(true);
      component.form.setValue({
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'mypassword'
      });

      // Act
      component.submit();

      // Assert
      expect(authService.register).toHaveBeenCalledWith({
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'mypassword'
      });
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
    it('should not display submit button if form is invalid', () => {

      // Arrange
      component.form.setValue({
        email: '',
        firstName: 'John',
        lastName: 'Doe',
        password: 'mypassword'
      });

      // Act
      component.submit();

      // Si le formulaire est invalide, le bouton de soumission devrait être désactivé
      expect(component.form.invalid).toBeTruthy();
      expect(component.form.get('email')?.hasError('required')).toBeTruthy();
    });
  });
});
