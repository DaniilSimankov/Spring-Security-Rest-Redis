package ru.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.web.models.Course;

public interface CoursesRepository extends JpaRepository<Course, Long> {
}
