package at.htl.franklynserver.control;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class WebUntisRepositoryTest {

    @Inject
    WebUntisRepository webUntisRepository;

    @Test
    void get_all_teachers_success(){

        String result = webUntisRepository.getAllTeachers();

        System.out.println(result);
    }
}
