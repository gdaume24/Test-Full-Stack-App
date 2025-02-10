import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  let mockSessionInfo: SessionInformation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
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

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in', () => {
    service.logIn(mockSessionInfo);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInfo);
  });
});
