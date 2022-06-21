package at.htl.franklynserver.control;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ExamineeRepositoryTest {
    @Inject
    ExamineeRepository examineeRepository;

    @Test
    void tryListAll() {
        var list = examineeRepository.listAll();
        Log.info("List: " + list);
    }
}