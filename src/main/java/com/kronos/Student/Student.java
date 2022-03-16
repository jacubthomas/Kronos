package com.kronos.Student;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Student {
  @Id
  @SequenceGenerator(
      name = "student_sequence",
      sequenceName = "student_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
  private Long id;

  private String name;
  @Transient private Integer age;
  private String email;
  private LocalDate dob;

  public Student() {}

  public Student(String name, String email, LocalDate dob) {
    this.name = name;
    this.email = email;
    this.dob = dob;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return Period.between(this.dob, LocalDate.now()).getYears();
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getDob() {
    return this.dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }

  public Student id(Long id) {
    setId(id);
    return this;
  }

  public Student name(String name) {
    setName(name);
    return this;
  }

  public Student email(String email) {
    setEmail(email);
    return this;
  }

  public Student dob(LocalDate dob) {
    setDob(dob);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Student)) {
      return false;
    }
    Student student = (Student) o;
    return Objects.equals(id, student.id)
        && Objects.equals(name, student.name)
        && Objects.equals(age, student.age)
        && Objects.equals(email, student.email)
        && Objects.equals(dob, student.dob);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, age, email, dob);
  }

  @Override
  public String toString() {
    return "{"
        + " id='"
        + getId()
        + "'"
        + ", name='"
        + getName()
        + "'"
        + ", age='"
        + getAge()
        + "'"
        + ", email='"
        + getEmail()
        + "'"
        + ", dob='"
        + getDob()
        + "'"
        + "}";
  }
}
