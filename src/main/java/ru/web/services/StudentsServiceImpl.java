package ru.web.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.web.dto.CourseDto;
import ru.web.dto.StudentDto;
import ru.web.models.Course;
import ru.web.models.Student;
import ru.web.repositories.CoursesRepository;
import ru.web.repositories.StudentsRepository;

import java.util.List;

import static ru.web.dto.StudentDto.from;

@Service
@RequiredArgsConstructor
public class StudentsServiceImpl implements StudentsService {

    private final StudentsRepository studentsRepository;
    private final CoursesRepository coursesRepository;

    @Override
    public List<StudentDto> getStudents(int page, int size) {
        PageRequest request = PageRequest.of(page, size, Sort.by("id"));
        Page<Student> result = studentsRepository.findAllByIsDeletedIsNull(request);
        return from(result.getContent());
    }

    @Override
    public StudentDto addStudent(StudentDto student) {
        Student newStudent = Student.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .build();

        studentsRepository.save(newStudent);

        return from(newStudent);
    }

    @Override
    public StudentDto updateStudent(Long studentId, StudentDto student) {
        Student existedStudent = studentsRepository.getById(studentId);
        existedStudent.setFirstName(student.getFirstName());
        existedStudent.setLastName(student.getLastName());
        studentsRepository.save(existedStudent);
        return from(existedStudent);
    }

    @Override
    public void deleteStudent(Long studentId) {
        Student student = studentsRepository.getById(studentId);
        student.setIsDeleted(true);
        studentsRepository.save(student);
    }

    @Override
    public List<CourseDto> addCourseToStudent(Long studentId, CourseDto course) {
        Student student = studentsRepository.getById(studentId);
        Course existedCourse = coursesRepository.getById(course.getId());
        student.getCourses().add(existedCourse);
        studentsRepository.save(student);

        return CourseDto.from(student.getCourses());
    }

    @Override
    public StudentDto getStudent(Long studentId) {

        return from(studentsRepository.getById(studentId));
    }


}
