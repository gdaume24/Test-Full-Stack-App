import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect, it } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        NoopAnimationsModule,
        RouterTestingModule.withRoutes([
          { path: "sessions", loadChildren: () => import('../../../sessions/sessions.module').then(m => m.SessionsModule) }
        ])
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it("complete form and Save button should be enabled", () => {
    component.sessionForm?.setValue({
      name: 'Session hardcore',
      date: new Date('2025-02-15'),
      teacher_id: 'Margot DELAYAHE',
      description: 'Attention cette session est extrêmement dangereuse...'
  });

    expect(component.sessionForm?.invalid).toBe(false);
  });

  it("should create session successfully: sessionApiService.create to have been called, and router.navigate to have been called", () => {
    component.sessionForm?.setValue({
      name: 'Session hardcore',
      date: new Date('2025-02-15'),
      teacher_id: 'Margot DELAYAHE',
      description: 'Attention cette session est extrêmement dangereuse...'
    });
    let sessionApiService = TestBed.inject(SessionApiService);
    let router = TestBed.inject(Router);

    let sessionApiServiceSpy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(component.sessionForm?.value as Session));
    let routerSpy = jest.spyOn(router, 'navigate');
    routerSpy.mockResolvedValue(true);

    component.submit();

    expect(sessionApiServiceSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });
});
