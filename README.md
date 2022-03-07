# franklyn-quarkus

Franklyn is an exam support system.

Franklyn Examiner is the user interface for examiners. Franklyn Client is the client for the examinees.

Franklyn can be used by teachers to view at the student's computer screens. 
It is an easy way to prevent cheating and to help students on the other hand.
The teacher can look at the students' screens and see if one or more students need help with a problem.


# gh-Pages
https://htl-leonding-project.github.io/franklyn-quarkus/

# How to start the application

1. start the server(backend) first

* open a terminal in `backend`-project and execute:

`./mvnw clean quarkus:dev`

2. then start the teacher-frontend

* open a terminal in `teacher-frontend`-project and execute:

`./mvnw clean quarkus:dev`

3. finally start the frontend

* open a terminal in `frontend`-project and execute:

`./mvnw clean quarkus:dev`