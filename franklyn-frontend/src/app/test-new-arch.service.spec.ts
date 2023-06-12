import { TestBed } from '@angular/core/testing';

import { TestNewArchService } from './test-new-arch.service';

describe('TestNewArchService', () => {
  let service: TestNewArchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestNewArchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
