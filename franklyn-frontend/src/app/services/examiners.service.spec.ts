import { TestBed } from '@angular/core/testing';

import { ExaminersService } from './examiners.service';

describe('ExaminersService', () => {
  let service: ExaminersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExaminersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
