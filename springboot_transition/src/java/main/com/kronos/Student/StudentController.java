package com.kronos.Student;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/student")
public class StudentController {
  private final StudentService studentService;

  @Autowired
  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @GetMapping
  public List<Student> getStudents() {
    return studentService.getStudents();
  }

  @PostMapping("/addstudent")
  public List<Student> registerNewStudent(@RequestBody Student student) {
    studentService.addNewStudent(student);
    return studentService.getStudents();
  }

  @DeleteMapping(path = "{studentId}")
  public void deleteStudent(@PathVariable("studentId") Long id) {
    studentService.deleteStudent(id);
  }

  @PutMapping(path = "{studentId}")
  public void updateStudent(
      @PathVariable("studentId") Long studentId,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email) {
    studentService.updateStudent(studentId, name, email);
  }
}

// curl -i -X POST -H 'Content-Type:application/json' -d '{"name":"joe",
// "email":"joe@usc.edu","dob":"1995-12-18"}' http://localhost:8080/api/v1/student

// curl -i -X DELETE http://localhost:8080/api/v1/student/1

// curl -i -X PUT http://localhost:8080/api/v1/student/1?name=seamus

// curl -i -X PUT -H 'Content-Type:application/json' -d
// '{"name":"seamus","email":"jharring@usc.edu"}' http://localhost:8080/api/v1/student/1

// curl -i -X PUT -H 'Content-Type:application/json' -d '{"name":"seamus",
// "email":"joe@usc.edu","dob":"1995-11-10"}' http://localhost:8080/api/v1/student
