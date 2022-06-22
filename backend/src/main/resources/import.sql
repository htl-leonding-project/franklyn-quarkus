insert into f_examiner (id ,e_user_name, e_first_name, e_last_name, e_is_admin) values (1, 'tmelch', 'Tamara', 'Melcher', true);
insert into f_examiner (id ,e_user_name, e_first_name, e_last_name, e_is_admin) values (2, 'mtran', 'Michael', 'Tran', true);
insert into f_examiner (id ,e_user_name, e_first_name, e_last_name, e_is_admin) values (3, 'tstuet', 'Thomas', 'Stütz', true);

insert into f_school_class (id, f_title, f_year)
    values (1, 'Franklyn', '2022');
insert into f_school_class (id, f_title, f_year)
    values (2, '3AHIF', '2021');

insert into f_exam (id, e_pin, e_ongoing, e_compression, e_date, e_start_time, e_end_time, e_interval, e_resolution, e_title, e_examiner_id, e_form_id)
    values (1, '1234', false, 100, TO_DATE('03/05/2022','DD/MM/YY'), TO_DATE('03/05/2022 8:30:00AM','DD/MM/YY HH:MI:SS AM'), TO_DATE('03/05/2022 10:30:00AM','DD/MM/YY HH:MI:SS AM'), 5, 1, 'Franklyn', 3, 1);

insert into f_examinee (id, e_first_name, e_last_name, e_exam_id)
    values (1, 'Max', 'Mustermann', 1);
insert into f_examinee (id, e_first_name, e_last_name, e_exam_id)
    values (2, 'Maxine', 'Musterfrau', 1);

insert into f_screenshot (id, s_screenshot_number, s_exam_id, s_examinee_id, s_is_compressed, e_compression, s_resolution, s_timestamp)
    values (1, 1, 1, 2, 1, 1, 1, CURRENT_TIMESTAMP );

insert into f_examinee_details (id, ed_exam_id, ed_is_online, ed_last_online, ed_latest_timestamp, ed_latest_screenshot_number, ed_examinee_id)
    values (1, 1, false, TO_DATE('03/05/2022 10:30:00AM','DD/MM/YY HH:MI:SS AM'), CURRENT_TIMESTAMP , 1, 1);


insert into f_screenshot (id, s_screenshot_number, s_exam_id, s_examinee_id, s_is_compressed, e_compression, s_resolution, s_timestamp)
    values (2, 2, 1, 2, 1, 1, 1, CURRENT_TIMESTAMP );

insert into f_examinee_details (id, ed_exam_id, ed_is_online, ed_last_online, ed_latest_timestamp, ed_latest_screenshot_number, ed_examinee_id)
    values (2, 1, false, TO_DATE('03/05/2022 10:30:00AM','DD/MM/YY HH:MI:SS AM'), CURRENT_TIMESTAMP , 2, 2);
