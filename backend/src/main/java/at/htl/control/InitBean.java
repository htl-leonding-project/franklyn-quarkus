package at.htl.control;

import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

public class InitBean {

    @Inject
    UserRepository userRepository;

    @Inject
    UserGroupRepository userGroupRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    ScreenshotRepository screenshotRepository;

    @Transactional
    public void init(@Observes StartupEvent event){
        /*
        List<User> examiners = new ArrayList<>();
        User examiner1 = new User("Tamara", "Melcher", true);
        User examiner2 = new User("Michael", "Tran", true);
        User examiner3 = new User("Thomas", "Stuetz", true);
        examiners.add(examiner1);
        examiners.add(examiner2);
        examiners.add(examiner3);



        String webUntisResult = webUntisService.initDB("NNRADIO", "PAuthlyn28");
        Log.info("WebUntis Result: " + webUntisResult);
        User examinerWU = userRepository.findByUsername("NNRADIO");
        examiners.add(examinerWU);

        List<UserGroup> userGroups = new ArrayList<>();
        UserGroup userGroup1 = new UserGroup("5AHIF", "2022");
        UserGroup userGroup2 = new UserGroup("3AHIF", "2021");
        userGroups.add(userGroup1);
        userGroups.add(userGroup2);
        for (UserGroup userGroup : userGroups) {
            userGroupRepository.persist(userGroup);
            Log.info("Persist Schoolclass: " + userGroup.title);
        }
        List<UserGroup> schoolClasses2 = new ArrayList<>();
        schoolClasses2.add(userGroup2);


        Exam exam1 = new Exam(
                "234",
                "Test",
                ExamState.FINISHED,
                schoolClasses2,
                LocalDate.parse("2022-06-23"),
                LocalDateTime.parse("2022-05-23T17:09:42.411"),
                LocalDateTime.parse("2022-05-23T18:09:42.411"),
                5,
                examiners
                );
        List<User> examiners1 = new ArrayList<>();
        examiners1.add(examinerWU);
        examiners1.add(examiner2);
        examiners1.add(examiner3);
        exam1.examiners = examiners1;
        exam1.adminId = examinerWU.id;

        Exam exam2 = new Exam(
                "948",
                "Franklyn",
                ExamState.RUNNING,
                userGroups,
                LocalDate.parse("2022-02-23"),
                LocalDateTime.parse("2022-02-23T10:09:42.411"),
                LocalDateTime.parse("2022-02-23T12:09:42.411"),
                5,
                examiners
        );

        exam1.userGroups = schoolClasses2;

        exam2.userGroups = userGroups;
        examRepository.persist(exam1);
        examRepository.persist(exam2);
        List<User> examiners2 = new ArrayList<>();
        examiners2.add(examinerWU);
        examiners2.add(examiner2);
        exam2.examiners = examiners2;
        exam2.adminId = examinerWU.id;
        Log.info("Persist: " + exam1.title);
        Log.info("Persist: " + exam2.title);

        examiner1.exams = List.of(exam1, exam2);
        examiner2.exams = List.of(exam1);
        examiner3.exams = List.of(exam1, exam2);

        List<User> examinees = new ArrayList<>();
        User examinee1 = new Examinee("Jakob", "Unterberger");
        examinee1.exam = exam1;
        examinee1.isOnline = true;
        User examinee2 = new Examinee("Jan", "Melcher");
        examinee2.lastOnline = LocalDateTime.now();
        examinee2.exam = exam1;
        examinee1.isOnline = false;
        User examinee3 = new Examinee("Anna", "Wiesinger");
        examinee3.lastOnline = LocalDateTime.now();
        examinee3.exam = exam2;
        examinee1.isOnline = true;
        Examinee examinee4 = new Examinee("Alina", "Schuster");
        examinee4.exam = exam2;
        examinee1.isOnline = false;
        examinees.add(examinee1);
        examinees.add(examinee2);
        examinees.add(examinee3);
        examinees.add(examinee4);
        for (Examinee examinee : examinees) {
            examineeRepository.persist(examinee);
            Log.info("Saved Examinee: " + examinee.lastName);
        }

        Screenshot screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                examinee1,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                2L,
                examinee1,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                examinee2,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                2L,
                examinee2,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

         */
    }
}
