// package com.kronos.Student;

// import java.time.LocalDate;
// import java.time.Month;
// import java.util.List;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class StudentConfig {
//   @Bean
//   CommandLineRunner commandLineRunner(StudentRepository repository) {
//     return args -> {
//       Student jacub = new Student("jacub", "jharring@usc.edu", LocalDate.of(1993, Month.APRIL,
// 16));

//       Student thomas = new Student("thomas", "thomas@usc.edu", LocalDate.of(1998, Month.APRIL,
// 16));

//       repository.saveAll(List.of(jacub, thomas));
//     };
//   }
// }

/* THIS ADDS OBJECTS TO DB ON RUN */
