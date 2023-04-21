package ru.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.web.dto.CourseDto;
import ru.web.dto.CoursesResponse;
import ru.web.dto.StudentDto;
import ru.web.dto.StudentsResponse;
import ru.web.services.StudentsService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping("/{student-id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable("student-id") Long studentId){
        return ResponseEntity.ok(studentsService.getStudent(studentId));
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<StudentsResponse> getStudents(@RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        return ResponseEntity.ok()
                .headers(httpHeaders -> {
                    httpHeaders.add("dateTime", LocalDate.now().toString());
                })
                .body(StudentsResponse.builder().data(studentsService.getStudents(page, size)).build());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto student) {
        return ResponseEntity.ok(studentsService.addStudent(student));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{student-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public StudentDto updateStudent(@PathVariable("student-id") Long studentId, @RequestBody StudentDto student) {
        return studentsService.updateStudent(studentId, student);
    }

    @RequestMapping(value = "/{student-id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteStudent(@PathVariable("student-id") Long studentId) {
        studentsService.deleteStudent(studentId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{student-id}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public CoursesResponse addCourseToStudent(@PathVariable("student-id") Long studentId,
                                              @RequestBody CourseDto course){
        return CoursesResponse.builder()
                .data(studentsService.addCourseToStudent(studentId, course))
                .build();

    }


}
