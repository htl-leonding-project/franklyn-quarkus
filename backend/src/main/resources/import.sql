insert into f_examiner (e_user_name, e_first_name, e_last_name, e_is_admin) values ( 'tmelch', 'Tamara', 'Melcher', true);
insert into f_examiner (e_user_name, e_first_name, e_last_name, e_is_admin) values ('mtran', 'Michael', 'Tran', true);
insert into f_examiner (e_user_name, e_first_name, e_last_name, e_is_admin) values ('tstuet', 'Thomas', 'Stütz', true);

insert into f_examinee (e_first_name, e_last_name) values ('Max', 'Mustermann');
insert into f_examinee (e_first_name, e_last_name) values ('Maxine', 'Musterfrau');

insert into f_exam (e_pin, e_ongoing, e_compression, e_date, e_start_time, e_end_time, e_interval, e_resolution, e_title) values ('1234', false, 100, TO_DATE('03/05/2022','DD/MM/YY'), TO_DATE('03/05/2022 8:30:00AM','DD/MM/YY HH:MI:SS AM'), TO_DATE('03/05/2022 10:30:00AM','DD/MM/YY HH:MI:SS AM'), 5, 1, 'Franklyn');

insert into f_school_class (sc_id, sc_title, sc_year) values (1, 'Franklyn', '2022');
insert into f_school_class (sc_id, sc_title, sc_year) values (2, '3AHIF', '2021');

insert into f_exam_f_examinee (exam_e_id, examineeids_e_id)
    values (1, 1);
insert into f_exam_f_examinee (exam_e_id, examineeids_e_id)
    values (1, 2);

insert into f_exam_f_examiner (exam_e_id, examinerids_e_id)
    values(1, 3);

insert into f_exam_f_school_class (exam_e_id, formids_sc_id)
    values (1, 1);

insert into f_screenshot (s_screenshot_number, s_exam_id, s_examinee_id, s_is_compressed, e_compression, s_resolution, s_timestamp) values (1, 1, 2, 1, 1, 1, CURRENT_TIMESTAMP );

insert into f_examinee_details (ed_exam_id, ed_examinee_id, ed_is_online, ed_last_online, ed_latest_timestamp, ed_latest_screenshot_number)
    values (1, 2, false, TO_DATE('03/05/2022 10:30:00AM','DD/MM/YY HH:MI:SS AM'), CURRENT_TIMESTAMP , 1);

insert into f_screenshot (s_screenshot_number, s_exam_id, s_examinee_id, s_is_compressed, e_compression, s_resolution, s_timestamp) values (1, 1, 2, 1, 1, 1, CURRENT_TIMESTAMP );

insert into f_examinee_details (ed_exam_id, ed_examinee_id, ed_is_online, ed_last_online, ed_latest_timestamp, ed_latest_screenshot_number)
    values (1, 2, false, TO_DATE('03/05/2022 10:30:00AM','DD/MM/YY HH:MI:SS AM'), CURRENT_TIMESTAMP , 1);
