package com.pawelnu.projectmanager.config;

import com.github.javafaker.Faker;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class JavaFakerExamplesTest {

  private final Faker faker = new Faker(new Random(12345));

  @Test
  void testAddressStreetAddress() {
//    log.info(faker.address().streetAddress());
    log.info(faker.address().streetName());
  }

  @Test
  void testPhoneNumber() {
//    log.info(faker.phoneNumber().phoneNumber());
    log.info(faker.phoneNumber().cellPhone());
  }
  @Test
  void testEmailAddress() {
//    log.info(faker.internet().emailAddress());
//    log.info(faker.internet().emailAddress("test.test"));
    log.info(faker.internet().safeEmailAddress());
    log.info(faker.internet().safeEmailAddress("test.test"));
  }
}