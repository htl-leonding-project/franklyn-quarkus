import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestNewArchComponent } from './test-new-arch.component';

describe('TestNewArchComponent', () => {
  let component: TestNewArchComponent;
  let fixture: ComponentFixture<TestNewArchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TestNewArchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestNewArchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
