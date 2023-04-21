package ru.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.web.models.Course;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {
    private Long id;
    private String title;

    public static CourseDto from(Course course){
        return CourseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .build();
    }

    public static List<CourseDto> from(Set<Course> courses){
        return courses.stream().map(CourseDto::from).collect(Collectors.toList());
    }
}
